/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.begin.MaterialBegin;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.begin.MaterialBeginService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Service
public class MaterialBeginServiceImpl extends BaseServiceImpl implements MaterialBeginService
{
	@Override
	public MaterialBegin get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialBegin.class);
		query.eq("id", id);
		MaterialBegin materialBegin = daoFactory.getCommonDao().getByDynamicQuery(query, MaterialBegin.class);

		if (materialBegin != null)
		{
			materialBegin.setDetailList(this.getDetail(id));
		}
		return materialBegin;
	}

	@Override
	public List<MaterialBeginDetail> getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialBeginDetail.class);
		query.eq("masterId", id);
		List<MaterialBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, MaterialBeginDetail.class);
		return detailList;

	}

	@Override
	@Transactional
	public Long save(MaterialBegin materialBegin)
	{
		BoolValue flag = materialBegin.getIsCheck();					// 标识是否保存并审核
		materialBegin.setIsCheck(BoolValue.NO);								// 默认未审核
		materialBegin.setBillType(BillType.BEGIN_MATERIAL);
		materialBegin.setBillNo(UserUtils.createBillNo(BillType.BEGIN_MATERIAL));
		materialBegin.setCompanyId(UserUtils.getCompanyId());
		materialBegin.setCreateTime(new Date());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			materialBegin.setCreateName(e.getName());
		}
		else
		{
			materialBegin.setCreateName(UserUtils.getUserName());
		}
		materialBegin.setCreateEmployeeId(UserUtils.getEmployeeId());
		materialBegin.setMemo(materialBegin.getMemo());
		daoFactory.getCommonDao().saveEntity(materialBegin);
		// daoFactory.getCommonDao().getEntity(Warehouse.class, materialBegin.getWarehouseId()).setIsBegin(BoolValue.YES);
		for (MaterialBeginDetail detail : materialBegin.getDetailList())
		{
			detail.setMasterId(materialBegin.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMemo(detail.getMemo());
			daoFactory.getCommonDao().saveEntity(detail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(materialBegin.getId());
		}
		return materialBegin.getId();

	}

	@Override
	@Transactional
	public Long update(MaterialBegin materialBegin)
	{
		MaterialBegin materialBegin_ = this.lockHasChildren(materialBegin.getId());
		// 先判断是否已经审核
		if (materialBegin_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (MaterialBeginDetail newItem : materialBegin.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (MaterialBeginDetail oldItem : materialBegin_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(MaterialBeginDetail.class, id);
			}
		}

		materialBegin_.setBeginTime(materialBegin.getBeginTime());
		materialBegin_.setMemo(materialBegin.getMemo());
		daoFactory.getCommonDao().getEntity(Warehouse.class, materialBegin_.getWarehouseId()).setIsBegin(BoolValue.NO);
		daoFactory.getCommonDao().getEntity(Warehouse.class, materialBegin.getWarehouseId()).setIsBegin(BoolValue.YES);
		materialBegin_.setWarehouseId(materialBegin.getWarehouseId());
		for (MaterialBeginDetail detail : materialBegin.getDetailList())
		{
			if (detail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(MaterialBeginDetail.class);
				querySt.eq("id", detail.getId());
				MaterialBeginDetail materialBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, MaterialBeginDetail.class);
				materialBeginDetail.setMaterialId(detail.getMaterialId());
				materialBeginDetail.setMaterialCode(detail.getMaterialCode());
				materialBeginDetail.setMaterialName(detail.getMaterialName());
				materialBeginDetail.setSpecifications(detail.getSpecifications());
				materialBeginDetail.setQty(detail.getQty());
				materialBeginDetail.setPrice(detail.getPrice());
				materialBeginDetail.setMoney(detail.getMoney());
				materialBeginDetail.setValuationPrice(detail.getValuationPrice());
				materialBeginDetail.setValuationQty(detail.getValuationQty());
				materialBeginDetail.setMemo(detail.getMemo());
				daoFactory.getCommonDao().updateEntity(materialBeginDetail);
			}
			else
			{
				detail.setMasterId(materialBegin.getId());
				detail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(detail);
			}
		}
		if (materialBegin.getIsCheck() == BoolValue.YES)
		{
			this.check(materialBegin.getId());
		}
		return materialBegin.getId();

	}

	

	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			MaterialBegin master = this.lockHasChildren(id);
			daoFactory.getCommonDao().getEntity(Warehouse.class, master.getWarehouseId()).setIsBegin(BoolValue.NO);
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
			daoFactory.getCommonDao().deleteEntity(master);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional
	public Integer check(Long id)
	{
		MaterialBegin order = this.get(id);
		// 先判断是否已经审核
		if (order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (MaterialBeginDetail detail : order.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(detail.getMaterialId());
			stockMaterial.setSpecifications(detail.getSpecifications());
			stockMaterial.setQty(detail.getQty());
			stockMaterial.setValuationQty(detail.getValuationQty());
			stockMaterial.setWarehouseId(order.getWarehouseId());
			stockMaterial.setPrice(detail.getPrice());
			stockMaterial.setValuationPrice(detail.getValuationPrice());
			stockMaterial.setMoney(detail.getMoney());
			stockMaterial.setUpdateTime(new Date());
			stockMaterial.setMaterialClassId(detail.getMaterialClassId());
			serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.IN);

			// 入库记录
			StockMaterialLog log = new StockMaterialLog();
			log.setBillId(id);
			log.setBillType(order.getBillType());
			log.setBillNo(order.getBillNo());
			log.setSourceId(detail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(detail.getMaterialCode());
			log.setMaterialClassId(detail.getMaterialClassId());
			log.setMaterialName(detail.getMaterialName());
			log.setMaterialId(detail.getMaterialId());
			log.setSpecifications(detail.getSpecifications());
			log.setWarehouseId(order.getWarehouseId());
			log.setWeight(detail.getWeight());
			log.setUnitId(detail.getStockUnitId());
			log.setPrice(detail.getPrice());
			log.setInQty(detail.getQty());
			log.setInMoney(detail.getMoney());
			daoFactory.getCommonDao().saveEntity(log);
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_MATERIAL, id, BoolValue.YES);
		return 1;

	}
	
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		MaterialBegin order = this.get(id);
		// 先判断是否已经反审核
		if (order.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		List<StockMaterialLog> delList = Lists.newArrayList();
		for (MaterialBeginDetail detail : order.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(detail.getMaterialId());
			stockMaterial.setSpecifications(detail.getSpecifications());
			stockMaterial.setQty(detail.getQty());
			stockMaterial.setValuationQty(detail.getValuationQty());
			stockMaterial.setWarehouseId(order.getWarehouseId());
			stockMaterial.setPrice(detail.getPrice());
			stockMaterial.setValuationPrice(detail.getValuationPrice());
			stockMaterial.setMoney(detail.getMoney());
			stockMaterial.setUpdateTime(new Date());
			stockMaterial.setMaterialClassId(detail.getMaterialClassId());
			serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.IN);

			// 查询入库记录
			DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
			query.eq("billNo", order.getBillNo());
			query.eq("materialId", detail.getMaterialId());
			query.eq("warehouseId", order.getWarehouseId());
			StockMaterialLog log = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialLog.class);
			delList.add(log);
		}
		daoFactory.getCommonDao().deleteAllEntity(delList);// 删除入库记录
		serviceFactory.getCommonService().audit(BillType.BEGIN_MATERIAL, id, BoolValue.NO);
		return 1;

	}
	
	@Override
	public List<MaterialBeginDetail> isBeginList(MaterialBegin materialBegin)
	{
		List<MaterialBeginDetail> result = new ArrayList<MaterialBeginDetail>();
		for (MaterialBeginDetail detail : materialBegin.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(MaterialBeginDetail.class, "a");
			query.addProjection(Projections.property("a"));
			query.createAlias(MaterialBegin.class, "b");
			query.eqProperty("b.id", "a.masterId");
			query.eq("a.materialId", detail.getMaterialId());
			if (detail.getSpecifications() == null || "".equals(detail.getSpecifications()))
			{
				detail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("a.specifications"), Restrictions.eq("a.specifications", "")));
			}
			else
			{
				query.eq("a.specifications", detail.getSpecifications());
			}
			query.eq("b.warehouseId", materialBegin.getWarehouseId());
			if (materialBegin.getId() != null)
			{
				query.ne("b.id", materialBegin.getId());
			}
			MaterialBeginDetail materialBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(query, MaterialBeginDetail.class);
			if (materialBeginDetail != null)
			{
				result.add(materialBeginDetail);
			}
		}
		return result;
	}

	@Override
	public SearchResult<MaterialBegin> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialBegin.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}

		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, MaterialBegin.class);
	}

	@Override
	public MaterialBegin lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(MaterialBegin.class);
		query.eq("id", id);
		MaterialBegin order = daoFactory.getCommonDao().lockByDynamicQuery(query, MaterialBegin.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(MaterialBeginDetail.class);
		query_detail.eq("masterId", id);
		List<MaterialBeginDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, MaterialBeginDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
