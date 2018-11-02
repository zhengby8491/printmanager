/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.service.basic.PaymentClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础模块 - 付款方式
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:37:56
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Service
public class PaymentClassServiceImpl extends BaseServiceImpl implements PaymentClassService
{
	@Override
	public PaymentClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PaymentClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, PaymentClass.class);
	}

	@Override
	public PaymentClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(PaymentClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, PaymentClass.class);
	}

	@Override
	@Transactional
	public PaymentClass save(PaymentClass paymentClass)
	{
		return daoFactory.getCommonDao().saveEntity(paymentClass);
	}

	@Override
	@Transactional
	public PaymentClass update(PaymentClass paymentClass)
	{
		return daoFactory.getCommonDao().updateEntity(paymentClass);
	}

	@Override
	public List<PaymentClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(PaymentClass.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, PaymentClass.class);
	}

	@Override
	public SearchResult<PaymentClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PaymentClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getPaymentClassName()))
		{
			query.like("name", "%" + queryParam.getPaymentClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, PaymentClass.class);
	}

}
