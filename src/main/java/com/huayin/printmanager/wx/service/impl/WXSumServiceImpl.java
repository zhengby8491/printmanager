/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service.impl;

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
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.service.WXSumService;
import com.huayin.printmanager.wx.vo.FinanceDetailSumVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.StockMaterialDetailVo;
import com.huayin.printmanager.wx.vo.StockProductDetailVo;
import com.huayin.printmanager.wx.vo.SupOrCustSumVo;

/**
 * <pre>
 * 微信 - 汇总
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXSumServiceImpl extends BaseServiceImpl implements WXSumService
{

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getTotalMoney(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "od");
			query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			if (queryParam.getBillType().equals(BillType.SALE_SO))
			{
				query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=o.customerId");
			}
			else
			{
				query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=o.supplierId");
			}

			Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
			if (employes.length > 0)
			{
				query.inArray("c.employeeId", employes);
			}
			if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
			{
				query.like("c.name", "%" + queryParam.getSearchContent() + "%");
			}
			query.addProjection(Projections.property("SUM(od.money)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.eq("o.companyId", queryParam.getCompanyId());
			query.ge("o.createTime", queryParam.getDateMin());
			query.le("o.createTime", queryParam.getDateMax());
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal((map.getResult().get(0).get("SUM(od.money)") == null ? 0 : map.getResult().get(0).get("SUM(od.money)")).toString());
		}
		catch (Exception e)
		{
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getReceiveMoney(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.addProjection(Projections.property("SUM(ifnull(a.money,0))-SUM(ifnull(a.receiveMoney,0))"));

		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getIsExpire().equals(BoolValue.YES))// 结算日期小于等于当前日期 则为到期应收
		{
			query.le("b.reconcilTime", DateUtils.getDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		query.eq("b.isForceComplete", BoolValue.NO);
		query.eq("a.isForceComplete", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(a.money,0))-SUM(ifnull(a.receiveMoney,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.money,0))-SUM(ifnull(a.receiveMoney,0))")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getPaymentMoney(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.addProjection(Projections.property("SUM(ifnull(a.money,0))-SUM(ifnull(a.paymentMoney,0))"));

		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=b.supplierId");

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getIsExpire().equals(BoolValue.YES))// 结算日期小于等于当前日期 则为到期应收
		{
			query.le("b.reconcilTime", DateUtils.getDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		query.eq("b.isForceComplete", BoolValue.NO);
		query.eq("a.isForceComplete", BoolValue.NO);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(a.money,0))-SUM(ifnull(a.paymentMoney,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.money,0))-SUM(ifnull(a.paymentMoney,0))")).toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SupOrCustSumVo> findSaleCusotmerSumByCondition(QueryParam queryParam)
	{
		SearchResult<SupOrCustSumVo> result = new SearchResult<SupOrCustSumVo>();
		List<SupOrCustSumVo> resultList = new ArrayList<SupOrCustSumVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
		// 如果是应收款或到期应收，则减去已收款金额
		if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD || queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			query.addProjection(Projections.property("b.customerId as id,c.name as name,SUM(ifnull(a.money-a.receiveMoney,0)) as money"));
		}
		else
		{
			query.addProjection(Projections.property("b.customerId as id,c.name as name,SUM(ifnull(a.money,0)) as money"));
		}

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);

		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getReconcilDate() != null)
		{
			query.le("b.reconcilTime", queryParam.getReconcilDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD || queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			query.eq("a.isReceiveOver", BoolValue.NO);
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.addGourp("b.customerId");
		query.desc("a.money");

		DynamicQuery queryPage = new DynamicQuery(query, "qp");

		queryPage.setPageIndex(queryParam.getPageNumber());
		queryPage.setPageSize(queryParam.getPageSize());
		queryPage.setIsSearchTotalCount(true);
		queryPage.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryPage, HashMap.class);
		for (Map<String, Object> map : mapResult.getResult())
		{
			SupOrCustSumVo vo = new SupOrCustSumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SupOrCustSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<SupOrCustSumVo> findPurOrOutSupSumByCondition(QueryParam queryParam)
	{
		SearchResult<SupOrCustSumVo> result = new SearchResult<SupOrCustSumVo>();
		List<SupOrCustSumVo> resultList = new ArrayList<SupOrCustSumVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=b.supplierId");
		if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD || queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			query.addProjection(Projections.property("b.supplierId as id,c.name as name,SUM(ifnull(a.money-a.paymentMoney,0)) as money"));
		}
		else
		{
			query.addProjection(Projections.property("b.supplierId as id,c.name as name,SUM(ifnull(a.money,0)) as money"));
		}
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getReconcilDate() != null)
		{
			query.le("b.reconcilTime", queryParam.getReconcilDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD || queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			query.eq("a.isPaymentOver", BoolValue.NO);
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.addGourp("b.supplierId");
		query.desc("a.money");
		DynamicQuery queryPage = new DynamicQuery(query, "qp");
		queryPage.setPageIndex(queryParam.getPageNumber());
		queryPage.setPageSize(queryParam.getPageSize());
		queryPage.setIsSearchTotalCount(true);
		queryPage.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = null;
		try
		{
			mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryPage, HashMap.class);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		for (Map<String, Object> map : mapResult.getResult())
		{
			SupOrCustSumVo vo = new SupOrCustSumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SupOrCustSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<SaleCheckDetailVo> findSaleDetail(QueryParam queryParam)
	{
		SearchResult<SaleCheckDetailVo> result = new SearchResult<SaleCheckDetailVo>();
		List<SaleCheckDetailVo> resultList = new ArrayList<SaleCheckDetailVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		if (queryParam.getBillType() == BillType.SALE_SO)
		{
			query.addProjection(Projections.property("a.id as id,b.billNo as billNo,a.productName as productName," + "a.style as style,u.name as unitName,a.qty as qty,a.price as price,a.money as money"));
		}
		else
		{
			query.addProjection(Projections.property("a.id as id,a.saleOrderBillNo as billNo,a.productName as productName," + "a.style as style,u.name as unitName,a.qty as qty,a.price as price,a.money as money"));
		}

		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Product.class, JoinType.LEFTJOIN, "c", "c.id=a.productId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=c.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "d", "d.id=b.customerId");
			query.inArray("d.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getReconcilDate() != null)
		{
			query.le("b.reconcilTime", queryParam.getReconcilDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("b.customerId", queryParam.getCustomerId());
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleCheckDetailVo vo = new SaleCheckDetailVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleCheckDetailVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<PurchCheckDetailVo> findPurchDetail(QueryParam queryParam)
	{
		SearchResult<PurchCheckDetailVo> result = new SearchResult<PurchCheckDetailVo>();
		List<PurchCheckDetailVo> resultList = new ArrayList<PurchCheckDetailVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		if (queryParam.getBillType() == BillType.PURCH_PO)
		{
			query.addProjection(Projections.property("a.id as id,b.billNo as billNo,a.materialName as materialName,a.specifications as specifications,u2.name as unitName,a.qty as qty," + "a.weight as weight,u.name as valuationUnitName,a.valuationQty as valuationQty,a.valuationPrice as valuationPrice,a.money as money"));
		}
		else
		{
			query.addProjection(Projections.property("a.id as id,a.orderBillNo as billNo,a.materialName as materialName,a.specifications as specifications,u2.name as unitName,a.qty as qty," + "a.weight as weight,u.name as valuationUnitName,a.valuationQty as valuationQty,a.valuationPrice as valuationPrice,a.money as money"));
		}

		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=a.valuationUnitId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u2", "u2.id=a.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "d", "d.id=b.supplierId");
			query.inArray("d.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getReconcilDate() != null)
		{
			query.le("b.reconcilTime", queryParam.getReconcilDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}
		if (queryParam.getwXSumQueryType() == WXSumQueryType.SHOULD || queryParam.getwXSumQueryType() == WXSumQueryType.EXPIRE)
		{
			query.eq("a.isPaymentOver", BoolValue.NO);
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		for (Map<String, Object> map : mapResult.getResult())
		{
			PurchCheckDetailVo vo = new PurchCheckDetailVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PurchCheckDetailVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<OutSourceDetailCheckVo> findOutSourceDetail(QueryParam queryParam)
	{
		SearchResult<OutSourceDetailCheckVo> result = new SearchResult<OutSourceDetailCheckVo>();
		List<OutSourceDetailCheckVo> resultList = new ArrayList<OutSourceDetailCheckVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getDetailCla(), "a");
		if (queryParam.getBillType() == BillType.OUTSOURCE_OP)
		{
			query.addProjection(Projections.property("a.id as id,b.billNo as billNo,a.productName as productName,a.procedureName as procedureName,a.style as style," + "a.qty as qty,a.price as price,a.money as money"));
		}
		else
		{
			query.addProjection(Projections.property("a.id as id,a.outSourceBillNo as billNo,a.productName as productName,a.procedureName as procedureName,a.style as style," + "a.qty as qty,a.price as price,a.money as money"));
		}

		query.createAlias(queryParam.getBillType().getCla(), JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "d", "d.id=b.supplierId");
			query.inArray("d.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (queryParam.getReconcilDate() != null)
		{
			query.le("b.reconcilTime", queryParam.getReconcilDate());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = null;
		try
		{
			mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		for (Map<String, Object> map : mapResult.getResult())
		{
			OutSourceDetailCheckVo vo = new OutSourceDetailCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, OutSourceDetailCheckVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getSumReceiveMoney(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceReceive.class, "b");
		query.addProjection(Projections.property("SUM(ifnull(money,0))"));
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("c.name", "%" + queryParam.getSearchContent() + "%");
		}

		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(money,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(money,0))")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getSumPaymentMoney(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinancePayment.class, "b");
		query.addProjection(Projections.property("SUM(ifnull(money,0))"));
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("s.name", "%" + queryParam.getSearchContent() + "%");
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(money,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(money,0))")).toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceDetailSumVo> findFinanceSumDetail(QueryParam queryParam)
	{
		SearchResult<FinanceDetailSumVo> result = new SearchResult<FinanceDetailSumVo>();
		List<FinanceDetailSumVo> resultList = new ArrayList<FinanceDetailSumVo>();
		DynamicQuery query = new DynamicQuery(queryParam.getBillType().getCla(), "b");
		if (queryParam.getBillType() == BillType.FINANCE_PAY)
		{
			query.addProjection(Projections.property("b.supplierName as name,SUM(ifnull(b.money,0)) as money,SUM(ifnull(b.discount,0)) as discount"));
		}
		else
		{
			query.addProjection(Projections.property("b.customerName as name,SUM(ifnull(b.money,0)) as money,SUM(ifnull(b.discount,0)) as discount"));
		}
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			if (queryParam.getBillType().equals(BillType.FINANCE_REC))
			{
				query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
				query.inArray("c.employeeId", employes);
			}
			else
			{
				query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "c.id=b.supplierId");
				query.inArray("c.employeeId", employes);
			}
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			if (queryParam.getBillType() == BillType.FINANCE_PAY)
			{
				query.like("b.supplierName", "%" + queryParam.getSearchContent() + "%");
			}
			else
			{
				query.like("b.customerName", "%" + queryParam.getSearchContent() + "%");
			}

		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		if (queryParam.getBillType() == BillType.FINANCE_PAY)
		{
			query.addGourp("b.supplierId");
		}
		else
		{
			query.addGourp("b.customerId");
		}

		DynamicQuery newQuery = new DynamicQuery(query, "qu");

		newQuery.setPageIndex(queryParam.getPageNumber());
		newQuery.setPageSize(queryParam.getPageSize());
		newQuery.setIsSearchTotalCount(true);
		newQuery.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(newQuery,

				HashMap.class);
		for (Map<String, Object> map : mapResult.getResult())
		{
			FinanceDetailSumVo vo = new FinanceDetailSumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, FinanceDetailSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getProductStockQty(String companyId, String name)
	{
		DynamicQuery query = new DynamicQuery(StockProduct.class, "a");
		query.createAlias(Product.class, JoinType.LEFTJOIN, "b", "b.id=a.productId");
		query.addProjection(Projections.property("SUM(ifnull(a.qty,0))"));
		if (StringUtils.isNotEmpty(name))
		{
			query.like("b.name", "%" + name + "%");
		}
		query.eq("a.companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("SUM(ifnull(a.qty,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.qty,0))")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getProductStockMoney(String companyId, String name)
	{
		DynamicQuery query = new DynamicQuery(StockProduct.class, "a");
		query.createAlias(Product.class, JoinType.LEFTJOIN, "b", "b.id=a.productId");
		query.addProjection(Projections.property("SUM(ifnull(a.money,0))"));
		if (StringUtils.isNotEmpty(name))
		{
			query.like("b.name", "%" + name + "%");
		}
		query.eq("a.companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(a.money,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.money,0))")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getMaterialStockQty(String companyId, String name)
	{
		DynamicQuery query = new DynamicQuery(StockMaterial.class, "a");
		query.createAlias(Material.class, JoinType.LEFTJOIN, "b", "b.id=a.materialId");
		query.addProjection(Projections.property("SUM(ifnull(a.qty,0))"));
		if (StringUtils.isNotEmpty(name))
		{
			query.like("b.name", "%" + name + "%");
		}
		query.eq("a.companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(a.qty,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.qty,0))")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getMaterialStockMoney(String companyId, String name)
	{
		DynamicQuery query = new DynamicQuery(StockMaterial.class, "a");
		query.createAlias(Material.class, JoinType.LEFTJOIN, "b", "b.id=a.materialId");
		query.addProjection(Projections.property("SUM(ifnull(a.money,0))"));
		if (StringUtils.isNotEmpty(name))
		{
			query.like("b.name", "%" + name + "%");
		}
		query.eq("a.companyId", companyId);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("SUM(ifnull(a.money,0))") == null ? 0 : map.getResult().get(0).get("SUM(ifnull(a.money,0))")).toString());
	}

	@Override
	public SearchResult<StockProductDetailVo> findStockProductList(QueryParam queryParam)
	{
		SearchResult<StockProductDetailVo> result = new SearchResult<StockProductDetailVo>();
		DynamicQuery query = new DynamicQuery(StockProduct.class, "s");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("s, p"));
		query.eqProperty("s.productId", "p.id");
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("p.name", "%" + queryParam.getSearchContent() + "%");
		}
		query.gt("s.qty", 0);
		query.eq("s.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("s.updateTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<StockProductDetailVo> resultList = new ArrayList<StockProductDetailVo>();
		for (Object[] c : temp_result.getResult())
		{
			StockProduct stockProduct = (StockProduct) c[0];
			stockProduct.setProduct((Product) c[1]);
			String unitName = daoFactory.getCommonDao().getEntity(Unit.class, stockProduct.getProduct().getUnitId()).getName();
			StockProductDetailVo vo = new StockProductDetailVo(stockProduct.getProduct().getName(), stockProduct.getProduct().getSpecifications(), unitName, stockProduct.getQty(), stockProduct.getPrice(), stockProduct.getMoney());
			resultList.add(vo);
		}
		result.setCount(temp_result.getCount());
		result.setResult(resultList);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<StockMaterialDetailVo> findStockMaterialList(QueryParam queryParam)
	{
		SearchResult<StockMaterialDetailVo> result = new SearchResult<StockMaterialDetailVo>();
		DynamicQuery query = new DynamicQuery(StockMaterial.class, "a");
		query.addProjection(Projections.property("b.name as materialName,a.specifications as style,a.weight as weight,u.name as unitName,a.qty as qty," + "a.price as price,a.money as money"));
		query.createAlias(Material.class, JoinType.LEFTJOIN, "b", "b.id=a.materialId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=b.stockUnitId");
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.like("b.name", "%" + queryParam.getSearchContent() + "%");
		}
		query.gt("a.qty", 0);
		query.eq("a.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("a.updateTime");
		query.setQueryType(QueryType.JDBC);
		query.setIsSearchTotalCount(true);

		SearchResult<HashMap> mapResult = null;
		try
		{
			mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		List<StockMaterialDetailVo> resultList = new ArrayList<StockMaterialDetailVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			StockMaterialDetailVo vo = new StockMaterialDetailVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, StockMaterialDetailVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			resultList.add(vo);
		}
		result.setResult(resultList);
		result.setCount(mapResult.getCount());
		return result;
	}

}
