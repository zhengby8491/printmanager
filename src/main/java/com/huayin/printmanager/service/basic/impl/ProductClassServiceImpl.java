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
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.service.basic.ProductClassService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Service
public class ProductClassServiceImpl extends BaseServiceImpl implements ProductClassService
{
	@Override
	public ProductClass get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductClass.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProductClass.class);
	}

	@Override
	public ProductClass get(String companyId, Long id)
	{
		DynamicQuery query = new DynamicQuery(ProductClass.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProductClass.class);
	}

	@Override
	public ProductClass getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductClass.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, ProductClass.class);
	}

	@Override
	@Transactional
	public ProductClass save(ProductClass productClass)
	{
		return daoFactory.getCommonDao().saveEntity(productClass);
	}

	@Override
	@Transactional
	public ProductClass saveQuick(ProductType productType, String name)
	{
		ProductClass productClass = new ProductClass();
		// 排序
		productClass.setSort(serviceFactory.getCommonService().getNextSort(BasicType.PRODUCTCLASS));
		productClass.setCompanyId(UserUtils.getCompanyId());
		// 产品分类
		productClass.setProductType(productType);
		// 产品名称
		productClass.setName(name);
		ProductClass productClassNew = this.save(productClass);
		// 清除缓存
		UserUtils.clearCacheBasic(BasicType.PRODUCTCLASS);
		return productClassNew;
	}

	@Override
	@Transactional
	public ProductClass update(ProductClass productClass)
	{
		return daoFactory.getCommonDao().updateEntity(productClass);
	}

	@Override
	public List<ProductClass> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductClass.class);
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, ProductClass.class);
	}

	@Override
	public SearchResult<ProductClass> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductClass.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getProductClassName()))
		{
			query.like("name", "%" + queryParam.getProductClassName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, ProductClass.class);
	}
}
