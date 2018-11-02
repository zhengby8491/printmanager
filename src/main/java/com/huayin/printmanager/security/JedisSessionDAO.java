package com.huayin.printmanager.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.JedisUtils;

/**
 * 自定义授权会话管理类
 * @author zhaojt
 * @version 2014-7-20
 */
@Service("jedisSessionDAO")
public class JedisSessionDAO implements SessionDAO
{

	// private Logger logger = LoggerFactory.getLogger(JedisSessionDAO.class);

	@Override
	public int getSessionsCount()
	{
		return JedisUtils.keys(SysConstants.REDIS_SESSION_EXPIRES_KEY_PREFIX + "*").size();
	}

	@Override
	public Collection<ExpiringSession> getActiveSessions()
	{
		return getActiveSessions(true);
	}

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	@Override
	public Collection<ExpiringSession> getActiveSessions(boolean includeLeave)
	{
		return getActiveSessions(includeLeave, null, null);
	}

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param userId 包含根据登录者对象主键ID
	 * @param filterSessionId 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	@Override
	public Collection<ExpiringSession> getActiveSessions(boolean includeLeave, String userId, String filterSessionId)
	{
		Set<ExpiringSession> sessions = Sets.newHashSet();

		Set<String> keys = JedisUtils.keys(SysConstants.REDIS_SESSION_KEY_PREFIX + "*");
		for (String key : keys)
		{
			if (key.contains("expires"))
			{// 也可以判断值类型JedisUtils.type(key)，但效率低下，故采用硬性判断
				continue;
			}
			Map<String, Object> sessionMap = JedisUtils.getObjectMap(key);
			if (sessionMap != null)
			{
				MapSession session = toMapSession(getSessionId(key), sessionMap);

				boolean isActiveSession = false;
				// 不包括离线并符合最后访问时间小于等于3分钟条件。
				if (includeLeave || DateUtils.pastMinutes(new Date((Long) (session.getLastAccessedTime()))) <= 3)
				{
					isActiveSession = true;
				}
				// 过滤掉的SESSION
				if (filterSessionId != null && filterSessionId.equals(session.getId()))
				{
					isActiveSession = false;
				}
				else
				{
					// 符合登陆者条件。
					if (userId != null)
					{
						Long session_userId = (Long) session
								.getAttribute("sessionAttr:" + SysConstants.SESSION_KEY_LOGIN_USERID);
						if (session_userId != null && userId.equals(String.valueOf(session_userId)))
						{
							isActiveSession = true;
						}
						else
						{
							isActiveSession = false;
						}
					}
				}

				if (isActiveSession)
				{
					sessions.add(session);
				}
			}
		}
		return sessions;
	}

	@Override
	public MapSession readSession(Serializable sessionId)
	{
		Map<String, Object> sessionMap = JedisUtils.getObjectMap(SysConstants.REDIS_SESSION_KEY_PREFIX + sessionId);
		return toMapSession(sessionId.toString(), sessionMap);
	}

	@Override
	public void delete(Serializable sessionId)
	{
		JedisUtils.del(SysConstants.REDIS_SESSION_KEY_PREFIX + sessionId);// 删除session
		JedisUtils.del(SysConstants.REDIS_SESSION_EXPIRES_KEY_PREFIX + sessionId);// 删除session过期时间
	}

	private MapSession toMapSession(String sessionId, Map<String, Object> sessionMap)
	{
		MapSession session = new MapSession();
		session.setId(sessionId);
		if(sessionMap.containsKey("creationTime"))
		{
			session.setCreationTime((Long) sessionMap.get("creationTime"));
		}
		if(sessionMap.containsKey("lastAccessedTime"))
		{
			session.setLastAccessedTime((Long) sessionMap.get("lastAccessedTime"));
		}
		if(sessionMap.containsKey("maxInactiveInterval"))
		{
			session.setMaxInactiveIntervalInSeconds((Integer) sessionMap.get("maxInactiveInterval"));
		}
		for (Map.Entry<String, Object> entry : sessionMap.entrySet())
		{
			if (entry.getKey().contains("sessionAttr:"))
			{
				session.setAttribute(entry.getKey().substring(entry.getKey().indexOf("sessionAttr:")),
						entry.getValue());
			}
		}
		return session;
	}

	private String getSessionId(String redisSessionKey)
	{
		if (redisSessionKey.contains(SysConstants.REDIS_SESSION_KEY_PREFIX))
		{
			return redisSessionKey.substring(redisSessionKey.indexOf(SysConstants.REDIS_SESSION_KEY_PREFIX)
					+ SysConstants.REDIS_SESSION_KEY_PREFIX.length());
		}
		else
		{
			return null;
		}
	}
}
