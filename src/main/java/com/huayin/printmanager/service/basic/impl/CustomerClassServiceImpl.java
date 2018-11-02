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
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.service.basic.CustomerClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 客户分类
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class CustomerClassServiceImpl extends BaseServiceImpl implements CustomerClassService
{
	@Override
	public CustomerClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, CustomerClass.class);
	}

	@Override
	public CustomerClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, CustomerClass.class);
	}

	@Override
	@Transactional
	public CustomerClass save(CustomerClass customerClass)
	{
		return daoFactory.getCommonDao().saveEntity(customerClass);
	}

	@Override
	@Transactional
	public CustomerClass update(CustomerClass customerClass)
	{
		return daoFactory.getCommonDao().updateEntity(customerClass);
	}

	@Override
	public List<CustomerClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerClass.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, CustomerClass.class);
	}

	@Override
	public SearchResult<CustomerClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getCustomerClassName()))
		{
			query.like("name", "%" + queryParam.getCustomerClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, CustomerClass.class);
	}
}
