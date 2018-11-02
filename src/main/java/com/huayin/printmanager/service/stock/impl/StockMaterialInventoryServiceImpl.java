/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.stock.impl;

import java.math.BigDecimal;
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
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventory;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialInventoryService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料盘点单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialInventoryServiceImpl extends BaseServiceImpl implements StockMaterialInventoryService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialInventoryService#findByCondition(com.huayin.printmanager.service
	 * .vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialInventory> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialInventory.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("inventoryTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("inventoryTime", queryParam.getDateMax());
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("inventoryEmployeeId", queryParam.getEmployeeId());
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
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialInventory.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialInventoryService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialInventoryDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialInventoryDetail.class, "ad");
		query.createAlias(StockMaterialInventory.class, "a");

		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.inventoryTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.inventoryTime", queryParam.getDateMax());
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
		SearchResult<StockMaterialInventoryDetail> result = new SearchResult<StockMaterialInventoryDetail>();
		result.setResult(new ArrayList<StockMaterialInventoryDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialInventoryDetail detail = (StockMaterialInventoryDetail) c[0];
			detail.setMaster((StockMaterialInventory) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialInventoryService#save(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialInventory)
	 */
	@Override
	@Transactional
	public Long save(StockMaterialInventory stockMaterialInventory)
	{
		BoolValue flag = stockMaterialInventory.getIsCheck();		// 标识是否保存并审核
		stockMaterialInventory.setIsCheck(BoolValue.NO);				// 默认未审核
		stockMaterialInventory.setBillType(BillType.STOCK_SMI);
		stockMaterialInventory.setBillNo(UserUtils.createBillNo(BillType.STOCK_SMI));
		stockMaterialInventory.setCompanyId(UserUtils.getCompanyId());
		stockMaterialInventory.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialInventory.setCreateName(e.getName());
		}
		else
		{
			stockMaterialInventory.setCreateName(UserUtils.getUserName());
		}
		stockMaterialInventory.setCreateEmployeeId(UserUtils.getEmployeeId());
		daoFactory.getCommonDao().saveEntity(stockMaterialInventory);
		for (StockMaterialInventoryDetail stockMaterialInventoryDetail : stockMaterialInventory.getDetailList())
		{
			if ("".equals(stockMaterialInventoryDetail.getCode()) || stockMaterialInventoryDetail.getCode() == null)
			{
				continue;
			}
			stockMaterialInventoryDetail.setMasterId(stockMaterialInventory.getId());
			stockMaterialInventoryDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockMaterialInventoryDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockMaterialInventory.getId());
		}
		return stockMaterialInventory.getId();

	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialInventoryService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialInventory)
	 */
	@Override
	@Transactional
	public Long update(StockMaterialInventory stockMaterialInventory)
	{
		StockMaterialInventory stockMaterialInventory_ = this.lockHasChildren(stockMaterialInventory.getId());
		if (stockMaterialInventory_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialInventoryDetail newItem : stockMaterialInventory.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialInventoryDetail oldItem : stockMaterialInventory_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialInventoryDetail.class, id);
			}
		}

		stockMaterialInventory_.setWarehouseId(stockMaterialInventory.getWarehouseId());
		stockMaterialInventory_.setInventoryTime(stockMaterialInventory.getInventoryTime());
		stockMaterialInventory_.setEmployeeId(stockMaterialInventory.getEmployeeId());
		stockMaterialInventory_.setEmployeeName(stockMaterialInventory.getEmployeeName());
		stockMaterialInventory_.setMemo(stockMaterialInventory.getMemo());
		daoFactory.getCommonDao().updateEntity(stockMaterialInventory_);
		for (StockMaterialInventoryDetail stockMaterialInventoryDetail : stockMaterialInventory.getDetailList())
		{
			if ("".equals(stockMaterialInventoryDetail.getCode()) || stockMaterialInventoryDetail.getCode() == null)
			{
				continue;
			}
			if (stockMaterialInventoryDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialInventoryDetail.class);
				querySt.eq("id", stockMaterialInventoryDetail.getId());
				StockMaterialInventoryDetail stockMaterialInventoryDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialInventoryDetail.class);
				stockMaterialInventoryDetail_.setCode(stockMaterialInventoryDetail.getCode());
				stockMaterialInventoryDetail_.setMaterialName(stockMaterialInventoryDetail.getMaterialName());
				stockMaterialInventoryDetail_.setSpecifications(stockMaterialInventoryDetail.getSpecifications());
				stockMaterialInventoryDetail_.setStockUnitId(stockMaterialInventoryDetail.getStockUnitId());
				stockMaterialInventoryDetail_.setQty(stockMaterialInventoryDetail.getQty());
				stockMaterialInventoryDetail_.setPrice(stockMaterialInventoryDetail.getPrice());
				stockMaterialInventoryDetail_.setValuationUnitId(stockMaterialInventoryDetail.getValuationUnitId());
				stockMaterialInventoryDetail_.setValuationQty(stockMaterialInventoryDetail.getValuationQty());
				stockMaterialInventoryDetail_.setValuationPrice(stockMaterialInventoryDetail.getValuationPrice());
				stockMaterialInventoryDetail_.setMoney(stockMaterialInventoryDetail.getMoney());
				stockMaterialInventoryDetail_.setMaterialClassId(stockMaterialInventoryDetail.getMaterialClassId());
				stockMaterialInventoryDetail_.setMemo(stockMaterialInventoryDetail.getMemo());
				stockMaterialInventoryDetail_.setProfitAndLossQty(stockMaterialInventoryDetail.getProfitAndLossQty());
				stockMaterialInventoryDetail_.setProfitAndLossMoney(stockMaterialInventoryDetail.getProfitAndLossMoney());
				daoFactory.getCommonDao().updateEntity(stockMaterialInventoryDetail_);
			}
			else
			{
				stockMaterialInventoryDetail.setMasterId(stockMaterialInventory.getId());
				stockMaterialInventoryDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialInventoryDetail);
			}

		}
		if (stockMaterialInventory.getIsCheck() == BoolValue.YES)
		{
			this.check(stockMaterialInventory.getId());
		}
		return stockMaterialInventory_.getId();

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialInventoryService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialInventory get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialInventory.class);
		query.eq("id", id);
		StockMaterialInventory stockMaterialInventory = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialInventory.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialInventoryDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialInventoryDetail> stockMaterialInventoryDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialInventoryDetail.class);
		if (stockMaterialInventory != null)
		{
			stockMaterialInventory.setDetailList(stockMaterialInventoryDetailList);
		}
		return stockMaterialInventory;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialInventoryService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialInventory master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialInventoryService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		StockMaterialInventory master = this.get(id);
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (StockMaterialInventoryDetail stockMaterialInventoryDetail : master.getDetailList())
		{
			StockMaterial stockMaterial_old = daoFactory.getCommonDao().getEntity(StockMaterial.class, stockMaterialInventoryDetail.getStockMaterialId());
			// 出入库记录
			StockMaterialLog log = new StockMaterialLog();
			log.setBillId(master.getId());
			log.setBillType(master.getBillType());
			log.setBillNo(master.getBillNo());
			log.setSourceId(stockMaterialInventoryDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(stockMaterialInventoryDetail.getCode());
			log.setMaterialClassId(stockMaterialInventoryDetail.getMaterialClassId());
			log.setMaterialName(stockMaterialInventoryDetail.getMaterialName());
			log.setMaterialId(stockMaterialInventoryDetail.getMaterialId());
			log.setSpecifications(stockMaterialInventoryDetail.getSpecifications());
			log.setWarehouseId(master.getWarehouseId());
			log.setWeight(stockMaterialInventoryDetail.getWeight());
			log.setUnitId(stockMaterialInventoryDetail.getStockUnitId());
			log.setPrice(stockMaterialInventoryDetail.getPrice());
			if (stockMaterialInventoryDetail.getQty().subtract(stockMaterial_old.getQty()).doubleValue() > 0)
			{
				log.setInQty(stockMaterialInventoryDetail.getQty().subtract(stockMaterial_old.getQty()));
				log.setInMoney(log.getInQty().multiply(log.getPrice()));
			}
			else
			{
				log.setOutQty(stockMaterial_old.getQty().subtract(stockMaterialInventoryDetail.getQty()));
				log.setOutMoney(log.getOutQty().multiply(log.getPrice()));
			}
			daoFactory.getCommonDao().saveEntity(log);

			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setId(stockMaterialInventoryDetail.getStockMaterialId());
			stockMaterial.setQty(stockMaterialInventoryDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialInventoryDetail.getValuationQty());
			stockMaterial.setMoney(stockMaterialInventoryDetail.getMoney());
			stockMaterial.setUpdateTime(new Date());
			serviceFactory.getMaterialStockService().adjust(stockMaterial);

		}
		serviceFactory.getCommonService().audit(BillType.STOCK_SMI, id, BoolValue.YES);
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialInventoryService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		StockMaterialInventory master = this.get(id);
		// 先判断是否已经反审核
		if (master.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		DynamicQuery queryd = new CompanyDynamicQuery(StockMaterialInventoryDetail.class);
		queryd.eq("masterId", id);
		List<StockMaterialInventoryDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryd, StockMaterialInventoryDetail.class);
		for (StockMaterialInventoryDetail stockMaterialInventoryDetail : detailList)
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setId(stockMaterialInventoryDetail.getStockMaterialId());
			stockMaterial.setQty(new BigDecimal(0).subtract(stockMaterialInventoryDetail.getQty()));
			stockMaterial.setValuationQty(new BigDecimal(0).subtract(stockMaterialInventoryDetail.getValuationQty()));
			stockMaterial.setMoney(stockMaterialInventoryDetail.getMoney());
			stockMaterial.setUpdateTime(new Date());
			serviceFactory.getMaterialStockService().adjust(stockMaterial);

			DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
			query.eq("billId", id);
			List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
			daoFactory.getCommonDao().deleteAllEntity(logs);
		}
		serviceFactory.getCommonService().audit(BillType.STOCK_SMI, id, BoolValue.NO);
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialInventoryService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialInventory lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialInventory.class);
		query.eq("id", id);
		StockMaterialInventory order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialInventory.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialInventoryDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialInventoryDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialInventoryDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
