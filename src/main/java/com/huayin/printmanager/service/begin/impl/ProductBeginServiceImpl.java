/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.begin.ProductBeginService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Service
public class ProductBeginServiceImpl extends BaseServiceImpl implements ProductBeginService
{
	@Override
	public ProductBegin get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductBegin.class);
		query.eq("id", id);
		ProductBegin productBegin = daoFactory.getCommonDao().getByDynamicQuery(query, ProductBegin.class);

		if (productBegin != null)
		{
			productBegin.setDetailList(this.getDetail(id));
		}
		return productBegin;
	}

	@Override
	public List<ProductBeginDetail> getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductBeginDetail.class);
		query.eq("masterId", id);
		List<ProductBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, ProductBeginDetail.class);
		return detailList;

	}

	@Override
	@Transactional
	public Long save(ProductBegin productBegin)
	{
		BoolValue flag = productBegin.getIsCheck();					// 标识是否保存并审核
		productBegin.setIsCheck(BoolValue.NO);							// 默认未审核
		productBegin.setBillType(BillType.BEGIN_PRODUCT); 
		productBegin.setBillNo(UserUtils.createBillNo(BillType.BEGIN_PRODUCT));
		productBegin.setCompanyId(UserUtils.getCompanyId());
		productBegin.setCreateTime(new Date());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			productBegin.setCreateName(e.getName());
		}
		else
		{
			productBegin.setCreateName(UserUtils.getUserName());
		}
		productBegin.setCreateEmployeeId(UserUtils.getEmployeeId());
		productBegin.setMemo(productBegin.getMemo());
		daoFactory.getCommonDao().saveEntity(productBegin);
		daoFactory.getCommonDao().getEntity(Warehouse.class, productBegin.getWarehouseId()).setIsBegin(BoolValue.YES);
		for (ProductBeginDetail detail : productBegin.getDetailList())
		{
			detail.setMasterId(productBegin.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMemo(detail.getMemo());
			daoFactory.getCommonDao().saveEntity(detail);
		}
		if (flag == BoolValue.YES)
		{
			this.audit(productBegin.getId());
		}
		return productBegin.getId();

	}

	@Override
	@Transactional
	public Long update(ProductBegin productBegin)
	{
		ProductBegin productBegin_ = this.lockHasChildren(productBegin.getId());
		// 先判断是否已经审核
		if (productBegin_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (ProductBeginDetail newItem : productBegin.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (ProductBeginDetail oldItem : productBegin_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(ProductBeginDetail.class, id);
			}
		}

		productBegin_.setBeginTime(productBegin.getBeginTime());
		daoFactory.getCommonDao().getEntity(Warehouse.class, productBegin_.getWarehouseId()).setIsBegin(BoolValue.NO);
		daoFactory.getCommonDao().getEntity(Warehouse.class, productBegin.getWarehouseId()).setIsBegin(BoolValue.YES);
		productBegin_.setWarehouseId(productBegin.getWarehouseId());
		productBegin_.setMemo(productBegin.getMemo());
		for (ProductBeginDetail detail : productBegin.getDetailList())
		{
			if (detail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(ProductBeginDetail.class);
				querySt.eq("id", detail.getId());
				ProductBeginDetail productBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, ProductBeginDetail.class);
				productBeginDetail.setProductId(detail.getProductId());
				productBeginDetail.setProductCode(detail.getProductCode());
				productBeginDetail.setProductName(detail.getProductName());
				productBeginDetail.setSpecifications(detail.getSpecifications());
				productBeginDetail.setQty(detail.getQty());
				productBeginDetail.setPrice(detail.getPrice());
				productBeginDetail.setMoney(detail.getMoney());
				productBeginDetail.setMemo(detail.getMemo());
				daoFactory.getCommonDao().updateEntity(productBeginDetail);
			}
			else
			{
				detail.setMasterId(productBegin.getId());
				detail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(detail);
			}
		}
		if (productBegin.getIsCheck() == BoolValue.YES)
		{
			this.audit(productBegin.getId());
		}
		return productBegin.getId();

	}

	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			ProductBegin master = this.lockHasChildren(id);
			daoFactory.getCommonDao().getEntity(Warehouse.class, master.getWarehouseId()).setIsBegin(BoolValue.NO);
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

	@Override
	@Transactional
	public Integer audit(Long id)
	{
		ProductBegin order = this.get(id);
		// 先判断是否已经审核
		if (order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (ProductBeginDetail detail : order.getDetailList())
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setProductId(detail.getProductId());
			stockProduct.setQty(detail.getQty());
			stockProduct.setPrice(detail.getPrice());
			stockProduct.setMoney(detail.getMoney());
			stockProduct.setUpdateTime(new Date());
			stockProduct.setWarehouseId(order.getWarehouseId());
			serviceFactory.getStockProductService().stock(stockProduct, InOutType.IN);

			// 入库记录
			StockProductLog log = new StockProductLog();
			Product product = daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId());
			log.setBillId(order.getId());
			log.setBillType(order.getBillType());
			log.setBillNo(order.getBillNo());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCode(detail.getProductCode());
			log.setProductClassId(((Product) daoFactory.getCommonDao().getEntity(Product.class, detail.getProductId())).getProductClassId());
			log.setProductName(detail.getProductName());
			log.setProductId(detail.getProductId());
			log.setSpecifications(detail.getSpecifications());
			log.setWarehouseId(order.getWarehouseId());
			log.setUnitId(detail.getUnit());
			log.setPrice(detail.getPrice());
			log.setInQty(detail.getQty());
			log.setInMoney(detail.getMoney());
			log.setCustomerMaterialCode(product.getCustomerMaterialCode());
			Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
			log.setStorgeQty(storgeQty);
			daoFactory.getCommonDao().saveEntity(log);
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_PRODUCT, id, BoolValue.YES);
		return 1;
	}
	
	@Override
	@Transactional
	public Integer auditCancel(Long id)
	{
		ProductBegin order = this.get(id);
		// 先判断是否已经反审核
		if (order.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		List<StockProductLog> delList = Lists.newArrayList();
		for (ProductBeginDetail detail : order.getDetailList())
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setProductId(detail.getProductId());
			stockProduct.setQty(detail.getQty());
			stockProduct.setPrice(detail.getPrice());
			stockProduct.setMoney(detail.getMoney());
			stockProduct.setUpdateTime(new Date());
			stockProduct.setWarehouseId(order.getWarehouseId());
			serviceFactory.getStockProductService().backStock(stockProduct, InOutType.IN);

			// 查询入库记录
			DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
			query.eq("billNo", order.getBillNo());
			query.eq("productId", detail.getProductId());
			query.eq("warehouseId", order.getWarehouseId());
			StockProductLog log = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductLog.class);
			delList.add(log);
		}
		daoFactory.getCommonDao().deleteAllEntity(delList);// 删除入库记录
		serviceFactory.getCommonService().audit(BillType.BEGIN_PRODUCT, id, BoolValue.NO);
		return 1;
	}
	@Override
	public SearchResult<ProductBegin> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductBegin.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, ProductBegin.class);
	}

	@Override
	public ProductBegin lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(ProductBegin.class);
		query.eq("id", id);
		ProductBegin order = daoFactory.getCommonDao().lockByDynamicQuery(query, ProductBegin.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(ProductBeginDetail.class);
		query_detail.eq("masterId", id);
		List<ProductBeginDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, ProductBeginDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

}
