/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.common.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.session.ExpiringSession;

import com.huayin.printmanager.persist.entity.sys.User;

/**
 * <pre>
 * 公共 - 统计在线人数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class OnlineUserVo implements Serializable,Comparable<OnlineUserVo>
{
	private static final long serialVersionUID = 1L;
	private User user;
	private ExpiringSession session;
	private String host;
	private Date lastAccessTime;//最近访问时间
	private Date startTimestamp;//开始时间
	private Integer timeout;//超时毫秒数
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public ExpiringSession getSession()
	{
		return session;
	}
	public void setSession(ExpiringSession session)
	{
		this.session = session;
	}
	public String getHost()
	{
		return host;
	}
	public void setHost(String host)
	{
		this.host = host;
	}
	public Date getLastAccessTime()
	{
		return lastAccessTime;
	}
	public void setLastAccessTime(Date lastAccessTime)
	{
		this.lastAccessTime = lastAccessTime;
	}
	public Date getStartTimestamp()
	{
		return startTimestamp;
	}
	public void setStartTimestamp(Date startTimestamp)
	{
		this.startTimestamp = startTimestamp;
	}
	public Integer getTimeout()
	{
		return timeout;
	}
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public int compareTo(OnlineUserVo o)
	{
		try
		{
			if (o.getSession() != null && this.getSession() != null)
			{
				return this.getSession().getLastAccessedTime() < o.getSession().getLastAccessedTime() ? -1
						: (this.getSession().getLastAccessedTime() == o.getSession().getLastAccessedTime() ? 0 : 1);

			}
			else
			{
				return o.getUser().getLastLoginTime().compareTo(this.getUser().getLastLoginTime());
			}
		}
		catch (Exception e)
		{
			return -1;
		}
	}

}
