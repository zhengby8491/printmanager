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

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjust;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductAdjustService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品调整
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductAdjustServiceImpl extends BaseServiceImpl implements StockProductAdjustService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductAdjustService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<StockProductAdjust> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductAdjust.class);
		query.setIsSearchTotalCount(true);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductAdjust.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductAdjustService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductAdjustDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductAdjustDetail.class, "ad");
		query.createAlias(StockProductAdjust.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a,p"));
		query.eqProperty("ad.masterId", "a.id");
		query.eqProperty("p.id", "ad.productId");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.adjustTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.adjustTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("ad.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("a.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("a.warehouseId", queryParam.getWarehouseId());
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
		SearchResult<StockProductAdjustDetail> result = new SearchResult<StockProductAdjustDetail>();
		result.setResult(new ArrayList<StockProductAdjustDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductAdjustDetail detail = (StockProductAdjustDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductAdjust) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductAdjustService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockProductAdjust)
	 */
	@Override
	@Transactional
	public Long save(StockProductAdjust stockProductAdjust)
	{
		BoolValue flag = stockProductAdjust.getIsCheck();						// 标识是否保存并审核
		stockProductAdjust.setIsCheck(BoolValue.NO);								// 默认未审核
		stockProductAdjust.setBillType(BillType.STOCK_SPA);
		stockProductAdjust.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPA));
		stockProductAdjust.setCompanyId(UserUtils.getCompanyId());
		stockProductAdjust.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductAdjust.setCreateName(e.getName());
		}
		else
		{
			stockProductAdjust.setCreateName(UserUtils.getUserName());
		}
		stockProductAdjust.setCreateEmployeeId(UserUtils.getEmployeeId());
		daoFactory.getCommonDao().saveEntity(stockProductAdjust);
		for (StockProductAdjustDetail stockProductAdjustDetail : stockProductAdjust.getDetailList())
		{
			if ("".equals(stockProductAdjustDetail.getCode()) || stockProductAdjustDetail.getCode() == null)
			{
				continue;
			}
			stockProductAdjustDetail.setMasterId(stockProductAdjust.getId());
			stockProductAdjustDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockProductAdjustDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockProductAdjust.getId());
		}
		return stockProductAdjust.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductAdjustService#update(com.huayin.printmanager.persist.entity.stock
	 * .StockProductAdjust)
	 */
	@Override
	@Transactional
	public Long update(StockProductAdjust stockProductAdjust)
	{
		StockProductAdjust stockProductAdjust_ = this.lockHasChildren(stockProductAdjust.getId());
		// 判断是否已审核
		if (stockProductAdjust_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductAdjustDetail newItem : stockProductAdjust.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductAdjustDetail oldItem : stockProductAdjust_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductAdjustDetail.class, id);
			}
		}

		stockProductAdjust_.setWarehouseId(stockProductAdjust.getWarehouseId());
		stockProductAdjust_.setAdjustTime(stockProductAdjust.getAdjustTime());
		stockProductAdjust_.setEmployeeId(stockProductAdjust.getEmployeeId());
		stockProductAdjust_.setEmployeeName(stockProductAdjust.getEmployeeName());
		stockProductAdjust_.setMemo(stockProductAdjust.getMemo());
		daoFactory.getCommonDao().updateEntity(stockProductAdjust_);
		for (StockProductAdjustDetail stockProductAdjustDetail : stockProductAdjust.getDetailList())
		{
			if ("".equals(stockProductAdjustDetail.getCode()) || stockProductAdjustDetail.getCode() == null)
			{
				continue;
			}
			if (stockProductAdjustDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockProductAdjustDetail.class);
				querySt.eq("id", stockProductAdjustDetail.getId());
				StockProductAdjustDetail stockProductAdjustDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockProductAdjustDetail.class);
				stockProductAdjustDetail_.setCode(stockProductAdjustDetail.getCode());
				stockProductAdjustDetail_.setProductName(stockProductAdjustDetail.getProductName());
				stockProductAdjustDetail_.setSpecifications(stockProductAdjustDetail.getSpecifications());
				stockProductAdjustDetail_.setUnitId(stockProductAdjustDetail.getUnitId());
				stockProductAdjustDetail_.setQty(stockProductAdjustDetail.getQty());
				stockProductAdjustDetail_.setPrice(stockProductAdjustDetail.getPrice());
				stockProductAdjustDetail_.setMoney(stockProductAdjustDetail.getMoney());
				stockProductAdjustDetail_.setProductClassId(stockProductAdjustDetail.getProductClassId());
				stockProductAdjustDetail_.setStockProductlId(stockProductAdjustDetail.getStockProductlId());
				stockProductAdjustDetail_.setMemo(stockProductAdjustDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockProductAdjustDetail_);
			}
			else
			{
				stockProductAdjustDetail.setMasterId(stockProductAdjust.getId());
				stockProductAdjustDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockProductAdjustDetail);
			}

		}
		if (stockProductAdjust.getIsCheck() == BoolValue.YES)
		{
			this.check(stockProductAdjust.getId());
		}
		return stockProductAdjust_.getId();

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductAdjustService#get(java.lang.Long)
	 */
	@Override
	public StockProductAdjust get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductAdjust.class);
		query.eq("id", id);
		StockProductAdjust stockProductAdjust = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductAdjust.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductAdjustDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp,p"));
		queryD.eq("masterId", id);
		queryD.eqProperty("p.id", "sp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductAdjustDetail> stockProductAdjustDetailList = new ArrayList<StockProductAdjustDetail>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductAdjustDetail detail = (StockProductAdjustDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductAdjustDetailList.add(detail);
		}
		// List<StockProductAdjustDetail> stockProductAdjustDetailList = daoFactory.getCommonDao()
		// .findEntityByDynamicQuery(queryD, StockProductAdjustDetail.class);

		if (stockProductAdjust != null)
		{
			stockProductAdjust.setDetailList(stockProductAdjustDetailList);
		}
		return stockProductAdjust;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductAdjustService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockProductAdjust master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockProductAdjustService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPA, id, BoolValue.YES))
		{
			return 0;
		}
		// 审核操作
		StockProductAdjust master = this.get(id);
		for (StockProductAdjustDetail stockProductAdjustDetail : master.getDetailList())
		{
		
			// 出入库记录
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("id", stockProductAdjustDetail.getStockProductlId());
			StockProduct stockProduct_old = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);

			// 出库记录
			StockProductLog log = new StockProductLog();
			Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductAdjustDetail.getProductId());
			log.setBillId(master.getId());
			log.setBillType(master.getBillType());
			log.setBillNo(master.getBillNo());
			log.setSourceId(stockProductAdjustDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCustomerMaterialCode(product.getCustomerMaterialCode());
			log.setCode(product.getCode());
			log.setProductClassId(product.getProductClassId());
			log.setProductName(product.getName());
			log.setProductId(product.getId());
			log.setSpecifications(product.getSpecifications());
			log.setWarehouseId(master.getWarehouseId());
			log.setUnitId(product.getUnitId());
			log.setPrice(stockProductAdjustDetail.getPrice());
			if (stockProductAdjustDetail.getQty() > stockProduct_old.getQty())
			{
				log.setInQty(stockProductAdjustDetail.getQty() - stockProduct_old.getQty());
				log.setInMoney(stockProductAdjustDetail.getPrice().multiply(new BigDecimal(log.getInQty())));
			}
			else
			{
				log.setOutQty(stockProduct_old.getQty() - stockProductAdjustDetail.getQty());
				log.setOutMoney(stockProductAdjustDetail.getPrice().multiply(new BigDecimal(log.getOutQty())));
			}
			log.setStorgeQty(stockProductAdjustDetail.getQty());
			daoFactory.getCommonDao().saveEntity(log);
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setId(stockProductAdjustDetail.getStockProductlId());
			stockProduct.setQty(stockProductAdjustDetail.getQty());
			stockProduct.setMoney(stockProductAdjustDetail.getMoney());
			stockProduct.setUpdateTime(new Date());
			serviceFactory.getStockProductService().adjust(stockProduct);
		}
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductAdjustService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductAdjust lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductAdjust.class);
		query.eq("id", id);
		StockProductAdjust order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductAdjust.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductAdjustDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductAdjustDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductAdjustDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
