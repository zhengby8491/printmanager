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
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.service.basic.SettlementClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础模块 - 结算方式
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Service
public class SettlementClassServiceImpl extends BaseServiceImpl implements SettlementClassService
{
	@Override
	public SettlementClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SettlementClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SettlementClass.class);
	}

	@Override
	public SettlementClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(SettlementClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, SettlementClass.class);
	}

	@Override
	@Transactional
	public SettlementClass save(SettlementClass settlementClass)
	{
		return daoFactory.getCommonDao().saveEntity(settlementClass);
	}

	@Override
	@Transactional
	public SettlementClass update(SettlementClass settlementClass)
	{
		return daoFactory.getCommonDao().updateEntity(settlementClass);
	}

	@Override
	public List<SettlementClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(SettlementClass.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, SettlementClass.class);
	}

	@Override
	public SearchResult<SettlementClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SettlementClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getSettlementClassName()))
		{
			query.like("name", "%" + queryParam.getSettlementClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SettlementClass.class);
	}
}
