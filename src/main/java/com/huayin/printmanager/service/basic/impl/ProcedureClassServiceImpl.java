/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.service.basic.ProcedureClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 工序分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Service
public class ProcedureClassServiceImpl extends BaseServiceImpl implements ProcedureClassService
{
	@Override
	public ProcedureClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProcedureClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProcedureClass.class);
	}

	@Override
	public ProcedureClass get(String companyId, Long id)
	{
		DynamicQuery query = new DynamicQuery(ProcedureClass.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProcedureClass.class);
	}

	@Override
	public ProcedureClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProcedureClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProcedureClass.class);
	}

	@Override
	@Transactional
	public ProcedureClass save(ProcedureClass procedureClass)
	{
		procedureClass.setCompanyId(UserUtils.getCompanyId());

		return daoFactory.getCommonDao().saveEntity(procedureClass);
	}

	@Override
	@Transactional
	public ProcedureClass saveQuick(String name, ProcedureType procedureType)
	{
		ProcedureClass procedureClass = new ProcedureClass();
		// 排序
		procedureClass.setSort(serviceFactory.getCommonService().getNextSort(BasicType.PROCEDURECLASS));
		// 工序分类
		procedureClass.setProcedureType(procedureType);
		// 工序分类名称
		procedureClass.setName(name);
		// 产品类型 1通用 2包装 3书刊
		procedureClass.setProductType(1);
		ProcedureClass procedureClassNew = this.save(procedureClass);

		// 清除缓存
		UserUtils.clearCacheBasic(BasicType.PROCEDURECLASS);

		return procedureClassNew;
	}

	@Override
	@Transactional
	public ProcedureClass update(ProcedureClass procedureClass)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.eq("procedureClassId", procedureClass.getId());

		List<Procedure> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Procedure.class);
		List<Procedure> newList = new ArrayList<Procedure>();
		for (Procedure procedure : list)
		{
			procedure.setProductType(procedureClass.getProductType());
			newList.add(procedure);
		}
		daoFactory.getCommonDao().updateAllEntity(newList);

		return daoFactory.getCommonDao().updateEntity(procedureClass);
	}

	@Override
	public SearchResult<ProcedureClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProcedureClass.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getProcedureType() != null)
		{
			query.eq("procedureType", queryParam.getProcedureType());
		}
		if (StringUtils.isNotEmpty(queryParam.getProcedureClassName()))
		{
			query.like("name", "%" + queryParam.getProcedureClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, ProcedureClass.class);
	}
}
