/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.UserShareType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.UserShareService;

/**
 * <pre>
 * 框架 - 用户快捷登录功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class UserShareServiceImpl extends BaseServiceImpl implements UserShareService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.UserShareService#save(com.huayin.printmanager.persist.entity.sys.UserShare)
	 */
	@Override
	@Transactional
	public void save(UserShare userShare)
	{
		daoFactory.getCommonDao().saveEntity(userShare);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.UserShareService#isExist(java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.UserShareType)
	 */
	@Override
	public UserShare isExist(String openid, UserShareType userType)
	{
		DynamicQuery query = new DynamicQuery(UserShare.class);
		query.eq("identifier", openid);
		query.eq("userType", userType);
		UserShare userShare = daoFactory.getCommonDao().getByDynamicQuery(query, UserShare.class);
		return userShare;
	}
}
