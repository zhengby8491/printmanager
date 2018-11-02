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
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockMaterialReturnService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产退料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockMaterialReturnServiceImpl extends BaseServiceImpl implements StockMaterialReturnService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialReturnService#findByCondition(com.huayin.printmanager.service.vo
	 * .QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialReturn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialReturn.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("returnTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("returnTime", queryParam.getDateMax());
		}
		if (null != queryParam.getSendEmployeeId())
		{
			query.eq("returnEmployeeId", queryParam.getSendEmployeeId());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockMaterialReturn.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialReturnService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockMaterialReturnDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialReturnDetail.class, "ad");
		query.createAlias(StockMaterialReturn.class, "a");
		query.addProjection(Projections.property("ad, a"));
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.returnTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.returnTime", queryParam.getDateMax());
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
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
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
		SearchResult<StockMaterialReturnDetail> result = new SearchResult<StockMaterialReturnDetail>();
		result.setResult(new ArrayList<StockMaterialReturnDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockMaterialReturnDetail detail = (StockMaterialReturnDetail) c[0];
			detail.setMaster((StockMaterialReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#get(java.lang.Long)
	 */
	@Override
	public StockMaterialReturn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialReturn.class);
		query.eq("id", id);
		StockMaterialReturn stockMaterialReturn = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialReturn.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockMaterialReturnDetail.class);
		queryD.eq("masterId", id);
		List<StockMaterialReturnDetail> stockMaterialReturnDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD, StockMaterialReturnDetail.class);
		if (stockMaterialReturn != null)
		{
			stockMaterialReturn.setDetailList(stockMaterialReturnDetailList);
		}
		return stockMaterialReturn;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#getDetail(java.lang.Long)
	 */
	@Override
	public StockMaterialReturnDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialReturnDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterialReturnDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialReturnService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockMaterialReturn)
	 */
	@Override
	@Transactional
	public Long save(StockMaterialReturn stockMaterialReturn)
	{
		BoolValue flag = stockMaterialReturn.getIsCheck();	// 标识是否保存并审核
		stockMaterialReturn.setIsCheck(BoolValue.NO);				// 默认未审核
		stockMaterialReturn.setBillType(BillType.STOCK_RM);

		if (stockMaterialReturn.getDetailList().size() > 0)
		{
			stockMaterialReturn.setBillNo(UserUtils.createBillNo(BillType.STOCK_RM));
			stockMaterialReturn.setCompanyId(UserUtils.getCompanyId());
			stockMaterialReturn.setCreateTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				stockMaterialReturn.setCreateName(e.getName());
			}
			else
			{
				stockMaterialReturn.setCreateName(UserUtils.getUserName());
			}
			stockMaterialReturn.setCreateEmployeeId(UserUtils.getEmployeeId());
			if (stockMaterialReturn.getReturnEmployeeId() != null)
			{
				stockMaterialReturn.setReturnEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialReturn.getReturnEmployeeId())).getName());
			}
			if (stockMaterialReturn.getReceiveEmployeeId() != null)
			{
				stockMaterialReturn.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialReturn.getReceiveEmployeeId())).getName());
			}
			daoFactory.getCommonDao().saveEntity(stockMaterialReturn);
		}

		for (StockMaterialReturnDetail stockMaterialReturnDetail : stockMaterialReturn.getDetailList())
		{
			if ("".equals(stockMaterialReturnDetail.getCode()) || stockMaterialReturnDetail.getCode() == null)
			{
				continue;
			}
			stockMaterialReturnDetail.setMasterId(stockMaterialReturn.getId());
			stockMaterialReturnDetail.setCompanyId(UserUtils.getCompanyId());
			daoFactory.getCommonDao().saveEntity(stockMaterialReturnDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockMaterialReturn.getId());
		}

		return stockMaterialReturn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockMaterialReturnService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockMaterialReturn)
	 */
	@Override
	@Transactional
	public Long update(StockMaterialReturn stockMaterialReturn)
	{
		StockMaterialReturn stockMaterialReturn_ = this.lockHasChildren(stockMaterialReturn.getId());
		// 判断是否已审核
		if (stockMaterialReturn_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockMaterialReturnDetail newItem : stockMaterialReturn.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockMaterialReturnDetail oldItem : stockMaterialReturn_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockMaterialReturnDetail.class, id);
			}
		}

		stockMaterialReturn_.setWarehouseId(stockMaterialReturn.getWarehouseId());
		stockMaterialReturn_.setReturnTime(stockMaterialReturn.getReturnTime());
		stockMaterialReturn_.setMemo(stockMaterialReturn.getMemo());
		stockMaterialReturn_.setWorkBillNo(stockMaterialReturn.getWorkBillNo());
		stockMaterialReturn_.setWorkBillType(stockMaterialReturn.getWorkBillType());
		stockMaterialReturn_.setWorkId(stockMaterialReturn.getWorkId());
		if (stockMaterialReturn.getReturnEmployeeId() != null)
		{
			stockMaterialReturn_.setReturnEmployeeId(stockMaterialReturn.getReturnEmployeeId());
			stockMaterialReturn_.setReturnEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialReturn.getReturnEmployeeId())).getName());
		}
		if (stockMaterialReturn.getReceiveEmployeeId() != null)
		{

			stockMaterialReturn_.setReceiveEmployeeId(stockMaterialReturn.getReceiveEmployeeId());
			stockMaterialReturn_.setReceiveEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockMaterialReturn.getReceiveEmployeeId())).getName());
		}

		daoFactory.getCommonDao().updateEntity(stockMaterialReturn_);

		for (StockMaterialReturnDetail stockMaterialReturnDetail : stockMaterialReturn.getDetailList())
		{
			if ("".equals(stockMaterialReturnDetail.getCode()) || stockMaterialReturnDetail.getCode() == null)
			{
				continue;
			}
			if (stockMaterialReturnDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockMaterialReturnDetail.class);
				querySt.eq("id", stockMaterialReturnDetail.getId());
				StockMaterialReturnDetail stockMaterialReturnDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockMaterialReturnDetail.class);
				stockMaterialReturnDetail_.setCode(stockMaterialReturnDetail.getCode());
				stockMaterialReturnDetail_.setMaterialName(stockMaterialReturnDetail.getMaterialName());
				stockMaterialReturnDetail_.setSpecifications(stockMaterialReturnDetail.getSpecifications());
				stockMaterialReturnDetail_.setStockUnitId(stockMaterialReturnDetail.getStockUnitId());
				stockMaterialReturnDetail_.setQty(stockMaterialReturnDetail.getQty());
				stockMaterialReturnDetail_.setPrice(stockMaterialReturnDetail.getPrice());
				stockMaterialReturnDetail_.setValuationUnitId(stockMaterialReturnDetail.getValuationUnitId());
				stockMaterialReturnDetail_.setValuationQty(stockMaterialReturnDetail.getValuationQty());
				stockMaterialReturnDetail_.setValuationPrice(stockMaterialReturnDetail.getValuationPrice());
				stockMaterialReturnDetail_.setMoney(stockMaterialReturnDetail.getMoney());
				stockMaterialReturnDetail_.setMaterialClassId(stockMaterialReturnDetail.getMaterialClassId());
				stockMaterialReturnDetail_.setMemo(stockMaterialReturnDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockMaterialReturnDetail_);
			}
			else
			{
				stockMaterialReturnDetail.setMasterId(stockMaterialReturn.getId());
				stockMaterialReturnDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockMaterialReturnDetail);
			}

		}
		if (stockMaterialReturn.getIsCheck() == BoolValue.YES)
		{
			this.check(stockMaterialReturn.getId());
		}
		return stockMaterialReturn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			StockMaterialReturn stockMaterialReturn = this.lockHasChildren(id);
			daoFactory.getCommonDao().deleteAllEntity(stockMaterialReturn.getDetailList());
			daoFactory.getCommonDao().deleteEntity(stockMaterialReturn);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_RM, id, BoolValue.YES))
		{
			return false;
		}
		StockMaterialReturn stockMaterialReturn = this.get(id);
		for (StockMaterialReturnDetail stockMaterialReturnDetail : stockMaterialReturn.getDetailList())
		{
			// 库存操作
			StockMaterial stockMaterial = new StockMaterial();
			stockMaterial.setMaterialId(stockMaterialReturnDetail.getMaterialId());
			stockMaterial.setSpecifications(stockMaterialReturnDetail.getSpecifications());
			stockMaterial.setQty(stockMaterialReturnDetail.getQty());
			stockMaterial.setValuationQty(stockMaterialReturnDetail.getValuationQty());
			stockMaterial.setWarehouseId(stockMaterialReturn.getWarehouseId());
			stockMaterial.setPrice(stockMaterialReturnDetail.getPrice());
			stockMaterial.setValuationPrice(stockMaterialReturnDetail.getValuationPrice());
			stockMaterial.setMoney(stockMaterialReturnDetail.getMoney());
			stockMaterial.setMaterialClassId(stockMaterialReturnDetail.getMaterialClassId());
			stockMaterial.setUpdateTime(new Date());
			serviceFactory.getMaterialStockService().stock(stockMaterial, InOutType.IN);

			// 入库记录
			StockMaterialLog log = new StockMaterialLog();
			log.setBillId(id);
			log.setBillType(stockMaterialReturn.getBillType());
			log.setBillNo(stockMaterialReturn.getBillNo());
			log.setSourceId(stockMaterialReturnDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(stockMaterialReturnDetail.getCode());
			log.setMaterialClassId(stockMaterialReturnDetail.getMaterialClassId());
			log.setMaterialName(stockMaterialReturnDetail.getMaterialName());
			log.setMaterialId(stockMaterialReturnDetail.getMaterialId());
			log.setSpecifications(stockMaterialReturnDetail.getSpecifications());
			log.setWarehouseId(stockMaterialReturn.getWarehouseId());
			log.setWeight(stockMaterialReturnDetail.getWeight());
			log.setUnitId(stockMaterialReturnDetail.getStockUnitId());
			log.setPrice(stockMaterialReturnDetail.getPrice());
			log.setInQty(stockMaterialReturnDetail.getQty());
			log.setInMoney(stockMaterialReturnDetail.getMoney());
			daoFactory.getCommonDao().saveEntity(log);
		}
		return true;

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockMaterial> list = new ArrayList<StockMaterial>();
		StockMaterialReturn stockMaterialReturn = this.get(id);
		// 先判断是否已经反审核
		if (stockMaterialReturn.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}

		for (StockMaterialReturnDetail stockMaterialReturnDetail : stockMaterialReturn.getDetailList())
		{
			DynamicQuery query = new CompanyDynamicQuery(StockMaterial.class);
			query.eq("materialId", stockMaterialReturnDetail.getMaterialId());
			if (stockMaterialReturnDetail.getSpecifications() == null || "".equals(stockMaterialReturnDetail.getSpecifications()))
			{
				stockMaterialReturnDetail.setSpecifications(null);
				query.add(Restrictions.or(Restrictions.isNull("specifications"), Restrictions.eq("specifications", "")));
			}
			else
			{
				query.eq("specifications", stockMaterialReturnDetail.getSpecifications());
			}
			query.eq("warehouseId", stockMaterialReturn.getWarehouseId());
			StockMaterial stockMaterial = daoFactory.getCommonDao().getByDynamicQuery(query, StockMaterial.class);
			if (stockMaterial == null)
			{
				stockMaterial = new StockMaterial();
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterialReturnDetail.getMaterialId()));
				stockMaterial.setQty(new BigDecimal(0));
			}
			else
			{
				stockMaterial.setMaterial(daoFactory.getCommonDao().getEntity(Material.class, stockMaterial.getMaterialId()));
			}

			if (stockMaterialReturnDetail.getQty().compareTo(stockMaterial.getQty()) > 0)
			{
				list.add(stockMaterial);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_RM, id, BoolValue.NO))
			{
				return null;
			}
			for (StockMaterialReturnDetail stockMaterialReturnDetail : stockMaterialReturn.getDetailList())
			{
				// 库存操作
				StockMaterial stockMaterial = new StockMaterial();
				stockMaterial.setMaterialId(stockMaterialReturnDetail.getMaterialId());
				stockMaterial.setSpecifications(stockMaterialReturnDetail.getSpecifications());
				stockMaterial.setQty(stockMaterialReturnDetail.getQty());
				stockMaterial.setValuationQty(stockMaterialReturnDetail.getValuationQty());
				stockMaterial.setWarehouseId(stockMaterialReturn.getWarehouseId());
				stockMaterial.setPrice(stockMaterialReturnDetail.getPrice());
				stockMaterial.setValuationPrice(stockMaterialReturnDetail.getValuationPrice());
				stockMaterial.setMoney(stockMaterialReturnDetail.getMoney());
				stockMaterial.setMaterialClassId(stockMaterialReturnDetail.getMaterialClassId());
				stockMaterial.setUpdateTime(new Date());
				stockMaterial.setIsIn(BoolValue.YES);
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
	 * @see com.huayin.printmanager.service.stock.StockMaterialReturnService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockMaterialReturn lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockMaterialReturn.class);
		query.eq("id", id);
		StockMaterialReturn order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockMaterialReturn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockMaterialReturnDetail.class);
		query_detail.eq("masterId", id);
		List<StockMaterialReturnDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockMaterialReturnDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
