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
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.service.basic.UnitConvertService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 单位换算
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Service
public class UnitConvertServiceImpl extends BaseServiceImpl implements UnitConvertService
{
	@Override
	public UnitConvert get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(UnitConvert.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, UnitConvert.class);
	}

	@Override
	@Transactional
	public UnitConvert save(UnitConvert unitConvert)
	{
		unitConvert.setCompanyId(UserUtils.getCompanyId());
		return daoFactory.getCommonDao().saveEntity(unitConvert);
	}

	@Override
	@Transactional
	public UnitConvert update(UnitConvert unitConvert)
	{
		UnitConvert unitConvert_ = this.get(unitConvert.getId());
		unitConvert_.setConversionContent(unitConvert.getConversionContent());
		unitConvert_.setConversionUnitId(unitConvert.getConversionUnitId());
		unitConvert_.setFormula(unitConvert.getFormula());
		unitConvert_.setName(unitConvert.getName());
		unitConvert_.setSourceUnitId(unitConvert.getSourceUnitId());
		return daoFactory.getCommonDao().updateEntity(unitConvert_);
	}

	@Override
	public List<UnitConvert> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(UnitConvert.class);
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, UnitConvert.class);
	}

	@Override
	public SearchResult<UnitConvert> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(UnitConvert.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("name", "%" + queryParam.getSearchContent() + "%");
		}
		else if (StringUtils.isNotEmpty(queryParam.getUnitName()))
		{
			query.like("name", "%" + queryParam.getUnitName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, UnitConvert.class);
	}

	@Override
	public UnitConvert getByUnit(Long sourceUnitId, Long conversionUnitId)
	{
		DynamicQuery query = new CompanyDynamicQuery(UnitConvert.class);
		query.eq("sourceUnitId", sourceUnitId);
		query.eq("conversionUnitId", conversionUnitId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, UnitConvert.class);
	}
}
