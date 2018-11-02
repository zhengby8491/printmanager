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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
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
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.service.WXWarnService;
import com.huayin.printmanager.wx.vo.OutSourceNotArriveVo;
import com.huayin.printmanager.wx.vo.OutSourceNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotStockVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleNotDeliveryVo;
import com.huayin.printmanager.wx.vo.SaleNotProductionVo;
import com.huayin.printmanager.wx.vo.SaleNotReceiveVo;

/**
 * <pre>
 * 微信 - 未清预警
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXWarnServiceImpl extends BaseServiceImpl implements WXWarnService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXWarnService#findNotProductionSaleByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SaleNotProductionVo> findNotProductionSaleByCondition(QueryParam queryParam)
	{
		SearchResult<SaleNotProductionVo> result = new SearchResult<SaleNotProductionVo>();
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.billNo as billNo,c.name as customerName,a.productName as productName,a.deliveryTime as deliveryTime," + "d.name as unitName,a.qty as saleQty,a.produceedQty as produceedQty,b.createTime as createTime"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "b.customerId=c.id");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "d", "d.id=a.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}

		query.eq("b.orderType", OrderType.NORMAL);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.isCancel", BoolValue.NO);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		query.add(Restrictions.gtProperty("a.qty", "a.produceedQty"));// 销售数>生产数
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));// 销售数>送货
		query.eq("b.companyId", queryParam.getCompanyId());
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("c.name", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("a.deliveryTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<SaleNotProductionVo> list = new ArrayList<SaleNotProductionVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleNotProductionVo vo = new SaleNotProductionVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleNotProductionVo.class);
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXWarnService#findNotDeliverSaleByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SaleNotDeliveryVo> findNotDeliverSaleByCondition(QueryParam queryParam)
	{
		SearchResult<SaleNotDeliveryVo> result = new SearchResult<SaleNotDeliveryVo>();
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.billNo as billNo,c.name as customerName,a.productName as productName,a.deliveryTime as deliveryTime," + "d.name as unitName,a.qty as saleQty,a.deliverQty as deliverQty,b.createTime as createTime"));
		query.createAlias(SaleOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "b.customerId=c.id");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "d", "d.id=a.unitId");
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("c.name", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.asc("a.deliveryTime");
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<SaleNotDeliveryVo> list = new ArrayList<SaleNotDeliveryVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleNotDeliveryVo vo = new SaleNotDeliveryVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleNotDeliveryVo.class);
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXWarnService#findNotStockPurchByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<PurchNotStockVo> findNotStockPurchByCondition(QueryParam queryParam)
	{
		SearchResult<PurchNotStockVo> result = new SearchResult<PurchNotStockVo>();
		DynamicQuery query = new DynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.billNo as billNo,c.name as supplierName,a.materialName as materialName,a.deliveryTime as deliveryTime," + "d.name as unitName,a.qty as purchQty,a.storageQty as stockQty,b.createTime as createTime,a.specifications as specifications"));
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "b.supplierId=c.id");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "d", "d.id=a.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.materialName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.add(Restrictions.gtProperty("a.qty", "a.storageQty"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("a.deliveryTime");
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<PurchNotStockVo> list = new ArrayList<PurchNotStockVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			PurchNotStockVo vo = new PurchNotStockVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PurchNotStockVo.class);
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

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXWarnService#findNotArriveOutSourceByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<OutSourceNotArriveVo> findNotArriveOutSourceByCondition(QueryParam queryParam)
	{
		SearchResult<OutSourceNotArriveVo> result = new SearchResult<OutSourceNotArriveVo>();
		DynamicQuery query = new CompanyDynamicQuery(OutSourceProcessDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.billNo as billNo,c.name as supplierName,a.productName as productName,a.workBillNo as workBillNo,b.deliveryTime as deliveryTime," 
		+ "u.name as unitName,a.procedureId as procedureId,a.procedureName as procedureName,a.style as style,a.qty as qty,a.arriveQty as arriveQty,b.createTime as createTime,a.type as type,a.sourceId as workId"));
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "b.supplierId=c.id");
		query.createAlias(Product.class, JoinType.LEFTJOIN, "d", "d.id=a.productId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=d.unitId");
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.procedureName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.workBillNo", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.add(Restrictions.gtProperty("a.qty", "a.arriveQty"));// 加工数>到货数
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.asc("b.deliveryTime");
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
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
		List<OutSourceNotArriveVo> list = new ArrayList<OutSourceNotArriveVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			OutSourceNotArriveVo vo = new OutSourceNotArriveVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, OutSourceNotArriveVo.class);
				if (StringUtils.isBlank(vo.getProductName()))
				{
					vo.setName(getProductNameList(vo.getProcedureId(), vo.getWorkBillNo()));
				}
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXWarnService#findShouldReceive(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SaleNotReceiveVo> findShouldReceive(QueryParam queryParam)
	{
		SearchResult<SaleNotReceiveVo> result = new SearchResult<SaleNotReceiveVo>();
		DynamicQuery query = new DynamicQuery(SaleReconcilDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.billNo as billNo,c.name as customerName,a.productName as productName,b.reconcilTime as reconcilTime," + "a.saleOrderBillNo as saleBillNo,d.name as unitName,a.qty as saleQty,a.money as saleMoney,a.receiveMoney as receiveMoney,b.createTime as createTime"));
		query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "b.customerId=c.id");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "d", "d.id=a.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("c.name", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.saleOrderBillNo", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%")));
		}

		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.receiveMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isReceiveOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("b.reconcilTime");
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<SaleNotReceiveVo> list = new ArrayList<SaleNotReceiveVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleNotReceiveVo vo = new SaleNotReceiveVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleNotReceiveVo.class);
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

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXWarnService#findPurchShouldPayment(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<PurchNotPaymentVo> findPurchShouldPayment(QueryParam queryParam)
	{
		SearchResult<PurchNotPaymentVo> result = new SearchResult<PurchNotPaymentVo>();
		DynamicQuery query = new DynamicQuery(PurchReconcilDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,a.orderBillNo as billNo,c.name as supplierName,a.materialName as materialName,b.reconcilTime as reconcilTime," + "d.name as unitName,a.qty as qty,a.money as money,b.createTime as createTime,a.paymentMoney as paymentMoney"));
		query.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "b.supplierId=c.id");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "d", "d.id=a.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.orderBillNo", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.materialName", "%" + queryParam.getSearchContent() + "%")));
		}
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.paymentMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.eq("b.companyId", queryParam.getCompanyId());
		query.asc("b.reconcilTime");
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
		List<PurchNotPaymentVo> list = new ArrayList<PurchNotPaymentVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			PurchNotPaymentVo vo = new PurchNotPaymentVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PurchNotPaymentVo.class);
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXWarnService#findOutSourceShouldPayment(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<OutSourceNotPaymentVo> findOutSourceShouldPayment(QueryParam queryParam)
	{
		SearchResult<OutSourceNotPaymentVo> result = new SearchResult<OutSourceNotPaymentVo>();
		DynamicQuery query = new DynamicQuery(OutSourceReconcilDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,a.outSourceBillNo as billNo,c.name as supplierName,a.productName as productName,a.workBillNo as workBillNo,b.reconcilTime as reconcilTime," + "u.name as unitName,a.procedureId as procedureId,a.procedureName as procedureName,a.style as style,a.qty as qty,a.money as money,a.paymentMoney as paymentMoney,b.createTime as createTime,a.type as type"));
		query.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "c", "b.supplierId=c.id");
		query.createAlias(Product.class, JoinType.LEFTJOIN, "d", "d.id=a.productId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=d.unitId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.outSourceBillNo", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.procedureName", "%" + queryParam.getSearchContent() + "%")));
		}
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.paymentMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.eq("b.companyId", queryParam.getCompanyId());
		query.asc("b.reconcilTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<OutSourceNotPaymentVo> list = new ArrayList<OutSourceNotPaymentVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			OutSourceNotPaymentVo vo = new OutSourceNotPaymentVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, OutSourceNotPaymentVo.class);
				if (StringUtils.isBlank(vo.getProductName()))
				{
					vo.setName(getProductNameList(vo.getProcedureId(), vo.getWorkBillNo()));
				}
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

	/**
	 * <pre>
	 * 根据工序ID 查所关联的产品
	 * </pre>
	 * @param procedureId
	 * @param workBillNo
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:57:08, think
	 */
	private List<String> getProductNameList(Long procedureId, String workBillNo)
	{
		List<String> result = new ArrayList<String>();
		DynamicQuery queryPro = new DynamicQuery(WorkProcedure.class);
		queryPro.eq("procedureId", procedureId);
		queryPro.eq("workBillNo", workBillNo);
		List<WorkProcedure> workProcedureList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryPro, WorkProcedure.class);
		if (workProcedureList == null)
		{
			return result;
		}
		for (WorkProcedure workProcedure : workProcedureList)
		{
			if (workProcedure.getWorkProcedureType() == WorkProcedureType.PART)
			{
				DynamicQuery query = new CompanyDynamicQuery(WorkPart2Product.class);
				query.eq("workPartId", workProcedure.getParentId());
				List<WorkPart2Product> resultList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkPart2Product.class);
				for (WorkPart2Product workPart2Product : resultList)
				{
					if (!result.contains(workPart2Product.getProductName()))
					{
						result.add(workPart2Product.getProductName());
					}
				}
			} else if (workProcedure.getWorkProcedureType() == WorkProcedureType.PRODUCT)
			{
				DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
				query.eq("masterId", workProcedure.getWorkId());
				List<WorkProduct> resultList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProduct.class);
				for (WorkProduct workProduct : resultList)
				{
					if (!result.contains(workProduct.getProductName()))
					{
						result.add(workProduct.getProductName());
					}
				}
			}
			
		}
		return result;
	}
}
