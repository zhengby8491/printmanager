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
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialOtherOutService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料其他出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialOtherOutServiceImpl extends BaseServiceImpl implements StockMaterialOtherOutService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherOutService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialOtherOut> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherOut.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("outTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("outTime", queryParam.getDateMax());
		}
		if (null != queryParam.getSendEmployeeId())
		{
			query.eq("sendEmployeeId", queryParam.getSendEmployeeId());
		}
		if (null != queryParam.getReceiveEmployeeId())
		{
			query.eq("receiveEmployeeId", queryParam.getReceiveEmployeeId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialOtherOut.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherOutService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialOtherOutDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherOutDetail.class, "ad");
		query.createAlias(StockMaterialOtherOut.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.outTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.outTime", queryParam.getDateMax());
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
		SearchResult<StockMaterialOtherOutDetail> result = new SearchResult<StockMaterialOtherOutDetail>();
		result.setResult(new ArrayList<StockMaterialOtherOutDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialOtherOutDetail detail = (StockMaterialOtherOutDetail) c[0];
			detail.setMaster((StockMaterialOtherOut) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherOutService#save(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialOtherOut)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialOtherOut> save(StockMaterialOtherOut stockMaterialOtherOut)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialOtherOut.getIsCheck();
		stockMaterialOtherOut.setIsCheck(BoolValue.NO);
		stockMaterialOtherOut.setBillType(BillType.STOCK_SMOO);
		stockMaterialOtherOut.setBillNo(UserUtils.createBillNo(BillType.STOCK_SMOO));
		stockMaterialOtherOut.setCompanyId(UserUtils.getCompanyId());
		stockMaterialOtherOut.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialOtherOut.setCreateName(e.getName());
		}
		else
		{
			stockMaterialOtherOut.setCreateName(UserUtils.getUserName());
		}
		stockMaterialOtherOut.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockMaterialOtherOut.getSendEmployeeId() != null)
		{
			stockMaterialOtherOut.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherOut.getSendEmployeeId())).getName());
		}
		if (stockMaterialOtherOut.getReceiveEmployeeId() != null)
		{
			stockMaterialOtherOut.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherOut.getReceiveEmployeeId())).getName());
		}
		daoFactory.getCommonDao().saveEntity(stockMaterialOtherOut);
		for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : stockMaterialOtherOut.getDetailList())
		{
			if ("".equals(stockMaterialOtherOutDetail.getCode()) || stockMaterialOtherOutDetail.getCode() == null)
			{
				continue;
			}
			stockMaterialOtherOutDetail.setMasterId(stockMaterialOtherOut.getId());
			stockMaterialOtherOutDetail.setCompanyId(UserUtils.getCompanyId());
			daoFactory.getCommonDao().saveEntity(stockMaterialOtherOutDetail);
		}

		ServiceResult<StockMaterialOtherOut> serviceResult = new ServiceResult<StockMaterialOtherOut>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialOtherOut.getId(), stockMaterialOtherOut.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialOtherOut);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherOutService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialOtherOut)
	 */
	@Override
	@Transactional
	public ServiceResult<StockMaterialOtherOut> update(StockMaterialOtherOut stockMaterialOtherOut)
	{
		// 是否要审核标识
		BoolValue flag = stockMaterialOtherOut.getIsCheck();

		ServiceResult<StockMaterialOtherOut> serviceResult = new ServiceResult<StockMaterialOtherOut>();

		// 再更新数据
		StockMaterialOtherOut stockMaterialOtherOut_ = this.lockHasChildren(stockMaterialOtherOut.getId());
		// 先判断是否已经审核
		if (stockMaterialOtherOut_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		stockMaterialOtherOut_.setIsCheck(BoolValue.NO);
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialOtherOutDetail newItem : stockMaterialOtherOut.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialOtherOutDetail oldItem : stockMaterialOtherOut_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialOtherOutDetail.class, id);
			}
		}

		stockMaterialOtherOut_.setOutTime(stockMaterialOtherOut.getOutTime());
		stockMaterialOtherOut_.setMemo(stockMaterialOtherOut.getMemo());
		if (stockMaterialOtherOut.getSendEmployeeId() != null)
		{
			stockMaterialOtherOut_.setSendEmployeeId(stockMaterialOtherOut.getSendEmployeeId());
			stockMaterialOtherOut_.setSendEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherOut.getSendEmployeeId())).getName());
		}
		if (stockMaterialOtherOut.getReceiveEmployeeId() != null)
		{
			stockMaterialOtherOut_.setReceiveEmployeeId(stockMaterialOtherOut.getReceiveEmployeeId());
			stockMaterialOtherOut_.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherOut.getReceiveEmployeeId())).getName());
		}
		stockMaterialOtherOut_.setWarehouseId(stockMaterialOtherOut.getWarehouseId());
		daoFactory.getCommonDao().updateEntity(stockMaterialOtherOut_);
		for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : stockMaterialOtherOut.getDetailList())
		{
			if ("".equals(stockMaterialOtherOutDetail.getCode()) || stockMaterialOtherOutDetail.getCode() == null)
			{
				continue;
			}
			if (stockMaterialOtherOutDetail.getId() != null)
			{
				StockMaterialOtherOutDetail stockMaterialOtherOutDetail_ = this.getDetail(stockMaterialOtherOutDetail.getId());
				stockMaterialOtherOutDetail_.setCode(stockMaterialOtherOutDetail.getCode());
				stockMaterialOtherOutDetail_.setMaterialName(stockMaterialOtherOutDetail.getMaterialName());
				stockMaterialOtherOutDetail_.setSpecifications(stockMaterialOtherOutDetail.getSpecifications());
				stockMaterialOtherOutDetail_.setStockUnitId(stockMaterialOtherOutDetail.getStockUnitId());
				stockMaterialOtherOutDetail_.setQty(stockMaterialOtherOutDetail.getQty());
				stockMaterialOtherOutDetail_.setPrice(stockMaterialOtherOutDetail.getPrice());
				stockMaterialOtherOutDetail_.setWeight(stockMaterialOtherOutDetail.getWeight());
				stockMaterialOtherOutDetail_.setValuationUnitId(stockMaterialOtherOutDetail.getValuationUnitId());
				stockMaterialOtherOutDetail_.setValuationQty(stockMaterialOtherOutDetail.getValuationQty());
				stockMaterialOtherOutDetail_.setValuationPrice(stockMaterialOtherOutDetail.getValuationPrice());
				stockMaterialOtherOutDetail_.setMoney(stockMaterialOtherOutDetail.getMoney());
				stockMaterialOtherOutDetail_.setMaterialClassId(stockMaterialOtherOutDetail.getMaterialClassId());
				stockMaterialOtherOutDetail_.setMemo(stockMaterialOtherOutDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockMaterialOtherOutDetail_);
			}
			else
			{
				stockMaterialOtherOutDetail.setMasterId(stockMaterialOtherOut.getId());
				stockMaterialOtherOutDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialOtherOutDetail);
			}

		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockMaterialOtherOut.getId(), stockMaterialOtherOut.getForceCheck()));
		}
		serviceResult.setReturnValue(stockMaterialOtherOut);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialOtherOut get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherOut.class);
		query.eq("id", id);
		StockMaterialOtherOut stockMaterialOtherOut = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialOtherOut.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialOtherOutDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialOtherOutDetail> stockMaterialOtherOutDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialOtherOutDetail.class);
		if (stockMaterialOtherOut != null)
		{
			stockMaterialOtherOut.setDetailList(stockMaterialOtherOutDetailList);
		}
		return stockMaterialOtherOut;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#getDetail(java.lang.Long)
	 */
	public StockMaterialOtherOutDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherOutDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialOtherOutDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialOtherOut master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> check(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialOtherOut master = this.get(id);
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : master.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialOtherOutDetail.getMaterialId());
			if (stockMaterialOtherOutDetail.getSpecifications() == null || "".equals(stockMaterialOtherOutDetail.getSpecifications()))
			{
				stockMaterialOtherOutDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialOtherOutDetail.getSpecifications());
			}
			query.eq("warehouseId", master.getWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialOtherOutDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialOtherOutDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMOO, id, BoolValue.YES))
			{
				return null;
			}
			// 库存操作
			for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : master.getDetailList())
			{
				StockMaterial stockMaterial = new StockMaterial();
				stockMaterial.setMaterialId(stockMaterialOtherOutDetail.getMaterialId());
				stockMaterial.setSpecifications(stockMaterialOtherOutDetail.getSpecifications());
				stockMaterial.setQty(stockMaterialOtherOutDetail.getQty());
				stockMaterial.setValuationQty(stockMaterialOtherOutDetail.getValuationQty());
				stockMaterial.setWarehouseId(master.getWarehouseId());
				stockMaterial.setPrice(stockMaterialOtherOutDetail.getPrice());
				stockMaterial.setMoney(stockMaterialOtherOutDetail.getMoney());
				stockMaterial.setMaterialClassId(stockMaterialOtherOutDetail.getMaterialClassId());
				serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.OUT);

				// 出库记录
				StockMaterialLog logOut = new StockMaterialLog();
				logOut.setBillId(id);
				logOut.setBillType(master.getBillType());
				logOut.setBillNo(master.getBillNo());
				logOut.setSourceId(stockMaterialOtherOutDetail.getId());
				logOut.setCreateTime(new Date());
				logOut.setCompanyId(UserUtils.getCompanyId());
				logOut.setCode(stockMaterialOtherOutDetail.getCode());
				logOut.setMaterialClassId(stockMaterialOtherOutDetail.getMaterialClassId());
				logOut.setMaterialName(stockMaterialOtherOutDetail.getMaterialName());
				logOut.setMaterialId(stockMaterialOtherOutDetail.getMaterialId());
				logOut.setSpecifications(stockMaterialOtherOutDetail.getSpecifications());
				logOut.setWarehouseId(master.getWarehouseId());
				logOut.setWeight(stockMaterialOtherOutDetail.getWeight());
				logOut.setUnitId(stockMaterialOtherOutDetail.getStockUnitId());
				logOut.setPrice(stockMaterialOtherOutDetail.getPrice());
				logOut.setOutQty(stockMaterialOtherOutDetail.getQty());
				logOut.setOutMoney(stockMaterialOtherOutDetail.getMoney());
				daoFactory.getCommonDao().saveEntity(logOut);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMOO, id, BoolValue.NO))
		{
			return 0;
		}
		StockMaterialOtherOut master = this.get(id);
		for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : master.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(stockMaterialOtherOutDetail.getMaterialId());
			stockMaterial.setSpecifications(stockMaterialOtherOutDetail.getSpecifications());
			stockMaterial.setQty(stockMaterialOtherOutDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialOtherOutDetail.getValuationQty());
			stockMaterial.setWarehouseId(master.getWarehouseId());
			stockMaterial.setPrice(stockMaterialOtherOutDetail.getPrice());
			stockMaterial.setMaterialClassId(stockMaterialOtherOutDetail.getMaterialClassId());
			stockMaterial.setMoney(stockMaterialOtherOutDetail.getMoney());
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherOutService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialOtherOut lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherOut.class);
		query.eq("id", id);
		StockMaterialOtherOut order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialOtherOut.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialOtherOutDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialOtherOutDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialOtherOutDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
