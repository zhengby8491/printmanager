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

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.printmanager.persist.entity.sys.SmsPortal;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SmsPortalService;

/**
 * <pre>
 * 系统模块 - 短信供应商
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SmsPortalServiceImpl extends BaseServiceImpl implements SmsPortalService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPortalService#get(java.lang.Long)
	 */
	@Override
	public SmsPortal get(Long id)
	{
		return daoFactory.getCommonDao().getEntity(SmsPortal.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPortalService#getByAccountId(java.lang.String)
	 */
	@Override
	public SmsPortal getByAccountId(String accountId)
	{
		DynamicQuery query = new DynamicQuery(SmsPortal.class);
		query.eq("accountId", accountId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsPortal.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPortalService#list()
	 */
	@Override
	public List<SmsPortal> list()
	{
		DynamicQuery query = new DynamicQuery(SmsPortal.class);
		query.asc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SmsPortal.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SmsPortalService#save(com.huayin.printmanager.persist.entity.sys.SmsPortal)
	 */
	@Override
	@Transactional
	public SmsPortal save(SmsPortal smsPortal)
	{
		return daoFactory.getCommonDao().saveEntity(smsPortal);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SmsPortalService#update(com.huayin.printmanager.persist.entity.sys.SmsPortal)
	 */
	@Override
	@Transactional
	public SmsPortal update(SmsPortal smsPortal)
	{
		return daoFactory.getCommonDao().updateEntity(smsPortal);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPortalService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteEntity(SmsPortal.class, id);
	}
}
