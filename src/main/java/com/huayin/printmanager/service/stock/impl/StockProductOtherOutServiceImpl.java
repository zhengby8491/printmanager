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
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductOtherOutService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品其它出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductOtherOutServiceImpl extends BaseServiceImpl implements StockProductOtherOutService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherOutService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductOtherOut> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherOut.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("outTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("outTime", queryParam.getDateMax());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductOtherOut.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherOutService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductOtherOutDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherOutDetail.class, "ad");
		query.createAlias(StockProductOtherOut.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a,p"));
		query.eqProperty("ad.masterId", "a.id");
		query.eqProperty("p.id", "ad.productId");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.outTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.outTime", queryParam.getDateMax());
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
		SearchResult<StockProductOtherOutDetail> result = new SearchResult<StockProductOtherOutDetail>();
		result.setResult(new ArrayList<StockProductOtherOutDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductOtherOutDetail detail = (StockProductOtherOutDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductOtherOut) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherOutService#save(com.huayin.printmanager.persist.entity.stock
	 * .StockProductOtherOut)
	 */
	@Override
	@Transactional
	public ServiceResult<StockProductOtherOut> save(StockProductOtherOut stockProductOtherOut)
	{
		// 是否要审核标识
		BoolValue flag = stockProductOtherOut.getIsCheck();
		stockProductOtherOut.setBillType(BillType.STOCK_SPOO);
		stockProductOtherOut.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPOO));
		stockProductOtherOut.setCompanyId(UserUtils.getCompanyId());
		stockProductOtherOut.setCreateTime(new Date());
		stockProductOtherOut.setIsCheck(BoolValue.NO);				// 默认未审核
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductOtherOut.setCreateName(e.getName());
		}
		else
		{
			stockProductOtherOut.setCreateName(UserUtils.getUserName());
		}
		stockProductOtherOut.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockProductOtherOut.getEmployeeId() != null)
		{
			stockProductOtherOut.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductOtherOut.getEmployeeId())).getName());
		}

		daoFactory.getCommonDao().saveEntity(stockProductOtherOut);
		for (StockProductOtherOutDetail stockProductOtherOutDetail : stockProductOtherOut.getDetailList())
		{
			if ("".equals(stockProductOtherOutDetail.getCode()) || stockProductOtherOutDetail.getCode() == null)
			{
				continue;
			}
			stockProductOtherOutDetail.setMasterId(stockProductOtherOut.getId());
			stockProductOtherOutDetail.setCompanyId(UserUtils.getCompanyId());
			daoFactory.getCommonDao().saveEntity(stockProductOtherOutDetail);
		}
		ServiceResult<StockProductOtherOut> serviceResult = new ServiceResult<StockProductOtherOut>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockProductOtherOut.getId(), stockProductOtherOut.getForceCheck()));
		}
		serviceResult.setReturnValue(stockProductOtherOut);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherOutService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockProductOtherOut)
	 */
	@Override
	@Transactional
	public ServiceResult<StockProductOtherOut> update(StockProductOtherOut stockProductOtherOut)
	{
		// 是否要审核标识
		BoolValue flag = stockProductOtherOut.getIsCheck();

		ServiceResult<StockProductOtherOut> serviceResult = new ServiceResult<StockProductOtherOut>();

		// 再更新数据
		StockProductOtherOut stockProductOtherOut_ = this.lockHasChildren(stockProductOtherOut.getId());
		// 判断是否已审核
		if (stockProductOtherOut_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductOtherOutDetail newItem : stockProductOtherOut.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductOtherOutDetail oldItem : stockProductOtherOut_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductOtherOutDetail.class, id);
			}
		}
		stockProductOtherOut_.setIsCheck(BoolValue.NO);
		stockProductOtherOut_.setOutTime(stockProductOtherOut.getOutTime());
		stockProductOtherOut_.setMemo(stockProductOtherOut.getMemo());

		stockProductOtherOut_.setWarehouseId(stockProductOtherOut.getWarehouseId());
		if (stockProductOtherOut.getEmployeeId() != null)
		{
			stockProductOtherOut_.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductOtherOut.getEmployeeId())).getName());
			stockProductOtherOut_.setEmployeeId(stockProductOtherOut.getEmployeeId());
		}

		daoFactory.getCommonDao().updateEntity(stockProductOtherOut_);
		for (StockProductOtherOutDetail stockProductOtherOutDetail : stockProductOtherOut.getDetailList())
		{
			if ("".equals(stockProductOtherOutDetail.getCode()) || stockProductOtherOutDetail.getCode() == null)
			{
				continue;
			}
			if (stockProductOtherOutDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockProductOtherOutDetail.class);
				querySt.eq("id", stockProductOtherOutDetail.getId());
				StockProductOtherOutDetail stockProductOtherOutDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockProductOtherOutDetail.class);
				stockProductOtherOutDetail_.setCode(stockProductOtherOutDetail.getCode());
				stockProductOtherOutDetail_.setProductName(stockProductOtherOutDetail.getProductName());
				stockProductOtherOutDetail_.setSpecifications(stockProductOtherOutDetail.getSpecifications());
				stockProductOtherOutDetail_.setUnitId(stockProductOtherOutDetail.getUnitId());
				stockProductOtherOutDetail_.setQty(stockProductOtherOutDetail.getQty());
				stockProductOtherOutDetail_.setPrice(stockProductOtherOutDetail.getPrice());
				stockProductOtherOutDetail_.setMoney(stockProductOtherOutDetail.getMoney());
				stockProductOtherOutDetail_.setProductClassId(stockProductOtherOutDetail.getProductClassId());
				stockProductOtherOutDetail_.setMemo(stockProductOtherOutDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockProductOtherOutDetail_);
			}
			else
			{
				stockProductOtherOutDetail.setMasterId(stockProductOtherOut.getId());
				stockProductOtherOutDetail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(stockProductOtherOutDetail);
			}
		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockProductOtherOut.getId(), stockProductOtherOut.getForceCheck()));
		}
		serviceResult.setReturnValue(stockProductOtherOut);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherOutService#get(java.lang.Long)
	 */
	@Override
	public StockProductOtherOut get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherOut.class);
		query.eq("id", id);
		StockProductOtherOut stockProductOtherOut = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductOtherOut.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductOtherOutDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp,p"));
		queryD.eq("masterId", id);
		queryD.eqProperty("p.id", "sp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductOtherOutDetail> stockProductOtherOutDetailList = new ArrayList<>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductOtherOutDetail detail = (StockProductOtherOutDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductOtherOutDetailList.add(detail);
		}
		// List<StockProductOtherOutDetail> stockProductOtherOutDetailList = daoFactory.getCommonDao()
		// .findEntityByDynamicQuery(queryD, StockProductOtherOutDetail.class);
		if (stockProductOtherOut != null)
		{
			stockProductOtherOut.setDetailList(stockProductOtherOutDetailList);
		}
		return stockProductOtherOut;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherOutService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockProductOtherOut master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockProductOtherOutService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockProduct> check(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		StockProductOtherOut master = this.get(id);
		// 先判断是否已经审核
		if (master.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (StockProductOtherOutDetail stockProductOtherOutDetail : master.getDetailList())
		{// 将库存不足的库存对象存List
			StockProduct stockProduct = daoFactory.getCommonDao().getEntity(StockProduct.class, stockProductOtherOutDetail.getStockProductId());
			if (stockProduct == null)
			{
				stockProduct = new StockProduct();
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProductOtherOutDetail.getProductId()));
				stockProduct.setQty(0);
			}
			else
			{
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			}

			if (stockProductOtherOutDetail.getQty() > stockProduct.getQty())
			{
				list.add(stockProduct);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPOO, id, BoolValue.YES))
			{
				return null;
			}
			// 库存操作
			for (StockProductOtherOutDetail stockProductOtherOutDetail : master.getDetailList())
			{
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(stockProductOtherOutDetail.getProductId());
				stockProduct.setQty(stockProductOtherOutDetail.getQty());
				stockProduct.setWarehouseId(master.getWarehouseId());
				stockProduct.setPrice(stockProductOtherOutDetail.getPrice());
				stockProduct.setMoney(new BigDecimal(0).subtract(stockProductOtherOutDetail.getMoney()));
				serviceFactory.getStockProductService().stock(stockProduct, InOutType.OUT);

				// 出库记录
				StockProductLog log = new StockProductLog();
				Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductOtherOutDetail.getProductId());
				log.setBillId(master.getId());
				log.setBillType(master.getBillType());
				log.setBillNo(master.getBillNo());
				log.setSourceId(stockProductOtherOutDetail.getId());
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
				log.setPrice(stockProductOtherOutDetail.getPrice());
				log.setOutQty(stockProductOtherOutDetail.getQty());
				log.setOutMoney(stockProductOtherOutDetail.getMoney());
				Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(),stockProduct.getWarehouseId());
				log.setStorgeQty(storgeQty);
				daoFactory.getCommonDao().saveEntity(log);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherOutService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPOO, id, BoolValue.NO))
		{
			return 0;
		}
		StockProductOtherOut master = this.get(id);
		for (StockProductOtherOutDetail stockProductOtherOutDetail : master.getDetailList())
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setProductId(stockProductOtherOutDetail.getProductId());
			stockProduct.setQty(stockProductOtherOutDetail.getQty());
			stockProduct.setWarehouseId(master.getWarehouseId());
			stockProduct.setPrice(stockProductOtherOutDetail.getPrice());
			stockProduct.setMoney(stockProductOtherOutDetail.getMoney());
			serviceFactory.getStockProductService().backStock(stockProduct,InOutType.OUT);

			DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
			query.eq("billId", id);
			List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
			daoFactory.getCommonDao().deleteAllEntity(logs);
		}

		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherOutService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductOtherOut lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherOut.class);
		query.eq("id", id);
		StockProductOtherOut order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductOtherOut.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductOtherOutDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductOtherOutDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductOtherOutDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
