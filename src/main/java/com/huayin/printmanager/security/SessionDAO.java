package com.huayin.printmanager.security;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.session.ExpiringSession;

public interface SessionDAO
{

	ExpiringSession readSession(Serializable sessionId);

	void delete(Serializable sessionId);

	Collection<ExpiringSession> getActiveSessions();

	/**
	 * <pre>
	 * 返回所有session数量
	 * </pre>
	 * @return
	 */
	public int getSessionsCount();

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	public Collection<ExpiringSession> getActiveSessions(boolean includeLeave);

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param userId 根据登录者对象获取活动会话
	 * @param filterSessionId 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<ExpiringSession> getActiveSessions(boolean includeLeave, String userId, String filterSessionId);

}
