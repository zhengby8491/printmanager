/**
 * <pre>
 * Title: 		DaoFactoryImpl.java
 * Author:		linriqing
 * Create:	 	2010-6-29 下午05:39:12
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.printmanager.persist.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.huayin.common.persist.CommonDao;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.dao.DaoFactory;
import com.huayin.printmanager.persist.dao.RoleDao;
import com.huayin.printmanager.persist.dao.SmsPartnerDao;
import com.huayin.printmanager.persist.dao.UserDao;
import com.huayin.printmanager.persist.dao.UserRepository;
import com.huayin.printmanager.security.SessionDAO;

/**
 * <pre>
 * DAO接口工厂实现
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-5-5
 */
@Repository
public class DaoFactoryImpl implements DaoFactory
{
	@Autowired
	private CommonDao commonDao;

	@Autowired(required=false)
	@Qualifier("ehcacheSessionDAO")
	private SessionDAO ehcacheSessionDao;

	@Autowired(required=false)
	@Qualifier("jedisSessionDAO")
	private SessionDAO jedisSessionDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SmsPartnerDao smsPartnerDao;

	public SmsPartnerDao getSmsPartnerDao()
	{
		return smsPartnerDao;
	}

	public CommonDao getCommonDao()
	{
		return commonDao;
	}

	@Override
	public SessionDAO getSessionDao()
	{
		SessionDAO sessionDao=null;
		if (SysConstants.CACHE_TYPE_EHCACHE.equals(SystemConfigUtil.getConfig(SysConstants.CACHE_TYPE)))
		{
			sessionDao=ehcacheSessionDao;
			if(sessionDao==null)
			{
				sessionDao=jedisSessionDao;
			}
		}
		else if (SysConstants.CACHE_TYPE_REDIS.equals(SystemConfigUtil.getConfig(SysConstants.CACHE_TYPE)))
		{
			sessionDao=jedisSessionDao;
			if(sessionDao==null)
			{
				sessionDao=ehcacheSessionDao;
			}
		}else
		{
			sessionDao=ehcacheSessionDao;
			if(sessionDao==null)
			{
				sessionDao=jedisSessionDao;
			}
		}
		return sessionDao;
	}

	@Override
	public RoleDao getRoleDao()
	{
		return roleDao;
	}

	@Override
	public UserDao getUserDao()
	{
		return userDao;
	}

	@Override
	public UserRepository getUserRepository()
	{
		return userRepository;
	}

}
