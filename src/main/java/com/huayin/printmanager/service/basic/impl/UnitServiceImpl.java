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
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.service.basic.UnitService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 单位信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Service
public class UnitServiceImpl extends BaseServiceImpl implements UnitService
{
	@Override
	public Unit get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Unit.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Unit.class);
	}

	@Override
	public Unit getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Unit.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Unit.class);
	}

	@Override
	@Transactional
	public Unit save(Unit unit)
	{
		return daoFactory.getCommonDao().saveEntity(unit);
	}

	@Override
	@Transactional
	public Unit update(Unit unit)
	{
		return daoFactory.getCommonDao().updateEntity(unit);
	}

	@Override
	public List<Unit> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Unit.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Unit.class);
	}

	@Override
	public SearchResult<Unit> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Unit.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getUnitName()))
		{
			query.like("name", "%" + queryParam.getUnitName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Unit.class);
	}

}
