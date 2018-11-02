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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.SettlementClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.service.WXCheckService;
import com.huayin.printmanager.wx.vo.OutSourceCheckVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PaymentCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.PurchCheckVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.ReceiveCheckVo;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.SaleCheckVo;

/**
 * <pre>
 * 微信 - 订单审核
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXCheckServiceImpl extends BaseServiceImpl implements WXCheckService
{

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SaleCheckVo> findSaleByCondition(QueryParam queryParam)
	{
		SearchResult<SaleCheckVo> result = new SearchResult<SaleCheckVo>();
		DynamicQuery query = new DynamicQuery(SaleOrder.class, "a");
		query.addProjection(Projections.property("a.id as id,a.billNo as billNo,b.name as customerName,a.linkName as linkName,a.mobile as mobile," + "a.totalMoney as money,a.totalTax as tax, a.noTaxTotalMoney as noTaxMoney"));
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "b", "b.id=a.customerId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=a.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eq("a.isCheck", BoolValue.NO);
		query.eq("a.companyId", queryParam.getCompanyId());
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.name", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.desc("a.createTime");
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
		List<SaleCheckVo> list = new ArrayList<SaleCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleCheckVo vo = new SaleCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleCheckVo.class);
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
	public SearchResult<SaleCheckDetailVo> findSaleDetailById(QueryParam queryParam)
	{
		SearchResult<SaleCheckDetailVo> result = new SearchResult<SaleCheckDetailVo>();
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,a.deliveryTime as deliveryTime,a.productName as productName,a.style as style," + "b.name as unitName,a.qty as qty,a.price as price,a.money as money"));
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "b", "b.id=a.unitId");
		query.eq("a.masterId", queryParam.getId());
		query.eq("a.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<SaleCheckDetailVo> list = new ArrayList<SaleCheckDetailVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SaleCheckDetailVo vo = new SaleCheckDetailVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SaleCheckDetailVo.class);
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
	@Transactional
	public Boolean checkSale(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(SaleOrder.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			SaleOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleOrder.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Boolean checkSaleAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(SaleOrder.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<SaleOrder> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, SaleOrder.class);
			for (SaleOrder order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<PurchCheckVo> findPurchByCondition(QueryParam queryParam)
	{
		SearchResult<PurchCheckVo> result = new SearchResult<PurchCheckVo>();
		DynamicQuery query = new DynamicQuery(PurchOrder.class, "a");
		query.addProjection(Projections.property("a.id as id,a.billNo as billNo,b.name as supplierName,a.linkName as linkName,a.mobile as mobile," + "a.totalMoney as money,a.totalTax as tax, a.noTaxTotalMoney as noTaxMoney"));
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "b", "b.id=a.supplierId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("b.employeeId", employes);
		}
		query.eq("a.isCheck", BoolValue.NO);
		query.eq("a.companyId", queryParam.getCompanyId());
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<PurchCheckVo> list = new ArrayList<PurchCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			PurchCheckVo vo = new PurchCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PurchCheckVo.class);
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
	public SearchResult<PurchCheckDetailVo> findPurchDetailById(QueryParam queryParam)
	{
		SearchResult<PurchCheckDetailVo> result = new SearchResult<PurchCheckDetailVo>();
		DynamicQuery query = new DynamicQuery(PurchOrderDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,a.deliveryTime as deliveryTime,a.materialName as materialName,a.specifications as specifications," + "a.weight as weight,b.name as unitName,c.name as valuationUnitName,a.qty as qty,a.valuationQty as valuationQty,a.valuationPrice as valuationPrice,a.money as money"));
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "b", "b.id=a.unitId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "c", "c.id=a.valuationUnitId");
		query.eq("a.masterId", queryParam.getId());
		query.eq("a.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<PurchCheckDetailVo> list = new ArrayList<PurchCheckDetailVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			PurchCheckDetailVo vo = new PurchCheckDetailVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PurchCheckDetailVo.class);
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
	@Transactional
	public Boolean checkPurch(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(PurchOrder.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			PurchOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, PurchOrder.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean checkPurchAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(PurchOrder.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<PurchOrder> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrder.class);
			for (PurchOrder order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<OutSourceCheckVo> findOutSourceByCondition(QueryParam queryParam)
	{
		SearchResult<OutSourceCheckVo> result = new SearchResult<OutSourceCheckVo>();
		DynamicQuery query = new DynamicQuery(OutSourceProcess.class, "a");
		query.addProjection(Projections.property("a.id as id,a.billNo as billNo,b.name as supplierName,a.linkName as linkName,a.mobile as mobile," + "a.totalMoney as money,a.totalTax as tax, a.noTaxTotalMoney as noTaxMoney"));
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "b", "b.id=a.supplierId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("b.employeeId", employes);
		}
		query.eq("a.isCheck", BoolValue.NO);
		query.eq("a.companyId", queryParam.getCompanyId());
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<OutSourceCheckVo> list = new ArrayList<OutSourceCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			OutSourceCheckVo vo = new OutSourceCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, OutSourceCheckVo.class);
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
	public SearchResult<OutSourceDetailCheckVo> findOutSourceDetailById(QueryParam queryParam)
	{
		SearchResult<OutSourceDetailCheckVo> result = new SearchResult<OutSourceDetailCheckVo>();
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "a");
		query.addProjection(Projections.property("a.id as id,b.deliveryTime as deliveryTime,a.productName as productName,a.procedureName as procedureName,a.style as style," + "a.qty as qty,a.price as price,a.money as money,a.productId as productId"));
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.eq("a.masterId", queryParam.getId());
		query.eq("a.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<OutSourceDetailCheckVo> list = new ArrayList<OutSourceDetailCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			OutSourceDetailCheckVo vo = new OutSourceDetailCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, OutSourceDetailCheckVo.class);
				if (vo.getProductId() != null)
				{
					Long unitId = daoFactory.getCommonDao().getEntity(Product.class, vo.getProductId()).getUnitId();
					vo.setUnitName(daoFactory.getCommonDao().getEntity(Unit.class, unitId).getName());
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

	@Override
	@Transactional
	public Boolean checkOutSource(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(OutSourceProcess.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			OutSourceProcess order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcess.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Boolean checkOutSourceAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(OutSourceProcess.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<OutSourceProcess> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceProcess.class);
			for (OutSourceProcess order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<ReceiveCheckVo> findReceiveByCondition(QueryParam queryParam)
	{
		SearchResult<ReceiveCheckVo> result = new SearchResult<ReceiveCheckVo>();
		DynamicQuery query = new DynamicQuery(FinanceReceive.class, "a");
		query.addProjection(Projections.property("a.id as id,a.billNo as billNo,a.customerName as customerName,b.name as settlementClassName," + "a.billTime as billTime,a.money as money,a.discount as discount,a.advance as advance"));
		query.createAlias(SettlementClass.class, JoinType.LEFTJOIN, "b", "b.id=a.settlementClassId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=r.customerId");
			query.inArray("c.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.customerName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("a.isCheck", BoolValue.NO);
		query.eq("a.companyId", queryParam.getCompanyId());
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<ReceiveCheckVo> list = new ArrayList<ReceiveCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			ReceiveCheckVo vo = new ReceiveCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, ReceiveCheckVo.class);
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
	public SearchResult<FinanceReceiveDetail> findReceiveDetailById(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceReceive.class, "b");
		query.createAlias(FinanceReceiveDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		query.eq("a.masterId", queryParam.getId());
		query.desc("b.createTime");
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<FinanceReceiveDetail> result = new SearchResult<FinanceReceiveDetail>();
		result.setResult(new ArrayList<FinanceReceiveDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceReceiveDetail detail = (FinanceReceiveDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceReceiveDetail();
				detail.setMasterId(((FinanceReceive) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceReceive) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceReceive) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public Boolean checkReceive(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceReceive.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			FinanceReceive order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceReceive.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean checkReceiveAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceReceive.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<FinanceReceive> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceReceive.class);
			for (FinanceReceive order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<PaymentCheckVo> findPaymentByCondition(QueryParam queryParam)
	{
		SearchResult<PaymentCheckVo> result = new SearchResult<PaymentCheckVo>();
		DynamicQuery query = new DynamicQuery(FinancePayment.class, "a");
		query.addProjection(Projections.property("a.id as id,a.billNo as billNo,a.supplierName as supplierName,b.name as settlementClassName," + "a.billTime as billTime,a.money as money,a.discount as discount,a.advance as advance"));
		query.createAlias(SettlementClass.class, JoinType.LEFTJOIN, "b", "b.id=a.settlementClassId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=a.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("a.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("a.isCheck", BoolValue.NO);
		query.eq("a.companyId", queryParam.getCompanyId());
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<PaymentCheckVo> list = new ArrayList<PaymentCheckVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			PaymentCheckVo vo = new PaymentCheckVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, PaymentCheckVo.class);
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
	public SearchResult<FinancePaymentDetail> findPaymentDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinancePayment.class, "b");
		query.createAlias(FinancePaymentDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		query.eq("a.masterId", queryParam.getId());
		query.eq("a.companyId", queryParam.getCompanyId());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<FinancePaymentDetail> result = new SearchResult<FinancePaymentDetail>();
		result.setResult(new ArrayList<FinancePaymentDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinancePaymentDetail detail = (FinancePaymentDetail) c[1];
			if (detail == null)
			{
				detail = new FinancePaymentDetail();
				detail.setMasterId(((FinancePayment) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinancePayment) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinancePayment) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public Boolean checkPayment(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinancePayment.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			FinancePayment order = daoFactory.getCommonDao().getByDynamicQuery(query, FinancePayment.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean checkPaymentAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinancePayment.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<FinancePayment> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinancePayment.class);
			for (FinancePayment order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public SearchResult<FinanceWriteoffReceive> findWriteoffReceiveByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffReceive.class, "w");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=w.customerId");
			query.inArray("c.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("w.customerName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("w.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("w.isCheck", BoolValue.NO);
		query.eq("w.companyId", queryParam.getCompanyId());
		query.desc("w.createTime");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceWriteoffReceive.class);
	}

	@Override
	public SearchResult<FinanceWriteoffReceiveDetail> findWriteoffReceiveDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffReceive.class, "b");
		query.createAlias(FinanceWriteoffReceiveDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		query.eq("a.masterId", queryParam.getId());
		query.desc("b.createTime");
		query.eq("a.companyId", queryParam.getCompanyId());
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<FinanceWriteoffReceiveDetail> result = new SearchResult<FinanceWriteoffReceiveDetail>();
		result.setResult(new ArrayList<FinanceWriteoffReceiveDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceWriteoffReceiveDetail detail = (FinanceWriteoffReceiveDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceWriteoffReceiveDetail();
				detail.setMasterId(((FinanceWriteoffReceive) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceWriteoffReceive) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceWriteoffReceive) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public Boolean checkWriteoffReceive(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceWriteoffReceive.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			FinanceWriteoffReceive order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffReceive.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean checkWriteoffReceiveAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceWriteoffReceive.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<FinanceWriteoffReceive> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffReceive.class);
			for (FinanceWriteoffReceive order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public SearchResult<FinanceWriteoffPayment> findWriteoffPaymentByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffPayment.class, "w");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=w.supplierId");
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("w.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("w.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("w.isCheck", BoolValue.NO);
		query.eq("w.companyId", queryParam.getCompanyId());
		query.desc("w.createTime");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceWriteoffPayment.class);
	}

	@Override
	public SearchResult<FinanceWriteoffPaymentDetail> findWriteoffPaymentDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(FinanceWriteoffPayment.class, "b");
		query.createAlias(FinanceWriteoffPaymentDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		query.eq("a.masterId", queryParam.getId());
		query.eq("a.companyId", queryParam.getCompanyId());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<FinanceWriteoffPaymentDetail> result = new SearchResult<FinanceWriteoffPaymentDetail>();
		result.setResult(new ArrayList<FinanceWriteoffPaymentDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceWriteoffPaymentDetail detail = (FinanceWriteoffPaymentDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceWriteoffPaymentDetail();
				detail.setMasterId(((FinanceWriteoffPayment) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceWriteoffPayment) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceWriteoffPayment) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public Boolean checkWriteoffPayment(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceWriteoffPayment.class);
			query.eq("id", queryParam.getId());
			query.eq("companyId", queryParam.getCompanyId());
			FinanceWriteoffPayment order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffPayment.class);
			order.setIsCheck(BoolValue.YES);
			order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
			order.setCheckTime(new Date());
			daoFactory.getCommonDao().updateEntity(order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}

	@Override
	@Transactional
	public Boolean checkWriteoffPaymentAll(QueryParam queryParam)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(FinanceWriteoffPayment.class);
			query.inArray("id", queryParam.getIds());
			query.eq("companyId", queryParam.getCompanyId());
			List<FinanceWriteoffPayment> listOrder = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffPayment.class);
			for (FinanceWriteoffPayment order : listOrder)
			{
				order.setIsCheck(BoolValue.YES);
				order.setCheckUserName(daoFactory.getCommonDao().getEntity(User.class, queryParam.getUserId()).getUserName());
				order.setCheckTime(new Date());
			}
			daoFactory.getCommonDao().updateAllEntity(listOrder);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

}
