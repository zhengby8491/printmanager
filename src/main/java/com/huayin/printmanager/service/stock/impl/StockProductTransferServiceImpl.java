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
import com.huayin.printmanager.persist.entity.stock.StockProductTransfer;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductTransferService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductTransferServiceImpl extends BaseServiceImpl implements StockProductTransferService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductTransferService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductTransfer> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductTransfer.class);
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
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getOutWarehouseId())
		{
			query.eq("outWarehouseId", queryParam.getOutWarehouseId());
		}
		if (null != queryParam.getInWarehouseId())
		{
			query.eq("inWarehouseId", queryParam.getInWarehouseId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductTransfer.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductTransferService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductTransferDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductTransferDetail.class, "ad");
		query.createAlias(StockProductTransfer.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a ,p"));
		query.eqProperty("ad.masterId", "a.id");
		query.eqProperty("ad.productId", "p.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.transferTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.transferTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("ad.productName", "%" + queryParam.getProductName() + "%");
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
		SearchResult<StockProductTransferDetail> result = new SearchResult<StockProductTransferDetail>();
		result.setResult(new ArrayList<StockProductTransferDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductTransferDetail detail = (StockProductTransferDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductTransfer) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductTransferService#save(com.huayin.printmanager.persist.entity.stock
	 * .StockProductTransfer)
	 */
	@Override
	@Transactional
	public ServiceResult<StockProductTransfer> save(StockProductTransfer stockProductTransfer)
	{
		// 是否要审核标识
		BoolValue flag = stockProductTransfer.getIsCheck();
		stockProductTransfer.setBillType(BillType.STOCK_SPT);
		stockProductTransfer.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPT));
		stockProductTransfer.setCompanyId(UserUtils.getCompanyId());
		stockProductTransfer.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductTransfer.setCreateName(e.getName());
		}
		else
		{
			stockProductTransfer.setCreateName(UserUtils.getUserName());
		}
		stockProductTransfer.setCreateEmployeeId(UserUtils.getEmployeeId());
		stockProductTransfer.setIsCheck(BoolValue.NO);
		daoFactory.getCommonDao().saveEntity(stockProductTransfer);
		for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
		{
			if ("".equals(stockProductTransferDetail.getCode()) || stockProductTransferDetail.getCode() == null)
			{
				continue;
			}
			stockProductTransferDetail.setMasterId(stockProductTransfer.getId());
			stockProductTransferDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockProductTransferDetail);
		}
		ServiceResult<StockProductTransfer> serviceResult = new ServiceResult<StockProductTransfer>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockProductTransfer.getId(), stockProductTransfer.getForceCheck()));
		}
		serviceResult.setReturnValue(stockProductTransfer);
		return serviceResult;

	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductTransferService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockProductTransfer)
	 */
	@Override
	@Transactional
	public ServiceResult<StockProductTransfer> update(StockProductTransfer stockProductTransfer)
	{
		// 是否要审核标识
		BoolValue flag = stockProductTransfer.getIsCheck();

		ServiceResult<StockProductTransfer> serviceResult = new ServiceResult<StockProductTransfer>();

		// 再更新数据
		StockProductTransfer stockProductTransfer_ = this.lockHasChildren(stockProductTransfer.getId());
		// 判断是否已审核
		if (stockProductTransfer_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductTransferDetail newItem : stockProductTransfer.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductTransferDetail oldItem : stockProductTransfer_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductTransferDetail.class, id);
			}
		}
		stockProductTransfer_.setIsCheck(BoolValue.NO);
		stockProductTransfer_.setOutWarehouseId(stockProductTransfer.getOutWarehouseId());
		stockProductTransfer_.setInWarehouseId(stockProductTransfer.getInWarehouseId());
		stockProductTransfer_.setTransferTime(stockProductTransfer.getTransferTime());
		stockProductTransfer_.setEmployeeId(stockProductTransfer.getEmployeeId());
		stockProductTransfer_.setEmployeeName(stockProductTransfer.getEmployeeName());
		stockProductTransfer_.setMemo(stockProductTransfer.getMemo());
		daoFactory.getCommonDao().updateEntity(stockProductTransfer_);
		for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
		{
			if ("".equals(stockProductTransferDetail.getCode()) || stockProductTransferDetail.getCode() == null)
			{
				continue;
			}
			if (stockProductTransferDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockProductTransferDetail.class);
				querySt.eq("id", stockProductTransferDetail.getId());
				StockProductTransferDetail stockProductTransferDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockProductTransferDetail.class);
				stockProductTransferDetail_.setCode(stockProductTransferDetail.getCode());
				stockProductTransferDetail_.setProductName(stockProductTransferDetail.getProductName());
				stockProductTransferDetail_.setSpecifications(stockProductTransferDetail.getSpecifications());
				stockProductTransferDetail_.setUnitId(stockProductTransferDetail.getUnitId());
				stockProductTransferDetail_.setQty(stockProductTransferDetail.getQty());
				stockProductTransferDetail_.setPrice(stockProductTransferDetail.getPrice());
				stockProductTransferDetail_.setMoney(stockProductTransferDetail.getMoney());
				stockProductTransferDetail_.setProductClassId(stockProductTransferDetail.getProductClassId());
				stockProductTransferDetail_.setMemo(stockProductTransferDetail.getMemo());
				daoFactory.getCommonDao().updateEntity(stockProductTransferDetail_);
			}
			else
			{
				stockProductTransferDetail.setMasterId(stockProductTransfer.getId());
				stockProductTransferDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockProductTransferDetail);
			}
		}
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(stockProductTransfer.getId(), stockProductTransfer.getForceCheck()));
		}
		serviceResult.setReturnValue(stockProductTransfer);
		return serviceResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductTransferService#get(java.lang.Long)
	 */
	@Override
	public StockProductTransfer get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductTransfer.class);
		query.eq("id", id);
		StockProductTransfer stockProductTransfer = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductTransfer.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductTransferDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp,p"));
		queryD.eq("masterId", id);
		queryD.eqProperty("p.id", "sp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductTransferDetail> stockProductTransferDetailList = new ArrayList<>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductTransferDetail detail = (StockProductTransferDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductTransferDetailList.add(detail);
		}
		if (stockProductTransfer != null)
		{
			stockProductTransfer.setDetailList(stockProductTransferDetailList);
		}
		return stockProductTransfer;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductTransferService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockProductTransfer master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockProductTransferService#check(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockProduct> check(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		StockProductTransfer stockProductTransfer = this.get(id);
		// 先判断是否已经审核
		if (stockProductTransfer.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		} 

		for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
		{// 将库存不足的库存对象存List
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProductTransferDetail.getProductId());
			query.eq("warehouseId", stockProductTransfer.getOutWarehouseId());
			StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			if (stockProduct == null)
			{
				stockProduct = new StockProduct();
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProductTransferDetail.getProductId()));
				stockProduct.setQty(0);
			}
			else
			{
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			}

			if (stockProductTransferDetail.getQty() > stockProduct.getQty())
			{
				list.add(stockProduct);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPT, id, BoolValue.YES))
			{
				return null;
			}
			for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
			{
				// 调出库存操作
				StockProduct stockProductOut = new StockProduct();
				stockProductOut.setProductId(stockProductTransferDetail.getProductId());
				stockProductOut.setQty(stockProductTransferDetail.getQty());
				stockProductOut.setWarehouseId(stockProductTransfer.getOutWarehouseId());
				stockProductOut.setPrice(stockProductTransferDetail.getPrice());
				stockProductOut.setMoney(stockProductTransferDetail.getMoney());
				stockProductOut.setUpdateTime(new Date());
				serviceFactory.getStockProductService().stock(stockProductOut, InOutType.OUT);

				// 调入库存操作
				StockProduct stockProductIn = new StockProduct();
				stockProductIn.setProductId(stockProductTransferDetail.getProductId());
				stockProductIn.setQty(stockProductTransferDetail.getQty());
				stockProductIn.setWarehouseId(stockProductTransfer.getInWarehouseId());
				stockProductIn.setPrice(stockProductTransferDetail.getPrice());
				stockProductIn.setMoney(stockProductTransferDetail.getMoney());
				stockProductIn.setUpdateTime(new Date());
				serviceFactory.getStockProductService().stock(stockProductIn, InOutType.IN);

				// 出库记录
				StockProductLog log = new StockProductLog();
				Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductTransferDetail.getProductId());
				log.setBillId(stockProductTransfer.getId());
				log.setBillType(stockProductTransfer.getBillType());
				log.setBillNo(stockProductTransfer.getBillNo());
				log.setSourceId(stockProductTransferDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCustomerMaterialCode(product.getCustomerMaterialCode());
				log.setCode(product.getCode());
				log.setProductClassId(product.getProductClassId());
				log.setProductName(product.getName());
				log.setProductId(product.getId());
				log.setSpecifications(product.getSpecifications());
				log.setWarehouseId(stockProductTransfer.getOutWarehouseId());
				log.setUnitId(product.getUnitId());
				log.setPrice(stockProductTransferDetail.getPrice());
				log.setOutQty(stockProductTransferDetail.getQty());
				log.setOutMoney(stockProductTransferDetail.getMoney());
				Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProductIn.getProductId(),stockProductTransfer.getOutWarehouseId());
				log.setStorgeQty(storgeQty);
				daoFactory.getCommonDao().saveEntity(log);

				// 入库记录
				StockProductLog logIn = new StockProductLog();
				logIn.setBillId(stockProductTransfer.getId());
				logIn.setBillType(stockProductTransfer.getBillType());
				logIn.setBillNo(stockProductTransfer.getBillNo());
				logIn.setCreateTime(new Date());
				logIn.setCompanyId(UserUtils.getCompanyId());
				logIn.setCustomerMaterialCode(product.getCustomerMaterialCode());
				logIn.setCode(product.getCode());
				logIn.setProductClassId(product.getProductClassId());
				logIn.setProductName(product.getName());
				logIn.setProductId(product.getId());
				logIn.setSpecifications(product.getSpecifications());
				logIn.setWarehouseId(stockProductTransfer.getInWarehouseId());
				logIn.setUnitId(product.getUnitId());
				logIn.setPrice(stockProductTransferDetail.getPrice());
				logIn.setInQty(stockProductTransferDetail.getQty());
				logIn.setInMoney(stockProductTransferDetail.getMoney());
				Integer _storgeQty = serviceFactory.getStockProductService().getStockQty(stockProductTransferDetail.getProductId(),stockProductTransfer.getInWarehouseId());
				logIn.setStorgeQty(_storgeQty);
				daoFactory.getCommonDao().saveEntity(logIn);
			}

		}
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductTransferService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		StockProductTransfer stockProductTransfer = this.get(id);
		// 先判断是否已经反审核
		if (stockProductTransfer.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}

		for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
		{// 将库存不足的库存对象存List
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProductTransferDetail.getProductId());
			query.eq("warehouseId", stockProductTransfer.getInWarehouseId());
			StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			if (stockProductTransferDetail.getQty() > stockProduct.getQty())
			{
				list.add(stockProduct);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPT, id, BoolValue.NO))
			{
				return null;
			}
			for (StockProductTransferDetail stockProductTransferDetail : stockProductTransfer.getDetailList())
			{
				// 调出库存操作
				StockProduct stockProductOut = new StockProduct();
				stockProductOut.setProductId(stockProductTransferDetail.getProductId());
				stockProductOut.setQty(stockProductTransferDetail.getQty());
				stockProductOut.setWarehouseId(stockProductTransfer.getOutWarehouseId());
				stockProductOut.setPrice(stockProductTransferDetail.getPrice());
				stockProductOut.setMoney(stockProductTransferDetail.getMoney());
				serviceFactory.getStockProductService().backStock(stockProductOut, InOutType.OUT);

				// 调入库存操作
				StockProduct stockProductIn = new StockProduct();
				stockProductIn.setProductId(stockProductTransferDetail.getProductId());
				stockProductIn.setQty(stockProductTransferDetail.getQty());
				stockProductIn.setWarehouseId(stockProductTransfer.getInWarehouseId());
				stockProductIn.setPrice(stockProductTransferDetail.getPrice());
				stockProductIn.setMoney(stockProductTransferDetail.getMoney());
				stockProductIn.setUpdateTime(new Date());
				serviceFactory.getStockProductService().backStock(stockProductIn, InOutType.IN);

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
	 * @see com.huayin.printmanager.service.stock.StockProductTransferService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductTransfer lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductTransfer.class);
		query.eq("id", id);
		StockProductTransfer order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductTransfer.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductTransferDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductTransferDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductTransferDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
