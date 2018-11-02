package com.huayin.printmanager.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.session.ExpiringSession;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.utils.CacheUtils;
import com.huayin.printmanager.utils.DateUtils;

import net.sf.ehcache.Cache;

/**
 * 系统安全认证实现类
 * 需要像spring-redis一样手工集成监听session变化，目前还没有实现，不能切换到ehcache
 * @author zhaojt
 * @version 2014-7-24
 */
@Service("ehcacheSessionDAO")
public class EhcacheSessionDAO implements SessionDAO
{
	//private Logger logger = LoggerFactory.getLogger(getClass());
	
	public EhcacheSessionDAO()
	{
		super();
	}

	@Override
	public ExpiringSession readSession(Serializable sessionId) 
	{	
		return (ExpiringSession)CacheUtils.get(SysConstants.REDIS_SESSION_KEY_PREFIX,sessionId.toString());
	}

	@Override
	public int getSessionsCount()
	{
		return CacheUtils.getCache(SysConstants.REDIS_SESSION_KEY_PREFIX).getSize();
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
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	@Override
	public Collection<ExpiringSession> getActiveSessions(boolean includeLeave, String userId, String filterSessionId)
	{
		// 如果包括离线，并无登录者条件。
		if (includeLeave && userId == null)
		{
			return getActiveSessions();
		}
		Set<ExpiringSession> sessions = Sets.newHashSet();
		Cache cache = CacheUtils.getCache(SysConstants.REDIS_SESSION_KEY_PREFIX);
		for (Object key : cache.getKeys())
		{
			ExpiringSession session=(ExpiringSession)CacheUtils.get(SysConstants.REDIS_SESSION_KEY_PREFIX, key.toString());
			boolean isActiveSession = false;
			// 不包括离线并符合最后访问时间小于等于3分钟条件。
			if (includeLeave || DateUtils.pastMinutes(new Date((Long)(session.getLastAccessedTime()))) <= 3)
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
					Long session_userId=(Long)session.getAttribute(SysConstants.SESSION_KEY_LOGIN_USERID);
					if (session_userId!=null&&userId.equals(String.valueOf(session_userId)))
					{
						isActiveSession = true;
					}else
					{
						isActiveSession=false;
					}
				}
			}
			if (isActiveSession)
			{
				sessions.add(session);
			}
		}
		return sessions;
	}


	@Override
	public void delete(Serializable sessionId)
	{
		CacheUtils.remove(SysConstants.REDIS_SESSION_KEY_PREFIX, sessionId.toString());
	}

	@Override
	public Collection<ExpiringSession> getActiveSessions()
	{
		return getActiveSessions(true);
	}

}
