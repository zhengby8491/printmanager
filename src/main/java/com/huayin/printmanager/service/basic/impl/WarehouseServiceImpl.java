/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.enumerate.WarehouseType;
import com.huayin.printmanager.service.basic.WarehouseService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 仓库信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WarehouseServiceImpl extends BaseServiceImpl implements WarehouseService
{
	@Override
	public Warehouse get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Warehouse.class);
	}

	@Override
	@Transactional
	public Warehouse save(Warehouse warehouse)
	{
		return daoFactory.getCommonDao().saveEntity(warehouse);
	}

	@Override
	@Transactional
	public Warehouse update(Warehouse warehouse)
	{
		return daoFactory.getCommonDao().updateEntity(warehouse);
	}

	@Override
	public Warehouse getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Warehouse.class);
	}

	@Override
	public List<Warehouse> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Warehouse.class);
	}

	@Override
	public SearchResult<Warehouse> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getWarehouseName()))
		{
			query.like("name", "%" + queryParam.getWarehouseName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Warehouse.class);
	}

	@Override
	public List<Warehouse> findByType(WarehouseType warehouseType)
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.eq("warehouseType", warehouseType);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Warehouse.class);
	}

	@Override
	public List<Warehouse> getByBegin(WarehouseType warehouseType)
	{
		DynamicQuery query = new CompanyDynamicQuery(Warehouse.class);
		query.eq("warehouseType", warehouseType);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Warehouse.class);
	}

}
