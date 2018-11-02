/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月16日 下午2:30:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerAddress;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.ZeroService;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 公共业务常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月16日
 */
@Service
public class ZeroServiceImpl extends BaseServiceImpl implements ZeroService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.ZeroService#findSupplierList(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Supplier> findSupplierList(QueryParam queryParam)
	{
		return serviceFactory.getSupplierService().quickFindByCondition(queryParam);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.ZeroService#findSupplierListFromCompany(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<Supplier> findSupplierListFromCompany(QueryParam queryParam)
	{
		// 空对象（前端切换显示才会显示空数据，否则显示上一次数据）
		SearchResult<Supplier> emptyResult = new SearchResult<>();
		List<Supplier> resultList = Lists.newArrayList();
		emptyResult.setResult(resultList);

		// 去除重复公司
		List<String> duplicateIds = Lists.newArrayList();

		// 查询公司代工平台信息
		SearchResult<CompanyVo> companyResult = serviceFactory.getOemZeroService().findCompanyByCondition(queryParam);
		for (CompanyVo vo : companyResult.getResult())
		{
			// 过滤本公司
			if (UserUtils.getCompanyId().equals(vo.getCompany().getId()))
			{
				continue;
			}

			// 去除重复公司
			if (duplicateIds.contains(vo.getCompany().getId()))
			{
				continue;
			}

			Supplier _supplier = new Supplier();
			// 代工平台 - 公司id（用于区别供应商来自公司）
			_supplier.setOriginCompanyId(vo.getCompany().getId());
			// 供应商名称
			_supplier.setName(vo.getCompany().getName());
			SupplierAddress _defaultAddress = new SupplierAddress();
			// 联系人
			_defaultAddress.setUserName(vo.getCompany().getLinkName());
			// 电话
			_defaultAddress.setMobile(vo.getCompany().getTel());
			// 地址
			_defaultAddress.setAddress(vo.getCompany().getAddress());
			_supplier.setDefaultAddress(_defaultAddress);
			resultList.add(_supplier);

			// 去除重复公司
			duplicateIds.add(vo.getCompany().getId());
		}

		emptyResult.setCount(resultList.size());
		return emptyResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.ZeroService#findCustomerList(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Customer> findCustomerList(QueryParam queryParam)
	{
		return serviceFactory.getCustomerService().quickFindByCondition(queryParam);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.ZeroService#findCustomerListFromCompany(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<Customer> findCustomerListFromCompany(QueryParam queryParam)
	{
		// 空对象（前端切换显示才会显示空数据，否则显示上一次数据）
		SearchResult<Customer> emptyResult = new SearchResult<>();
		List<Customer> resultList = Lists.newArrayList();
		emptyResult.setResult(resultList);

		// 去除重复公司
		List<String> duplicateIds = Lists.newArrayList();

		// 查询公司代工平台信息
		SearchResult<CompanyVo> companyResult = serviceFactory.getOemZeroService().findCompanyByOutSource(queryParam);
		for (CompanyVo vo : companyResult.getResult())
		{
			// 过滤本公司
			if (UserUtils.getCompanyId().equals(vo.getCompany().getId()))
			{
				continue;
			}

			// 去除重复公司
			if (duplicateIds.contains(vo.getCompany().getId()))
			{
				continue;
			}

			Customer _customer = new Customer();
			// 代工平台 - 公司id（用于区别供应商来自公司）
			_customer.setOriginCompanyId(vo.getCompany().getId());
			// 客户名称
			_customer.setName(vo.getCompany().getName());
			CustomerAddress _defaultAddress = new CustomerAddress();
			// 联系人
			_defaultAddress.setUserName(vo.getCompany().getLinkName());
			// 电话
			_defaultAddress.setMobile(vo.getCompany().getTel());
			// 地址
			_defaultAddress.setAddress(vo.getCompany().getAddress());
			_customer.setDefaultAddress(_defaultAddress);
			resultList.add(_customer);

			// 去除重复公司
			duplicateIds.add(vo.getCompany().getId());
		}

		emptyResult.setCount(resultList.size());
		return emptyResult;
	}

	@Override
	public Customer findCustomerByOriginCompanyId(String originCompanyId)
	{
		DynamicQuery query = new DynamicQuery(Customer.class);
		query.eq("originCompanyId", originCompanyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Customer.class);
	}

	@Override
	public boolean isOemOrderUse(QueryParam queryParam)
	{
		SearchResult<OemOrderDetail> result = serviceFactory.getOemZeroService().findOrderDetailByOutSourceDetail(queryParam);
		return result.getResult().size() > 0;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Map<Long, List<WorkProduct>>> findAllProductForMap(List<String> targetCompanyId)
	{
		// 以公司id作为key
		Map<String, Map<Long, List<WorkProduct>>> companyMap = Maps.newHashMap();
		for (String companyId : targetCompanyId)
		{
			DynamicQuery query = new DynamicQuery(WorkProduct.class, "a");
			query.createAlias(Work.class, "b");
			query.eqProperty("a.masterId", "b.id");
			query.eq("a.companyId", companyId);
			query.addProjection(Projections.property("a.masterId, a.productName,a.productId, b.id, a.companyId"));
			query.eq("b.isCheck", BoolValue.YES);
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			
			Map<Long, List<WorkProduct>> map = Maps.newHashMap();
			
			for(HashMap p : result.getResult())
			{
				try
				{
					WorkProduct workProduct = ObjectHelper.mapToObject(p, WorkProduct.class);
					long workId = workProduct.getMasterId();
					List<WorkProduct> list = map.get(workId);
					if (null == list)
					{
						list = Lists.newArrayList();
						map.put(workId, list);
					}
					list.add(workProduct);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			companyMap.put(companyId, map);
		}
		
		return companyMap;
	}
}
