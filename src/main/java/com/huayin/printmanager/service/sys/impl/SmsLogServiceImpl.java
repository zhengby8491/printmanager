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

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.enumerate.SmsLogState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SmsLogService;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 系统模块 - 短信日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SmsLogServiceImpl extends BaseServiceImpl implements SmsLogService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsLogService#getSmsLogByUserNo(java.lang.String)
	 */
	@Override
	public SmsLog getSmsLogByUserNo(String userNo)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.eq("userNo", userNo);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsLog.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsLogService#getSmsLogByCompanyId(java.lang.String)
	 */
	@Override
	public SmsLog getSmsLogByCompanyId(String companyId)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsLog.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsLogService#getByMobile(java.lang.String)
	 */
	@Override
	public SmsLog getByMobile(String mobile)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.like("mobile", "%" + mobile + "%");
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsLog.class);

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsLogService#getByContent(java.lang.String)
	 */
	@Override
	public SmsLog getByContent(String content)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.like("content", "%" + content + "%");
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsLog.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsLogService#findByCondition(java.lang.String, java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.SmsSendType, com.huayin.printmanager.persist.enumerate.SmsType,
	 * com.huayin.printmanager.persist.enumerate.SmsLogState, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public SearchResult<SmsLog> findByCondition(String mobile, String content, SmsSendType smsSendType, SmsType type, SmsLogState state, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.setIsSearchTotalCount(true);

		if (StringUtils.isNotEmpty(mobile))
		{
			query.like("mobile", "%" + mobile + "%");
		}
		if (StringUtils.isNotEmpty(content))
		{
			query.like("content", "%" + content + "%");
		}
		if (smsSendType != null)
		{
			query.eq("smsSendType", smsSendType);
		}
		if (type != null)
		{
			query.eq("type", type);
		}
		if (state != null)
		{
			query.eq("state", state);
		}
		query.desc("sendTime");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SmsLog.class);
	}
}
