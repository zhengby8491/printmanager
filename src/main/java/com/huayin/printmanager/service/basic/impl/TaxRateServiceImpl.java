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
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.service.basic.TaxRateService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 基础设置 - 税率信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Service
public class TaxRateServiceImpl extends BaseServiceImpl implements TaxRateService
{
	@Override
	public TaxRate get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(TaxRate.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, TaxRate.class);
	}

	@Override
	public TaxRate getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(TaxRate.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, TaxRate.class);
	}

	@Override
	public TaxRate getByPercent(int percent)
	{
		DynamicQuery query = new CompanyDynamicQuery(TaxRate.class);
		query.eq("percent", percent);
		return daoFactory.getCommonDao().getByDynamicQuery(query, TaxRate.class);
	}

	@Override
	@Transactional
	public TaxRate save(TaxRate taxRate)
	{
		return daoFactory.getCommonDao().saveEntity(taxRate);
	}

	@Override
	@Transactional
	public TaxRate update(TaxRate taxRate)
	{
		return daoFactory.getCommonDao().updateEntity(taxRate);
	}

	@Override
	public List<TaxRate> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(TaxRate.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, TaxRate.class);
	}

	@Override
	public SearchResult<TaxRate> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(TaxRate.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getTaxRateName()))
		{
			query.like("name", "%" + queryParam.getTaxRateName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, TaxRate.class);
	}
}
