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
import java.util.List;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.service.WXScheduleService;
import com.huayin.printmanager.wx.vo.OutSourceScheduleVo;
import com.huayin.printmanager.wx.vo.ProduceScheduleVo;
import com.huayin.printmanager.wx.vo.PurchScheduleVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleScheduleVo;

/**
 * <pre>
 * 微信 - 进度追踪
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXScheduleServiceImpl extends BaseServiceImpl implements WXScheduleService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXScheduleService#findSaleScheduleByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<SaleScheduleVo> findSaleScheduleByCondition(QueryParam queryParam)
	{
		SearchResult<SaleScheduleVo> result = new SearchResult<SaleScheduleVo>();
		DynamicQuery query = new DynamicQuery(SaleOrderDetail.class, "sod");
		query.addProjection(Projections.property("sod, so"));
		query.createAlias(SaleOrder.class, "so");
		query.createAlias(Customer.class, "c");
		query.eqProperty("sod.masterId", "so.id");
		query.eqProperty("c.id", "so.customerId");
		query.eq("so.isCancel", BoolValue.NO);

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "ct", "ct.id=so.customerId");
			query.inArray("ct.employeeId", employes);
		}

		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("sod.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("c.name", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("so.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("so.isCheck", BoolValue.YES);
		query.eq("so.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("so.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<SaleScheduleVo> resultList = new ArrayList<SaleScheduleVo>();
		for (Object[] c : temp_result.getResult())
		{
			SaleOrderDetail detail = (SaleOrderDetail) c[0];
			detail.setMaster((SaleOrder) c[1]);
			String unitName = daoFactory.getCommonDao().getEntity(Unit.class, detail.getUnitId()).getName();
			String customerName = daoFactory.getCommonDao().getEntity(Customer.class, detail.getMaster().getCustomerId()).getName();

			// 查询工单已返写的入库数量 说明:工单源单明细ID即销售订单明细ID
			countStockQty(detail);
			// 查询送货单已返写的对账数量 说明:送货源单明细ID即销售订单明细ID
			coutReturnReconcilQty(detail);

			SaleScheduleVo vo = new SaleScheduleVo(detail.getId(), detail.getDeliveryTime(), detail.getMaster().getBillNo(), customerName, detail.getProductName(), detail.getStyle(), unitName, detail.getQty(), detail.getMoney(), detail.getProduceedQty(), detail.getDeliverQty(), detail.getReconcilQty(), detail.getReceiveMoney());
			resultList.add(vo);
		}
		result.setCount(temp_result.getCount());
		result.setResult(resultList);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXScheduleService#findWorkScheduleByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	public SearchResult<ProduceScheduleVo> findWorkScheduleByCondition(QueryParam queryParam)
	{
		SearchResult<ProduceScheduleVo> result = new SearchResult<ProduceScheduleVo>();
		DynamicQuery query = new DynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "ct", "ct.id=a.customerId");
			query.inArray("ct.employeeId", employes);
		}

		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.customerName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		List<ProduceScheduleVo> resultList = new ArrayList<ProduceScheduleVo>();
		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			detail.setMaster((Work) c[1]);
			Integer percent = 0;
			String schedule = null;
			if (detail.getProduceQty() == detail.getInStockQty())
			{
				percent = 100;
				schedule = "已完工";
			}
			if (detail.getInStockQty() < detail.getProduceQty())
			{
				schedule = "未完工";
				percent = new BigDecimal(detail.getInStockQty()).multiply(new BigDecimal(100)).divide(new BigDecimal(detail.getProduceQty()), 0, BigDecimal.ROUND_HALF_UP).intValue();
			}
			String unitName = daoFactory.getCommonDao().getEntity(Unit.class, detail.getUnitId()).getName();
			ProduceScheduleVo vo = new ProduceScheduleVo(detail.getId(), schedule, detail.getDeliveryTime(), detail.getSourceBillNo(), detail.getMaster().getBillNo(), detail.getCustomerName(), detail.getProductName(), detail.getStyle(), unitName, detail.getProduceQty(), detail.getInStockQty());
			vo.setSchedulePercent(percent);
			resultList.add(vo);
		}
		result.setCount(temp_result.getCount());
		result.setResult(resultList);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXScheduleService#findPurchScheduleByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<PurchScheduleVo> findPurchScheduleByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(PurchOrderDetail.class, "pod");
		query.addProjection(Projections.property("pod, po"));
		query.createAlias(PurchOrder.class, "po");
		query.createAlias(Unit.class, "u");
		query.createAlias(Supplier.class, "s");
		query.eqProperty("pod.masterId", "po.id");
		query.eqProperty("s.id", "po.supplierId");
		query.eqProperty("u.id", "pod.unitId");

		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}

		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("pod.materialName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("po.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("po.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("po.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("po.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<PurchScheduleVo> result = new SearchResult<PurchScheduleVo>();
		result.setResult(new ArrayList<PurchScheduleVo>());
		for (Object[] c : temp_result.getResult())
		{
			PurchOrderDetail detail = (PurchOrderDetail) c[0];
			detail.setMaster((PurchOrder) c[1]);
			PurchScheduleVo vo = new PurchScheduleVo();
			vo.setId(detail.getId());
			vo.setBillNo(detail.getMaster().getBillNo());
			String supplierName = daoFactory.getCommonDao().getEntity(Supplier.class, detail.getMaster().getSupplierId()).getName();
			vo.setSupplierName(supplierName);
			vo.setMaterialName(detail.getMaterialName());
			vo.setDeliveryTime(detail.getDeliveryTime());
			vo.setStyle(detail.getSpecifications());
			vo.setUnit(detail.getPurchUnitName());
			vo.setPurchQty(detail.getQty());
			vo.setPurchMoney(detail.getMoney());
			vo.setStockQty(detail.getStorageQty());
			getStockQty(vo);// 获取下游单据数据
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.wx.service.WXScheduleService#findOutSourceDetailByCondition(com.huayin.printmanager.wx.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<OutSourceScheduleVo> findOutSourceDetailByCondition(QueryParam queryParam)
	{
		SearchResult<OutSourceScheduleVo> result = new SearchResult<OutSourceScheduleVo>();
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "a");
		query.createAlias(OutSourceProcess.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(Supplier.class, "c");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.supplierId");
		Long[] employes = serviceFactory.getWXBasicService().findSharedEmployeeIds(queryParam.getUserId(), queryParam.getCompanyId());
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (StringUtils.isNotEmpty(queryParam.getSearchContent()))
		{
			query.add(Restrictions.or(Restrictions.like("b.supplierName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("a.procedureName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("b.billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		query.eq("b.companyId", queryParam.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<OutSourceScheduleVo> voResult = new ArrayList<OutSourceScheduleVo>();
		for (Object[] c : temp_result.getResult())
		{
			OutSourceProcessDetail detail = (OutSourceProcessDetail) c[0];
			// 查询返写对账数量
			coutReturnReconcil(detail);
			detail.setMaster((OutSourceProcess) c[1]);

			List<String> name = null;
			String unitName = null;
			if (detail.getProcedureId() != null)
			{
				name = getProductNameList(detail.getProcedureId(), detail.getSourceId());
			}
			else
			{// 查询产品单位
				Long unitId = daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId()).getUnitId();
				unitName = daoFactory.getCommonDao().getEntity(Unit.class, unitId).getName();
			}

			OutSourceScheduleVo vo = new OutSourceScheduleVo(detail.getId(), detail.getWorkBillNo(), detail.getPartName(), unitName, detail.getMaster().getDeliveryTime(), detail.getMaster().getBillNo(), detail.getMaster().getSupplierName(), name, detail.getStyle(), detail.getQty(), detail.getMoney(), detail.getArriveQty(), detail.getReconcilQty(), detail.getPayment());
			if (detail.getProcedureName() != null)
			{
				vo.setProcedureName(detail.getProcedureName());
			}
			if (detail.getProductName() != null)
			{
				vo.setProductName(detail.getProductName());
			}
			voResult.add(vo);
		}
		result.setCount(temp_result.getCount());
		result.setResult(voResult);
		return result;
	}

	/**
	 * 查询工单：已返写的入库数量 说明:工单源单明细ID即销售订单明细ID
	 */
	private void countStockQty(SaleOrderDetail saleOrderDetail)
	{
		Integer sum = 0;
		DynamicQuery query = new DynamicQuery(WorkProduct.class, "a");
		query.createAlias(Work.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", saleOrderDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<WorkProduct> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, WorkProduct.class);
		for (WorkProduct workProduct : result.getResult())
		{
			sum += workProduct.getInStockQty();
		}
		saleOrderDetail.setStockQty(sum);
	}

	/**
	 * <pre>
	 * 查询工单：送货单已返写的对账数量 说明:送货源单明细ID即销售订单明细ID
	 * </pre>
	 */
	private void coutReturnReconcilQty(SaleOrderDetail saleOrderDetail)
	{
		Integer sum = 0;// 对账数量
		BigDecimal sumMony = new BigDecimal(0.00);// 已收款
		DynamicQuery query = new DynamicQuery(SaleDeliverDetail.class, "a");
		query.createAlias(SaleDeliver.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", saleOrderDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleDeliverDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleDeliverDetail.class);
		for (SaleDeliverDetail saleDeliverDetail : result.getResult())
		{
			// 退货数量
			Integer reconcil = coutReturnReconcilQty(saleDeliverDetail.getId());
			// 对账数量=送货对账数量-退货(换货)对账数量
			sum += saleDeliverDetail.getReconcilQty() - reconcil;
			BigDecimal receiveMoney = countReturnReceiveMoney(saleDeliverDetail);
			sumMony = sumMony.add(receiveMoney);
		}
		saleOrderDetail.setReconcilQty(sum);
		saleOrderDetail.setReceiveMoney(sumMony);
	}

	/**
	 * <pre>
	 * 查询工单：退货数量
	 * </pre>
	 */
	private Integer coutReturnReconcilQty(Long id)
	{
		Integer sum = 0;// 对账数量
		DynamicQuery query = new DynamicQuery(SaleReturnDetail.class, "a");
		query.createAlias(SaleReturn.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", id);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.returnType", ReturnGoodsType.EXCHANGE);// 换货类型
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleReturnDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReturnDetail.class);
		for (SaleReturnDetail saleReturnDetail : result.getResult())
		{
			sum += saleReturnDetail.getReconcilQty();
		}
		return sum;
	}

	/**
	 * <pre>
	 * 查询工单：查询对账单的返写总金额根据原单
	 * </pre>
	 */
	private BigDecimal countReturnReceiveMoney(SaleDeliverDetail saleDeliverDetail)
	{
		BigDecimal sum = new BigDecimal(0.00);
		DynamicQuery query = new DynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", saleDeliverDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleReconcilDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReconcilDetail.class);
		for (SaleReconcilDetail saleReconcilDetail : result.getResult())
		{
			sum = sum.add(saleReconcilDetail.getReceiveMoney());
		}
		return sum;
	}

	/**
	 * <pre>
	 * 查询采购：获取进度入库、对账数量/金额
	 * </pre>
	 * @param vo
	 */
	private void getStockQty(PurchScheduleVo vo)
	{
		BigDecimal sumReconcilQty = new BigDecimal("0");// 入库部分的对账数量
		DynamicQuery query = new DynamicQuery(PurchStockDetail.class, "ps");
		query.eq("ps.sourceDetailId", vo.getId());
		List<PurchStockDetail> stockList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchStockDetail.class);
		for (PurchStockDetail purchStockDetail : stockList)
		{
			sumReconcilQty = sumReconcilQty.add(purchStockDetail.getReconcilQty());
			getReturnQty(vo, purchStockDetail.getId());
			getPaymentMoney(vo, purchStockDetail.getId(), BillType.PURCH_PN);
		}
		vo.setReconcilQty(vo.getReconcilQty().add(sumReconcilQty));
	}

	/**
	 * <pre>
	 * 查询采购：获取进度退货、对账数量/金额
	 * </pre>
	 * @param vo
	 * @param sourceDetailId 采购入库明细ID
	 */
	private void getReturnQty(PurchScheduleVo vo, Long sourceDetailId)
	{
		BigDecimal sumReturnQty = new BigDecimal("0");// 退货数量
		BigDecimal sumReconcilQty = new BigDecimal("0");// 退货部分的对账数量
		DynamicQuery query = new DynamicQuery(PurchRefundDetail.class, "pr");
		query.eq("pr.sourceDetailId", sourceDetailId);

		List<PurchRefundDetail> stockList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchRefundDetail.class);
		for (PurchRefundDetail purchRefundDetail : stockList)
		{
			sumReturnQty = sumReturnQty.add(purchRefundDetail.getQty());
			sumReconcilQty = sumReconcilQty.add(purchRefundDetail.getReconcilQty());
			getPaymentMoney(vo, purchRefundDetail.getId(), BillType.PURCH_PR);
		}
		vo.setReconcilQty(vo.getReconcilQty().subtract(sumReconcilQty));// 退货部分的对账数量减去
	}

	/**
	 * <pre>
	 * 查询采购：获取对账金额、付款金额
	 * </pre>
	 * @param vo
	 * @param sourceDetailId
	 * @param sourceBillType
	 */
	private void getPaymentMoney(PurchScheduleVo vo, Long sourceDetailId, BillType sourceBillType)
	{
		BigDecimal sumPaymentMoney = new BigDecimal("0");// 付款金额
		DynamicQuery query = new DynamicQuery(PurchReconcilDetail.class, "pr");
		query.eq("pr.sourceDetailId", sourceDetailId);
		query.eq("pr.sourceBillType", sourceBillType);
		List<PurchReconcilDetail> reconcilList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchReconcilDetail.class);
		for (PurchReconcilDetail purchReconcilDetail : reconcilList)
		{
			sumPaymentMoney = sumPaymentMoney.add(purchReconcilDetail.getPaymentMoney());
		}
		vo.setPaymentMoney(vo.getPaymentMoney().add(sumPaymentMoney));

	}

	/**
	 * <pre>
	 * 查询发外：查询返写对账数量 说明:发外加工单明细ID即到货源明细ID
	 * </pre>
	 * @param outSourceProcessDetail
	 */
	private void coutReturnReconcil(OutSourceProcessDetail outSourceProcessDetail)
	{
		BigDecimal sum = new BigDecimal(0);// 对账数量
		BigDecimal sumMony = new BigDecimal(0.00);// 已收款
		DynamicQuery query = new DynamicQuery(OutSourceArriveDetail.class, "a");
		query.createAlias(OutSourceArrive.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", outSourceProcessDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceArriveDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceArriveDetail.class);
		for (OutSourceArriveDetail outSourceArriveDetail : result.getResult())
		{
			// 对账数量 = 到货对账数量-退货对账数量
			// 根据到货ID查询退货对账数量
			BigDecimal reconcil = coutReturnReconcil(outSourceArriveDetail.getId());
			sum = sum.add(outSourceArriveDetail.getReconcilQty().subtract(reconcil));
			BigDecimal paymentMoney = countReturnPaymentMoney(outSourceArriveDetail);
			sumMony = sumMony.add(paymentMoney);
		}
		outSourceProcessDetail.setReconcilQty(sum);
		outSourceProcessDetail.setPayment(sumMony);
	}

	/**
	 * <pre>
	 * 查询发外：查询退货返写的对账数量
	 * </pre>
	 * @param id
	 * @return
	 */
	private BigDecimal coutReturnReconcil(Long id)
	{
		BigDecimal sum = new BigDecimal(0);// 对账数量
		DynamicQuery query = new DynamicQuery(OutSourceReturnDetail.class, "a");
		query.createAlias(OutSourceReturn.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", id);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.returnGoodsType", ReturnGoodsType.EXCHANGE);// 换货
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceReturnDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReturnDetail.class);
		for (OutSourceReturnDetail outSourceReturnDetail : result.getResult())
		{
			sum = sum.add(outSourceReturnDetail.getReconcilQty());
		}
		return sum;
	}

	/**
	 * <pre>
	 * 查询发外：查询返写付款金额 说明:发外到货明细ID即对账源明细ID
	 * </pre>
	 * @param outSourceArriveDetail
	 * @return
	 */
	private BigDecimal countReturnPaymentMoney(OutSourceArriveDetail outSourceArriveDetail)
	{
		BigDecimal sum = new BigDecimal(0.00);
		DynamicQuery query = new DynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", outSourceArriveDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceReconcilDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReconcilDetail.class);
		for (OutSourceReconcilDetail outSourceReconcilDetail : result.getResult())
		{
			sum = sum.add(outSourceReconcilDetail.getPaymentMoney());

		}
		return sum;
	}

	/**
	 * <pre>
	 * 根据工序ID 查所关联的产品
	 * </pre>
	 * @return
	 */
	private List<String> getProductNameList(Long procedureId, Long workId)
	{
		List<String> result = new ArrayList<String>();
		DynamicQuery queryPro = new CompanyDynamicQuery(WorkProcedure.class);
		queryPro.eq("procedureId", procedureId);
		queryPro.eq("workId", workId);
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
