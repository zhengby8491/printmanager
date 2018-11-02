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
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialOtherInService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料其他入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialOtherInServiceImpl extends BaseServiceImpl implements StockMaterialOtherInService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherInService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialOtherIn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherIn.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("inTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("inTime", queryParam.getDateMax());
		}
		if (null != queryParam.getEmployeeId())
		{
			query.eq("employeeId", queryParam.getEmployeeId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialOtherIn.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherInService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialOtherInDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherInDetail.class, "ad");
		query.createAlias(StockMaterialOtherIn.class, "a");

		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.inTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.inTime", queryParam.getDateMax());
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
		SearchResult<StockMaterialOtherInDetail> result = new SearchResult<StockMaterialOtherInDetail>();
		result.setResult(new ArrayList<StockMaterialOtherInDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialOtherInDetail detail = (StockMaterialOtherInDetail) c[0];
			detail.setMaster((StockMaterialOtherIn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherInService#save(com.huayin.printmanager.persist.entity.stock
	 * .StockMaterialOtherIn)
	 */
	@Override
	@Transactional
	public Long save(StockMaterialOtherIn stockMaterialOtherIn)
	{
		BoolValue flag = stockMaterialOtherIn.getIsCheck();			// 标识是否保存并审核
		stockMaterialOtherIn.setIsCheck(BoolValue.NO);					// 默认未审核
		stockMaterialOtherIn.setBillType(BillType.STOCK_SMOI);

		stockMaterialOtherIn.setBillNo(UserUtils.createBillNo(BillType.STOCK_SMOI));
		stockMaterialOtherIn.setCompanyId(UserUtils.getCompanyId());
		stockMaterialOtherIn.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockMaterialOtherIn.setCreateName(e.getName());
		}
		else
		{
			stockMaterialOtherIn.setCreateName(UserUtils.getUserName());
		}
		stockMaterialOtherIn.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockMaterialOtherIn.getEmployeeId() != null)
		{
			stockMaterialOtherIn.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherIn.getEmployeeId())).getName());
		}

		daoFactory.getCommonDao().saveEntity(stockMaterialOtherIn);

		for (StockMaterialOtherInDetail stockMaterialOtherInDetail : stockMaterialOtherIn.getDetailList())
		{
			if ("".equals(stockMaterialOtherInDetail.getCode()) || stockMaterialOtherInDetail.getCode() == null)
			{
				continue;
			}
			stockMaterialOtherInDetail.setMasterId(stockMaterialOtherIn.getId());
			stockMaterialOtherInDetail.setCompanyId(UserUtils.getCompanyId());
			daoFactory.getCommonDao().saveEntity(stockMaterialOtherInDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockMaterialOtherIn.getId());
		}

		return stockMaterialOtherIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialOtherInService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialOtherIn)
	 */
	@Override
	@Transactional
	public Long update(StockMaterialOtherIn stockMaterialOtherIn)
	{
		StockMaterialOtherIn stockMaterialOtherIn_ = this.lockHasChildren(stockMaterialOtherIn.getId());
		// 判断是否已审核
		if (stockMaterialOtherIn_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialOtherInDetail newItem : stockMaterialOtherIn.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialOtherInDetail oldItem : stockMaterialOtherIn_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialOtherInDetail.class, id);
			}
		}

		stockMaterialOtherIn_.setWarehouseId(stockMaterialOtherIn.getWarehouseId());
		stockMaterialOtherIn_.setInTime(stockMaterialOtherIn.getInTime());
		stockMaterialOtherIn_.setMemo(stockMaterialOtherIn.getMemo());
		if (stockMaterialOtherIn.getEmployeeId() != null)
		{
			stockMaterialOtherIn_.setEmployeeId(stockMaterialOtherIn.getEmployeeId());
			stockMaterialOtherIn_.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialOtherIn.getEmployeeId())).getName());
		}

		daoFactory.getCommonDao().updateEntity(stockMaterialOtherIn_);

		for (StockMaterialOtherInDetail stockMaterialOtherInDetail : stockMaterialOtherIn.getDetailList())
		{
			if (stockMaterialOtherInDetail.getId() != null)
			{

				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialOtherInDetail.class);
				querySt.eq("id", stockMaterialOtherInDetail.getId());
				StockMaterialOtherInDetail stockMaterialOtherInDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialOtherInDetail.class);
				stockMaterialOtherInDetail_.setCode(stockMaterialOtherInDetail.getCode());
				stockMaterialOtherInDetail_.setMaterialName(stockMaterialOtherInDetail.getMaterialName());
				stockMaterialOtherInDetail_.setSpecifications(stockMaterialOtherInDetail.getSpecifications());
				stockMaterialOtherInDetail_.setStockUnitId(stockMaterialOtherInDetail.getStockUnitId());
				stockMaterialOtherInDetail_.setQty(stockMaterialOtherInDetail.getQty());
				stockMaterialOtherInDetail_.setPrice(stockMaterialOtherInDetail.getPrice());
				stockMaterialOtherInDetail_.setValuationUnitId(stockMaterialOtherInDetail.getValuationUnitId());
				stockMaterialOtherInDetail_.setValuationQty(stockMaterialOtherInDetail.getValuationQty());
				stockMaterialOtherInDetail_.setValuationPrice(stockMaterialOtherInDetail.getValuationPrice());
				stockMaterialOtherInDetail_.setMoney(stockMaterialOtherInDetail.getMoney());
				stockMaterialOtherInDetail_.setMaterialClassId(stockMaterialOtherInDetail.getMaterialClassId());
				stockMaterialOtherInDetail_.setMemo(stockMaterialOtherInDetail.getMemo());
				stockMaterialOtherInDetail_.setMaster(stockMaterialOtherInDetail.getMaster());
				daoFactory.getCommonDao().updateEntity(stockMaterialOtherInDetail_);
			}
			else
			{
				stockMaterialOtherInDetail.setMasterId(stockMaterialOtherIn.getId());
				stockMaterialOtherInDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialOtherInDetail);
			}

		}
		if (stockMaterialOtherIn.getIsCheck() == BoolValue.YES)
		{
			this.check(stockMaterialOtherIn.getId());
		}
		return stockMaterialOtherIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherInService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialOtherIn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherIn.class);
		query.eq("id", id);
		StockMaterialOtherIn stockMaterialOtherIn = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialOtherIn.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialOtherInDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialOtherInDetail> stockMaterialOtherInDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialOtherInDetail.class);
		if (stockMaterialOtherIn != null)
		{
			stockMaterialOtherIn.setDetailList(stockMaterialOtherInDetailList);
		}
		return stockMaterialOtherIn;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherInService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockMaterialOtherIn master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherInService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMOI, id, BoolValue.YES))
		{
			return 0;
		}
		StockMaterialOtherIn stockMaterialOtherIn = this.get(id);

		for (StockMaterialOtherInDetail stockMaterialOtherInDetail : stockMaterialOtherIn.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(stockMaterialOtherInDetail.getMaterialId());
			stockMaterial.setSpecifications(stockMaterialOtherInDetail.getSpecifications());
			stockMaterial.setQty(stockMaterialOtherInDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialOtherInDetail.getValuationQty());
			stockMaterial.setWarehouseId(stockMaterialOtherIn.getWarehouseId());
			stockMaterial.setPrice(stockMaterialOtherInDetail.getPrice());
			stockMaterial.setValuationPrice(stockMaterialOtherInDetail.getValuationPrice());
			stockMaterial.setMoney(stockMaterialOtherInDetail.getMoney());
			stockMaterial.setMaterialClassId(stockMaterialOtherInDetail.getMaterialClassId());
			stockMaterial.setUpdateTime(new Date());
			serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.IN);

			// 入库记录
			StockMaterialLog log = new StockMaterialLog();
			log.setBillId(stockMaterialOtherIn.getId());
			log.setBillType(stockMaterialOtherIn.getBillType());
			log.setBillNo(stockMaterialOtherIn.getBillNo());
			log.setSourceId(stockMaterialOtherInDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(stockMaterialOtherInDetail.getCode());
			log.setMaterialClassId(stockMaterialOtherInDetail.getMaterialClassId());
			log.setMaterialName(stockMaterialOtherInDetail.getMaterialName());
			log.setMaterialId(stockMaterialOtherInDetail.getMaterialId());
			log.setSpecifications(stockMaterialOtherInDetail.getSpecifications());
			log.setWarehouseId(stockMaterialOtherIn.getWarehouseId());
			log.setUnitId(stockMaterialOtherInDetail.getStockUnitId());
			log.setPrice(stockMaterialOtherInDetail.getPrice());
			log.setInQty(stockMaterialOtherInDetail.getQty());
			log.setWeight(stockMaterialOtherInDetail.getWeight());
			log.setInMoney(stockMaterialOtherInDetail.getMoney());
			daoFactory.getCommonDao().saveEntity(log);
		}

		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherInService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialOtherIn stockMaterialOtherIn = this.get(id);
		// 先判断是否已经反审核
		if (stockMaterialOtherIn.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (StockMaterialOtherInDetail stockMaterialOtherInDetail : stockMaterialOtherIn.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialOtherInDetail.getMaterialId());
			if (stockMaterialOtherInDetail.getSpecifications() == null || "".equals(stockMaterialOtherInDetail.getSpecifications()))
			{
				stockMaterialOtherInDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialOtherInDetail.getSpecifications());
			}
			query.eq("warehouseId", stockMaterialOtherIn.getWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialOtherInDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}
			if (stockMaterialOtherInDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SMOI, id, BoolValue.NO))
			{
				return null;
			}
			for (StockMaterialOtherInDetail stockMaterialOtherInDetail : stockMaterialOtherIn.getDetailList())
			{
				// 库存操作
				StockMaterial stockMaterial = new StockMaterial();
				stockMaterial.setMaterialId(stockMaterialOtherInDetail.getMaterialId());
				stockMaterial.setSpecifications(stockMaterialOtherInDetail.getSpecifications());
				stockMaterial.setQty(stockMaterialOtherInDetail.getQty());
				stockMaterial.setValuationQty(stockMaterialOtherInDetail.getValuationQty());
				stockMaterial.setWarehouseId(stockMaterialOtherIn.getWarehouseId());
				stockMaterial.setPrice(stockMaterialOtherInDetail.getPrice());
				stockMaterial.setValuationPrice(stockMaterialOtherInDetail.getValuationPrice());
				stockMaterial.setMaterialClassId(stockMaterialOtherInDetail.getMaterialClassId());
				stockMaterial.setMoney(stockMaterialOtherInDetail.getMoney());
				stockMaterial.setUpdateTime(new Date());
				serviceFactory.getMaterialStockService().backStock(stockMaterial, InOutType.IN);

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
	 * @see com.huayin.printmanager.service.stock.StockMaterialOtherInService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialOtherIn lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialOtherIn.class);
		query.eq("id", id);
		StockMaterialOtherIn order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialOtherIn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialOtherInDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialOtherInDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialOtherInDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
