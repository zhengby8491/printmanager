/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.outsource.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.outsource.OutSourceReturnService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:53:09, zhengby, 代码规范
 */
@Service
public class OutSourceReturnServiceImpl extends BaseServiceImpl implements OutSourceReturnService
{

	@Override
	public OutSourceReturn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturn.class);
		query.eq("id", id);
		OutSourceReturn order = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceReturn.class);

		// OutSourceReturn order = daoFactory.getCommonDao().getEntity(OutSourceReturn.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public OutSourceReturnDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturnDetail.class);
		query.eq("id", id);
		OutSourceReturnDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceReturnDetail.class);

		// OutSourceReturnDetail detail = daoFactory.getCommonDao().getEntity(OutSourceReturnDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(OutSourceReturn.class, detail.getMasterId()));
		return detail;
	}

	@Override
	public OutSourceReturn lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturn.class);
		query.eq("id", id);
		OutSourceReturn order = daoFactory.getCommonDao().lockByDynamicQuery(query, OutSourceReturn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OutSourceReturnDetail.class);
		query_detail.eq("masterId", id);
		List<OutSourceReturnDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OutSourceReturnDetail.class, LockType.LOCK_WAIT);
		/*
		 * OutSourceReturn order = daoFactory.getCommonDao().lockObject(OutSourceReturn.class, id);
		 * List<OutSourceReturnDetail> detailList = this.getDetailList(id); for (OutSourceReturnDetail detail : detailList)
		 * { detail = daoFactory.getCommonDao().lockObject(OutSourceReturnDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<OutSourceReturnDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturnDetail.class);
		query.eq("masterId", id);
		List<OutSourceReturnDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceReturnDetail.class);
		return detailList;
	}

	@Override
	@Transactional
	public ServiceResult<OutSourceReturn> save(OutSourceReturn order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.OUTSOURCE_OR);
		order.setBillNo(UserUtils.createBillNo(BillType.OUTSOURCE_OR));
		order.setUserNo(UserUtils.getUser().getUserNo());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			order.setCreateName(e.getName());
		}
		else
		{
			order.setCreateName(UserUtils.getUserName());
		}
		order.setCreateTime(new Date());
		order.setCreateEmployeeId(UserUtils.getEmployeeId());
		order.setIsCheck(BoolValue.NO);
		order.setIsForceComplete(BoolValue.NO);
		order = daoFactory.getCommonDao().saveEntity(order);
		for (OutSourceReturnDetail detail : order.getDetailList())
		{
			detail.setReconcilQty(new BigDecimal(0));
			detail.setReconcilMoney(new BigDecimal(0));
			detail.setIsForceComplete(BoolValue.NO);

			detail.setMasterId(order.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setTaxRateName(taxRate.getName());
			detail.setTaxRatePercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			// 反写到货单退货数量
			OutSourceArriveDetail source = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getSourceDetailId());
			source.setReturnQty(source.getReturnQty().add(detail.getQty()));
			source.setReturnMoney(source.getReturnMoney().add(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
			{// 换货：加工单到货数-换货数
				OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
				process.setArriveQty(process.getArriveQty().subtract(detail.getQty()));
				process.setArriveMoney(process.getArriveMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(process);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		ServiceResult<OutSourceReturn> serviceResult = new ServiceResult<OutSourceReturn>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(order.getId(), order.getForceCheck()));
		}
		serviceResult.setReturnValue(order);
		return serviceResult;
	}

	@Override
	@Transactional
	public ServiceResult<OutSourceReturn> update(OutSourceReturn order)
	{
		// 是否要审核标识
		BoolValue flag = order.getIsCheck();

		ServiceResult<OutSourceReturn> serviceResult = new ServiceResult<OutSourceReturn>();

		// 再更新数据
		order.setIsCheck(BoolValue.NO);
		List<OutSourceReturnDetail> add_detail_list = new ArrayList<OutSourceReturnDetail>();
		List<OutSourceReturnDetail> update_detail_list = new ArrayList<OutSourceReturnDetail>();
		List<OutSourceReturnDetail> del_detail_list = new ArrayList<OutSourceReturnDetail>();

		OutSourceReturn old_order = serviceFactory.getOutSourceReturnService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, OutSourceReturnDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();

		for (OutSourceReturnDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (OutSourceReturnDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (OutSourceReturnDetail new_detail : order.getDetailList())
		{
			OutSourceReturnDetail old_detail = old_detail_map.get(new_detail.getId());
			// 反写到货单退货数量
			OutSourceArriveDetail source = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, new_detail.getSourceDetailId());
			if (new_detail.getId() == null)
			{// 新增
				new_detail.setMasterId(order.getId());
				new_detail.setReconcilQty(new BigDecimal(0));
				new_detail.setReconcilMoney(new BigDecimal(0));
				new_detail.setIsForceComplete(BoolValue.NO);
				new_detail.setCompanyId(UserUtils.getCompanyId());
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), new_detail.getTaxRateId());
				new_detail.setTaxRateName(taxRate.getName());
				new_detail.setTaxRatePercent(taxRate.getPercent());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());

				source.setReturnQty(source.getReturnQty().add(new_detail.getQty()));
				source.setReturnMoney(source.getReturnMoney().add(new_detail.getMoney()));
				if (order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{// 换货：加工单到货数-换货数
					OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
					process.setArriveQty(process.getArriveQty().subtract(new_detail.getQty()));
					process.setArriveMoney(process.getArriveMoney().subtract(new_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}
				add_detail_list.add(new_detail);
			}
			else
			{
				// 退换货都需要：更新退货逻辑
				source.setReturnQty(source.getReturnQty().subtract(old_detail.getQty().subtract(new_detail.getQty())));
				source.setReturnMoney(source.getReturnMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
				if (old_order.getReturnGoodsType() == ReturnGoodsType.RETURN)
				{// 老类型=退货
					if (order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
					{// 新类型=换货：新增换货逻辑
						OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
						process.setArriveQty(process.getArriveQty().subtract(new_detail.getQty()));
						process.setArriveMoney(process.getArriveMoney().subtract(new_detail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
				}
				else if (old_order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{// 老类型=换货
					if (order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
					{// 新类型=换货：更新换货逻辑
						OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
						process.setArriveQty(process.getArriveQty().add(old_detail.getQty().subtract(new_detail.getQty())));
						process.setArriveMoney(process.getArriveMoney().add(old_detail.getMoney()).subtract(new_detail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
					else
					{// 新类型=退货:删除换货记录逻辑
						OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
						process.setArriveQty(process.getArriveQty().add(old_detail.getQty()));
						process.setArriveMoney(process.getArriveMoney().add(old_detail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}
				}
				PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容
				update_detail_list.add(old_detail);
			}
			daoFactory.getCommonDao().updateEntity(source);
		}

		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))
			{
				OutSourceReturnDetail old_detail = old_detail_map.get(id);
				// 反写到货单退货数量
				OutSourceArriveDetail source = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, old_detail.getSourceDetailId());
				source.setReturnQty(source.getReturnQty().subtract(old_detail.getQty()));
				source.setReturnMoney(source.getReturnMoney().subtract(old_detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
				if (old_order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{// 换货：加工单到货数-换货数
					OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
					process.setArriveQty(process.getArriveQty().add(old_detail.getQty()));
					process.setArriveMoney(process.getArriveMoney().add(old_detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}
				del_detail_list.add(old_detail);
			}
		}
		daoFactory.getCommonDao().saveAllEntity(add_detail_list);
		daoFactory.getCommonDao().updateAllEntity(update_detail_list);
		daoFactory.getCommonDao().deleteAllEntity(del_detail_list);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表

		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(order.getId(), order.getForceCheck()));
		}
		serviceResult.setReturnValue(order);
		return serviceResult;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OutSourceReturn order = this.lock(id);
		List<OutSourceReturnDetail> detailList = order.getDetailList();

		// OutSourceReturn order = daoFactory.getCommonDao().lockObject(OutSourceReturn.class, id);
		// List<OutSourceReturnDetail> detailList = getDetailList(id);
		for (OutSourceReturnDetail detail : detailList)
		{
			// 反写到货单退货数量
			OutSourceArriveDetail source = daoFactory.getCommonDao().lockObject(OutSourceArriveDetail.class, detail.getSourceDetailId());
			source.setReturnQty(source.getReturnQty().subtract(detail.getQty()));
			source.setReturnMoney(source.getReturnMoney().subtract(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (order.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
			{// 换货：加工单到货数-换货数
				OutSourceProcessDetail process = daoFactory.getCommonDao().lockObject(OutSourceProcessDetail.class, source.getSourceDetailId());
				process.setArriveQty(process.getArriveQty().add(detail.getQty()));
				process.setArriveMoney(process.getArriveMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(process);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OutSourceReturn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturn.class, "o");
		query.addProjection(Projections.property("o"));
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("o.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "o.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReturn.class);
	}

	@Override
	public SearchResult<OutSourceReturnDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturnDetail.class, "a");
		query.createAlias(OutSourceReturn.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(Supplier.class, "c");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.supplierId");

		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOutSourceBillNo()))
		{
			query.like("a.outSourceBillNo", "%" + queryParam.getOutSourceBillNo() + "%");
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
		}

		if (queryParam.getOutSourceType() != null)
		{
			query.eq("a.type", queryParam.getOutSourceType());
		}
		if (queryParam.getSupplierClassId() != null && queryParam.getSupplierClassId() != -1)
		{
			query.eq("c.supplierClassId", queryParam.getSupplierClassId());
		}

		if (queryParam.getProcedureType() != null)
		{
			query.eq("a.procedureType", queryParam.getProcedureType());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<OutSourceReturnDetail> result = new SearchResult<OutSourceReturnDetail>();
		result.setResult(new ArrayList<OutSourceReturnDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceReturnDetail detail = (OutSourceReturnDetail) c[0];
			detail.setMaster((OutSourceReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<OutSourceReturnDetail> findForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturnDetail.class, "a");
		query.createAlias(OutSourceReturn.class, "b");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "b.supplierId");
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
		if (StringUtils.isNoneBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.outSourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));// 退货数>对账数
		query.desc("b.createTime");
		query.eq("b.isCheck", BoolValue.YES);// 已审核

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber() == null ? 1 : queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize() == null ? Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_RETURN_PAGE_SIZE_MAX)) : queryParam.getPageSize());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<OutSourceReturnDetail> result = new SearchResult<OutSourceReturnDetail>();
		result.setResult(new ArrayList<OutSourceReturnDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceReturnDetail detail = (OutSourceReturnDetail) c[0];
			detail.setMaster((OutSourceReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	@Transactional
	public List<StockProduct> check(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();

		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);
			// 先判断是否已经审核
			if (order.getIsCheck() == BoolValue.YES)
			{
				throw new BusinessException("已审核");
			}
			for (OutSourceReturnDetail detail : order.getDetailList())
			{
				if (detail.getType() == OutSourceType.PRODUCT)
				{
					DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
					query.eq("productId", detail.getProductId());
					query.eq("warehouseId", detail.getWarehouseId());
					StockProduct stock = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
					if (stock == null)
					{
						stock = new StockProduct();
						stock.setProduct(daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId()));
						stock.setQty(0);
					}
					else
					{
						stock.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stock.getProductId()));
					}
					if (detail.getQty().intValue() > stock.getQty())
					{
						list.add(stock);
					}
				}
			}

			// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
			if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
			{
				list.clear();

				if (!serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OR, id, BoolValue.YES))
				{
					return null;
				}
				// 库存操作
				for (OutSourceReturnDetail detail : order.getDetailList())
				{
					if (detail.getType() == OutSourceType.PRODUCT)
					{
						StockProduct stockProduct = new StockProduct();
						stockProduct.setProductId(detail.getProductId());
						stockProduct.setWarehouseId(detail.getWarehouseId());
						stockProduct.setPrice(detail.getPrice());
						stockProduct.setUpdateTime(new Date());
						stockProduct.setQty(detail.getQty().intValue());
						stockProduct.setMoney(detail.getMoney());
						serviceFactory.getStockProductService().stock(stockProduct, InOutType.OUT);

						Product product = daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId());
						// 出库记录
						StockProductLog log = new StockProductLog();
						log.setBillId(order.getId());
						log.setBillType(order.getBillType());
						log.setBillNo(order.getBillNo());
						log.setSourceId(detail.getId());
						log.setCreateTime(new Date());
						log.setCompanyId(UserUtils.getCompanyId());
						log.setCode(product.getCode());
						log.setProductClassId(product.getProductClassId());
						log.setCustomerMaterialCode(product.getCustomerMaterialCode());
						log.setProductName(detail.getProductName());
						log.setProductId(detail.getProductId());
						log.setSpecifications(detail.getStyle());
						log.setWarehouseId(detail.getWarehouseId());
						log.setUnitId(product.getUnitId());
						log.setPrice(detail.getPrice());
						log.setOutQty(detail.getQty().intValue());
						log.setOutMoney(detail.getMoney());
						Integer storgeQty = serviceFactory.getStockProductService().getStockQty(detail.getProductId(), detail.getWarehouseId());
						log.setStorgeQty(storgeQty);
						daoFactory.getCommonDao().saveEntity(log);
					}
				}
			}
		}
		else
		{
			if (!serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OR, id, BoolValue.YES))
			{
				return null;
			}
		}
		return list;
	}


	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();

		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			OutSourceReturn order = serviceFactory.getOutSourceReturnService().get(id);

			if (!serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OR, id, BoolValue.NO))
			{
				return null;
			}
			// 库存操作
			for (OutSourceReturnDetail detail : order.getDetailList())
			{
				if (detail.getType() == OutSourceType.PRODUCT)
				{
					StockProduct stockProduct = new StockProduct();
					stockProduct.setProductId(detail.getProductId());
					stockProduct.setWarehouseId(detail.getWarehouseId());
					stockProduct.setPrice(detail.getPrice());
					stockProduct.setUpdateTime(new Date());
					stockProduct.setQty(detail.getQty().intValue());
					stockProduct.setMoney(detail.getMoney());
					serviceFactory.getStockProductService().backStock(stockProduct, InOutType.OUT);

					DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
					query.eq("billId", id);
					List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
					daoFactory.getCommonDao().deleteAllEntity(logs);
				}
			}

		}
		else
		{
			if (!serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OR, id, BoolValue.NO))
			{
				return null;
			}
		}
		return list;
	}

	@Override
	public BigDecimal coutReturnReconcil(Long id)
	{
		BigDecimal sum = new BigDecimal(0);// 对账数量
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReturnDetail.class, "a");
		query.createAlias(OutSourceReturn.class, "b");
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", id);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.returnGoodsType", ReturnGoodsType.EXCHANGE);// 换货
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<OutSourceReturnDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OutSourceReturnDetail.class);
		for (OutSourceReturnDetail outSourceReturnDetail : result.getResult())
		{
			sum = sum.add(outSourceReturnDetail.getReconcilQty());
		}
		return sum;
	}
}
