/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.purch.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.purch.PurchSumService;
import com.huayin.printmanager.service.purch.vo.PurchScheduleVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购订单汇总
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年9月30日, raintear
 * @version 	   2.0, 2018年2月23日下午3:01:40, zhengby, 代码规范
 */
@Service
public class PurchSumServiceImpl extends BaseServiceImpl implements PurchSumService
{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> findBySupplierName(QueryParam queryParam)
	{
		if (!StringUtils.isNotBlank(queryParam.getYear()))
		{
			queryParam.setYear(DateUtils.getYear());
		}
		String year = queryParam.getYear();
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id,b.supplierName as name,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y') WHEN '" + year
				+ "' THEN a.money ELSE 0 END ) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-01' THEN a.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-02' THEN a.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-03' THEN a.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-04' THEN a.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-05' THEN a.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-06' THEN a.money ELSE 0 END ) as 'june' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-07' THEN a.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-08' THEN a.money ELSE 0 END ) as 'august' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-09' THEN a.money ELSE 0 END ) as 'september' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-10' THEN a.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-11' THEN a.money ELSE 0 END ) as 'november' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-12' THEN a.money ELSE 0 END ) as 'december'"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		query.addGourp("b.supplierId");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> findBySupplierClass(QueryParam queryParam)
	{
		if (!StringUtils.isNotBlank(queryParam.getYear()))
		{
			queryParam.setYear(DateUtils.getYear());
		}
		String year = queryParam.getYear();
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id,d.name as name,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y') WHEN '" + year
				+ "' THEN a.money ELSE 0 END ) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-01' THEN a.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-02' THEN a.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-03' THEN a.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-04' THEN a.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-05' THEN a.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-06' THEN a.money ELSE 0 END ) as 'june' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-07' THEN a.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-08' THEN a.money ELSE 0 END ) as 'august' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-09' THEN a.money ELSE 0 END ) as 'september' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-10' THEN a.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-11' THEN a.money ELSE 0 END ) as 'november' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-12' THEN a.money ELSE 0 END ) as 'december'"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=b.supplierId");
		query.createAlias(SupplierClass.class, JoinType.LEFTJOIN, "d", "d.id=c.supplierClassId");
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierClassName()))
		{
			query.like("d.name", "%" + queryParam.getSupplierClassName() + "%");
		}
		query.addGourp("d.id");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> findByMaterialName(QueryParam queryParam)
	{
		if (!StringUtils.isNotBlank(queryParam.getYear()))
		{
			queryParam.setYear(DateUtils.getYear());
		}
		String year = queryParam.getYear();
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id,a.materialName as name,a.specifications as style,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y') WHEN '" + year
				+ "' THEN a.money ELSE 0 END ) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-01' THEN a.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-02' THEN a.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-03' THEN a.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-04' THEN a.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-05' THEN a.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-06' THEN a.money ELSE 0 END ) as 'june' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-07' THEN a.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-08' THEN a.money ELSE 0 END ) as 'august' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-09' THEN a.money ELSE 0 END ) as 'september' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-10' THEN a.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-11' THEN a.money ELSE 0 END ) as 'november' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-12' THEN a.money ELSE 0 END ) as 'december'"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getMaterialName()))
		{
			query.like("a.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		query.addGourp("a.materialId");
		query.addGourp("a.specifications");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> findByMaterialClass(QueryParam queryParam)
	{
		if (!StringUtils.isNotBlank(queryParam.getYear()))
		{
			queryParam.setYear(DateUtils.getYear());
		}
		String year = queryParam.getYear();
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id,d.name as name,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y') WHEN '" + year
				+ "' THEN a.money ELSE 0 END ) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-01' THEN a.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-02' THEN a.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-03' THEN a.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-04' THEN a.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-05' THEN a.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-06' THEN a.money ELSE 0 END ) as 'june' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-07' THEN a.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-08' THEN a.money ELSE 0 END ) as 'august' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-09' THEN a.money ELSE 0 END ) as 'september' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-10' THEN a.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-11' THEN a.money ELSE 0 END ) as 'november' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-12' THEN a.money ELSE 0 END ) as 'december'"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.createAlias(Material.class, JoinType.LEFTJOIN, "c", "c.id=a.materialId");
		query.createAlias(MaterialClass.class, JoinType.LEFTJOIN, "d", "d.id=c.materialClassId");
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getMaterialClassName()))
		{
			query.like("d.name", "%" + queryParam.getMaterialClassName() + "%");
		}
		query.addGourp("d.id");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> findByEmployeeName(QueryParam queryParam)
	{
		if (!StringUtils.isNotBlank(queryParam.getYear()))
		{
			queryParam.setYear(DateUtils.getYear());
		}
		String year = queryParam.getYear();
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id,c.name as name,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y') WHEN '" + year
				+ "' THEN a.money ELSE 0 END ) as 'sumMoney' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-01' THEN a.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-02' THEN a.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-03' THEN a.money ELSE 0 END ) as 'march' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-04' THEN a.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-05' THEN a.money ELSE 0 END ) as 'may' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-06' THEN a.money ELSE 0 END ) as 'june' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-07' THEN a.money ELSE 0 END ) as 'july' ," + "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '"
				+ year + "-08' THEN a.money ELSE 0 END ) as 'august' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-09' THEN a.money ELSE 0 END ) as 'september' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-10' THEN a.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-11' THEN a.money ELSE 0 END ) as 'november' ,"
				+ "SUM(CASE DATE_FORMAT(a.deliveryTime,'%Y-%m') WHEN '" + year
				+ "-12' THEN a.money ELSE 0 END ) as 'december'"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.createAlias(Employee.class, JoinType.LEFTJOIN, "c", "c.id=b.employeeId");
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getEmployeeName()))
		{
			query.like("c.name", "%" + queryParam.getEmployeeName() + "%");
		}
		query.ne("c.name", "");
		query.addGourp("c.id");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				HashMap.class);
		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@Override
	public SearchResult<PurchScheduleVo> findPurchSchedule(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "pod");
		query.addProjection(Projections.property("pod, po"));
		query.createAlias(PurchOrder.class, "po");
		query.createAlias(Unit.class, "u");
		query.createAlias(Supplier.class, "s");
		query.eqProperty("pod.masterId", "po.id");
		query.eqProperty("s.id", "po.supplierId");
		query.eqProperty("u.id", "pod.unitId");
		Long[] employes=UserUtils.getBusinessDataAuthorizationUser();
		if(employes.length>0){
			query.inArray("s.employeeId", employes);
		}
		if(queryParam.getDeliverDateMin()!=null && queryParam.getDeliverDateMax()!=null){
			query.between("pod.deliveryTime", queryParam.getDeliverDateMin(), queryParam.getDeliverDateMax());
		}
		if(StringUtils.isNotBlank(queryParam.getWorkBillNo())){
			query.like("pod.sourceBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if(StringUtils.isNotBlank(queryParam.getProductStyle())){
			query.like("pod.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("po.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("po.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("po.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getMaterialName()))
		{
			query.like("pod.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("po.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getEmployeeId() != null)
		{
			query.eq("po.employeeId", queryParam.getEmployeeId());
		}	
		if (queryParam.getSupplierClassId()!=null)
		{
			query.eq("s.supplierClassId", queryParam.getSupplierClassId());
		}
		if (queryParam.getMaterialClassId()!=null)
		{
			query.eq("pod.materialClassId", queryParam.getMaterialClassId());
		}
		query.eq("po.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("po.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query,
				Object[].class);
		SearchResult<PurchScheduleVo> result = new SearchResult<PurchScheduleVo>();
		result.setResult(new ArrayList<PurchScheduleVo>());
		for (Object[] c : temp_result.getResult())
		{
			PurchOrderDetail detail = (PurchOrderDetail) c[0];
			detail.setMaster((PurchOrder) c[1]);
			PurchScheduleVo vo = new PurchScheduleVo();
			vo.setId(detail.getId());
			vo.setWorkId(detail.getSourceId());
			vo.setCreateTime(detail.getMaster().getCreateTime());
			vo.setWorkNo(detail.getSourceBillNo());
			vo.setBillNo(detail.getMaster().getBillNo());
			vo.setPurchOrderId(detail.getMaster().getId());
			vo.setSupplierName(detail.getMaster().getSupplierName());
			vo.setEmployeeName(detail.getMaster().getEmployeeName());
			vo.setMaterialName(detail.getMaterialName());
			vo.setStyle(detail.getSpecifications());
			vo.setWeight(detail.getWeight());
			vo.setUnit(detail.getPurchUnitName());
			vo.setDeliveryTime(detail.getDeliveryTime());
			vo.setPurchQty(detail.getQty());
			vo.setMoney(detail.getMoney());
			vo.setStockQty(detail.getStorageQty());
			getStockQty(vo);
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public List<PurchOrder> getYearsFromPurchOrder()
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class, "a");
		query.addProjection(Projections
				.property("distinct DATE_FORMAT(createTime,'%Y') as name,DATE_FORMAT(createTime,'%Y') as value"));
		query.eq("isCheck", BoolValue.YES);
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrder.class);
	}

	/**
	 * <pre>
	 * 获取进度入库、对账数量/金额
	 * </pre>
	 * @param vo
	 */
	private void getStockQty(PurchScheduleVo vo)
	{
		BigDecimal sumReconcilQty = new BigDecimal("0");// 入库部分的对账数量
		BigDecimal sumReconcilMoney = new BigDecimal("0");// 入库部分的对账金额
		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class, "ps");		
		query.eq("ps.sourceDetailId", vo.getId());
		List<PurchStockDetail> stockList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchStockDetail.class);
		for (PurchStockDetail purchStockDetail : stockList)
		{
			sumReconcilQty = sumReconcilQty.add(purchStockDetail.getReconcilQty());			
			getReturnQty(vo, purchStockDetail.getId());
			getPaymentMoney(vo, purchStockDetail.getId(), BillType.PURCH_PN);
		}
		vo.setReconcilMoney(vo.getReconcilMoney().add(sumReconcilMoney));
		vo.setReconcilQty(vo.getReconcilQty().add(sumReconcilQty));
	}

	/**
	 * <pre>
	 * 获取进度退货、对账数量/金额
	 * </pre>
	 * @param vo
	 * @param sourceDetailId 采购入库明细ID
	 */
	private void getReturnQty(PurchScheduleVo vo, Long sourceDetailId)
	{
		BigDecimal sumReturnQty = new BigDecimal("0");// 退货数量
		BigDecimal sumReconcilQty = new BigDecimal("0");// 退货部分的对账数量
		BigDecimal sumReconcilMoney = new BigDecimal("0");// 退货部分的对账金额
		DynamicQuery query = new CompanyDynamicQuery(PurchRefundDetail.class, "pr");		
		query.eq("pr.sourceDetailId", sourceDetailId);

		List<PurchRefundDetail> stockList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchRefundDetail.class);
		for (PurchRefundDetail purchRefundDetail : stockList)
		{
			sumReturnQty = sumReturnQty.add(purchRefundDetail.getQty());
			sumReconcilQty = sumReconcilQty.add(purchRefundDetail.getReconcilQty());
			getPaymentMoney(vo, purchRefundDetail.getId(), BillType.PURCH_PR);
		}
		vo.setRefundQty(sumReturnQty);
		vo.setReconcilMoney(vo.getReconcilMoney().subtract(sumReconcilMoney));
		vo.setReconcilQty(vo.getReconcilQty().subtract(sumReconcilQty));// 退货部分的对账数量减去
	}
	
	/**
	 * <pre>
	 * 获取对账金额、付款金额
	 * </pre>
	 * @param vo
	 * @param sourceDetailId
	 * @param sourceBillType
	 */
	private void getPaymentMoney(PurchScheduleVo vo, Long sourceDetailId,BillType sourceBillType){
		BigDecimal sumReconcilMoney = new BigDecimal("0");// 对账金额
		BigDecimal sumPaymentMoney = new BigDecimal("0");// 付款金额
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "pr");
		query.eq("pr.sourceDetailId", sourceDetailId);
		query.eq("pr.sourceBillType", sourceBillType);
		List<PurchReconcilDetail> reconcilList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchReconcilDetail.class);
		for (PurchReconcilDetail purchReconcilDetail : reconcilList)
		{
			sumReconcilMoney=sumReconcilMoney.add(purchReconcilDetail.getMoney());
			sumPaymentMoney=sumPaymentMoney.add(purchReconcilDetail.getPaymentMoney());
		}
		vo.setReconcilMoney(vo.getReconcilMoney().add(sumReconcilMoney));
		vo.setPaymentMoney(vo.getPaymentMoney().add(sumPaymentMoney));
		
	}

}
