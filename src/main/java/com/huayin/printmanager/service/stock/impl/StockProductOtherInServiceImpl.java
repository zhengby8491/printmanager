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
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductOtherInService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品其它入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductOtherInServiceImpl extends BaseServiceImpl implements StockProductOtherInService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherInService#findByCondition(com.huayin.printmanager.service.vo
	 * .QueryParam)
	 */
	@Override
	public SearchResult<StockProductOtherIn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherIn.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductOtherIn.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherInService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductOtherInDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherInDetail.class, "ad");
		query.createAlias(StockProductOtherIn.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a,p"));
		query.eqProperty("ad.masterId", "a.id");
		query.eqProperty("p.id", "ad.productId");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.inTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.inTime", queryParam.getDateMax());
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
		SearchResult<StockProductOtherInDetail> result = new SearchResult<StockProductOtherInDetail>();
		result.setResult(new ArrayList<StockProductOtherInDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductOtherInDetail detail = (StockProductOtherInDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductOtherIn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherInService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockProductOtherIn)
	 */
	@Override
	@Transactional
	public Long save(StockProductOtherIn stockProductOtherIn)
	{
		BoolValue flag = stockProductOtherIn.getIsCheck();	// 标识是否保存并审核
		stockProductOtherIn.setIsCheck(BoolValue.NO);				// 默认未审核
		stockProductOtherIn.setBillType(BillType.STOCK_SPOI);
		stockProductOtherIn.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPOI));
		stockProductOtherIn.setCompanyId(UserUtils.getCompanyId());
		stockProductOtherIn.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductOtherIn.setCreateName(e.getName());
		}
		else
		{
			stockProductOtherIn.setCreateName(UserUtils.getUserName());
		}
		stockProductOtherIn.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockProductOtherIn.getEmployeeId() != null)
		{
			stockProductOtherIn.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductOtherIn.getEmployeeId())).getName());
		}
		daoFactory.getCommonDao().saveEntity(stockProductOtherIn);

		for (StockProductOtherInDetail stockProductOtherInDetail : stockProductOtherIn.getDetailList())
		{
			if ("".equals(stockProductOtherInDetail.getCode()) || stockProductOtherInDetail.getCode() == null)
			{
				continue;
			}
			stockProductOtherInDetail.setMasterId(stockProductOtherIn.getId());
			stockProductOtherInDetail.setCompanyId(UserUtils.getCompanyId());
			if (stockProductOtherInDetail.getPrice() == null)
			{
				stockProductOtherInDetail.setPrice(new BigDecimal(0));
			}
			if (stockProductOtherInDetail.getMoney() == null)
			{
				stockProductOtherInDetail.setMoney(new BigDecimal(0));
			}
			daoFactory.getCommonDao().saveEntity(stockProductOtherInDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockProductOtherIn.getId());
		}
		return stockProductOtherIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductOtherInService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockProductOtherIn)
	 */
	@Override
	@Transactional
	public Long update(StockProductOtherIn stockProductOtherIn)
	{
		StockProductOtherIn stockProductOtherIn_ = this.lockHasChildren(stockProductOtherIn.getId());
		// 判断是否已审核
		if (stockProductOtherIn_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductOtherInDetail newItem : stockProductOtherIn.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductOtherInDetail oldItem : stockProductOtherIn_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductOtherInDetail.class, id);
			}
		}

		stockProductOtherIn_.setWarehouseId(stockProductOtherIn.getWarehouseId());
		stockProductOtherIn_.setInTime(stockProductOtherIn.getInTime());
		stockProductOtherIn_.setMemo(stockProductOtherIn.getMemo());
		if (stockProductOtherIn.getEmployeeId() != null)
		{
			stockProductOtherIn_.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductOtherIn.getEmployeeId())).getName());
			stockProductOtherIn_.setEmployeeId(stockProductOtherIn.getEmployeeId());
		}

		daoFactory.getCommonDao().updateEntity(stockProductOtherIn_);

		for (StockProductOtherInDetail stockProductOtherInDetail : stockProductOtherIn.getDetailList())
		{
			if (stockProductOtherInDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockProductOtherInDetail.class);
				querySt.eq("id", stockProductOtherInDetail.getId());
				StockProductOtherInDetail stockProductOtherInDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockProductOtherInDetail.class);
				stockProductOtherInDetail_.setCode(stockProductOtherInDetail.getCode());
				stockProductOtherInDetail_.setProductName(stockProductOtherInDetail.getProductName());
				stockProductOtherInDetail_.setSpecifications(stockProductOtherInDetail.getSpecifications());
				stockProductOtherInDetail_.setUnitId(stockProductOtherInDetail.getUnitId());
				stockProductOtherInDetail_.setQty(stockProductOtherInDetail.getQty());
				stockProductOtherInDetail_.setPrice(stockProductOtherInDetail.getPrice() == null ? new BigDecimal(0) : stockProductOtherInDetail.getPrice());
				stockProductOtherInDetail_.setMoney(stockProductOtherInDetail.getMoney() == null ? new BigDecimal(0) : stockProductOtherInDetail.getMoney());
				stockProductOtherInDetail_.setProductClassId(stockProductOtherInDetail.getProductClassId());
				stockProductOtherInDetail_.setMemo(stockProductOtherInDetail.getMemo());

				daoFactory.getCommonDao().updateEntity(stockProductOtherInDetail_);
			}
			else
			{
				stockProductOtherInDetail.setMasterId(stockProductOtherIn.getId());
				stockProductOtherInDetail.setCompanyId(UserUtils.getCompanyId());
				if (stockProductOtherInDetail.getPrice() == null)
				{
					stockProductOtherInDetail.setPrice(new BigDecimal(0));
				}
				if (stockProductOtherInDetail.getMoney() == null)
				{
					stockProductOtherInDetail.setMoney(new BigDecimal(0));
				}
				daoFactory.getCommonDao().saveEntity(stockProductOtherInDetail);
			}

		}
		if (stockProductOtherIn.getIsCheck() == BoolValue.YES)
		{
			this.check(stockProductOtherIn.getId());
		}
		return stockProductOtherIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherInService#get(java.lang.Long)
	 */
	@Override
	public StockProductOtherIn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherIn.class);
		query.eq("id", id);
		StockProductOtherIn stockProductOtherIn = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductOtherIn.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductOtherInDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp,p"));
		queryD.eq("masterId", id);
		queryD.eqProperty("p.id", "sp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductOtherInDetail> stockProductOtherInDetailList = new ArrayList<>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductOtherInDetail detail = (StockProductOtherInDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductOtherInDetailList.add(detail);
		}
		// List<StockProductOtherInDetail> stockProductOtherInDetailList = daoFactory.getCommonDao()
		// .findEntityByDynamicQuery(queryD, StockProductOtherInDetail.class);
		if (stockProductOtherIn != null)
		{
			stockProductOtherIn.setDetailList(stockProductOtherInDetailList);
		}
		return stockProductOtherIn;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherInService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockProductOtherIn master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockProductOtherInService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPOI, id, BoolValue.YES))
		{
			return 0;
		}
		StockProductOtherIn stockProductOtherIn = this.get(id);
		for (StockProductOtherInDetail stockProductOtherInDetail : stockProductOtherIn.getDetailList())
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setProductId(stockProductOtherInDetail.getProductId());
			stockProduct.setQty(stockProductOtherInDetail.getQty());
			stockProduct.setWarehouseId(stockProductOtherIn.getWarehouseId());
			stockProduct.setPrice(stockProductOtherInDetail.getPrice());
			stockProduct.setMoney(stockProductOtherInDetail.getMoney());
			stockProduct.setUpdateTime(new Date());
			serviceFactory.getStockProductService().stock(stockProduct, InOutType.IN);

			// 入库记录
			StockProductLog log = new StockProductLog();
			Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductOtherInDetail.getProductId());
			log.setBillId(stockProductOtherIn.getId());
			log.setBillType(stockProductOtherIn.getBillType());
			log.setBillNo(stockProductOtherIn.getBillNo());
			log.setSourceId(stockProductOtherInDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCustomerMaterialCode(product.getCustomerMaterialCode());
			log.setCode(product.getCode());
			log.setProductClassId(product.getProductClassId());
			log.setProductName(product.getName());
			log.setProductId(product.getId());
			log.setSpecifications(product.getSpecifications());
			log.setWarehouseId(stockProductOtherIn.getWarehouseId());
			log.setUnitId(product.getUnitId());
			log.setPrice(stockProductOtherInDetail.getPrice());
			log.setInQty(stockProductOtherInDetail.getQty());
			log.setInMoney(stockProductOtherInDetail.getMoney());
			Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
			log.setStorgeQty(storgeQty);
			daoFactory.getCommonDao().saveEntity(log);
		}

		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherInService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		StockProductOtherIn master = this.get(id);
		// 先判断是否已经反审核
		if (master.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		} 
		for (StockProductOtherInDetail stockProductOtherInDetail : master.getDetailList())
		{// 将库存不足的库存对象存List
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProductOtherInDetail.getProductId());
			query.eq("warehouseId", master.getWarehouseId());
			StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			if (stockProduct == null)
			{
				stockProduct = new StockProduct();
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProductOtherInDetail.getProductId()));
				stockProduct.setQty(0);
			}
			else
			{
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			}

			if (stockProductOtherInDetail.getQty() > stockProduct.getQty())
			{
				list.add(stockProduct);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPOI, id, BoolValue.NO))
			{
				return null;
			}
			// 库存操作
			for (StockProductOtherInDetail stockProductOtherInDetail : master.getDetailList())
			{
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(stockProductOtherInDetail.getProductId());
				stockProduct.setQty(stockProductOtherInDetail.getQty());
				stockProduct.setWarehouseId(master.getWarehouseId());
				stockProduct.setPrice(stockProductOtherInDetail.getPrice());
				stockProduct.setMoney(stockProductOtherInDetail.getMoney());
				// TODO 反审核操作
				serviceFactory.getStockProductService().backStock(stockProduct, InOutType.IN);

				DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
				query.eq("billId", id);
				List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductOtherInService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductOtherIn lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductOtherIn.class);
		query.eq("id", id);
		StockProductOtherIn order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductOtherIn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductOtherInDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductOtherInDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductOtherInDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
