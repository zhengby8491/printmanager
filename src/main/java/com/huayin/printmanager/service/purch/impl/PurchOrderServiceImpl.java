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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrder;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrderDetail;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProgressStatusPurch;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.purch.PurchOrderService;
import com.huayin.printmanager.service.purch.vo.WorkToPurchVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购订单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月20日, raintear
 * @version 	   2.0, 2018年2月23日下午2:39:13, zhengby, 代码规范
 */
@Service
public class PurchOrderServiceImpl extends BaseServiceImpl implements PurchOrderService
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	@Override
	public PurchOrder get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
		query.eq("id", id);
		PurchOrder purchOrder = daoFactory.getCommonDao().getByDynamicQuery(query, PurchOrder.class);

		DynamicQuery queryD = new CompanyDynamicQuery(PurchOrderDetail.class);
		queryD.eq("masterId", id);
		List<PurchOrderDetail> purchOrderDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, PurchOrderDetail.class);
		purchOrder.setDetailList(purchOrderDetail);
		return purchOrder;
	}

	@Override
	public PurchOrder get(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
		query.eq("billNO", billNo);
		PurchOrder purchOrder = daoFactory.getCommonDao().getByDynamicQuery(query, PurchOrder.class);
		return purchOrder;
	}

	public PurchOrderDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class);
		query.eq("id", id);
		PurchOrderDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, PurchOrderDetail.class);
		return detail;
	}

	@Override
	public PurchOrder getOrderDetail(String billNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
		query.eq("billNO", billNo);
		PurchOrder purchOrder = daoFactory.getCommonDao().getByDynamicQuery(query, PurchOrder.class);

		if (null != purchOrder)
		{
			DynamicQuery queryD = new CompanyDynamicQuery(PurchOrderDetail.class);
			queryD.eq("masterId", purchOrder.getId());
			List<PurchOrderDetail> purchOrderDetail = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, PurchOrderDetail.class);
			purchOrder.setDetailList(purchOrderDetail);
		}

		return purchOrder;
	}

	@Override
	public List<PurchOrderDetail> getDetailByWork(Long workId)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class);
		query.eq("sourceId", workId);
		List<PurchOrderDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrderDetail.class);
		return detailList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BigDecimal getPurchQty(Long materialId, String style)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "a");
		query.createAlias(PurchOrder.class, JoinType.LEFTJOIN, "b", "b.id=a.materialId");
		query.addProjection(Projections.property("sum(a.qty-a.storageQty)"));
		query.eq("a.materialId", materialId);
		query.eq("a.specifications", style);
		// query.eq("b.isCheck", BoolValue.YES);
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return new BigDecimal((map.getResult().get(0).get("sum(a.qty-a.storageQty)") == null ? 0 : map.getResult().get(0).get("sum(a.qty-a.storageQty)")).toString());
	}

	@Override
	public List<WorkMaterial> getWorkToPurchByMaterial(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
		query.addProjection(Projections.property("wm, w"));
		query.createAlias(Work.class, "w");
		query.eqProperty("wm.workId", "w.id");
		query.eq("wm.isNotPurch", queryParam.getCompleteFlag());// isNotPurch表示未在“工单未采购页面”强制完工
		query.add(Restrictions.gtProperty("wm.qty", "wm.purchQty"));

		if (queryParam.getBillNo() != null && !"".equals(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getMaterialName() != null && !"".equals(queryParam.getMaterialName()))
		{
			query.like("wm.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getSpecifications() == null || "".equals(queryParam.getSpecifications()))
		{
			query.add(Restrictions.or(Restrictions.isNull("wm.style"), Restrictions.eq("wm.style", "")));
		}
		if (queryParam.getSpecifications() != null && !"".equals(queryParam.getSpecifications()))
		{
			query.eq("wm.style", queryParam.getSpecifications());
		}
		// if (queryParam.getCompleteFlag() != null && !"".equals(queryParam.getCompleteFlag()))
		// {
		// query.eq("w.isForceComplete", queryParam.getCompleteFlag());
		// }

		query.eq("w.isForceComplete", BoolValue.NO);
		query.eq("wm.isCustPaper", BoolValue.NO);
		query.eq("w.isCheck", BoolValue.YES);
		query.setPageIndex(1);
		query.setPageSize(1000);
		query.desc("w.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<WorkMaterial> result = new SearchResult<WorkMaterial>();
		result.setResult(new ArrayList<WorkMaterial>());
		for (Object[] c : temp_result.getResult())
		{
			WorkMaterial detail = (WorkMaterial) c[0];
			detail.setWork((Work) c[1]);
			result.getResult().add(detail);
		}
		return result.getResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<WorkToPurchVo> transmitPurchOrderList(QueryParam queryParam)
	{
		SearchResult<WorkToPurchVo> result = new SearchResult<WorkToPurchVo>();
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class, "wm");
		query.addProjection(Projections.property("mc.name as materialClassName,m.code,wm.materialName,wm.style,m.weight,u.name as unitName,sum(wm.qty-purchQty) as workQty,wm.materialId"));
		query.createAlias(Work.class, JoinType.LEFTJOIN, "w", "w.id=wm.workId");
		query.createAlias(Material.class, JoinType.LEFTJOIN, "m", "m.id=wm.materialId");
		query.createAlias(Unit.class, JoinType.LEFTJOIN, "u", "u.id=m.stockUnitId");
		query.createAlias(MaterialClass.class, JoinType.LEFTJOIN, "mc", "mc.id=m.materialClassId");
		query.eq("wm.isNotPurch", queryParam.getCompleteFlag());
		query.add(Restrictions.gtProperty("wm.qty", "wm.purchQty"));

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("wm.style", "%" + queryParam.getProductStyle() + "%");
		}
		if (queryParam.getMaterialClassId() != null)
		{
			query.like("mc.id", "%" + queryParam.getMaterialClassId() + "%");
		}
		if (queryParam.getMaterialName() != null && !"".equals(queryParam.getMaterialName()))
		{
			query.like("wm.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getSpecifications() != null && !"".equals(queryParam.getSpecifications()))
		{
			query.like("wm.style", "%" + queryParam.getSpecifications() + "%");
		}
		if (queryParam.getBillNo() != null && !"".equals(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCode() != null && !"".equals(queryParam.getCode()))
		{
			query.like("m.code", "%" + queryParam.getCode() + "%");
		}
		// if (queryParam.getCompleteFlag() != null && !"".equals(queryParam.getCompleteFlag()))
		// {
		// query.eq("w.isForceComplete",queryParam.getCompleteFlag());
		// }
		query.eq("w.isForceComplete", BoolValue.NO);
		query.eq("wm.isCustPaper", BoolValue.NO);
		query.eq("w.isCheck", BoolValue.YES);
		query.desc("w.createTime");
		query.addGourp("wm.materialId");
		query.addGourp("wm.style");

		DynamicQuery query_page = new DynamicQuery(query, "q");
		query_page.addProjection(Projections.property("q.*"));
		query_page.setIsSearchTotalCount(true);
		query_page.setPageIndex(queryParam.getPageNumber());
		query_page.setPageSize(queryParam.getPageSize());
		query_page.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query_page, HashMap.class);
		List<WorkToPurchVo> list = new ArrayList<WorkToPurchVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			WorkToPurchVo vo = new WorkToPurchVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, WorkToPurchVo.class);
				vo.setStockQty(serviceFactory.getMaterialStockService().getQty(vo.getMaterialId(), vo.getStyle()));
				vo.setPurchQty(getPurchQty(vo.getMaterialId(), vo.getStyle()));
				BigDecimal qty = vo.getWorkQty().subtract(vo.getStockQty());
				vo.setQty(qty.doubleValue() < 0 ? new BigDecimal(0) : qty);
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
	public List<PurchOrderDetail> toPurch(Long[] ids)
	{
		List<PurchOrderDetail> result = new ArrayList<PurchOrderDetail>();
		DynamicQuery query = new CompanyDynamicQuery(WorkMaterial.class);
		query.in("id", Arrays.asList(ids));
		List<WorkMaterial> workMaterialList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkMaterial.class);
		for (WorkMaterial workMaterial : workMaterialList)
		{
			// 已采购数量 大于或等于 需要采购数量 则 break !(a>b) 等价于 (a<=b)
			if (!(workMaterial.getQty().compareTo(workMaterial.getPurchQty()) == 1))
			{
				continue;
			}
			PurchOrderDetail detail = new PurchOrderDetail();
			// 获取材料对象
			Material material = serviceFactory.getMaterialService().get(workMaterial.getMaterialId());
			// 获取工单对象
			Work work = daoFactory.getCommonDao().getEntity(Work.class, workMaterial.getWorkId());
			detail.setCode(material.getCode());
			detail.setMaterialName(material.getName());
			detail.setWeight(material.getWeight());
			detail.setValuationPrice(material.getLastPurchPrice());
			detail.setMaterialClassId(material.getMaterialClassId());
			detail.setSpecifications(workMaterial.getStyle());
			detail.setMasterId(workMaterial.getMaterialId());
			detail.setMaterialId(material.getId());
			detail.setUnitId(material.getStockUnitId());
			detail.setPurchUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getStockUnitId())).getName());
			detail.setValuationUnitId(material.getValuationUnitId());
			detail.setValuationUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getValuationUnitId())).getName());
			detail.setSourceDetailId(workMaterial.getId());
			detail.setSourceId(work.getId());
			detail.setSourceQty(workMaterial.getQty());
			detail.setQty(workMaterial.getQty().subtract(workMaterial.getPurchQty()));
			detail.setSourceBillNo(work.getBillNo());
			detail.setSourceBillType(work.getBillType());
			/* 采购增加部件或成品ID，方便统计工单部件或成品材料入库数量 */
			detail.setParentId(workMaterial.getId());
			detail.setWorkMaterialType(workMaterial.getWorkMaterialType());
			result.add(detail);
		}
		return result;

	}

	@Override
	public List<PurchOrderDetail> warnToPurch(Long[] ids)
	{
		List<PurchOrderDetail> result = new ArrayList<PurchOrderDetail>();
		DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
		query.in("id", Arrays.asList(ids));
		List<StockMaterial> stockMaterialList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterial.class);
		for (StockMaterial stockMaterial : stockMaterialList)
		{
			PurchOrderDetail detail = new PurchOrderDetail();
			// 获取材料对象
			Material material = serviceFactory.getMaterialService().get(stockMaterial.getMaterialId());
			detail.setCode(material.getCode());
			detail.setMaterialName(material.getName());
			detail.setWeight(material.getWeight());
			detail.setValuationPrice(material.getLastPurchPrice());
			detail.setMaterialClassId(material.getMaterialClassId());
			detail.setSpecifications(stockMaterial.getSpecifications());
			detail.setMasterId(stockMaterial.getMaterialId());
			detail.setMaterialId(material.getId());
			detail.setUnitId(material.getStockUnitId());
			detail.setPurchUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getStockUnitId())).getName());
			detail.setValuationUnitId(material.getValuationUnitId());
			detail.setValuationUnitName(((Unit) UserUtils.getBasicInfo("UNIT", material.getValuationUnitId())).getName());
			detail.setQty(material.getMinStockNum().subtract(stockMaterial.getQty()));
			result.add(detail);
		}
		return result;

	}

	@Override
	public PurchOrder lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
		query.eq("id", id);
		PurchOrder order = daoFactory.getCommonDao().lockByDynamicQuery(query, PurchOrder.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(PurchOrderDetail.class);
		query_detail.eq("masterId", id);
		List<PurchOrderDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, PurchOrderDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	@Transactional
	public PurchOrder save(PurchOrder purchOrder)
	{
		BoolValue flag = purchOrder.getIsCheck();			// 标识是否保存并审核
		purchOrder.setCompanyId(UserUtils.getCompanyId());
		purchOrder.setBillNo(UserUtils.createBillNo(BillType.PURCH_PO));
		purchOrder.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			purchOrder.setCreateName(e.getName());
		}
		else
		{
			purchOrder.setCreateName(UserUtils.getUserName());
		}
		purchOrder.setCreateEmployeeId(UserUtils.getEmployeeId());
		purchOrder.setBillType(BillType.PURCH_PO);
		purchOrder.setIsCancel(BoolValue.NO);
		purchOrder.setIsForceComplete(BoolValue.NO);
		purchOrder.setProgressStatus(ProgressStatusPurch.NO_STORAGE);
		if (flag == BoolValue.YES)
		{
			purchOrder.setCheckTime(new Date());
			if (e != null)
			{
				purchOrder.setCheckUserName(e.getName());
			}
			else
			{
				purchOrder.setCheckUserName(UserUtils.getUserName());
			}
		}
		// 存汇率
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(purchOrder.getCurrencyType());
		purchOrder.setRate(exchangeRate.getRate());
		purchOrder.setRateId(exchangeRate.getId());
		daoFactory.getCommonDao().saveEntity(purchOrder);
		for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
		{
			// 回写工单表
			if (purchOrderDetail.getSourceDetailId() != null && !"".equals(purchOrderDetail.getSourceDetailId()))
			{
				WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, purchOrderDetail.getSourceDetailId());
				workMaterial.setPurchQty(workMaterial.getPurchQty().add(purchOrderDetail.getQty()));

			}
			// 回写外部采购单
			if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
			{
				ExteriorPurchOrderDetail source = daoFactory.getCommonDao().getEntity(ExteriorPurchOrderDetail.class, purchOrderDetail.getExtOrderDetailId());
				source.setPurchOrderId(purchOrder.getId()); 
				ExteriorPurchOrder master = daoFactory.getCommonDao().getEntity(ExteriorPurchOrder.class, purchOrderDetail.getExtOrderId());
				master.setPurchOrderId(purchOrder.getId());
			}
			purchOrderDetail.setMasterId(purchOrder.getId());
			purchOrderDetail.setCompanyId(UserUtils.getCompanyId());
			purchOrderDetail.setStorageQty(new BigDecimal(0));
			purchOrderDetail.setIsForceComplete(BoolValue.NO);
			purchOrderDetail.setSourceBillNo(purchOrderDetail.getSourceBillNo() == "" ? null : purchOrderDetail.getSourceBillNo());
			daoFactory.getCommonDao().saveEntity(purchOrderDetail);
		}
		return purchOrder;
	}

	@Override
	@Transactional
	public void update(PurchOrder purchOrder)
	{
		if (purchOrder.getIsCheck() == BoolValue.YES)
		{
			purchOrder.setCheckTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				purchOrder.setCheckUserName(e.getName());
			}
			else
			{
				purchOrder.setCheckUserName(UserUtils.getUserName());
			}
		}
		PurchOrder purchOrder_ = this.lockHasChildren(purchOrder.getId());
		// 判断是否已审核
		if (purchOrder_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (PurchOrderDetail newItem : purchOrder.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (PurchOrderDetail oldItem : purchOrder_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				PurchOrderDetail purchOrderDetail = this.getDetail(id);
				// 回写工单表
				if (purchOrderDetail.getSourceDetailId() != null && !"".equals(purchOrderDetail.getSourceDetailId()))
				{
					WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, purchOrderDetail.getSourceDetailId());
					workMaterial.setPurchQty(workMaterial.getPurchQty().subtract(purchOrderDetail.getQty()));
					daoFactory.getCommonDao().updateEntity(workMaterial);
				}
				daoFactory.getCommonDao().deleteEntity(PurchOrderDetail.class, id);
			}
		}
		PropertyClone.copyProperties(purchOrder_, purchOrder, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(purchOrder.getCurrencyType());
		purchOrder_.setRate(exchangeRate.getRate());
		purchOrder_.setRateId(exchangeRate.getId());
		purchOrder_.setUpdateName(UserUtils.getUserName());
		purchOrder_.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(purchOrder_);

		for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
		{
			if ("".equals(purchOrderDetail.getCode()) || purchOrderDetail.getCode() == null)
			{
				continue;
			}
			// 回写工单表
			if (purchOrderDetail.getSourceDetailId() != null && !"".equals(purchOrderDetail.getSourceDetailId()))
			{
				// 之前记录
				PurchOrderDetail purchOrderDetail_ = serviceFactory.getPurOrderService().getDetail(purchOrderDetail.getId());
				WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, purchOrderDetail.getSourceDetailId());
				workMaterial.setPurchQty(workMaterial.getPurchQty().subtract(purchOrderDetail_.getQty()).add(purchOrderDetail.getQty()));
				daoFactory.getCommonDao().updateEntity(workMaterial);
			}

			if (purchOrderDetail.getId() != null)
			{
				PurchOrderDetail purchOrderDetail_ = this.getDetail(purchOrderDetail.getId());
				purchOrderDetail_.setMemo(purchOrderDetail.getMemo());
				PropertyClone.copyProperties(purchOrderDetail_, purchOrderDetail, false, null, new String[] { "memo" });// 替换成新内容
				daoFactory.getCommonDao().updateEntity(purchOrderDetail_);
			}
			else
			{
				purchOrderDetail.setMasterId(purchOrder_.getId());
				purchOrderDetail.setStorageQty(new BigDecimal(0));
				purchOrderDetail.setCompanyId(UserUtils.getCompanyId());
				purchOrderDetail.setIsForceComplete(BoolValue.NO);
				daoFactory.getCommonDao().saveEntity(purchOrderDetail);
			}
		}
	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			PurchOrder purchOrder = this.lockHasChildren(id);
			for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
			{
				// 回写工单表
				if (purchOrderDetail.getSourceDetailId() != null && !"".equals(purchOrderDetail.getSourceDetailId()))
				{
					WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, purchOrderDetail.getSourceDetailId());
					workMaterial.setPurchQty(workMaterial.getPurchQty().subtract(purchOrderDetail.getQty()));
				}
				// 回写外部采购单
				if (purchOrderDetail.getExtOrderDetailId() != null && !"".equals(purchOrderDetail.getExtOrderDetailId()))
				{
					ExteriorPurchOrderDetail source = daoFactory.getCommonDao().getEntity(ExteriorPurchOrderDetail.class, purchOrderDetail.getExtOrderDetailId());
					source.setPurchOrderId(null);
					ExteriorPurchOrder master = daoFactory.getCommonDao().getEntity(ExteriorPurchOrder.class, purchOrderDetail.getExtOrderId());
					master.setPurchOrderId(null);
				}
			}

			daoFactory.getCommonDao().deleteAllEntity(purchOrder.getDetailList());
			daoFactory.getCommonDao().deleteEntity(purchOrder);
			return true;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public PurchOrder copy(Long id)
	{
		/**
		 * 查详情列表
		 */
		DynamicQuery query1 = new CompanyDynamicQuery(PurchOrderDetail.class);
		query1.eq("masterId", id);
		List<PurchOrderDetail> purchOrderDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query1, PurchOrderDetail.class);
		/**
		 * 查订单信息
		 */
		DynamicQuery query2 = new CompanyDynamicQuery(PurchOrder.class);
		query2.eq("id", id);
		PurchOrder purchOrder = daoFactory.getCommonDao().getByDynamicQuery(query2, PurchOrder.class);
		/**
		 * 将id置空
		 */
		purchOrder.setId(null);
		purchOrder.setCreateTime(null);
		purchOrder.setCreateName(null);
		purchOrder.setCreateEmployeeId(null);
		purchOrder.setCheckTime(null);
		purchOrder.setCheckUserName(null);
		purchOrder.setIsCheck(BoolValue.NO);
		purchOrder.setPrintCount(0);
		for (PurchOrderDetail list : purchOrderDetailList)
		{
			list.setId(null);
			list.setSourceId(null);
			list.setSourceBillNo(null);
			list.setSourceBillType(null);
			list.setSourceQty(null);
		}
		purchOrder.setDetailList(purchOrderDetailList);
		return purchOrder;

	}

	@Override
	@Transactional
	public Boolean forceComplete(Long[] ids, BoolValue flag)
	{
		try
		{
			for (Long id : ids)
			{
				WorkMaterial workMaterial = daoFactory.getCommonDao().getEntity(WorkMaterial.class, id);
				workMaterial.setIsNotPurch(flag);
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	@Override
	@Transactional
	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
			if (employes.length > 0)
			{
				query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=supplierId");
				query.inArray("s.employeeId", employes);
			}
			List<PurchOrder> purchOrderlList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, PurchOrder.class);
			for (PurchOrder purchOrder : purchOrderlList)
			{
				purchOrder.setIsCheck(BoolValue.YES);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public SearchResult<PurchOrder> findByCondition(QueryParam queryParam)
	{
		SearchResult<PurchOrder> purchOrderList = new SearchResult<PurchOrder>();
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(PurchOrder.class, "p");
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
			purchOrderList = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, PurchOrder.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return purchOrderList;
	}

	@Override
	public SearchResult<PurchOrderDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchOrderDetail.class, "po");
		query.addProjection(Projections.property("po, p"));
		query.createAlias(PurchOrder.class, "p");
		query.createAlias(Supplier.class, "s");
		query.createAlias(Material.class, "m");
		query.eqProperty("po.masterId", "p.id");
		query.eqProperty("s.id", "p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}

		if (null != queryParam.getDeliverDateMin() && null != queryParam.getDeliverDateMax())
		{
			query.between("po.deliveryTime", queryParam.getDeliverDateMin(), queryParam.getDeliverDateMax());
		}

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("po.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("po.specifications", "%" + queryParam.getProductStyle() + "%");
		}
		query.eqProperty("m.id", "po.materialId");
		if (null != queryParam.getDateMin() && !"".equals(queryParam.getDateMin()))
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (null != queryParam.getDateMax() && !"".equals(queryParam.getDateMax()))
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
		if (null != queryParam.getEmployeeId())
		{
			query.eq("p.employeeId", queryParam.getEmployeeId());
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
			query.eq("m.materialClassId", queryParam.getMaterialClassId());
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
	@Transactional
	public void changePrice(PurchOrderDetail purchOrderDetail) throws Exception
	{
		/**
		 * 1. 修改采购明细的单价、金额、交期日期
		 * 2. 修改采购主表的单价、总金额、总税额、总金额（不含税）
		 * 3. 修改采购明细的下游单据的单价，金额，税额，不含税金额，不含税单价也需要重新计算并保存（采购入库，采购退货，采购对账）
		 *    3.1 修改采购入库明细
		 *      3.1.1 线路1 - 修改采购对账
		 *      3.1.2 线路2 - 修改采购退货、采购退货对账
		 *      3.1.3 修改库存金额、材料出库日志
		 */

		PurchOrderDetail detail = daoFactory.getCommonDao().getEntity(PurchOrderDetail.class, purchOrderDetail.getId());
		if (detail != null)
		{
			// 1. 修改采购明细的单价、金额、交期日期
			PurchOrderDetail detailTmp = (PurchOrderDetail) BeanUtils.cloneBean(detail);
			// 计价单价(含税)
			detail.setValuationPrice(purchOrderDetail.getValuationPrice());
			// 金额（含税）
			detail.setMoney(purchOrderDetail.getMoney());
			// 不含税金额
			BigDecimal noTaxMoney = purchOrderDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
			// 税额
			BigDecimal tax = detail.getMoney().subtract(noTaxMoney);
			// 库存单价 = 金额（含税） / 数量
			BigDecimal price = detail.getMoney().divide(detail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
			// 不含税单价 = 含税金额 / 数量
			BigDecimal noTaxPrice = noTaxMoney.divide(detail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
			// 不含税计价单价
			BigDecimal noTaxValuationPrice = noTaxMoney.divide(detail.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP);
			detail.setNoTaxMoney(noTaxMoney);
			detail.setTax(tax);
			detail.setPrice(price);
			detail.setNoTaxPrice(noTaxPrice);
			detail.setNoTaxValuationPrice(noTaxValuationPrice);
			detail.setDeliveryTime(purchOrderDetail.getDeliveryTime());
			daoFactory.getCommonDao().updateEntity(detail);

			// 2. 修改采购主表的单价、总金额、总税额、总金额（不含税）
			PurchOrder purchOrder = daoFactory.getCommonDao().getEntity(PurchOrder.class, detail.getMasterId());
			/**
			 * 总金额算法：
			 *   算法1： 新价格-原价格+总价格（【使用中】）
			 *   算法2： 总价格-原价格+新价格
			 */
			BigDecimal totalMoney = detail.getMoney().subtract(detailTmp.getMoney()).add(purchOrder.getTotalMoney());
			BigDecimal totalTax = detail.getTax().subtract(detailTmp.getTax()).add(purchOrder.getTotalTax());
			BigDecimal noTaxTotalMoney = detail.getNoTaxMoney().subtract(detailTmp.getNoTaxMoney()).add(purchOrder.getNoTaxTotalMoney());
			purchOrder.setTotalMoney(totalMoney);
			purchOrder.setTotalTax(totalTax);
			purchOrder.setNoTaxTotalMoney(noTaxTotalMoney);
			daoFactory.getCommonDao().updateEntity(purchOrder);

			// 3. 修改采购明细的下游单据的单价，金额，税额，不含税金额，不含税单价也需要重新计算并保存（采购入库，采购退货，采购对账）
			DynamicQuery queryStock = new CompanyDynamicQuery(PurchStockDetail.class);
			// queryStock.eq("sourceBillType", BillType.PURCH_PO);
			queryStock.eq("sourceDetailId", purchOrderDetail.getId());
			List<PurchStockDetail> purchStockDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryStock, PurchStockDetail.class);
			if (CollectionUtils.isNotEmpty(purchStockDetailList))
			{
				for (PurchStockDetail stockDetail : purchStockDetailList)
				{
					PurchStockDetail purchStockDetailTmp = (PurchStockDetail) BeanUtils.cloneBean(stockDetail);

					// 3.1 修改采购入库明细
					stockDetail.setValuationPrice(purchOrderDetail.getValuationPrice());
					// 金额=单价*数量
					stockDetail.setMoney(purchOrderDetail.getValuationPrice().multiply(stockDetail.getValuationQty()));
					// 不含税金额
					BigDecimal noTaxMoney_work = stockDetail.getMoney().divide(new BigDecimal(1d + stockDetail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
					// 不含税单价
					BigDecimal noTaxValuationPrice_work = noTaxMoney_work.divide(stockDetail.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP);
					// 税额
					BigDecimal tax_work = stockDetail.getMoney().subtract(noTaxMoney_work);
					// 库存单价 = 金额（含税） / 数量
					BigDecimal price_work = stockDetail.getMoney().divide(stockDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
					// 不含税单价 = 含税金额 / 数量
					BigDecimal noTaxPrice_work = noTaxMoney_work.divide(stockDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
					stockDetail.setTax(tax_work);
					stockDetail.setPrice(price_work);
					stockDetail.setNoTaxPrice(noTaxPrice_work);
					stockDetail.setNoTaxMoney(noTaxMoney_work);
					stockDetail.setNoTaxValuationPrice(noTaxValuationPrice_work);
					daoFactory.getCommonDao().updateEntity(stockDetail);
					// 修改采购入库主表
					PurchStock purchStock = daoFactory.getCommonDao().getEntity(PurchStock.class, stockDetail.getMasterId());
					BigDecimal _totalMoney = stockDetail.getMoney().subtract(purchStockDetailTmp.getMoney()).add(purchStock.getTotalMoney());
					BigDecimal _totalTax = stockDetail.getTax().subtract(purchStockDetailTmp.getTax()).add(purchStock.getTotalTax());
					BigDecimal _noTaxTotalMoney = stockDetail.getNoTaxMoney().subtract(purchStockDetailTmp.getNoTaxMoney()).add(purchStock.getNoTaxTotalMoney());
					purchStock.setTotalMoney(_totalMoney);
					purchStock.setNoTaxTotalMoney(_noTaxTotalMoney);
					purchStock.setTotalTax(_totalTax);
					daoFactory.getCommonDao().updateEntity(purchStock);

					// 3.1.1 线路1 - 修改采购对账
					DynamicQuery queryReconcil = new CompanyDynamicQuery(PurchReconcilDetail.class);
					// queryReconcil.eq("sourceBillType", BillType.PURCH_PN);
					queryReconcil.eq("sourceDetailId", stockDetail.getId());
					List<PurchReconcilDetail> purchReconcilDetailList2 = daoFactory.getCommonDao().findEntityByDynamicQuery(queryReconcil, PurchReconcilDetail.class);
					if (CollectionUtils.isNotEmpty(purchReconcilDetailList2))
					{
						for (PurchReconcilDetail reconcilDetail : purchReconcilDetailList2)
						{
							PurchReconcilDetail purchReconcilDetailTmp = (PurchReconcilDetail) BeanUtils.cloneBean(reconcilDetail);

							reconcilDetail.setValuationPrice(purchOrderDetail.getValuationPrice());
							reconcilDetail.setMoney(purchOrderDetail.getValuationPrice().multiply(reconcilDetail.getValuationQty()));
							// 不含税金额
							BigDecimal noTaxMoney_reconcil = reconcilDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxValuationPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 税额
							BigDecimal tax_reconcil = reconcilDetail.getMoney().subtract(noTaxMoney_reconcil);
							// 库存单价 = 金额（含税） / 数量
							BigDecimal price_reconcil = reconcilDetail.getMoney().divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 不含税单价 = 含税金额 / 数量
							BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							reconcilDetail.setTax(tax_reconcil);
							reconcilDetail.setPrice(price_reconcil);
							reconcilDetail.setNoTaxPrice(noTaxPrice_reconcil);
							reconcilDetail.setNoTaxMoney(noTaxMoney_reconcil);
							reconcilDetail.setNoTaxValuationPrice(noTaxValuationPrice_reconcil);
							daoFactory.getCommonDao().updateEntity(reconcilDetail);

							// 修改采购对账主表数据
							PurchReconcil purchReconcil = daoFactory.getCommonDao().getEntity(PurchReconcil.class, reconcilDetail.getMasterId());
							BigDecimal totalMoney_reconcil = reconcilDetail.getMoney().subtract(purchReconcilDetailTmp.getMoney()).add(purchReconcil.getTotalMoney());
							BigDecimal totalTax_reconcil = reconcilDetail.getTax().subtract(purchReconcilDetailTmp.getTax()).add(purchReconcil.getTotalTax());
							BigDecimal noTaxTotalMoney_reconcil = reconcilDetail.getNoTaxMoney().subtract(purchReconcilDetailTmp.getNoTaxMoney()).add(purchReconcil.getNoTaxTotalMoney());
							purchReconcil.setTotalMoney(totalMoney_reconcil);
							purchReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
							purchReconcil.setTotalTax(totalTax_reconcil);
							daoFactory.getCommonDao().updateEntity(purchReconcil);
						}
					}

					// 3.1.2 线路2 - 修改采购退货、采购退货对账
					// 退货金额
					BigDecimal refundMoney = new BigDecimal(0);
					DynamicQuery queryRefund = new CompanyDynamicQuery(PurchRefundDetail.class);
					// queryRefund.eq("sourceBillType", BillType.PURCH_PN);
					queryRefund.eq("sourceDetailId", stockDetail.getId());
					List<PurchRefundDetail> purchRefundDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryRefund, PurchRefundDetail.class);
					if (CollectionUtils.isNotEmpty(purchRefundDetailList))
					{
						for (PurchRefundDetail refundDetail : purchRefundDetailList)
						{
							// 修改采购退货明细
							PurchRefundDetail refundDetailTmp = (PurchRefundDetail) BeanUtils.cloneBean(refundDetail);

							refundDetail.setValuationPrice(purchOrderDetail.getValuationPrice());
							refundDetail.setMoney(purchOrderDetail.getValuationPrice().multiply(refundDetail.getValuationQty()));
							// 不含税金额
							BigDecimal noTaxMoney_return = refundDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
							// 不含税单价
							BigDecimal noTaxValuationPrice_return = noTaxMoney_return.divide(refundDetail.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 税额
							BigDecimal tax_return = refundDetail.getMoney().subtract(noTaxMoney_return);
							// 库存单价 = 金额（含税） / 数量
							BigDecimal price_return = refundDetail.getMoney().divide(refundDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							// 不含税单价 = 含税金额 / 数量
							BigDecimal noTaxPrice_return = noTaxMoney_return.divide(refundDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
							refundDetail.setTax(tax_return);
							refundDetail.setPrice(price_return);
							refundDetail.setNoTaxPrice(noTaxPrice_return);
							refundDetail.setNoTaxMoney(noTaxMoney_return);
							refundDetail.setNoTaxValuationPrice(noTaxValuationPrice_return);
							daoFactory.getCommonDao().updateEntity(refundDetail);

							// 修改采购退货主表数据
							PurchRefund purchReturn = daoFactory.getCommonDao().getEntity(PurchRefund.class, refundDetail.getMasterId());
							BigDecimal totalMoney_return = refundDetail.getMoney().subtract(refundDetailTmp.getMoney()).add(purchReturn.getTotalMoney());
							BigDecimal totalTax_return = refundDetail.getTax().subtract(refundDetailTmp.getTax()).add(purchReturn.getTotalTax());
							BigDecimal noTaxTotalMoney_return = refundDetail.getNoTaxMoney().subtract(refundDetailTmp.getNoTaxMoney()).add(purchReturn.getNoTaxTotalMoney());
							purchReturn.setTotalMoney(totalMoney_return);
							purchReturn.setNoTaxTotalMoney(noTaxTotalMoney_return);
							purchReturn.setTotalTax(totalTax_return);
							daoFactory.getCommonDao().updateEntity(purchReturn);
							// 修改材料出库日志
							DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
							query.eq("billNo", purchReturn.getBillNo());
							query.eq("sourceId", refundDetail.getId());
							query.eq("materialId", refundDetail.getMaterialId());
							query.eq("warehouseId", refundDetail.getWarehouseId());
							if (refundDetail.getSpecifications() == null || "".equals(refundDetail.getSpecifications()))
							{
								query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
							}
							else
							{
								query.eq("specifications", refundDetail.getSpecifications());
							}
							// getByDynamicQuery自动更新数据
							StockMaterialLog stockMaterialLog_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialLog.class);
							if (null != stockMaterialLog_)
							{
								// 金额
								stockMaterialLog_.setOutMoney(refundDetail.getMoney());
								// 单价
								stockMaterialLog_.setPrice(refundDetail.getMoney().divide(refundDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP));
							}

							// 必须是审核之后
							if (purchReturn.getIsCheck() == BoolValue.YES)
							{
								// 相加：退货金额=当前金额-历史金额
								if (stockDetail.getValuationPrice().compareTo(purchStockDetailTmp.getValuationPrice()) > 0)
								{
									refundMoney = refundMoney.add(refundDetail.getMoney().subtract(refundDetailTmp.getMoney()));
								}
								// 相减：退货金额=历史金额-当前金额
								else if (stockDetail.getValuationPrice().compareTo(purchStockDetailTmp.getValuationPrice()) < 0)
								{
									refundMoney = refundMoney.add(refundDetailTmp.getMoney().subtract(refundDetail.getMoney()));
								}
							}

							// 采购对账-退货
							DynamicQuery queryReconcil2 = new CompanyDynamicQuery(PurchReconcilDetail.class);
							// queryReconcil2.eq("sourceBillType", BillType.PURCH_PR);
							queryReconcil2.eq("sourceDetailId", refundDetail.getId());
							List<PurchReconcilDetail> purchReconcilDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryReconcil2, PurchReconcilDetail.class);
							if (CollectionUtils.isNotEmpty(purchReconcilDetailList))
							{
								for (PurchReconcilDetail reconcilDetail : purchReconcilDetailList)
								{
									PurchReconcilDetail purchReconcilDetailTmp = (PurchReconcilDetail) BeanUtils.cloneBean(reconcilDetail);

									reconcilDetail.setValuationPrice(purchOrderDetail.getValuationPrice());
									reconcilDetail.setMoney(purchOrderDetail.getValuationPrice().multiply(reconcilDetail.getValuationQty()));
									// 不含税金额
									BigDecimal noTaxMoney_reconcil = reconcilDetail.getMoney().divide(new BigDecimal(1d + detail.getPercent() / 100d), 2, BigDecimal.ROUND_HALF_UP);
									// 不含税单价
									BigDecimal noTaxValuationPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getValuationQty(), 4, BigDecimal.ROUND_HALF_UP);
									// 税额
									BigDecimal tax_reconcil = reconcilDetail.getMoney().subtract(noTaxMoney_reconcil);
									// 库存单价 = 金额（含税） / 数量
									BigDecimal price_reconcil = reconcilDetail.getMoney().divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
									// 不含税单价 = 含税金额 / 数量
									BigDecimal noTaxPrice_reconcil = noTaxMoney_reconcil.divide(reconcilDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP);
									reconcilDetail.setTax(tax_reconcil);
									reconcilDetail.setPrice(price_reconcil);
									reconcilDetail.setNoTaxPrice(noTaxPrice_reconcil);
									reconcilDetail.setNoTaxMoney(noTaxMoney_reconcil);
									reconcilDetail.setNoTaxValuationPrice(noTaxValuationPrice_reconcil);
									daoFactory.getCommonDao().updateEntity(reconcilDetail);

									// 修改采购对账主表数据
									PurchReconcil purchReconcil = daoFactory.getCommonDao().getEntity(PurchReconcil.class, reconcilDetail.getMasterId());
									BigDecimal totalMoney_reconcil = reconcilDetail.getMoney().subtract(purchReconcilDetailTmp.getMoney()).add(purchReconcil.getTotalMoney());
									BigDecimal totalTax_reconcil = reconcilDetail.getTax().subtract(purchReconcilDetailTmp.getTax()).add(purchReconcil.getTotalTax());
									BigDecimal noTaxTotalMoney_reconcil = reconcilDetail.getNoTaxMoney().subtract(purchReconcilDetailTmp.getNoTaxMoney()).add(purchReconcil.getNoTaxTotalMoney());
									purchReconcil.setTotalMoney(totalMoney_reconcil);
									purchReconcil.setNoTaxTotalMoney(noTaxTotalMoney_reconcil);
									purchReconcil.setTotalTax(totalTax_reconcil);
									daoFactory.getCommonDao().updateEntity(purchReconcil);
								}
							}
						}
					}

					// 3.1.3 修改库存金额、材料出库日志
					StockMaterial stockMaterial = new StockMaterial();
					stockMaterial.setMaterialId(stockDetail.getMaterialId());
					stockMaterial.setSpecifications(stockDetail.getSpecifications());
					stockMaterial.setMaterialClassId(stockDetail.getMaterialClassId());
					stockMaterial.setWarehouseId(stockDetail.getWarehouseId());
					stockMaterial.setValuationPrice(stockDetail.getValuationPrice());
					// 单价增加
					if (stockDetail.getValuationPrice().compareTo(purchStockDetailTmp.getValuationPrice()) > 0)
					{
						stockMaterial.setMoney(stockDetail.getMoney().subtract(purchStockDetailTmp.getMoney()).subtract(refundMoney));
					}
					// 单价相减
					else if (stockDetail.getValuationPrice().compareTo(purchStockDetailTmp.getValuationPrice()) < 0)
					{
						// StringBuilder sb = new StringBuilder();
						// sb.append("当前金额：").append(stockDetail.getMoney()).append(" -
						// 历史金额：").append(purchStockDetailTmp.getMoney());
						// sb.append(" = ").append(stockDetail.getMoney().subtract(purchStockDetailTmp.getMoney()));
						// sb.append("\r\n");
						// sb.append("退货金额：").append(refundMoney);
						// System.out.println(sb.toString());
						stockMaterial.setMoney(stockDetail.getMoney().subtract(purchStockDetailTmp.getMoney()).add(refundMoney));
					}
					else
					{
						// stockMaterial.setMoney(new BigDecimal(0));
						return;
					}
					serviceFactory.getMaterialStockService().changeValuationPrice(stockMaterial);
					// 修改材料出库日志
					DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
					query.eq("billNo", purchStock.getBillNo());
					query.eq("sourceId", stockDetail.getId());
					query.eq("materialId", stockDetail.getMaterialId());
					query.eq("warehouseId", stockDetail.getWarehouseId());
					if (stockMaterial.getSpecifications() == null || "".equals(stockMaterial.getSpecifications()))
					{
						// stockMaterial.setSpecifications(null);
						query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
					}
					else
					{
						query.eq("specifications", stockDetail.getSpecifications());
					}
					// getByDynamicQuery自动更新数据
					StockMaterialLog stockMaterialLog_ = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialLog.class);
					if (null != stockMaterialLog_)
					{
						// 金额
						stockMaterialLog_.setInMoney(stockDetail.getMoney());
						// 单价
						stockMaterialLog_.setPrice(stockDetail.getMoney().divide(stockDetail.getQty(), 4, BigDecimal.ROUND_HALF_UP));
					}

					// daoFactory.getCommonDao().updateEntity(stockMaterialLog_);
				}
			}
		}
	}

}
