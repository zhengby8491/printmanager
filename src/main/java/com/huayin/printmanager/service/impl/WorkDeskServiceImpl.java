/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.SystemVersionNotice;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OrderType;
import com.huayin.printmanager.service.WorkDeskService;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
@Service
public class WorkDeskServiceImpl extends BaseServiceImpl implements WorkDeskService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.WorkDeskService#getTotalSaleQty(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getTotalSaleQty(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "od");
			query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("SUM(od.qty)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt((map.getResult().get(0).get("SUM(od.qty)") == null ? 0 : map.getResult().get(0).get("SUM(od.qty)")).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getTotalSaleMoney(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "od");
			query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("SUM(od.money)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
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
	public BigDecimal getTotalPurchQty(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}

		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "od");
			query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("SUM(od.qty)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal((map.getResult().get(0).get("SUM(od.qty)") == null ? 0 : map.getResult().get(0).get("SUM(od.qty)")).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getTotalPurchMoney(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "od");
			query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("SUM(od.money)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal((map.getResult().get(0).get("SUM(od.money)") == null ? 0 : map.getResult().get(0).get("SUM(od.money)")).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getCustomerDebt(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "od");
			query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("SUM(od.money-od.receiveMoney)"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal((map.getResult().get(0).get("SUM(od.money-od.receiveMoney)") == null ? 0 : map.getResult().get(0).get("SUM(od.money-od.receiveMoney)")).toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getSupplierDebt(String dateMin, String dateMax)
	{
		if (dateMin == null)
		{
			dateMin = DateUtils.getDate("yyyy-MM-dd") + " 00:00:00";
			dateMax = DateUtils.getDate("yyyy-MM-dd") + " 23:59:59";
		}
		try
		{
			DynamicQuery queryPurch = new CompanyDynamicQuery(PurchReconcilDetail.class, "od");
			queryPurch.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			queryPurch.addProjection(Projections.property("SUM(od.money-od.paymentMoney)"));
			queryPurch.eq("o.isCheck", BoolValue.YES);
			queryPurch.ge("o.createTime", dateMin);
			queryPurch.le("o.createTime", dateMax);
			queryPurch.eq("o.companyId", UserUtils.getCompanyId());
			queryPurch.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> mapPurch = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryPurch, HashMap.class);
			BigDecimal purchMoney = new BigDecimal((mapPurch.getResult().get(0).get("SUM(od.money-od.paymentMoney)") == null ? 0 : mapPurch.getResult().get(0).get("SUM(od.money-od.paymentMoney)")).toString());

			DynamicQuery queryOut = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "od");
			queryOut.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			queryOut.addProjection(Projections.property("SUM(od.money-od.paymentMoney)"));
			queryOut.eq("o.isCheck", BoolValue.YES);
			queryOut.ge("o.createTime", dateMin);
			queryOut.le("o.createTime", dateMax);
			queryOut.eq("o.companyId", UserUtils.getCompanyId());
			queryOut.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> mapOut = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryOut, HashMap.class);
			BigDecimal outMoney = new BigDecimal((mapOut.getResult().get(0).get("SUM(od.money-od.paymentMoney)") == null ? 0 : mapOut.getResult().get(0).get("SUM(od.money-od.paymentMoney)")).toString());
			return purchMoney.add(outMoney);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getProductStockQty()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class, "s");
			query.addProjection(Projections.property("ifnull(SUM(s.qty),0) total"));
			query.setQueryType(QueryType.JDBC);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("total").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getProductStockMoney()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class, "s");
			query.addProjection(Projections.property("ifnull(SUM(s.money),0) total"));
			query.setQueryType(QueryType.JDBC);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal(map.getResult().get(0).get("total").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getMaterialStockQty()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
			query.addProjection(Projections.property("ifnull(SUM(s.qty),0) total"));
			query.setQueryType(QueryType.JDBC);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal(map.getResult().get(0).get("total").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getMaterialStockMoney()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
			query.addProjection(Projections.property("ifnull(SUM(s.money),0) total"));
			query.setQueryType(QueryType.JDBC);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return new BigDecimal(map.getResult().get(0).get("total").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new BigDecimal(0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getMaterialMinStock()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class, "s");
			query.createAlias(Material.class, JoinType.INNERJOIN, "m", "m.id=s.materialId");
			query.addProjection(Projections.property("count(1)"));
			query.add(Restrictions.ltProperty("s.qty", "m.minStockNum"));
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotArriveOutSource()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "od");
			query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.add(Restrictions.gtProperty("od.qty", "od.arriveQty"));
			query.ge("o.deliveryTime", DateUtils.getDate());
			query.le("o.deliveryTime", getThreeAfterDate());
			query.eq("o.isCheck", BoolValue.YES);
			query.eq("o.companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotStockPurch()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "pd");
			query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "p", "p.id=pd.masterId");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.add(Restrictions.gtProperty("pd.qty", "pd.storageQty"));
			query.ge("pd.deliveryTime", DateUtils.getDate());
			query.le("pd.deliveryTime", getThreeAfterDate());
			query.eq("p.isCheck", BoolValue.YES);
			query.eq("p.companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotDeliveSale()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "sd");
			query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "s", "s.id=sd.masterId");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.add(Restrictions.gtProperty("sd.qty", "sd.deliverQty"));
			query.ge("sd.deliveryTime", DateUtils.getDate());
			query.le("sd.deliveryTime", getThreeAfterDate());
			query.eq("s.isCheck", BoolValue.YES);
			query.eq("s.companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotPayment()
	{
		try
		{
			DynamicQuery queryPurch = new CompanyDynamicQuery(PurchReconcilDetail.class, "pd");
			queryPurch.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "p", "p.id=pd.masterId");
			queryPurch.addProjection(Projections.property("count(1)"));
			queryPurch.setQueryType(QueryType.JDBC);
			queryPurch.add(Restrictions.gtProperty("ABS(pd.money)", "ABS(pd.paymentMoney)"));
			queryPurch.ge("p.reconcilTime", DateUtils.getDate());
			queryPurch.le("p.reconcilTime", getThreeAfterDate());
			queryPurch.eq("p.isCheck", BoolValue.YES);
			queryPurch.eq("p.companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> mapPurch = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryPurch, HashMap.class);
			return Integer.parseInt(mapPurch.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotReceiveOrder()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "sd");
			query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "s", "s.id=sd.masterId");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.add(Restrictions.gtProperty("ABS(sd.money)", "ABS(sd.receiveMoney)"));
			query.ge("s.reconcilTime", DateUtils.getDate());
			query.le("s.reconcilTime", getThreeAfterDate());
			query.eq("s.isCheck", BoolValue.YES);
			query.eq("s.companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckSale()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrder.class, "s");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("s.isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckPurch()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class, "p");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckWork()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(Work.class, "w");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckPayment()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class, "p");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckWriteoffPayment()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class, "p");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckOutSource()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(OutSourceProcess.class, "o");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckReceive()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class, "r");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getNotCheckWriteoffReceive()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class, "r");
			query.addProjection(Projections.property("count(1)"));
			query.setQueryType(QueryType.JDBC);
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			return Integer.parseInt(map.getResult().get(0).get("count(1)").toString());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * <pre>
	 * 获取3天后的日期字符串
	 * </pre>
	 * @return
	 */
	private String getThreeAfterDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) + 3);
		return sdf.format(now.getTime()) + " 23:59:59";
	}

	@Override
	public Map<String, BigDecimal> getTotalSaleMoneyByTimeType(String dateMin, String dateMax, String timeType)
	{
		Map<String, BigDecimal> returMap = new LinkedMap<String, BigDecimal>();

		if ("ThisMonth".equals(timeType))
		{
			for (Integer day = 1; day <= DateUtils.getDaysByYearMonth(Integer.parseInt(dateMin.substring(0, 4)), Integer.parseInt(dateMin.substring(5, 7))); day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 8).concat(strDay), new BigDecimal(0));
			}
		}
		else if ("Year".equals(timeType))
		{
			for (Integer day = 1; day <= 12; day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 5).concat(strDay), new BigDecimal(0));
			}
		}

		try
		{
			DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "od");
			query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			if ("ThisMonth".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m-%d') days"));
			}
			else if ("Year".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m') days"));
			}
			query.addProjection(Projections.property("SUM(od.money) total"));
			query.isNotNull("o.createTime");
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.addGourp("days");
			query.asc("days");
			query.setQueryType(QueryType.JDBC);
			@SuppressWarnings("rawtypes")
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
			for (HashMap<?, ?> entry : map.getResult())
			{
				returMap.put(entry.get("days").toString(), new BigDecimal(entry.get("total").toString()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return returMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, BigDecimal> getTotalMaterialMoneyByTimeType(String dateMin, String dateMax, String timeType)
	{
		Map<String, BigDecimal> returMap = new LinkedMap<String, BigDecimal>();
		if ("ThisMonth".equals(timeType))
		{
			for (Integer day = 1; day <= DateUtils.getDaysByYearMonth(Integer.parseInt(dateMin.substring(0, 4)), Integer.parseInt(dateMin.substring(5, 7))); day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 8).concat(strDay), new BigDecimal(0));
			}
		}
		else if ("Year".equals(timeType))
		{
			for (Integer day = 1; day <= 12; day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 5).concat(strDay), new BigDecimal(0));
			}
		}

		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "od");
			query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			if ("ThisMonth".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m-%d') days"));
			}
			else if ("Year".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m') days"));
			}
			query.addProjection(Projections.property("SUM(od.money) total"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.addGourp("days");
			query.asc("days");
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

			for (HashMap entry : map.getResult())
			{
				returMap.put(entry.get("days").toString(), new BigDecimal(entry.get("total").toString()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return returMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<String, BigDecimal> getTotalOutSourceMoneyByTimeType(String dateMin, String dateMax, String timeType)
	{
		Map<String, BigDecimal> returMap = new LinkedMap<String, BigDecimal>();
		if ("ThisMonth".equals(timeType))
		{
			for (Integer day = 1; day <= DateUtils.getDaysByYearMonth(Integer.parseInt(dateMin.substring(0, 4)), Integer.parseInt(dateMin.substring(5, 7))); day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 8).concat(strDay), new BigDecimal(0));
			}
		}
		else if ("Year".equals(timeType))
		{
			for (Integer day = 1; day <= 12; day++)
			{
				String strDay = day.toString();
				if (day < 10)
				{
					strDay = "0".concat(day.toString());
				}
				returMap.put(dateMin.substring(0, 5).concat(strDay), new BigDecimal(0));
			}
		}

		try
		{
			DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "od");
			query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
			if ("ThisMonth".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m-%d') days"));
			}
			else if ("Year".equals(timeType))
			{
				query.addProjection(Projections.property("DATE_FORMAT(o.createTime,'%Y-%m') days"));
			}
			query.addProjection(Projections.property("SUM(od.money) total"));
			query.eq("o.isCheck", BoolValue.YES);
			query.ge("o.createTime", dateMin);
			query.le("o.createTime", dateMax);
			query.eq("o.companyId", UserUtils.getCompanyId());
			query.addGourp("days");
			query.asc("days");
			query.setQueryType(QueryType.JDBC);
			SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

			for (HashMap entry : map.getResult())
			{
				returMap.put(entry.get("days").toString(), new BigDecimal(entry.get("total").toString()));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return returMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getWorkSaleSumQty()
	{
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.orderType", OrderType.NORMAL);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.produceedQty"));// 销售数>生产数
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));// 销售数>送货
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getDeliverSumQty()
	{
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工

		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	public Integer getDeliverWorkSumQty()
	{
		DynamicQuery query = new DynamicQuery(WorkProduct.class, "a");
		query.addProjection(Projections.property("count(1)"));
		query.createAlias(Work.class, "b");
		query.createAlias(Customer.class, "c");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "a.customerId");
		query.eqProperty("a.sourceBillNo", "''");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		query.add(Restrictions.gtProperty("a.saleProduceQty", "a.deliverQty"));
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);

		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPurchSumQty()
	{
		DynamicQuery query = new DynamicQuery(PurchOrderDetail.class, "a");
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.add(Restrictions.gtProperty("a.qty", "a.storageQty"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getArriveSumQty()
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "a");
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		query.add(Restrictions.gtProperty("a.qty", "a.arriveQty"));// 加工数>到货数
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getReceiveSumQty()
	{
		DynamicQuery query = new DynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.receiveMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isReceiveOver", BoolValue.NO);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentPurchSumQty()
	{
		DynamicQuery query = new DynamicQuery(PurchReconcilDetail.class, "a");
		query.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.paymentMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentOutSourceSumQty()
	{
		DynamicQuery query = new DynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getWorkPurchQty()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
		query.addProjection(Projections.property("count(1)"));
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "wm.workId=w.id");
		query.eq("wm.isNotPurch", BoolValue.NO);
		query.add(Restrictions.gtProperty("wm.qty", "wm.purchQty"));
		query.eq("wm.isCustPaper", BoolValue.NO);
		query.eq("w.isCheck", BoolValue.YES);
		query.eq("wm.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getWorkTakeQty()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
		query.addProjection(Projections.property("count(1)"));
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "wm.workId=w.id");
		query.eq("wm.isNotTake", BoolValue.NO);
		query.eq("w.isForceComplete", BoolValue.NO);
		query.add(Restrictions.gtProperty("wm.qty", "wm.takeQty"));
		query.eq("wm.isCustPaper", BoolValue.NO);
		query.eq("w.isCheck", BoolValue.YES);
		query.eq("wm.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPurchReconcilQty()
	{
		// 采购入库
		DynamicQuery queryStock = new CompanyDynamicQuery(PurchStockDetail.class, "ps");
		queryStock.addProjection(Projections.property("count(1)"));
		queryStock.createAlias(PurchStock.class, JoinType.LEFTJOIN, "p", "p.id=ps.masterId");
		queryStock.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			queryStock.inArray("s.employeeId", employes);
		}
		queryStock.eq("p.isCheck", BoolValue.YES);
		queryStock.add(Restrictions.gtProperty("ps.qty", "ps.reconcilQty"));
		queryStock.eq("ps.isForceComplete", BoolValue.NO);
		queryStock.eq("p.isForceComplete", BoolValue.NO);
		queryStock.eq("ps.companyId", UserUtils.getCompanyId());
		queryStock.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapStock = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryStock, HashMap.class);
		Integer stockQty = Integer.parseInt((mapStock.getResult().get(0).get("count(1)") == null ? 0 : mapStock.getResult().get(0).get("count(1)")).toString());

		// 采购退货
		DynamicQuery queryRefund = new CompanyDynamicQuery(PurchRefundDetail.class, "pr");
		queryRefund.addProjection(Projections.property("count(1)"));
		queryRefund.createAlias(PurchRefund.class, JoinType.LEFTJOIN, "p", "p.id=pr.masterId");
		queryRefund.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
		if (employes.length > 0)
		{
			queryRefund.inArray("s.employeeId", employes);
		}
		queryRefund.eq("p.isCheck", BoolValue.YES);
		queryRefund.add(Restrictions.gtProperty("pr.qty", "pr.reconcilQty"));
		queryRefund.eq("pr.isForceComplete", BoolValue.NO);
		queryRefund.eq("p.isForceComplete", BoolValue.NO);
		queryRefund.eq("pr.companyId", UserUtils.getCompanyId());
		queryRefund.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapRefund = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryRefund, HashMap.class);
		Integer refundQty = Integer.parseInt((mapRefund.getResult().get(0).get("count(1)") == null ? 0 : mapRefund.getResult().get(0).get("count(1)")).toString());
		return stockQty + refundQty;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getSaleReconcilQty()
	{
		// 销售送货
		DynamicQuery queryStock = new CompanyDynamicQuery(SaleDeliverDetail.class, "a");
		queryStock.addProjection(Projections.property("count(1)"));
		queryStock.createAlias(SaleDeliver.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		queryStock.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			queryStock.inArray("c.employeeId", employes);
		}
		queryStock.eq("b.isCheck", BoolValue.YES);
		queryStock.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));
		queryStock.eq("a.isForceComplete", BoolValue.NO);
		queryStock.eq("b.isForceComplete", BoolValue.NO);
		queryStock.eq("a.companyId", UserUtils.getCompanyId());
		queryStock.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapStock = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryStock, HashMap.class);
		Integer stockQty = Integer.parseInt((mapStock.getResult().get(0).get("count(1)") == null ? 0 : mapStock.getResult().get(0).get("count(1)")).toString());

		// 销售退货
		DynamicQuery queryRefund = new CompanyDynamicQuery(SaleDeliverDetail.class, "a");
		queryRefund.addProjection(Projections.property("count(1)"));
		queryRefund.createAlias(SaleDeliver.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		queryRefund.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
		if (employes.length > 0)
		{
			queryRefund.inArray("c.employeeId", employes);
		}
		queryRefund.eq("b.isCheck", BoolValue.YES);
		queryRefund.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));
		queryRefund.eq("a.isForceComplete", BoolValue.NO);
		queryRefund.eq("b.isForceComplete", BoolValue.NO);
		queryRefund.eq("a.companyId", UserUtils.getCompanyId());
		queryRefund.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapRefund = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryRefund, HashMap.class);
		Integer refundQty = Integer.parseInt((mapRefund.getResult().get(0).get("count(1)") == null ? 0 : mapRefund.getResult().get(0).get("count(1)")).toString());
		return stockQty + refundQty;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getProcessReconcilQty()
	{
		// 发外到货
		DynamicQuery queryStock = new CompanyDynamicQuery(OutSourceArriveDetail.class, "a");
		queryStock.addProjection(Projections.property("count(1)"));
		queryStock.createAlias(OutSourceArrive.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			queryStock.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			queryStock.inArray("s.employeeId", employes);
		}
		queryStock.eq("b.isCheck", BoolValue.YES);
		queryStock.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));
		queryStock.eq("a.isForceComplete", BoolValue.NO);
		queryStock.eq("b.isForceComplete", BoolValue.NO);
		queryStock.eq("a.companyId", UserUtils.getCompanyId());
		queryStock.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapStock = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryStock, HashMap.class);
		Integer stockQty = Integer.parseInt((mapStock.getResult().get(0).get("count(1)") == null ? 0 : mapStock.getResult().get(0).get("count(1)")).toString());

		// 发外退货
		DynamicQuery queryRefund = new CompanyDynamicQuery(OutSourceReturnDetail.class, "a");
		queryRefund.addProjection(Projections.property("count(1)"));
		queryRefund.createAlias(OutSourceReturn.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		if (employes.length > 0)
		{
			queryRefund.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			queryRefund.inArray("s.employeeId", employes);
		}
		queryRefund.eq("b.isCheck", BoolValue.YES);
		queryRefund.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));
		queryRefund.eq("a.isForceComplete", BoolValue.NO);
		queryRefund.eq("b.isForceComplete", BoolValue.NO);
		queryRefund.eq("a.companyId", UserUtils.getCompanyId());
		queryRefund.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapRefund = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryRefund, HashMap.class);
		Integer refundQty = Integer.parseInt((mapRefund.getResult().get(0).get("count(1)") == null ? 0 : mapRefund.getResult().get(0).get("count(1)")).toString());
		return stockQty + refundQty;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer getWorkStockQty()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "wp");
		query.addProjection(Projections.property("count(1)"));
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "w.id=wp.masterId");
		query.add(Restrictions.gtProperty("wp.produceQty", "wp.inStockQty"));
		query.eq("wp.isForceComplete", BoolValue.NO);
		query.eq("w.isForceComplete", BoolValue.NO);
		query.eq("w.isCheck", BoolValue.YES);
		query.eq("w.isOutSource", BoolValue.NO);
		query.eq("wp.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	@Override
	public String queryExpire()
	{
		Company company = UserUtils.getCompany();
		int days = (int) DateUtils.getDistanceOfTwoDate(new Date(), company.getExpireTime());
		if (company.getIsFormal() == BoolValue.YES)
		{// 正式用户
			if (days <= 7)
			{
				if (days < 0)
				{
					return 3 + "_" + days;
				}
				return 1 + "_" + days;
			}
		}
		else
		{// 体验用户
			if (days <= 3)
			{
				if (days < 0)
				{
					return 4 + "_" + days;
				}
				return 2 + "_" + days;
			}
		}
		return 0 + "_" + days;
	}

	@Override
	@Transactional
	public SystemVersionNotice getVersionNotice()
	{

		User user = daoFactory.getCommonDao().getEntity(User.class, UserUtils.getUserId());
		if (user != null)
		{
			if (user.getVersionNotify() == 0)
			{
				DynamicQuery query = new DynamicQuery(SystemVersionNotice.class, "nc");
				query.eq("nc.publish", BoolValue.YES);
				query.desc("nc.createTime");
				query.setPageIndex(0);
				query.setPageSize(1);
				SystemVersionNotice systemVersionNotice = daoFactory.getCommonDao().getByDynamicQuery(query, SystemVersionNotice.class);

				user.setVersionNotify(1);
				daoFactory.getCommonDao().updateEntity(user);
				return systemVersionNotice;
			}
		}
		return null;
	}

}
