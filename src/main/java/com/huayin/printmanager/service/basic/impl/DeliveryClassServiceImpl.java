/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.service.basic.DeliveryClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 送货方式
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Service
public class DeliveryClassServiceImpl extends BaseServiceImpl implements DeliveryClassService
{

	@Override
	public DeliveryClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(DeliveryClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, DeliveryClass.class);
	}

	@Override
	public DeliveryClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(DeliveryClass.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, DeliveryClass.class);
	}

	@Override
	@Transactional
	public DeliveryClass save(DeliveryClass deliveryClass)
	{
		return daoFactory.getCommonDao().saveEntity(deliveryClass);
	}

	@Override
	@Transactional
	public DeliveryClass update(DeliveryClass deliveryClass)
	{
		return daoFactory.getCommonDao().updateEntity(deliveryClass);
	}

	@Override
	public List<DeliveryClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(DeliveryClass.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, DeliveryClass.class);
	}

	@Override
	public SearchResult<DeliveryClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(DeliveryClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getDeliveryClassName()))
		{
			query.like("name", "%" + queryParam.getDeliveryClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, DeliveryClass.class);
	}

}
