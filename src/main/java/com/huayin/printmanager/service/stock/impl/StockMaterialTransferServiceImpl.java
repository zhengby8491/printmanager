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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransfer;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialTransferService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialTransferServiceImpl extends BaseServiceImpl implements StockMaterialTransferService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTransferService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialTransfer> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTransfer.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("transferTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("transferTime", queryParam.getDateMax());
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("employeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getOutWarehouseId())
		{
			query.eq("outWarehouseId", queryParam.getOutWarehouseId());
		}
		if (null != queryParam.getInWarehouseId())
		{
			query.eq("inWarehouseId", queryParam.getInWarehouseId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialTransfer.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTransferService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialTransferDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTransferDetail.class, "ad");
		query.createAlias(StockMaterialTransfer.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.transferTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.transferTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("ad.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getOutWarehouseId())
		{
			query.eq("a.outWarehouseId", queryParam.getOutWarehouseId());
		}
		if (null != queryParam.getInWarehouseId())
		{
			query.eq("a.inWarehouseId", queryParam.getInWarehouseId());
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
		SearchResult<StockMaterialTransferDetail> result = new SearchResult<StockMaterialTransferDetail>();
		result.setResult(new ArrayList<StockMaterialTransferDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialTransferDetail detail = (StockMaterialTransferDetail) c[0];
			detail.setMaster((StockMaterialTransfer) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTransferService#save(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialTransfer)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialTransfer> save(StockMaterialTransfer stockMaterialTransfer)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialTransfer.getIsCheck();
		stockMaterialTransfer.setBillType(BillType.STOCK_SMT);
		stockMaterialTransfer.setBillNo(UserUtils.createBillNo(BillType.STOCK_SMT));
		stockMaterialTransfer.setCompanyId(UserUtils.getCompanyId());
		stockMaterialTransfer.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialTransfer.setCreateName(e.getName());
		}
		else
		{
			stockMaterialTransfer.setCreateName(UserUtils.getUserName());
		}
		stockMaterialTransfer.setCreateEmployeeId(UserUtils.getEmployeeId());
		stockMaterialTransfer.setIsCheck(BoolValue.NO);
		daoFactory.getCommonDao().saveEntity(stockMaterialTransfer);
		for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
		{
			stockMaterialTransferDetail.setMasterId(stockMaterialTransfer.getId());
			stockMaterialTransferDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockMaterialTransferDetail);
		}
		ServiceResult<StockMaterialTransfer> serviceResult = new ServiceResult<StockMaterialTransfer>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialTransfer.getId(), stockMaterialTransfer.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialTransfer);
		return serviceResult;

	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialTransferService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialTransfer)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialTransfer> update(StockMaterialTransfer stockMaterialTransfer)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialTransfer.getIsCheck();

		ServiceResult<StockMaterialTransfer> serviceResult = new ServiceResult<StockMaterialTransfer>();

		// 再更新数据
		StockMaterialTransfer stockMaterialTransfer_ = this.lockHasChildren(stockMaterialTransfer.getId());
		// 先判断是否已经审核
		if (stockMaterialTransfer_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialTransferDetail newItem : stockMaterialTransfer.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialTransferDetail oldItem : stockMaterialTransfer_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialTransferDetail.class, id);
			}
		}
		stockMaterialTransfer_.setIsCheck(BoolValue.NO);
		stockMaterialTransfer_.setOutWarehouseId(stockMaterialTransfer.getOutWarehouseId());
		stockMaterialTransfer_.setInWarehouseId(stockMaterialTransfer.getInWarehouseId());
		stockMaterialTransfer_.setTransferTime(stockMaterialTransfer.getTransferTime());
		stockMaterialTransfer_.setEmployeeId(stockMaterialTransfer.getEmployeeId());
		stockMaterialTransfer_.setEmployeeName(stockMaterialTransfer.getEmployeeName());
		stockMaterialTransfer_.setMemo(stockMaterialTransfer.getMemo());
		daoFactory.getCommonDao().updateEntity(stockMaterialTransfer_);
		for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
		{
			if (stockMaterialTransferDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialTransferDetail.class);
				querySt.eq("id", stockMaterialTransferDetail.getId());
				StockMaterialTransferDetail stockMaterialTransferDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialTransferDetail.class);
				stockMaterialTransferDetail_.setCode(stockMaterialTransferDetail.getCode());
				stockMaterialTransferDetail_.setMaterialName(stockMaterialTransferDetail.getMaterialName());
				stockMaterialTransferDetail_.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				stockMaterialTransferDetail_.setStockUnitId(stockMaterialTransferDetail.getStockUnitId());
				stockMaterialTransferDetail_.setQty(stockMaterialTransferDetail.getQty());
				stockMaterialTransferDetail_.setPrice(stockMaterialTransferDetail.getPrice());
				stockMaterialTransferDetail_.setValuationUnitId(stockMaterialTransferDetail.getValuationUnitId());
				stockMaterialTransferDetail_.setValuationQty(stockMaterialTransferDetail.getValuationQty());
				stockMaterialTransferDetail_.setValuationPrice(stockMaterialTransferDetail.getValuationPrice());
				stockMaterialTransferDetail_.setMoney(stockMaterialTransferDetail.getMoney());
				stockMaterialTransferDetail_.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				stockMaterialTransferDetail_.setMemo(stockMaterialTransferDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockMaterialTransferDetail_);
			}
			else
			{
				stockMaterialTransferDetail.setMasterId(stockMaterialTransfer.getId());
				stockMaterialTransferDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialTransferDetail);
			}

		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialTransfer.getId(), stockMaterialTransfer.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialTransfer);
		return serviceResult;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTransferService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialTransfer get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTransfer.class);
		query.eq("id", id);
		StockMaterialTransfer stockMaterialTransfer = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialTransfer.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialTransferDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialTransferDetail> stockMaterialTransferDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialTransferDetail.class);
		if (stockMaterialTransfer != null)
		{
			stockMaterialTransfer.setDetailList(stockMaterialTransferDetailList);
		}
		return stockMaterialTransfer;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTransferService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialTransfer master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialTransferService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialTransfer stockMaterialTransfer = this.get(id);
		// 先判断是否已经审核
		if (stockMaterialTransfer.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialTransferDetail.getMaterialId());
			if (stockMaterialTransferDetail.getSpecifications() == null || "".equals(stockMaterialTransferDetail.getSpecifications()))
			{
				stockMaterialTransferDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialTransferDetail.getSpecifications());
			}
			query.eq("warehouseId", stockMaterialTransfer.getOutWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialTransferDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialTransferDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMT, id, BoolValue.YES))
			{
				return null;
			}
			for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
			{
				// 调出库存操作
				StockMaterial stockMaterialOut = new StockMaterial();
				stockMaterialOut.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				stockMaterialOut.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				stockMaterialOut.setQty(stockMaterialTransferDetail.getQty());
				stockMaterialOut.setValuationQty(stockMaterialTransferDetail.getValuationQty());
				stockMaterialOut.setWarehouseId(stockMaterialTransfer.getOutWarehouseId());
				stockMaterialOut.setPrice(stockMaterialTransferDetail.getPrice());
				stockMaterialOut.setValuationPrice(stockMaterialTransferDetail.getValuationPrice());
				stockMaterialOut.setMoney(stockMaterialTransferDetail.getMoney());
				stockMaterialOut.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				stockMaterialOut.setUpdateTime(new Date());
				stockMaterialOut.setWeight(stockMaterialTransferDetail.getWeight());
				serviceFactory.getMaterialStockService().stock(stockMaterialOut, InOutType.OUT);

				// 调入库存操作
				StockMaterial stockMaterialIn = new StockMaterial();
				stockMaterialIn.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				stockMaterialIn.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				stockMaterialIn.setQty(stockMaterialTransferDetail.getQty());
				stockMaterialIn.setValuationQty(stockMaterialTransferDetail.getValuationQty());
				stockMaterialIn.setWarehouseId(stockMaterialTransfer.getInWarehouseId());
				stockMaterialIn.setPrice(stockMaterialTransferDetail.getPrice());
				stockMaterialIn.setValuationPrice(stockMaterialTransferDetail.getValuationPrice());
				stockMaterialIn.setMoney(stockMaterialTransferDetail.getMoney());
				stockMaterialIn.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				stockMaterialIn.setUpdateTime(new Date());
				stockMaterialIn.setWeight(stockMaterialTransferDetail.getWeight());
				serviceFactory.getMaterialStockService().stock(stockMaterialIn, InOutType.IN);

				// 出库记录
				StockMaterialLog logOut = new StockMaterialLog();
				logOut.setBillId(id);
				logOut.setBillType(stockMaterialTransfer.getBillType());
				logOut.setBillNo(stockMaterialTransfer.getBillNo());
				logOut.setSourceId(stockMaterialTransferDetail.getId());
				logOut.setCreateTime(new Date());
				logOut.setCompanyId(UserUtils.getCompanyId());
				logOut.setCode(stockMaterialTransferDetail.getCode());
				logOut.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				logOut.setMaterialName(stockMaterialTransferDetail.getMaterialName());
				logOut.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				logOut.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				logOut.setWarehouseId(stockMaterialTransfer.getOutWarehouseId());
				logOut.setUnitId(stockMaterialTransferDetail.getStockUnitId());
				logOut.setPrice(stockMaterialTransferDetail.getPrice());
				logOut.setOutQty(stockMaterialTransferDetail.getQty());
				logOut.setOutMoney(stockMaterialTransferDetail.getMoney());
				logOut.setWeight(stockMaterialTransferDetail.getWeight());
				daoFactory.getCommonDao().saveEntity(logOut);

				// 入库记录
				StockMaterialLog logIn = new StockMaterialLog();
				logIn.setBillId(id);
				logIn.setBillType(stockMaterialTransfer.getBillType());
				logIn.setBillNo(stockMaterialTransfer.getBillNo());
				logIn.setSourceId(stockMaterialTransferDetail.getId());
				logIn.setCreateTime(new Date());
				logIn.setCompanyId(UserUtils.getCompanyId());
				logIn.setCode(stockMaterialTransferDetail.getCode());
				logIn.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				logIn.setMaterialName(stockMaterialTransferDetail.getMaterialName());
				logIn.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				logIn.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				logIn.setWarehouseId(stockMaterialTransfer.getInWarehouseId());
				logIn.setUnitId(stockMaterialTransferDetail.getStockUnitId());
				logIn.setPrice(stockMaterialTransferDetail.getPrice());
				logIn.setInQty(stockMaterialTransferDetail.getQty());
				logIn.setInMoney(stockMaterialTransferDetail.getMoney());
				logIn.setWeight(stockMaterialTransferDetail.getWeight());
				daoFactory.getCommonDao().saveEntity(logIn);
			}

		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTransferService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialTransfer stockMaterialTransfer = this.get(id);
		// 先判断是否已经反审核
		if (stockMaterialTransfer.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialTransferDetail.getMaterialId());
			if (stockMaterialTransferDetail.getSpecifications() == null || "".equals(stockMaterialTransferDetail.getSpecifications()))
			{
				stockMaterialTransferDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialTransferDetail.getSpecifications());
			}
			query.eq("warehouseId", stockMaterialTransfer.getInWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialTransferDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialTransferDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMT, id, BoolValue.NO))
			{
				return null;
			}
			for (StockMaterialTransferDetail stockMaterialTransferDetail : stockMaterialTransfer.getDetailList())
			{
				// 调出库存操作
				StockMaterial stockMaterialOut = new StockMaterial();
				stockMaterialOut.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				stockMaterialOut.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				stockMaterialOut.setQty(stockMaterialTransferDetail.getQty());
				stockMaterialOut.setValuationQty(stockMaterialTransferDetail.getValuationQty());
				stockMaterialOut.setWarehouseId(stockMaterialTransfer.getOutWarehouseId());
				stockMaterialOut.setPrice(stockMaterialTransferDetail.getPrice());
				stockMaterialOut.setValuationPrice(stockMaterialTransferDetail.getValuationPrice());
				stockMaterialOut.setMoney(stockMaterialTransferDetail.getMoney());
				stockMaterialOut.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				stockMaterialOut.setUpdateTime(new Date());
				serviceFactory.getMaterialStockService().backStock(stockMaterialOut, InOutType.OUT);

				// 调入库存操作
				StockMaterial stockMaterialIn = new StockMaterial();
				stockMaterialIn.setMaterialId(stockMaterialTransferDetail.getMaterialId());
				stockMaterialIn.setSpecifications(stockMaterialTransferDetail.getSpecifications());
				stockMaterialIn.setQty(stockMaterialTransferDetail.getQty());
				stockMaterialIn.setValuationQty(stockMaterialTransferDetail.getValuationQty());
				stockMaterialIn.setWarehouseId(stockMaterialTransfer.getInWarehouseId());
				stockMaterialIn.setPrice(stockMaterialTransferDetail.getPrice());
				stockMaterialIn.setValuationPrice(stockMaterialTransferDetail.getValuationPrice());
				stockMaterialIn.setMoney(stockMaterialTransferDetail.getMoney());
				stockMaterialIn.setMaterialClassId(stockMaterialTransferDetail.getMaterialClassId());
				stockMaterialIn.setUpdateTime(new Date());
				serviceFactory.getMaterialStockService().backStock(stockMaterialIn, InOutType.IN);

				DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
				query.eq("billId", id);
				List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}

		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialTransferService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialTransfer lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialTransfer.class);
		query.eq("id", id);
		StockMaterialTransfer order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialTransfer.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialTransferDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialTransferDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialTransferDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
