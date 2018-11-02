/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月16日 上午11:20:12
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.persist.enumerate.ZeroOriginType;
import com.huayin.printmanager.service.QuickService;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 公共 - 快捷选择
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月16日
 */
@Service
public class QuickServiceImpl extends BaseServiceImpl implements QuickService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.QuickService#findSupplierOemList(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Supplier> findSupplierOemList(QueryParam queryParam)
	{
		// 1.2 从公司查询,并设置真实存在的originCompanyId
		// Map<公司id, Supplier>
		SearchResult<Supplier> supplierOemListResult = serviceFactory.getZeroService().findSupplierListFromCompany(queryParam);

		// 从供应商获取（默认供应商）
		if (queryParam.getZeroOriginType() == null || queryParam.getZeroOriginType() == ZeroOriginType.SUPPLIER)
		{
			// 只过滤加工和综合数据
			queryParam.setSupplierType(SupplierType.PROCESS);
			SearchResult<Supplier> supplierListResult = serviceFactory.getZeroService().findSupplierList(queryParam);
			// Map<代工平台公司id, Supplier>
			Map<String, Supplier> supplierMap = Maps.newHashMap();
			for (Supplier supplier : supplierListResult.getResult())
			{
				supplierMap.put(supplier.getOriginCompanyId(), supplier);
				// 当前供应商可能已经关闭代工了
				supplier.setOriginCompanyId(null);
			}

			// 过滤已经关闭的代工公司
			for (Supplier supplier : supplierOemListResult.getResult())
			{
				// 是否已经关联了代工平台
				Supplier _supplier = supplierMap.get(supplier.getOriginCompanyId());
				if (null != _supplier)
				{
					_supplier.setOriginCompanyId(supplier.getOriginCompanyId());
				}
			}
			return supplierListResult;
		}
		// 从公司获取
		else if (queryParam.getZeroOriginType() == ZeroOriginType.COMPANY)
		{
			// 过滤所有供应商
			queryParam.setSupplierType(null);
			SearchResult<Supplier> supplierListResult = serviceFactory.getZeroService().findSupplierList(queryParam);
			// Map<代工平台公司名称, Supplier>
			Map<String, Supplier> supplierNameMap = Maps.newHashMap();
			// Map<代工平台公司id, Supplier>
			Map<String, Supplier> supplierMap = Maps.newHashMap();
			for (Supplier supplier : supplierListResult.getResult())
			{
				supplierNameMap.put(supplier.getName(), supplier);
				supplierMap.put(supplier.getOriginCompanyId(), supplier);
				// 当前供应商可能已经关闭代工了
				supplier.setOriginCompanyId(null);
			}

			for (Supplier supplier : supplierOemListResult.getResult())
			{
				// 基础资料是否存在代工平台供应商（用于代工平台）
				Supplier _supplier2 = supplierNameMap.get(supplier.getName());
				if (null != _supplier2)
				{
					_supplier2.setOriginCompanyExit(BoolValue.YES);
				}

				// 是否已经关联了代工平台
				Supplier _supplier = supplierMap.get(supplier.getOriginCompanyId());
				if (null != _supplier)
				{
					_supplier.setOriginCompanyId(supplier.getOriginCompanyId());
				}
			}

			// SearchResult<Supplier> supplierOemListResult =
			// serviceFactory.getZeroService().findSupplierListFromCompany(queryParam);

			for (Supplier supplier : supplierOemListResult.getResult())
			{
				// 设置基础资料是否存在代工平台供应商（用于代工平台）
				Supplier _self2 = supplierNameMap.get(supplier.getName());
				if (null != _self2)
				{
					supplier.setId(_self2.getId());
					supplier.setType(_self2.getType());
					supplier.setOriginCompanyExit(BoolValue.YES);
				}

				Supplier _self = supplierMap.get(supplier.getOriginCompanyId());
				if (null != _self)
				{
					// 复制数据
					PropertyClone.copyProperties(supplier, _self, false);
				}
			}

			return supplierOemListResult;
		}

		// 空对象（前端切换显示才会显示空数据，否则显示上一次数据）
		SearchResult<Supplier> emptyResult = new SearchResult<>();
		List<Supplier> resultList = Lists.newArrayList();
		emptyResult.setResult(resultList);
		return emptyResult;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.QuickService#findCustomerOemList(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Customer> findCustomerOemList(QueryParam queryParam)
	{
		SearchResult<Customer> customerListResult = serviceFactory.getZeroService().findCustomerList(queryParam);
		// Map<代工平台公司名称, Customer>
		Map<String, Customer> customerNameMap = Maps.newHashMap();
		// Map<代工平台公司id, Customer>
		Map<String, Customer> customerMap = Maps.newHashMap();
		for (Customer customer : customerListResult.getResult())
		{
			customerNameMap.put(customer.getName(), customer);
			customerMap.put(customer.getOriginCompanyId(), customer);
		}

		// 从客户信息获取（默认客户信息）
		if (queryParam.getZeroOriginType() == null || queryParam.getZeroOriginType() == ZeroOriginType.CUSTOMER)
		{
			return customerListResult;
		}
		// 从公司获取
		else if (queryParam.getZeroOriginType() == ZeroOriginType.COMPANY)
		{
			SearchResult<Customer> customerOemListResult = serviceFactory.getZeroService().findCustomerListFromCompany(queryParam);

			for (Customer customer : customerOemListResult.getResult())
			{
				// 设置基础资料是否存在代工平台客户（用于代工平台）
				Customer _self2 = customerNameMap.get(customer.getName());
				if (null != _self2)
				{
					customer.setId(_self2.getId());
					customer.setOriginCompanyExit(BoolValue.YES);
				}

				Customer _self = customerMap.get(customer.getOriginCompanyId());
				if (null != _self)
				{
					// 复制数据
					PropertyClone.copyProperties(customer, _self, false);
				}
			}

			return customerOemListResult;
		}

		// 空对象（前端切换显示才会显示空数据，否则显示上一次数据）
		SearchResult<Customer> emptyResult = new SearchResult<>();
		List<Customer> resultList = Lists.newArrayList();
		emptyResult.setResult(resultList);
		return emptyResult;
	}
}
