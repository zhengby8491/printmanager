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
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialSupplementService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产补料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialSupplementServiceImpl extends BaseServiceImpl implements StockMaterialSupplementService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#findByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialSupplement> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSupplement.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("supplementTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("supplementTime", queryParam.getDateMax());
		}
		if (null != queryParam.getSendEmployeeId())
		{
			query.eq("sendEmployeeId", queryParam.getSendEmployeeId());
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("warehouseId", queryParam.getWarehouseId());
		}
		if (null != queryParam.getReceiveEmployeeId())
		{
			query.eq("receiveEmployeeId", queryParam.getReceiveEmployeeId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialSupplement.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialSupplementService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialSupplementDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSupplementDetail.class, "ad");
		query.createAlias(StockMaterialSupplement.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.supplementTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.supplementTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getMaterialName()))
		{
			query.like("ad.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.eq("ad.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("a.warehouseId", queryParam.getWarehouseId());
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("ad.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("ad.productName", "%" + queryParam.getProductName() + "%");
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
		SearchResult<StockMaterialSupplementDetail> result = new SearchResult<StockMaterialSupplementDetail>();
		result.setResult(new ArrayList<StockMaterialSupplementDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialSupplementDetail detail = (StockMaterialSupplementDetail) c[0];
			detail.setMaster((StockMaterialSupplement) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialSupplementService#save(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialSupplement)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialSupplement> save(StockMaterialSupplement stockMaterialSupplement)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialSupplement.getIsCheck();
		stockMaterialSupplement.setBillType(BillType.STOCK_SM);
		stockMaterialSupplement.setIsCheck(BoolValue.NO);
		if (stockMaterialSupplement.getDetailList().size() > 0)
		{
			stockMaterialSupplement.setBillNo(UserUtils.createBillNo(BillType.STOCK_SM));
			stockMaterialSupplement.setCompanyId(UserUtils.getCompanyId());
			stockMaterialSupplement.setCreateTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				stockMaterialSupplement.setCreateName(e.getName());
			}
			else
			{
				stockMaterialSupplement.setCreateName(UserUtils.getUserName());
			}
			stockMaterialSupplement.setCreateEmployeeId(UserUtils.getEmployeeId());
			if (stockMaterialSupplement.getSendEmployeeId() != null)
			{
				stockMaterialSupplement.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialSupplement.getSendEmployeeId())).getName());
			}
			if (stockMaterialSupplement.getReceiveEmployeeId() != null)
			{
				stockMaterialSupplement.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialSupplement.getReceiveEmployeeId())).getName());
			}

			daoFactory.getCommonDao().saveEntity(stockMaterialSupplement);
		}
		for (StockMaterialSupplementDetail stockMaterialSupplementDetail : stockMaterialSupplement.getDetailList())
		{
			if ("".equals(stockMaterialSupplementDetail.getCode()) || stockMaterialSupplementDetail.getCode() == null)
			{
				continue;
			}
			stockMaterialSupplementDetail.setMasterId(stockMaterialSupplement.getId());
			stockMaterialSupplementDetail.setCompanyId(UserUtils.getCompanyId());
			daoFactory.getCommonDao().saveEntity(stockMaterialSupplementDetail);
		}

		ServiceResult<StockMaterialSupplement> serviceResult = new ServiceResult<StockMaterialSupplement>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialSupplement.getId(), stockMaterialSupplement.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialSupplement);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialSupplementService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialSupplement)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialSupplement> update(StockMaterialSupplement stockMaterialSupplement)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialSupplement.getIsCheck();
		stockMaterialSupplement.setIsCheck(BoolValue.NO);

		ServiceResult<StockMaterialSupplement> serviceResult = new ServiceResult<StockMaterialSupplement>();

		// 再更新数据
		StockMaterialSupplement stockMaterialSupplement_ = this.lockHasChildren(stockMaterialSupplement.getId());
		// 判断是否已审核
		if (stockMaterialSupplement_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		stockMaterialSupplement_.setIsCheck(BoolValue.NO);
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialSupplementDetail newItem : stockMaterialSupplement.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialSupplementDetail oldItem : stockMaterialSupplement_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialSupplementDetail.class, id);
			}
		}

		stockMaterialSupplement_.setSupplementTime(stockMaterialSupplement.getSupplementTime());
		stockMaterialSupplement_.setMemo(stockMaterialSupplement.getMemo());
		stockMaterialSupplement_.setWorkBillNo(stockMaterialSupplement.getWorkBillNo());
		stockMaterialSupplement_.setWorkBillType(stockMaterialSupplement.getWorkBillType());
		stockMaterialSupplement_.setWorkId(stockMaterialSupplement.getWorkId());
		if (stockMaterialSupplement.getSendEmployeeId() != null)
		{
			stockMaterialSupplement_.setSendEmployeeId(stockMaterialSupplement.getSendEmployeeId());
			stockMaterialSupplement_.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialSupplement.getSendEmployeeId())).getName());
		}
		if (stockMaterialSupplement.getReceiveEmployeeId() != null)
		{
			stockMaterialSupplement_.setReceiveEmployeeId(stockMaterialSupplement.getReceiveEmployeeId());

			stockMaterialSupplement_.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialSupplement.getReceiveEmployeeId())).getName());
		}

		stockMaterialSupplement_.setWarehouseId(stockMaterialSupplement.getWarehouseId());
		daoFactory.getCommonDao().updateEntity(stockMaterialSupplement_);
		for (StockMaterialSupplementDetail stockMaterialSupplementDetail : stockMaterialSupplement.getDetailList())
		{
			if ("".equals(stockMaterialSupplementDetail.getCode()) || stockMaterialSupplementDetail.getCode() == null)
			{
				continue;
			}
			if (stockMaterialSupplementDetail.getId() != null)
			{
				StockMaterialSupplementDetail stockMaterialSupplementDetail_ = this.getDetail(stockMaterialSupplementDetail.getId());
				stockMaterialSupplementDetail_.setCode(stockMaterialSupplementDetail.getCode());
				stockMaterialSupplementDetail_.setMaterialName(stockMaterialSupplementDetail.getMaterialName());
				stockMaterialSupplementDetail_.setSpecifications(stockMaterialSupplementDetail.getSpecifications());
				stockMaterialSupplementDetail_.setStockUnitId(stockMaterialSupplementDetail.getStockUnitId());
				stockMaterialSupplementDetail_.setQty(stockMaterialSupplementDetail.getQty());
				stockMaterialSupplementDetail_.setValuationQty(stockMaterialSupplementDetail.getValuationQty());
				stockMaterialSupplementDetail_.setPrice(stockMaterialSupplementDetail.getPrice());
				stockMaterialSupplementDetail_.setWeight(stockMaterialSupplementDetail.getWeight());
				stockMaterialSupplementDetail_.setMoney(stockMaterialSupplementDetail.getMoney());
				stockMaterialSupplementDetail_.setMaterialClassId(stockMaterialSupplementDetail.getMaterialClassId());
				stockMaterialSupplementDetail_.setMemo(stockMaterialSupplementDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockMaterialSupplementDetail_);
			}
			else
			{
				stockMaterialSupplementDetail.setMasterId(stockMaterialSupplement.getId());
				stockMaterialSupplementDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialSupplementDetail);
			}

		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this._check(stockMaterialSupplement, stockMaterialSupplement.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialSupplement);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialSupplement get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSupplement.class);
		query.eq("id", id);
		StockMaterialSupplement stockMaterialSupplement = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialSupplement.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialSupplementDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialSupplementDetail> stockMaterialSupplementDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialSupplementDetail.class);
		if (stockMaterialSupplement != null)
		{
			stockMaterialSupplement.setDetailList(stockMaterialSupplementDetailList);
		}
		return stockMaterialSupplement;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#getDetail(java.lang.Long)
	 */
	public StockMaterialSupplementDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSupplementDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialSupplementDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialSupplement stockMaterialSupplement = this.lockHasChildren(id);
			daoFactory.getCommonDao().deleteAllEntity(stockMaterialSupplement.getDetailList());
			daoFactory.getCommonDao().deleteEntity(stockMaterialSupplement);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		return _check(this.get(id), forceCheck);
	}

	/**
	 * <pre>
	 * 审核、反审核
	 * </pre>
	 * @param master
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:14:02, think
	 */
	public List<StockMaterial> _check(StockMaterialSupplement master, BoolValue forceCheck)
	{
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES) 
		{
			throw new BusinessException("已审核");
		}
		
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		for (StockMaterialSupplementDetail stockMaterialSupplementDetail : master.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialSupplementDetail.getMaterialId());
			if (stockMaterialSupplementDetail.getSpecifications() == null || "".equals(stockMaterialSupplementDetail.getSpecifications()))
			{
				stockMaterialSupplementDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialSupplementDetail.getSpecifications());
			}
			query.eq("warehouseId", master.getWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialSupplementDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialSupplementDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SM, master.getId(), BoolValue.YES))
			{
				return null;
			}
			// 库存操作
			for (StockMaterialSupplementDetail stockMaterialSupplementDetail : master.getDetailList())
			{
				StockMaterial stockMaterial = new StockMaterial();
				stockMaterial.setMaterialId(stockMaterialSupplementDetail.getMaterialId());
				stockMaterial.setSpecifications(stockMaterialSupplementDetail.getSpecifications());
				stockMaterial.setQty(stockMaterialSupplementDetail.getQty());
				stockMaterial.setValuationQty(stockMaterialSupplementDetail.getValuationQty());
				stockMaterial.setWarehouseId(master.getWarehouseId());
				stockMaterial.setPrice(stockMaterialSupplementDetail.getPrice());
				stockMaterial.setMoney(stockMaterialSupplementDetail.getMoney());
				stockMaterial.setMaterialClassId(stockMaterialSupplementDetail.getMaterialClassId());
				serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.OUT);

				// 出库记录
				StockMaterialLog log = new StockMaterialLog();
				log.setBillId(master.getId());
				log.setBillType(master.getBillType());
				log.setBillNo(master.getBillNo());
				log.setSourceId(stockMaterialSupplementDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCode(stockMaterialSupplementDetail.getCode());
				log.setMaterialClassId(stockMaterialSupplementDetail.getMaterialClassId());
				log.setMaterialName(stockMaterialSupplementDetail.getMaterialName());
				log.setMaterialId(stockMaterialSupplementDetail.getMaterialId());
				log.setSpecifications(stockMaterialSupplementDetail.getSpecifications());
				log.setWarehouseId(master.getWarehouseId());
				log.setUnitId(stockMaterialSupplementDetail.getStockUnitId());
				log.setWeight(stockMaterialSupplementDetail.getWeight());
				log.setPrice(stockMaterialSupplementDetail.getPrice());
				log.setOutQty(stockMaterialSupplementDetail.getQty());
				log.setOutMoney(stockMaterialSupplementDetail.getMoney());
				daoFactory.getCommonDao().saveEntity(log);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SM, id, BoolValue.NO))
		{
			return 0;
		}
		StockMaterialSupplement master = this.get(id);
		for (StockMaterialSupplementDetail stockMaterialSupplementDetail : master.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(stockMaterialSupplementDetail.getMaterialId());
			stockMaterial.setSpecifications(stockMaterialSupplementDetail.getSpecifications());
			stockMaterial.setQty(stockMaterialSupplementDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialSupplementDetail.getValuationQty());
			stockMaterial.setWarehouseId(master.getWarehouseId());
			stockMaterial.setPrice(stockMaterialSupplementDetail.getPrice());
			stockMaterial.setMaterialClassId(stockMaterialSupplementDetail.getMaterialClassId());
			stockMaterial.setMoney(stockMaterialSupplementDetail.getMoney());
			serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.OUT);

			DynamicQuery query = new CompanyDynamicQuery(StockMaterialLog.class);
			query.eq("billId", id);
			List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
			daoFactory.getCommonDao().deleteAllEntity(logs);
		}

		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialSupplementService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialSupplement lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialSupplement.class);
		query.eq("id", id);
		StockMaterialSupplement order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialSupplement.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialSupplementDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialSupplementDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialSupplementDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
