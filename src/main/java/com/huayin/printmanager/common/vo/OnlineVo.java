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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 公共 - 统计在线人数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class OnlineVo
{
	private Integer unLoginUserCount = 0;

	private Integer loginedUserCount = 0;

	private Map<String, Collection<OnlineUserVo>> companyUsersMap = new HashMap<String, Collection<OnlineUserVo>>();

	public Integer getUnLoginUserCount()
	{
		return unLoginUserCount;
	}

	public void setUnLoginUserCount(Integer unLoginUserCount)
	{
		this.unLoginUserCount = unLoginUserCount;
	}

	public Integer getLoginedUserCount()
	{
		return loginedUserCount;
	}

	public void setLoginedUserCount(Integer loginedUserCount)
	{
		this.loginedUserCount = loginedUserCount;
	}

	public Map<String, Collection<OnlineUserVo>> getCompanyUsersMap()
	{
		return companyUsersMap;
	}

	public void setCompanyUsersMap(Map<String, Collection<OnlineUserVo>> companyUsersMap)
	{
		this.companyUsersMap = companyUsersMap;
	}

}
