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

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OrderType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.wx.service.WXStatisticsService;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 单据数量统计
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXStatisticsServiceImpl extends BaseServiceImpl implements WXStatisticsService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getWorkSaleSumQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getWorkSaleSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
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
		query.eq("b.companyId", queryParam.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.produceedQty"));// 销售数>生产数
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));// 销售数>送货
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getDeliverSumQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getDeliverSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
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
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getPurchSumQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPurchSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(PurchOrderDetail.class, "a");
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.add(Restrictions.gtProperty("a.qty", "a.storageQty"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getArriveSumQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getArriveSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "a");
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
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
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getReceiveSumQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getReceiveSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
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
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getPaymentPurchSumQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentPurchSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(PurchReconcilDetail.class, "a");
		query.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
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
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getPaymentOutSourceSumQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentOutSourceSumQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "b", "a.masterId=b.id");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getSaleOrderCheckQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getSaleOrderCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(SaleOrder.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getPurchOrderCheckQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPurchOrderCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(PurchOrder.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getOutSourceCheckQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getOutSourceCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcess.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getReceiveCheckQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getReceiveCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceReceive.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXStatisticsService#getPaymentCheckQty(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinancePayment.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getReceiveWriteCheckQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getReceiveWriteCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffReceive.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXStatisticsService#getPaymentWriteCheckQty(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getPaymentWriteCheckQty(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffPayment.class, "b");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.addProjection(Projections.property("count(1)"));
		query.eq("b.isCheck", BoolValue.NO);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt((map.getResult().get(0).get("count(1)") == null ? 0 : map.getResult().get(0).get("count(1)")).toString());
	}

}
