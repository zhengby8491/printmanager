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

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.SmsPartnerService;
import com.huayin.printmanager.sms.SmsSendFactory;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 系统模块 - 短信渠道
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class SmsPartnerServiceImpl extends BaseServiceImpl implements SmsPartnerService, ApplicationContextAware
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPartnerService#get(java.lang.Long)
	 */
	@Override
	public SmsPartner get(Long id)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsPartner.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPartnerService#getByName(java.lang.String)
	 */
	@Override
	public SmsPartner getByName(String name)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SmsPartner.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPartnerService#findByCondition(java.util.Date, java.util.Date,
	 * java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public SearchResult<SmsPartner> findByCondition(Date dateMin, Date dateMax, String smsPartnerName, Integer pageIndex, Integer pageSize)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.setIsSearchTotalCount(true);
		if (dateMin != null)
		{
			query.ge("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMin) + " 00:00:01", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (dateMax != null)
		{
			query.le("createTime", DateTimeUtil.formatToDate(DateTimeUtil.formatShortStr(dateMax) + " 23:59:59", DateTimeUtil.DATE_FORMAT_YMDHMS));
		}
		if (StringUtils.isNotEmpty(smsPartnerName))
		{
			query.like("name", "%" + smsPartnerName + "%");
		}
		query.desc("createTime");
		query.setPageIndex(pageIndex);
		query.setPageSize(pageSize);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SmsPartner.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPartnerService#list(com.huayin.printmanager.persist.enumerate.
	 * SmsPartnerState)
	 */
	@Override
	public List<SmsPartner> list(SmsPartnerState state)
	{
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("state", state);
		query.asc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SmsPartner.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SmsPartnerService#save(com.huayin.printmanager.persist.entity.sys.SmsPartner)
	 */
	@Override
	@Transactional
	public SmsPartner save(SmsPartner smsPartner)
	{
		return daoFactory.getCommonDao().saveEntity(smsPartner);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.SmsPartnerService#update(com.huayin.printmanager.persist.entity.sys.SmsPartner)
	 */
	@Override
	@Transactional
	public SmsPartner update(SmsPartner smsPartner)
	{
		return daoFactory.getCommonDao().updateEntity(smsPartner);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.SmsPartnerService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteEntity(SmsPartner.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.
	 * ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		// 初始化所有正常开通的短信网关
		// ServiceFactory bean = context.getBean(ServiceFactory.class);

		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("state", SmsPartnerState.OPEN);
		List<SmsPartner> list = serviceFactory.getPersistService().findByDynamicQuery(SmsPartner.class, query);
		for (SmsPartner partner : list)
		{
			try
			{
				SmsSendFactory.clearGateway(partner.getId());
				SmsSendFactory.getGateway(context, partner);
			}
			catch (Exception e)
			{
				logger.error("初始化短信网关异常", e);
			}
		}
	}

}
