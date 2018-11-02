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
import com.huayin.common.util.BeanClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInventory;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOut;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductInventoryService;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品盘点
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductInventoryServiceImpl extends BaseServiceImpl implements StockProductInventoryService
{
	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInventoryService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductInventory> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInventory.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductInventory.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInventoryService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductInventoryDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInventoryDetail.class, "ad");
		query.createAlias(StockProductInventory.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a, p"));
		query.eqProperty("ad.masterId", "a.id");
		query.eqProperty("p.id", "ad.productId");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.inventoryTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.inventoryTime", queryParam.getDateMax());
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
		SearchResult<StockProductInventoryDetail> result = new SearchResult<StockProductInventoryDetail>();
		result.setResult(new ArrayList<StockProductInventoryDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductInventoryDetail detail = (StockProductInventoryDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductInventory) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInventoryService#save(com.huayin.printmanager.persist.entity.
	 * stock.StockProductInventory)
	 */
	@Override
	@Transactional
	public Long save(StockProductInventory stockProductInventory)
	{
		BoolValue flag = stockProductInventory.getIsCheck();		// 标识是否保存并审核
		stockProductInventory.setIsCheck(BoolValue.NO);					// 默认未审核
		stockProductInventory.setBillType(BillType.STOCK_SPI);
		stockProductInventory.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPI));
		stockProductInventory.setCompanyId(UserUtils.getCompanyId());
		stockProductInventory.setCreateTime(new Date());
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductInventory.setCreateName(e.getName());
		}
		else
		{
			stockProductInventory.setCreateName(UserUtils.getUserName());
		}
		stockProductInventory.setCreateEmployeeId(UserUtils.getEmployeeId());
		daoFactory.getCommonDao().saveEntity(stockProductInventory);
		for (StockProductInventoryDetail stockProductInventoryDetail : stockProductInventory.getDetailList())
		{
			if ("".equals(stockProductInventoryDetail.getCode()) || stockProductInventoryDetail.getCode() == null)
			{
				continue;
			}
			stockProductInventoryDetail.setMasterId(stockProductInventory.getId());
			stockProductInventoryDetail.setCompanyId(UserUtils.getCompanyId());

			daoFactory.getCommonDao().saveEntity(stockProductInventoryDetail);
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockProductInventory.getId());
		}
		return stockProductInventory.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInventoryService#update(com.huayin.printmanager.persist.entity.
	 * stock.StockProductInventory)
	 */
	@Override
	@Transactional
	public Long update(StockProductInventory stockProductInventory)
	{
		StockProductInventory stockProductInventory_ = this.lockHasChildren(stockProductInventory.getId());
		// 判断是否已审核
		if (stockProductInventory_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductInventoryDetail newItem : stockProductInventory.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductInventoryDetail oldItem : stockProductInventory_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductInventoryDetail.class, id);
			}
		}

		stockProductInventory_.setWarehouseId(stockProductInventory.getWarehouseId());
		stockProductInventory_.setInventoryTime(stockProductInventory.getInventoryTime());
		stockProductInventory_.setEmployeeId(stockProductInventory.getEmployeeId());
		stockProductInventory_.setEmployeeName(stockProductInventory.getEmployeeName());
		stockProductInventory_.setMemo(stockProductInventory.getMemo());
		daoFactory.getCommonDao().updateEntity(stockProductInventory_);
		for (StockProductInventoryDetail stockProductInventoryDetail : stockProductInventory.getDetailList())
		{
			if ("".equals(stockProductInventoryDetail.getCode()) || stockProductInventoryDetail.getCode() == null)
			{
				continue;
			}
			if (stockProductInventoryDetail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(StockProductInventoryDetail.class);
				querySt.eq("id", stockProductInventoryDetail.getId());
				StockProductInventoryDetail stockProductInventoryDetail_ = daoFactory.getCommonDao().getByDynamicQuery(querySt, StockProductInventoryDetail.class);
				stockProductInventoryDetail_.setCode(stockProductInventoryDetail.getCode());
				stockProductInventoryDetail_.setProductName(stockProductInventoryDetail.getProductName());
				stockProductInventoryDetail_.setSpecifications(stockProductInventoryDetail.getSpecifications());
				stockProductInventoryDetail_.setUnitId(stockProductInventoryDetail.getUnitId());
				stockProductInventoryDetail_.setQty(stockProductInventoryDetail.getQty());
				stockProductInventoryDetail_.setPrice(stockProductInventoryDetail.getPrice());
				stockProductInventoryDetail_.setMoney(stockProductInventoryDetail.getMoney());
				stockProductInventoryDetail_.setProductClassId(stockProductInventoryDetail.getProductClassId());
				stockProductInventoryDetail_.setMemo(stockProductInventoryDetail.getMemo());
				stockProductInventoryDetail_.setProfitAndLossMoney(stockProductInventoryDetail.getProfitAndLossMoney());
				stockProductInventoryDetail_.setProfitAndLossQty(stockProductInventoryDetail.getProfitAndLossQty());
				daoFactory.getCommonDao().updateEntity(stockProductInventoryDetail_);
			}
			else
			{
				stockProductInventoryDetail.setMasterId(stockProductInventory.getId());
				stockProductInventoryDetail.setCompanyId(UserUtils.getCompanyId());

				daoFactory.getCommonDao().saveEntity(stockProductInventoryDetail);
			}

		}
		if (stockProductInventory.getIsCheck() == BoolValue.YES)
		{
			this.check(stockProductInventory.getId());
		}
		return stockProductInventory_.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#get(java.lang.Long)
	 */
	@Override
	public StockProductInventory get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInventory.class);
		query.eq("id", id);
		StockProductInventory stockProductInventory = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductInventory.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductInventoryDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp,p"));
		queryD.eqProperty("p.id", "sp.productId");
		queryD.eq("masterId", id);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductInventoryDetail> stockProductInventoryDetailList = new ArrayList<>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductInventoryDetail detail = (StockProductInventoryDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductInventoryDetailList.add(detail);
		}
		// List<StockProductInventoryDetail> stockProductInventoryDetailList = daoFactory.getCommonDao()
		// .findEntityByDynamicQuery(queryD, StockProductInventoryDetail.class);
		if (stockProductInventory != null)
		{
			stockProductInventory.setDetailList(stockProductInventoryDetailList);
		}
		return stockProductInventory;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			StockProductInventory master = this.lockHasChildren(id);
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
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPI, id, BoolValue.YES))
		{
			return 0;
		}
		StockProductInventory master = this.get(id);
		for (StockProductInventoryDetail stockProductInventoryDetail : master.getDetailList())
		{
			
			StockProduct stockProduct_old = daoFactory.getCommonDao().getEntity(StockProduct.class, stockProductInventoryDetail.getStockProductId());
			// 出库记录
			StockProductLog log = new StockProductLog();
			Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductInventoryDetail.getProductId());
			log.setBillId(master.getId());
			log.setBillType(master.getBillType());
			log.setBillNo(master.getBillNo());
			log.setSourceId(stockProductInventoryDetail.getId());
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
			log.setPrice(stockProductInventoryDetail.getPrice());
			if (stockProductInventoryDetail.getQty() > stockProduct_old.getQty())
			{
				log.setInQty(stockProductInventoryDetail.getQty() - stockProduct_old.getQty());
				log.setInMoney(stockProductInventoryDetail.getPrice().multiply(new BigDecimal(log.getInQty())));
			}
			else
			{
				log.setOutQty(stockProduct_old.getQty() - stockProductInventoryDetail.getQty());
				log.setOutMoney(stockProductInventoryDetail.getPrice().multiply(new BigDecimal(log.getOutQty())));
			}
			
			log.setStorgeQty(stockProductInventoryDetail.getQty());
			daoFactory.getCommonDao().saveEntity(log);
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setId(stockProductInventoryDetail.getStockProductId());
			stockProduct.setQty(stockProductInventoryDetail.getQty());
			stockProduct.setMoney(stockProductInventoryDetail.getMoney());
			stockProduct.setUpdateTime(new Date());
			serviceFactory.getStockProductService().adjust(stockProduct);
		}
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#checkBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public Integer checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPI, id, BoolValue.NO))
		{
			return 0;
		}
		DynamicQuery queryd = new CompanyDynamicQuery(StockProductInventoryDetail.class);
		queryd.eq("masterId", id);
		List<StockProductInventoryDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryd, StockProductInventoryDetail.class);
		for (StockProductInventoryDetail stockProductInventoryDetail : detailList)
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setId(stockProductInventoryDetail.getStockProductId());
			stockProduct.setQty(0 - stockProductInventoryDetail.getQty());
			stockProduct.setMoney(stockProductInventoryDetail.getMoney());
			stockProduct.setUpdateTime(new Date());
			serviceFactory.getStockProductService().adjust(stockProduct);

			DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
			query.eq("billId", id);
			List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
			daoFactory.getCommonDao().deleteAllEntity(logs);
		}
		return 1;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#findNotCheckStockProduct()
	 */
	@Override
	public List<NotCheckStockVo> findNotCheckStockProduct()
	{
		try
		{
			List<NotCheckStockVo> result = new ArrayList<NotCheckStockVo>();
			// 查成品其他入库未审核记录并记录在Vo列表
			DynamicQuery querySPOI = new CompanyDynamicQuery(StockProductOtherIn.class);
			querySPOI.eq("isCheck", BoolValue.NO);
			querySPOI.eq("companyId", UserUtils.getCompanyId());
			List<StockProductOtherIn> SPOIList = daoFactory.getCommonDao().findEntityByDynamicQuery(querySPOI, StockProductOtherIn.class);
			for (StockProductOtherIn detail : SPOIList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				BeanClone.copy(detail, vo);
				result.add(vo);
			}
			// 查成品其他出库未审核记录并记录在Vo列表
			DynamicQuery querySPOO = new CompanyDynamicQuery(StockProductOtherOut.class);
			querySPOO.eq("isCheck", BoolValue.NO);
			querySPOO.eq("companyId", UserUtils.getCompanyId());
			List<StockProductOtherOut> SPOOList = daoFactory.getCommonDao().findEntityByDynamicQuery(querySPOO, StockProductOtherOut.class);
			for (StockProductOtherOut detail : SPOOList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				BeanClone.copy(detail, vo);
				result.add(vo);
			}

			// 查成品入库未审核记录并记录在Vo列表
			DynamicQuery querySPI = new CompanyDynamicQuery(StockProductIn.class);
			querySPI.eq("isCheck", BoolValue.NO);
			querySPI.eq("companyId", UserUtils.getCompanyId());
			List<StockProductIn> SPIList = daoFactory.getCommonDao().findEntityByDynamicQuery(querySPI, StockProductIn.class);
			for (StockProductIn detail : SPIList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				BeanClone.copy(detail, vo);
				result.add(vo);
			}

			// 查销售送货未审核记录并记录在Vo列表
			DynamicQuery queryIV = new CompanyDynamicQuery(SaleDeliver.class);
			queryIV.eq("isCheck", BoolValue.NO);
			queryIV.eq("companyId", UserUtils.getCompanyId());
			List<SaleDeliver> IVList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryIV, SaleDeliver.class);
			for (SaleDeliver detail : IVList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				BeanClone.copy(detail, vo);
				result.add(vo);
			}

			// 查销售退货货未审核记录并记录在Vo列表
			DynamicQuery queryIR = new CompanyDynamicQuery(SaleReturn.class);
			queryIR.eq("isCheck", BoolValue.NO);
			queryIR.eq("companyId", UserUtils.getCompanyId());
			List<SaleReturn> IRList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryIR, SaleReturn.class);
			for (SaleReturn detail : IRList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				BeanClone.copy(detail, vo);
				result.add(vo);
			}
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInventoryService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductInventory lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInventory.class);
		query.eq("id", id);
		StockProductInventory order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductInventory.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductInventoryDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductInventoryDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductInventoryDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
