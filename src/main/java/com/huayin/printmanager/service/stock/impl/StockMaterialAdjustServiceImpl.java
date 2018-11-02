/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.stock.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjust;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialAdjustService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调整单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialAdjustServiceImpl extends BaseServiceImpl implements StockMaterialAdjustService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialAdjustService#findByCondition(com.huayin.printmanager.service.vo
	 * .QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialAdjust> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialAdjust.class);
		if (queryParam.getDateMin() != null)
		{
			query.ge("adjustTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("adjustTime", queryParam.getDateMax());
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("adjustEmployeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
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
		query.setIsSearchTotalCount(true);
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialAdjust.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialAdjustService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialAdjustDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialAdjustDetail.class, "ad");
		query.createAlias(StockMaterialAdjust.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.adjustTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.adjustTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("ad.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("a.warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotBlank(queryParam.getSpecifications()))
		{
			query.like("ad.specifications", "%" + queryParam.getSpecifications() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("a.isCheck", queryParam.getAuditFlag());
		}
		query.eq("ad.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("a.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<StockMaterialAdjustDetail> result = new SearchResult<StockMaterialAdjustDetail>();
		result.setResult(new ArrayList<StockMaterialAdjustDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialAdjustDetail detail = (StockMaterialAdjustDetail) c[0];
			detail.setMaster((StockMaterialAdjust) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialAdjustService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterialAdjust)
	 */
	@Override
	@Transactional
	public Long save(StockMaterialAdjust stockMaterialAdjust)
	{
		try
		{
			stockMaterialAdjust.setBillType(BillType.STOCK_SMA);
			stockMaterialAdjust.setBillNo(UserUtils.createBillNo(BillType.STOCK_SMA));
			stockMaterialAdjust.setCompanyId(UserUtils.getCompanyId());
			stockMaterialAdjust.setCreateTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				stockMaterialAdjust.setCreateName(e.getName());
			}
			else
			{
				stockMaterialAdjust.setCreateName(UserUtils.getUserName());
			}
			stockMaterialAdjust.setCreateEmployeeId(UserUtils.getEmployeeId());
			daoFactory.getCommonDao().saveEntity(stockMaterialAdjust);
			for (StockMaterialAdjustDetail stockMaterialAdjustDetail : stockMaterialAdjust.getDetailList())
			{
				stockMaterialAdjustDetail.setMasterId(stockMaterialAdjust.getId());
				stockMaterialAdjustDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialAdjustDetail);
			}
			if (stockMaterialAdjust.getIsCheck() == BoolValue.YES)
			{
				this.check(stockMaterialAdjust.getId());
			}
			return stockMaterialAdjust.getId();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return (long) 0;
		}

	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialAdjustService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialAdjust)
	 */
	@Override
	@Transactional
	public Long update(StockMaterialAdjust stockMaterialAdjust)
	{
		StockMaterialAdjust stockMaterialAdjust_ = this.lockHasChildren(stockMaterialAdjust.getId());
		// 先判断是否已经审核
		if (stockMaterialAdjust_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialAdjustDetail newItem : stockMaterialAdjust.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialAdjustDetail oldItem : stockMaterialAdjust_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialAdjustDetail.class, id);
			}
		}

		stockMaterialAdjust_.setWarehouseId(stockMaterialAdjust.getWarehouseId());
		stockMaterialAdjust_.setAdjustTime(stockMaterialAdjust.getAdjustTime());
		stockMaterialAdjust_.setEmployeeId(stockMaterialAdjust.getEmployeeId());
		stockMaterialAdjust_.setEmployeeName(stockMaterialAdjust.getEmployeeName());
		stockMaterialAdjust_.setMemo(stockMaterialAdjust.getMemo());
		daoFactory.getCommonDao().updateEntity(stockMaterialAdjust_);
		for (StockMaterialAdjustDetail stockMaterialAdjustDetail : stockMaterialAdjust.getDetailList())
		{
			if (stockMaterialAdjustDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialAdjustDetail.class);
				querySt.eq("id", stockMaterialAdjustDetail.getId());
				StockMaterialAdjustDetail stockMaterialAdjustDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialAdjustDetail.class);
				stockMaterialAdjustDetail_.setCode(stockMaterialAdjustDetail.getCode());
				stockMaterialAdjustDetail_.setMaterialName(stockMaterialAdjustDetail.getMaterialName());
				stockMaterialAdjustDetail_.setSpecifications(stockMaterialAdjustDetail.getSpecifications());
				stockMaterialAdjustDetail_.setStockUnitId(stockMaterialAdjustDetail.getStockUnitId());
				stockMaterialAdjustDetail_.setQty(stockMaterialAdjustDetail.getQty());
				stockMaterialAdjustDetail_.setPrice(stockMaterialAdjustDetail.getPrice());
				stockMaterialAdjustDetail_.setValuationUnitId(stockMaterialAdjustDetail.getValuationUnitId());
				stockMaterialAdjustDetail_.setValuationQty(stockMaterialAdjustDetail.getValuationQty());
				stockMaterialAdjustDetail_.setValuationPrice(stockMaterialAdjustDetail.getValuationPrice());
				stockMaterialAdjustDetail_.setMoney(stockMaterialAdjustDetail.getMoney());
				stockMaterialAdjustDetail_.setMaterialClassId(stockMaterialAdjustDetail.getMaterialClassId());
				stockMaterialAdjustDetail_.setMemo(stockMaterialAdjustDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockMaterialAdjustDetail_);
			}
			else
			{
				stockMaterialAdjustDetail.setMasterId(stockMaterialAdjust.getId());
				stockMaterialAdjustDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialAdjustDetail);
			}

		}
		if (stockMaterialAdjust.getIsCheck() == BoolValue.YES)
		{
			this.check(stockMaterialAdjust.getId());
		}
		return stockMaterialAdjust_.getId();

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialAdjustService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialAdjust get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialAdjust.class);
		query.eq("id", id);
		StockMaterialAdjust stockMaterialAdjust = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialAdjust.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialAdjustDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialAdjustDetail> stockMaterialAdjustDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialAdjustDetail.class);
		if (stockMaterialAdjust != null)
		{
			stockMaterialAdjust.setDetailList(stockMaterialAdjustDetailList);
		}
		return stockMaterialAdjust;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialAdjustService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialAdjust master = this.lockHasChildren(id);
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialAdjustService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMA, id, BoolValue.YES))
		{
			return 0;
		}
		// 审核操作
		StockMaterialAdjust master = this.get(id);
		for (StockMaterialAdjustDetail stockMaterialAdjustDetail : master.getDetailList())
		{
			StockMaterial stockMaterial_old = daoFactory.getCommonDao().getEntity(StockMaterial.class, stockMaterialAdjustDetail.getStockMaterialId());

			// 出入库记录
			StockMaterialLog log = new StockMaterialLog();
			log.setBillId(master.getId());
			log.setBillType(master.getBillType());
			log.setBillNo(master.getBillNo());
			log.setSourceId(stockMaterialAdjustDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(stockMaterialAdjustDetail.getCode());
			log.setMaterialClassId(stockMaterialAdjustDetail.getMaterialClassId());
			log.setMaterialName(stockMaterialAdjustDetail.getMaterialName());
			log.setMaterialId(stockMaterialAdjustDetail.getMaterialId());
			log.setSpecifications(stockMaterialAdjustDetail.getSpecifications());
			log.setWeight(stockMaterialAdjustDetail.getWeight());
			log.setWarehouseId(master.getWarehouseId());
			log.setUnitId(stockMaterialAdjustDetail.getStockUnitId());
			log.setPrice(stockMaterialAdjustDetail.getPrice());
			if (stockMaterialAdjustDetail.getQty().subtract(stockMaterial_old.getQty()).doubleValue() > 0)
			{
				log.setInQty(stockMaterialAdjustDetail.getQty().subtract(stockMaterial_old.getQty()));
				log.setInMoney(log.getInQty().multiply(log.getPrice()));
			}
			else
			{
				log.setOutQty(stockMaterial_old.getQty().subtract(stockMaterialAdjustDetail.getQty()));
				log.setOutMoney(log.getOutQty().multiply(log.getPrice()));
			}
			daoFactory.getCommonDao().saveEntity(log);

			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setId(stockMaterialAdjustDetail.getStockMaterialId());
			stockMaterial.setQty(stockMaterialAdjustDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialAdjustDetail.getValuationQty());
			stockMaterial.setMoney(stockMaterialAdjustDetail.getMoney());
			stockMaterial.setUpdateTime(new Date());
			serviceFactory.getMaterialStockService().adjust(stockMaterial);
		}
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialAdjustService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialAdjust lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialAdjust.class);
		query.eq("id", id);
		StockMaterialAdjust order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialAdjust.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialAdjustDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialAdjustDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialAdjustDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
