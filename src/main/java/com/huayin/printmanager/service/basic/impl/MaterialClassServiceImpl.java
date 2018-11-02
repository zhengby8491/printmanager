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
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.service.basic.MaterialClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Service
public class MaterialClassServiceImpl extends BaseServiceImpl implements MaterialClassService
{
	@Override
	public MaterialClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, MaterialClass.class);
	}

	@Override
	public MaterialClass get(String companyId, Long id)
	{
		DynamicQuery query = new DynamicQuery(MaterialClass.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, MaterialClass.class);
	}

	@Override
	public MaterialClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, MaterialClass.class);
	}

	@Override
	@Transactional
	public MaterialClass save(MaterialClass materialClass)
	{
		return daoFactory.getCommonDao().saveEntity(materialClass);
	}

	@Override
	@Transactional
	public MaterialClass saveQuick(String name)
	{
		MaterialClass materialClass = new MaterialClass();
		materialClass.setCompanyId(UserUtils.getCompanyId());
		// 排序
		materialClass.setSort(serviceFactory.getCommonService().getNextSort(BasicType.MATERIALCLASS));
		// 分类名称
		materialClass.setName(name);

		MaterialClass materialClassNew = this.save(materialClass);

		// 清除缓存
		UserUtils.clearCacheBasic(BasicType.MATERIALCLASS);

		return materialClassNew;
	}

	@Override
	@Transactional
	public MaterialClass update(MaterialClass materialClass)
	{
		return daoFactory.getCommonDao().updateEntity(materialClass);
	}

	@Override
	public List<MaterialClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialClass.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, MaterialClass.class);
	}

	@Override
	public SearchResult<MaterialClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getMaterialClassName()))
		{
			query.like("name", "%" + queryParam.getMaterialClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, MaterialClass.class);
	}
}
