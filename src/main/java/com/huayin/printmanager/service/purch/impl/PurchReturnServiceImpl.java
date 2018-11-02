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
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.purch.PurchReturnService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午2:40:01, zhengby, 代码规范
 */
@Service
public class PurchReturnServiceImpl extends BaseServiceImpl implements PurchReturnService
{

	@Override
	public PurchRefund get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchRefund.class);
		query.eq("id", id);
		PurchRefund purchRefund = daoFactory.getCommonDao().getByDynamicQuery(query, PurchRefund.class);

		DynamicQuery queryD = new CompanyDynamicQuery(PurchRefundDetail.class);
		queryD.eq("masterId", id);
		List<PurchRefundDetail> purchRefundDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, PurchRefundDetail.class);
		if (purchRefund != null)
		{
			purchRefund.setDetailList(purchRefundDetail);
		}
		return purchRefund;
	}

	@Override
	public PurchRefund get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchRefund.class);
		query.eq("billNo", billNo);
		PurchRefund purchRefund = daoFactory.getCommonDao().getByDynamicQuery(query, PurchRefund.class);
		return purchRefund;
	}

	@Override
	public PurchRefundDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchRefundDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, PurchRefundDetail.class);
	}

	@Override
	public PurchRefund lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchRefund.class);
		query.eq("id", id);
		PurchRefund order = daoFactory.getCommonDao().lockByDynamicQuery(query, PurchRefund.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(PurchRefundDetail.class);
		query_detail.eq("masterId", id);
		List<PurchRefundDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, PurchRefundDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	@Transactional
	public ServiceResult<PurchRefund> save(PurchRefund purchRefund)
	{
		// 是否要审核标识
		BoolValue flag = purchRefund.getIsCheck();
		purchRefund.setCompanyId(UserUtils.getCompanyId());
		purchRefund.setBillNo(UserUtils.createBillNo(BillType.PURCH_PR));
		purchRefund.setBillType(BillType.PURCH_PR);
		purchRefund.setIsCheck(BoolValue.NO);
		purchRefund.setIsForceComplete(BoolValue.NO);

		if (purchRefund.getDetailList().size() > 0)
		{
			purchRefund.setCreateTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				purchRefund.setCreateName(e.getName());
			}
			else
			{
				purchRefund.setCreateName(UserUtils.getUserName());
			}
			purchRefund.setCreateEmployeeId(UserUtils.getEmployeeId());
			daoFactory.getCommonDao().saveEntity(purchRefund);
		}

		for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
		{
			if ("".equals(purchRefundDetail.getCode()) || purchRefundDetail.getCode() == null)
			{
				continue;
			}
			PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchRefundDetail.getSourceDetailId());

			PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());

			purchRefundDetail.setMasterId(purchRefund.getId());
			purchRefundDetail.setCompanyId(UserUtils.getCompanyId());
			purchRefundDetail.setReconcilQty(new BigDecimal(0));
			purchRefundDetail.setIsForceComplete(BoolValue.NO);
			purchRefundDetail.setWorkBillNo(purchStockDetail.getWorkBillNo());
			purchRefundDetail.setWorkId(purchStockDetail.getWorkId());
			// 只有换货才反写订单数量数量
			if (purchRefund.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
			{
				purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(purchRefundDetail.getQty()));
			}
			purchStockDetail.setRefundQty(purchStockDetail.getRefundQty().add(purchRefundDetail.getQty()));
			daoFactory.getCommonDao().saveEntity(purchRefundDetail);

		}
		ServiceResult<PurchRefund> serviceResult = new ServiceResult<PurchRefund>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(purchRefund.getId(), purchRefund.getForceCheck()));
		}
		serviceResult.setReturnValue(purchRefund);
		return serviceResult;
	}

	@Override
	@Transactional
	public ServiceResult<PurchRefund> update(PurchRefund purchRefund)
	{
		// 是否要审核标识
		BoolValue flag = purchRefund.getIsCheck();

		ServiceResult<PurchRefund> serviceResult = new ServiceResult<PurchRefund>();

		// 再更新数据
		purchRefund.setIsCheck(BoolValue.NO);
		// 之前记录对象----
		PurchRefund purchRefund_ = this.lockHasChildren(purchRefund.getId());
		// 判断是否已审核
		if (purchRefund_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		// 记录的退货类型
		ReturnGoodsType returnType = purchRefund_.getReturnGoodsType();

		// 删除记录操作
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (PurchRefundDetail newItem : purchRefund.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (PurchRefundDetail oldItem : purchRefund_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{

				PurchRefundDetail purchRefundDetail = this.getDetail(id);
				PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchRefundDetail.getSourceDetailId());
				PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());
				if (purchRefund.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{
					purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchRefundDetail.getQty()));
				}
				purchStockDetail.setRefundQty(purchStockDetail.getRefundQty().subtract(purchRefundDetail.getQty()));

				daoFactory.getCommonDao().deleteEntity(PurchRefundDetail.class, id);
			}
		}

		PropertyClone.copyProperties(purchRefund_, purchRefund, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		purchRefund_.setUpdateName(UserUtils.getUserName());
		purchRefund_.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(purchRefund_);
		for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
		{
			if ("".equals(purchRefundDetail.getCode()) || purchRefundDetail.getCode() == null)
			{
				continue;
			}
			PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchRefundDetail.getSourceDetailId());

			PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());

			// 查修改前退货记录
			PurchRefundDetail purchRefundDetail_ = this.getDetail(purchRefundDetail.getId());

			if (purchRefundDetail.getId() == null)
			{
				purchRefundDetail.setMasterId(purchRefund.getId());
				purchRefundDetail.setCompanyId(UserUtils.getCompanyId());
				purchRefundDetail.setReconcilQty(new BigDecimal(0));
				purchRefundDetail.setIsForceComplete(BoolValue.NO);
				// 只有换货才反写订单数量数量
				if (purchRefund.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{
					purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(purchRefundDetail.getQty()));
				}
				purchStockDetail.setRefundQty(purchStockDetail.getRefundQty().add(purchRefundDetail.getQty()));
				daoFactory.getCommonDao().saveEntity(purchRefundDetail);
			}
			else
			{

				// 此次改成换货
				if (purchRefund.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{
					if (returnType == ReturnGoodsType.EXCHANGE)// 记录也是换货
					{
						// 修改后已入库数量 = 订单已入库数量 + 退货记录数量 -本次修改退货数量
						purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchRefundDetail_.getQty()).subtract(purchRefundDetail.getQty()));
					}
					else
					{// 退货转 换货 订单已入库数量 减少
						purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().subtract(purchRefundDetail.getQty()));
					}
				}
				else// 此次改成退货
				{
					// 换货 转退货
					if (returnType == ReturnGoodsType.EXCHANGE)// 记录是换货
					{
						purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchRefundDetail.getQty()));
					}
				}
				purchStockDetail.setRefundQty(purchStockDetail.getRefundQty().subtract(purchRefundDetail_.getQty()).add(purchRefundDetail.getQty()));
				purchRefundDetail_.setMemo(purchRefundDetail.getMemo());
				PropertyClone.copyProperties(purchRefundDetail_, purchRefundDetail, false, null, new String[] { "memo" });
				daoFactory.getCommonDao().updateEntity(purchRefundDetail_);
			}

		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(purchRefund.getId(), purchRefund.getForceCheck()));
		}
		serviceResult.setReturnValue(purchRefund);
		return serviceResult;
	}

	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:material:list"))
		{
			PurchRefund purchRefund = this.get(id);
			// 先判断是否已经审核
			if (purchRefund.getIsCheck() == BoolValue.YES)
			{
				throw new BusinessException("已审核");
			}
			for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
			{
				DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
				query.eq("materialId", purchRefundDetail.getMaterialId());
				query.eq("specifications", "".equals(purchRefundDetail.getSpecifications()) ? null : purchRefundDetail.getSpecifications());
				query.eq("warehouseId", purchRefundDetail.getWarehouseId());
				StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
				if (stockMaterial == null)
				{
					stockMaterial = new StockMaterial();
					stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, purchRefundDetail.getMaterialId()));
					stockMaterial.setQty(new BigDecimal(0));
				}
				else
				{
					stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
				}

				if (purchRefundDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
				{
					list.add(stockMaterial);
				}
			}

			// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
			if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
			{
				list.clear();
				if (!serviceFactory.getCommonService().audit(BillType.PURCH_PR, id, BoolValue.YES))
				{
					return null;
				}
				for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
				{
					StockMaterial stockMaterial = new StockMaterial();
					stockMaterial.setMaterialId(purchRefundDetail.getMaterialId());
					stockMaterial.setSpecifications(purchRefundDetail.getSpecifications());
					stockMaterial.setQty(purchRefundDetail.getQty());
					stockMaterial.setValuationQty(purchRefundDetail.getValuationQty());
					stockMaterial.setMaterialClassId(purchRefundDetail.getMaterialClassId());
					stockMaterial.setWarehouseId(purchRefundDetail.getWarehouseId());
					stockMaterial.setPrice(purchRefundDetail.getPrice());
					stockMaterial.setMoney(purchRefundDetail.getMoney());

					serviceFactory.getMaterialStockService().stock(stockMaterial,InOutType.OUT);

					// 出库记录
					StockMaterialLog logOut = new StockMaterialLog();
					logOut.setBillId(id);
					logOut.setBillType(purchRefund.getBillType());
					logOut.setBillNo(purchRefund.getBillNo());
					logOut.setSourceId(purchRefundDetail.getId());
					logOut.setCreateTime(new Date());
					logOut.setCompanyId(UserUtils.getCompanyId());
					logOut.setWeight(purchRefundDetail.getWeight());
					logOut.setCode(purchRefundDetail.getCode());
					logOut.setMaterialClassId(purchRefundDetail.getMaterialClassId());
					logOut.setMaterialName(purchRefundDetail.getMaterialName());
					logOut.setMaterialId(purchRefundDetail.getMaterialId());
					logOut.setSpecifications(purchRefundDetail.getSpecifications());
					logOut.setWarehouseId(purchRefundDetail.getWarehouseId());
					logOut.setUnitId(purchRefundDetail.getUnitId());
					logOut.setPrice(purchRefundDetail.getPrice());
					logOut.setOutQty(purchRefundDetail.getQty());
					logOut.setOutMoney(purchRefundDetail.getMoney());
					daoFactory.getCommonDao().saveEntity(logOut);
				}

			}
		}
		else
		{
			if (!serviceFactory.getCommonService().audit(BillType.PURCH_PR, id, BoolValue.YES))
			{
				return null;
			}
		}
		return list;
	}

	@Override
	@Transactional
	public Boolean checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.PURCH_PR, id, BoolValue.NO))
		{
			return false;
		}
		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:material:list"))
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchRefundDetail.class);
			query.in("masterId", id);
			List<PurchRefundDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchRefundDetail.class);
			for (PurchRefundDetail purchRefundDetail : detailList)
			{
				StockMaterial stockMaterial = new StockMaterial();
				// 库存操作
				stockMaterial.setQty(purchRefundDetail.getQty());
				stockMaterial.setValuationQty(purchRefundDetail.getValuationQty());
				stockMaterial.setMoney(purchRefundDetail.getMoney());
				stockMaterial.setPrice(purchRefundDetail.getPrice());
				stockMaterial.setMaterialId(purchRefundDetail.getMaterialId());
				stockMaterial.setSpecifications(purchRefundDetail.getSpecifications());
				stockMaterial.setMaterialClassId(purchRefundDetail.getMaterialClassId());
				stockMaterial.setWarehouseId(purchRefundDetail.getWarehouseId());
				serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.OUT);

				// 删除材料入库明细记录
				DynamicQuery query2 = new DynamicQuery(StockMaterialLog.class);
				query2.eq("billId", id);
				List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query2, StockMaterialLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}
		}
		return true;
	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			PurchRefund purchRefund = this.lockHasChildren(id);
			// 换货

			for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
			{
				PurchStockDetail purchStockDetail = serviceFactory.getPurStockService().getDetail(purchRefundDetail.getSourceDetailId());

				PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(purchStockDetail.getSourceDetailId());
				if (purchRefund.getReturnGoodsType() == ReturnGoodsType.EXCHANGE)
				{
					purchOrderDetail.setStorageQty(purchOrderDetail.getStorageQty().add(purchRefundDetail.getQty()));
				}
				purchStockDetail.setRefundQty(purchStockDetail.getRefundQty().subtract(purchRefundDetail.getQty()));
				daoFactory.getCommonDao().updateEntity(purchOrderDetail);
			}

			daoFactory.getCommonDao().deleteAllEntity(purchRefund.getDetailList());
			daoFactory.getCommonDao().deleteEntity(purchRefund);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public SearchResult<PurchRefund> findByCondition(QueryParam queryParam)
	{
		SearchResult<PurchRefund> purchOrderList = new SearchResult<PurchRefund>();
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchRefund.class, "p");
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
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.desc("p.createTime");
			query.setIsSearchTotalCount(true);
			query.setQueryType(QueryType.JDBC);
			purchOrderList = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, PurchRefund.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return purchOrderList;
	}

	@Override
	public SearchResult<PurchRefundDetail> findDetailByCondition(QueryParam queryParam)
	{

		DynamicQuery query = new CompanyDynamicQuery(PurchRefundDetail.class, "po");
		query.addProjection(Projections.property("po, p"));
		query.createAlias(PurchRefund.class, "p");
		query.createAlias(Supplier.class, "s");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}

		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("po.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSpecifications()))
		{
			query.like("po.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("po.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOrderBillNo()))
		{
			query.like("po.orderBillNo", "%" + queryParam.getOrderBillNo() + "%");
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
		SearchResult<PurchRefundDetail> result = new SearchResult<PurchRefundDetail>();
		result.setResult(new ArrayList<PurchRefundDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchRefundDetail detail = (PurchRefundDetail) c[0];
			detail.setMaster((PurchRefund) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;

	}

}
