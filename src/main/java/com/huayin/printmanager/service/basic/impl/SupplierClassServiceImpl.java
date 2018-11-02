/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.service.basic.SupplierClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 供应商类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class SupplierClassServiceImpl extends BaseServiceImpl implements SupplierClassService
{
	@Override
	public SupplierClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SupplierClass.class);
	}

	@Override
	public List<SupplierClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierClass.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SupplierClass.class);
	}

	@Override
	public SearchResult<SupplierClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getSupplierClassName()))
		{
			query.like("name", "%" + queryParam.getSupplierClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SupplierClass.class);
	}

	@Override
	@Transactional
	public SupplierClass save(SupplierClass supplierClass)
	{
		return daoFactory.getCommonDao().saveEntity(supplierClass);
	}

	@Override
	@Transactional
	public SupplierClass update(SupplierClass supplierClass)
	{
		return daoFactory.getCommonDao().updateEntity(supplierClass);
	}

	@Override
	public SupplierClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SupplierClass.class);
	}
}
