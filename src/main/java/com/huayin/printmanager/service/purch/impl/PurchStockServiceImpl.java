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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrder;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrderDetail;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.purch.PurchStockService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购入库
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月20日, raintear
 * @version 	   2.0, 2018年2月23日下午2:41:07, zhengby, 代码规范
 */
@Service
public class PurchStockServiceImpl extends BaseServiceImpl implements PurchStockService
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	@Override
	public PurchStock get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStock.class);
		query.eq("id", id);
		PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query, PurchStock.class);

		DynamicQuery queryD = new CompanyDynamicQuery(PurchStockDetail.class);
		queryD.eq("masterId", id);
		List<PurchStockDetail> purchStockDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, PurchStockDetail.class);
		if (purchStock != null)
		{
			purchStock.setDetailList(purchStockDetail);
		}
		return purchStock;

	}

	@Override
	public PurchStock get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStock.class);
		query.eq("billNo", billNo);
		PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query, PurchStock.class);
		return purchStock;
	}

	@Override
	public PurchStockDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, PurchStockDetail.class);
	}

	@Override
	public PurchStockDetail getDetailMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class);
		query.eq("id", id);
		PurchStockDetail purchStockDetail = daoFactory.getCommonDao().getByDynamicQuery(query, PurchStockDetail.class);

		if (null != purchStockDetail)
		{
			DynamicQuery query2 = new CompanyDynamicQuery(PurchStock.class);
			query2.eq("id", purchStockDetail.getMasterId());
			PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query2, PurchStock.class);
			purchStockDetail.setMaster(purchStock);
		}

		return purchStockDetail;
	}

	@Override
	public PurchStock lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStock.class);
		query.eq("id", id);
		PurchStock order = daoFactory.getCommonDao().lockByDynamicQuery(query, PurchStock.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(PurchStockDetail.class);
		query_detail.eq("masterId", id);
		List<PurchStockDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, PurchStockDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	@Transactional
	public PurchStock save(PurchStock purchStock)
	{
		BoolValue flag = purchStock.getIsCheck();		// 标识是否保存并审核
		purchStock.setIsCheck(BoolValue.NO);				// 默认未审核
		purchStock.setCompanyId(UserUtils.getCompanyId());
		purchStock.setBillNo(UserUtils.createBillNo(BillType.PURCH_PN));
		purchStock.setBillType(BillType.PURCH_PN);

		purchStock.setIsForceComplete(BoolValue.NO);
		purchStock.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			purchStock.setCreateName(e.getName());
		}
		else
		{
			purchStock.setCreateName(UserUtils.getUserName());
		}
		// purchStock.setCreateName(UserUtils.getUserName());
		purchStock.setCreateEmployeeId(UserUtils.getEmployeeId());
		daoFactory.getCommonDao().saveEntity(purchStock);

		for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
		{
			if ("".equals(purchStockDetail.getCode()) || purchStockDetail.getCode() == null)
			{
				continue;
			}
			PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());

			purchStockDetail.setMasterId(purchStock.getId());
			purchStockDetail.setCompanyId(UserUtils.getCompanyId());
			purchStockDetail.setReconcilQty(new BigDecimal(0));
			purchStockDetail.setRefundQty(new BigDecimal(0));
			purchStockDetail.setIsForceComplete(BoolValue.NO);
			purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchStockDetail.getQty()).add(purchStockDetail.getFreeQty()));
			// 来源是印刷家下单
			if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
			{
				ExteriorPurchOrderDetail extDetail = serviceFactory.getExteriorPurchService().getDetailHasMater(purchOrderDetail.getExtOrderDetailId());
				extDetail.setStorageQty(extDetail.getStorageQty().add(purchStockDetail.getQty()));
				daoFactory.getCommonDao().updateEntity(extDetail);
				ExteriorPurchOrder master = extDetail.getMaster();
				master.setLastOperatTime(new Date()); // 更新主表的操作时间
				daoFactory.getCommonDao().updateEntity(master);
			}
			
			daoFactory.getCommonDao().saveEntity(purchStockDetail);
			daoFactory.getCommonDao().updateEntity(purchOrderDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(purchStock.getId());
		}
		return purchStock;
	}

	@Override
	@Transactional
	public PurchStock update(PurchStock purchStock)
	{
		BoolValue flag = purchStock.getIsCheck();						// 标识是否保存并审核
		purchStock.setIsCheck(BoolValue.NO);								// 默认未审核
		PurchStock purchStock_ = this.lockHasChildren(purchStock.getId());
		// 判断是否已审核
		if (purchStock_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, PurchStockDetail> old_detail_map = ConverterUtils.list2Map(purchStock_.getDetailList(), "id");
		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (PurchStockDetail newItem : purchStock.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (PurchStockDetail oldItem : purchStock_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(PurchStockDetail.class, id);
				// 反写采购订单的入库数量
				PurchStockDetail stockDetail = old_detail_map.get(id);
				PurchOrderDetail purchOrderDetail = daoFactory.getCommonDao().getEntity(PurchOrderDetail.class, stockDetail.getSourceDetailId());
				purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(stockDetail.getQty()).subtract(stockDetail.getFreeQty()));
				// 来源是印刷家下单
				if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
				{
					ExteriorPurchOrderDetail extDetail = serviceFactory.getExteriorPurchService().getDetailHasMater(purchOrderDetail.getExtOrderDetailId());
					extDetail.setStorageQty(extDetail.getStorageQty().subtract(stockDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(extDetail);
					ExteriorPurchOrder master = extDetail.getMaster();
					master.setLastOperatTime(new Date()); // 更新主表的操作时间
					daoFactory.getCommonDao().updateEntity(master);
				}
			}
		
		}

		PropertyClone.copyProperties(purchStock_, purchStock, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(purchStock.getCurrencyType());
		purchStock_.setRate(exchangeRate.getRate());
		purchStock_.setRateId(exchangeRate.getId());
		purchStock_.setUpdateName(UserUtils.getUserName());
		purchStock_.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(purchStock_);

		for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
		{
			if ("".equals(purchStockDetail.getCode()) || purchStockDetail.getCode() == null)
			{
				continue;
			}
			if (purchStockDetail.getId() != null)
			{
				PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());

				PurchStockDetail purchStockDetail_ = this.getDetail(purchStockDetail.getId());

				// 数量反写 修改后已入库数量= 订单已入库数量-之前入库数量+本次修改的入库数量
				purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(purchStockDetail_.getQty()).subtract(purchStockDetail_.getFreeQty()).add(purchStockDetail.getQty()).add(purchStockDetail.getFreeQty()));
				PropertyClone.copyProperties(purchStockDetail_, purchStockDetail, false, null, new String[] { "memo" });
				daoFactory.getCommonDao().updateEntity(purchStockDetail_);
				// 来源是印刷家下单
				if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
				{
					ExteriorPurchOrderDetail extDetail = serviceFactory.getExteriorPurchService().getDetailHasMater(purchOrderDetail.getExtOrderDetailId());
					extDetail.setStorageQty(extDetail.getStorageQty().subtract(purchStockDetail_.getQty()).add(purchStockDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(extDetail);
					ExteriorPurchOrder master = extDetail.getMaster();
					master.setLastOperatTime(new Date()); // 更新主表的操作时间
					daoFactory.getCommonDao().updateEntity(master);
				}
			}
			else
			{
				PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());

				purchStockDetail.setMasterId(purchStock.getId());
				purchStockDetail.setCompanyId(UserUtils.getCompanyId());
				purchStockDetail.setReconcilQty(new BigDecimal(0));
				purchStockDetail.setRefundQty(new BigDecimal(0));
				purchStockDetail.setIsForceComplete(BoolValue.NO);
				purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchStockDetail.getQty()).add(purchStockDetail.getFreeQty()));
				// 来源是印刷家下单
				if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
				{
					ExteriorPurchOrderDetail extDetail = serviceFactory.getExteriorPurchService().getDetail(purchOrderDetail.getExtOrderDetailId());
					extDetail.setStorageQty(extDetail.getStorageQty().add(purchStockDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(extDetail);
					ExteriorPurchOrder master = extDetail.getMaster();
					master.setLastOperatTime(new Date()); // 更新主表的操作时间
					daoFactory.getCommonDao().updateEntity(master);
				}
				daoFactory.getCommonDao().saveEntity(purchStockDetail);
				daoFactory.getCommonDao().updateEntity(purchOrderDetail);
			}

		}
		if (flag == BoolValue.YES)
		{
			this.check(purchStock.getId());
		}
		return purchStock;

	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			PurchStock master = this.lockHasChildren(id);
			for (PurchStockDetail purchStockDetail : master.getDetailList())
			{
				DynamicQuery queryOD = new CompanyDynamicQuery(PurchOrderDetail.class);
				queryOD.eq("id", purchStockDetail.getSourceDetailId());
				PurchOrderDetail purchOrderDetail = daoFactory.getCommonDao().getByDynamicQuery(queryOD, PurchOrderDetail.class);
				purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(purchStockDetail.getQty()).subtract(purchStockDetail.getFreeQty()));
				
				if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
				{
					// 来源是印刷家下单
					ExteriorPurchOrderDetail extDetail = serviceFactory.getExteriorPurchService().getDetailHasMater(purchOrderDetail.getExtOrderDetailId());
					extDetail.setStorageQty(extDetail.getStorageQty().subtract(purchStockDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(extDetail);
					ExteriorPurchOrder master1 = extDetail.getMaster();
					master1.setLastOperatTime(new Date()); // 更新主表的操作时间
					daoFactory.getCommonDao().updateEntity(master1);
				}
				
				daoFactory.getCommonDao().updateEntity(purchOrderDetail);

			}
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
			daoFactory.getCommonDao().deleteEntity(master);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public PurchStock copy(Long id)
	{
		/**
		 * 查详情列表
		 */
		DynamicQuery query1 = new CompanyDynamicQuery(PurchStockDetail.class);
		query1.eq("masterId", id);
		List<PurchStockDetail> purchStockDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query1, PurchStockDetail.class);
		/**
		 * 查入库单信息
		 */
		DynamicQuery query2 = new CompanyDynamicQuery(PurchStock.class);
		query2.eq("id", id);
		PurchStock purchStock = daoFactory.getCommonDao().getByDynamicQuery(query2, PurchStock.class);
		/**
		 * 将id置空
		 */
		purchStock.setId(null);
		purchStock.setCreateTime(null);
		purchStock.setCreateName(null);
		purchStock.setCreateEmployeeId(null);
		purchStock.setCheckTime(null);
		purchStock.setCheckUserName(null);
		purchStock.setIsCheck(BoolValue.NO);
		purchStock.setPrintCount(0);
		for (PurchStockDetail list : purchStockDetailList)
		{
			list.setId(null);
			list.setSourceId(null);
			list.setSourceBillNo(null);
			list.setSourceBillType(null);
			list.setSourceQty(null);
		}
		purchStock.setDetailList(purchStockDetailList);
		return purchStock;

	}

	@Override
	public PurchStock toStock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
		query.eq("id", id);
		PurchOrder purchOrder = serviceFactory.getPurOrderService().get(id);
		PurchStock purchStock = new PurchStock();

		PropertyClone.copyProperties(purchStock, purchOrder, false);

		purchStock.setCompanyId(UserUtils.getCompanyId());
		purchStock.setCreateName(UserUtils.getUserName());
		purchStock.setCreateEmployeeId(UserUtils.getEmployeeId());
		purchStock.setBillType(BillType.PURCH_PN);
		purchStock.setPrintCount(0);
		purchStock.setId(null);
		purchStock.setBillNo(null);
		purchStock.setIsCheck(BoolValue.NO);
		purchStock.setCheckTime(null);
		purchStock.setCheckUserName(null);
		purchStock.setStorageTime(new Date());
		for (PurchOrderDetail purOrderDetail : purchOrder.getDetailList())
		{
			if (purOrderDetail.getQty().subtract(purOrderDetail.getStorageQty()).compareTo(new BigDecimal(0)) > 0 && !"YES".equals(purOrderDetail.getIsForceComplete().name()))
			{

				PurchStockDetail purStockDetail = new PurchStockDetail();
				PropertyClone.copyProperties(purStockDetail, purOrderDetail, false);
				purStockDetail.setSourceBillNo(purchOrder.getBillNo());
				purStockDetail.setSourceBillType(purchOrder.getBillType());
				purStockDetail.setSourceId(purOrderDetail.getMasterId());
				purStockDetail.setSourceDetailId(purOrderDetail.getId());
				purStockDetail.setWorkBillNo(purOrderDetail.getSourceBillNo());
				purStockDetail.setWorkId(purOrderDetail.getSourceId());
				purStockDetail.setQty(purOrderDetail.getQty().subtract(purOrderDetail.getStorageQty()));
				purStockDetail.setSourceQty(purOrderDetail.getQty());
				purStockDetail.setId(null);

				purchStock.getDetailList().add(purStockDetail);
			}

		}
		return purchStock;
	}

	@Override
	public SearchResult<PurchOrderDetail> transmitPurchStockList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "pd");
		query.addProjection(Projections.property("pd, p"));
		query.createAlias(PurchOrder.class, "p");

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("pd.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("pd.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eqProperty("pd.masterId", "p.id");
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("pd.isForceComplete", BoolValue.NO);// 是否强制完工
			query.eq("p.isForceComplete", BoolValue.NO);// 是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("pd.isForceComplete", BoolValue.YES);

		}
		query.add(Restrictions.gtProperty("qty", "storageQty"));

		if (queryParam.getBillNo() != null && !"".equals(queryParam.getBillNo()))
		{
			query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSupplierName() != null && !"".equals(queryParam.getSupplierName()))
		{
			query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getMaterialName() != null && !"".equals(queryParam.getMaterialName()))
		{
			query.like("pd.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("pd.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("pd.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("p.createTime", queryParam.getDateMax());
		}

		query.eq("p.isCheck", BoolValue.YES);
		query.eq("pd.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<PurchOrderDetail> result = new SearchResult<PurchOrderDetail>();
		result.setResult(new ArrayList<PurchOrderDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchOrderDetail detail = (PurchOrderDetail) c[0];
			detail.setMaster((PurchOrder) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;

	}

	@Override
	public PurchStock findListByDetailIds(Long[] ids)
	{
		PurchStock purchStock = new PurchStock();
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class);
		query.in("id", Arrays.asList(ids));
		List<PurchOrderDetail> purchOrderDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrderDetail.class);

		DynamicQuery queryOrder = new CompanyDynamicQuery(PurchOrder.class);
		queryOrder.in("id", purchOrderDetailList.get(0).getMasterId());
		PurchOrder purchOrder = daoFactory.getCommonDao().getByDynamicQuery(queryOrder, PurchOrder.class);

		Set<String> set = new HashSet<String>();
		set.add("detailList");
		PropertyClone.copyProperties(purchStock, purchOrder, false);
		purchStock.setCompanyId(UserUtils.getCompanyId());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			purchStock.setCreateName(e.getName());
		}
		else
		{
			purchStock.setCreateName(UserUtils.getUserName());
		}
		purchStock.setCreateEmployeeId(UserUtils.getEmployeeId());
		purchStock.setBillType(BillType.PURCH_PN);
		purchStock.setPrintCount(0);
		purchStock.setId(null);
		purchStock.setBillNo(null);
		purchStock.setStorageTime(new Date());
		purchStock.setIsCancel(BoolValue.NO);
		purchStock.setIsCheck(BoolValue.NO);
		purchStock.setCheckTime(null);
		purchStock.setCheckUserName(null);
		for (PurchOrderDetail purchOrderDetail : purchOrderDetailList)
		{
			// 数量>已入库数量
			if (purchOrderDetail.getQty().compareTo(purchOrderDetail.getStorageQty()) != 1)
				continue;

			DynamicQuery queryOrder_ = new CompanyDynamicQuery(PurchOrder.class);
			queryOrder_.eq("id", purchOrderDetail.getMasterId());
			PurchOrder purchOrder_ = daoFactory.getCommonDao().getByDynamicQuery(queryOrder_, PurchOrder.class);

			PurchStockDetail purchStockDetail = new PurchStockDetail();
			PropertyClone.copyProperties(purchStockDetail, purchOrderDetail, false);
			purchStockDetail.setSourceBillNo(purchOrder_.getBillNo());
			purchStockDetail.setSourceBillType(purchOrder_.getBillType());
			purchStockDetail.setSourceId(purchOrder_.getId());
			purchStockDetail.setSourceDetailId(purchOrderDetail.getId());
			purchStockDetail.setQty(purchOrderDetail.getQty().subtract(purchOrderDetail.getStorageQty()));
			purchStockDetail.setSourceQty(purchOrderDetail.getQty());
			purchStockDetail.setWorkBillNo(purchOrderDetail.getSourceBillNo());
			purchStockDetail.setWorkId(purchOrderDetail.getSourceId());
			purchStockDetail.setId(null);
			purchStock.getDetailList().add(purchStockDetail);
		}
		return purchStock;
	}

	@Override
	public SearchResult<PurchStock> findByCondition(QueryParam queryParam)
	{
		SearchResult<PurchStock> purchStockList = new SearchResult<PurchStock>();
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchStock.class, "p");
			if (queryParam.getDateMin() != null)
			{
				query.ge("p.createTime", queryParam.getDateMin());
			}
			if (queryParam.getDateMax() != null)
			{
				query.le("p.createTime", queryParam.getDateMax());
			}
			if (StringUtils.isNotBlank(queryParam.getBillNo()))
			{
				query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getSupplierName()))
			{
				query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
			}
			if (queryParam.getAuditFlag() != null)
			{
				query.eq("p.isCheck", queryParam.getAuditFlag());
			}
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
				query.inArray("s.employeeId", employes);
			}
			query.eq("p.companyId", UserUtils.getCompanyId());
			query.desc("p.createTime");
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.setIsSearchTotalCount(true);
			query.setQueryType(QueryType.JDBC);
			purchStockList = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, PurchStock.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return purchStockList;
	}

	@Override
	@Transactional
	public Boolean check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.PURCH_PN, id, BoolValue.YES))
		{
			return false;
		}

		// 有库存模块才进行库存操作
		if (UserUtils.hasCompanyPermission("stock:material:list"))
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class);
			query.in("masterId", id);
			PurchStock purchStock = this.get(id);
			for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
			{
				StockMaterial stockMaterial = new StockMaterial();
				// 库存操作
				stockMaterial.setQty(purchStockDetail.getQty().add(purchStockDetail.getFreeQty()));
				stockMaterial.setValuationQty(purchStockDetail.getValuationQty().add(purchStockDetail.getFreeValuationQty()));
				stockMaterial.setMoney(purchStockDetail.getMoney());
				stockMaterial.setPrice(purchStockDetail.getMoney().divide(stockMaterial.getQty(), 4, RoundingMode.HALF_UP));
				stockMaterial.setValuationPrice(purchStockDetail.getValuationPrice());
				stockMaterial.setMaterialId(purchStockDetail.getMaterialId());
				stockMaterial.setSpecifications(purchStockDetail.getSpecifications());
				stockMaterial.setMaterialClassId(purchStockDetail.getMaterialClassId());
				stockMaterial.setWarehouseId(purchStockDetail.getWarehouseId());
				serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.IN);

				// 材料最近采购价操作
				Material material = serviceFactory.getMaterialService().get(purchStockDetail.getMaterialId());
				material.setLastPurchPrice(purchStockDetail.getValuationPrice());
				daoFactory.getCommonDao().updateEntity(material);

				// 入库记录
				StockMaterialLog log = new StockMaterialLog();
				log.setBillId(id);
				log.setBillType(purchStock.getBillType());
				log.setBillNo(purchStock.getBillNo());
				log.setSourceId(purchStockDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCode(purchStockDetail.getCode());
				log.setMaterialClassId(purchStockDetail.getMaterialClassId());
				log.setMaterialName(purchStockDetail.getMaterialName());
				log.setMaterialId(purchStockDetail.getMaterialId());
				log.setSpecifications(purchStockDetail.getSpecifications());
				log.setWarehouseId(purchStockDetail.getWarehouseId());
				log.setUnitId(purchStockDetail.getUnitId());
				log.setWeight(purchStockDetail.getWeight());
				log.setPrice(purchStockDetail.getPrice());
				log.setInQty(purchStockDetail.getQty());
				log.setInMoney(purchStockDetail.getMoney());
				daoFactory.getCommonDao().saveEntity(log);
			}

		}

		return true;
	}

	@Override
	@Transactional
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:material:list"))
		{
			PurchStock purchStock = this.get(id);
			// 先判断是否已经反审核
			if (purchStock.getIsCheck() == BoolValue.NO)
			{
				throw new BusinessException("已反审核");
			}

			for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
			{
				DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
				query.eq("materialId", purchStockDetail.getMaterialId());
				if (purchStockDetail.getSpecifications() == null || "".equals(purchStockDetail.getSpecifications()))
				{
					purchStockDetail.setSpecifications(null);
					query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
				}
				else
				{
					query.eq("specifications", purchStockDetail.getSpecifications());
				}
				query.eq("warehouseId", purchStockDetail.getWarehouseId());
				StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
				if (stockMaterial == null)
				{
					stockMaterial = new StockMaterial();
					stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, purchStockDetail.getMaterialId()));
					stockMaterial.setQty(new BigDecimal(0));
				}
				else
				{
					stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
				}

				if (purchStockDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
				{
					list.add(stockMaterial);
				}
			}

			// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
			if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
			{
				list.clear();

				if (!serviceFactory.getCommonService().audit(BillType.PURCH_PN, id, BoolValue.NO))
				{
					return null;
				}
				for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
				{
					StockMaterial stockMaterial = new StockMaterial();

					// 库存操作
					stockMaterial.setMaterialId(purchStockDetail.getMaterialId());
					stockMaterial.setSpecifications(purchStockDetail.getSpecifications());
					stockMaterial.setQty(purchStockDetail.getQty().add(purchStockDetail.getFreeQty()));
					stockMaterial.setValuationQty(purchStockDetail.getValuationQty().add(purchStockDetail.getFreeValuationQty()));
					stockMaterial.setMaterialClassId(purchStockDetail.getMaterialClassId());
					stockMaterial.setWarehouseId(purchStockDetail.getWarehouseId());
					stockMaterial.setPrice(purchStockDetail.getPrice());
					stockMaterial.setValuationPrice(purchStockDetail.getValuationPrice());
					stockMaterial.setMoney(purchStockDetail.getMoney());
					stockMaterial.setIsIn(BoolValue.YES);
					serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.IN);

					DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
					query.eq("billId", id);
					List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
					daoFactory.getCommonDao().deleteAllEntity(logs);
				}

			}
		}
		else
		{
			serviceFactory.getCommonService().audit(BillType.PURCH_PN, id, BoolValue.NO);
		}

		return list;
	}

	@Override
	public Double findRefundQty(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchRefundDetail.class);
		query.eq("sourceDetailId", id);
		List<PurchRefundDetail> refundDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchRefundDetail.class);
		Double refundQty = (double) 0;
		for (PurchRefundDetail purchRefundDetail : refundDetailList)
		{
			refundQty += purchRefundDetail.getQty().doubleValue();
		}
		return refundQty;
	}

	@Override
	public SearchResult<PurchStockDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class, "po");
		query.addProjection(Projections.property("po, p"));
		query.createAlias(PurchStock.class, "p");
		query.createAlias(Supplier.class, "s");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNoneBlank(queryParam.getSourceBillNo()))
		{
			query.like("po.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNoneBlank(queryParam.getProductStyle()))
		{
			query.like("po.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNoneBlank(queryParam.getWorkBillNo()))
		{
			query.like("po.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (null != queryParam.getDateMin())
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (null != queryParam.getDateMax())
		{
			query.le("p.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getMaterialName()))
		{
			query.like("po.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("po.warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getSupplierClassId())
		{
			query.eq("s.supplierClassId", queryParam.getSupplierClassId());
		}
		if (null != queryParam.getMaterialClassId())
		{
			query.eq("po.materialClassId", queryParam.getMaterialClassId());
		}
		if (null != queryParam.getAuditFlag())
		{
			query.eq("p.isCheck", queryParam.getAuditFlag());
		}
		query.eq("po.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<PurchStockDetail> result = new SearchResult<PurchStockDetail>();
		result.setResult(new ArrayList<PurchStockDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchStockDetail detail = (PurchStockDetail) c[0];
			detail.setMaster((PurchStock) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<PurchStockDetail> findRefundSource(Date dateMin, Date dateMax, Long supplierId, String billNo, int pageSize, int pageNumber, BoolValue auditFlag)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchStockDetail.class, "po");
		query.addProjection(Projections.property("po, p"));
		query.createAlias(PurchStock.class, "p");
		query.createAlias(Supplier.class, "s");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		query.add(Restrictions.gtProperty("po.qty", "po.refundQty"));
		query.add(Restrictions.gtProperty("po.qty", "po.reconcilQty"));
		if (dateMin != null && !"".equals(dateMin))
		{
			query.ge("p.createTime", dateMin);
		}
		if (dateMax != null && !"".equals(dateMax))
		{
			query.le("p.createTime", dateMax);
		}
		if (supplierId != null && !"".equals(supplierId))
		{
			query.eq("p.supplierId", supplierId);
		}
		if (billNo != null && !"".equals(billNo))
		{
			query.like("p.billNo", "%" + billNo + "%");
		}
		if (auditFlag != null)
		{
			query.eq("p.isCheck", auditFlag);
		}
		query.eq("po.isForceComplete", BoolValue.NO);// 是否强制完工
		query.eq("p.isForceComplete", BoolValue.NO);// 是否强制完工
		query.setPageIndex(pageNumber);
		query.setPageSize(pageSize);
		query.desc("p.createTime");
		query.setIsSearchTotalCount(true);
		query.eq("po.companyId", UserUtils.getCompanyId());
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<PurchStockDetail> result = new SearchResult<PurchStockDetail>();
		result.setResult(new ArrayList<PurchStockDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchStockDetail detail = (PurchStockDetail) c[0];
			detail.setMaster((PurchStock) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;

	}

}
