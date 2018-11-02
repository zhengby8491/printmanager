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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockProductInService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Service
public class StockProductInServiceImpl extends BaseServiceImpl implements StockProductInService
{

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<StockProductIn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductIn.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, StockProductIn.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInService#findDetailByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<StockProductInDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInDetail.class, "ad");
		query.createAlias(StockProductIn.class, "a");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("ad, a, p"));
		query.eqProperty("ad.productId", "p.id");
		query.eqProperty("ad.masterId", "a.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.inTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.inTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("ad.customerName", "%" + queryParam.getCustomerName() + "%");
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
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("ad.sourceBillNo", "%" + queryParam.getWorkBillNo() + "%");
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
		SearchResult<StockProductInDetail> result = new SearchResult<StockProductInDetail>();
		result.setResult(new ArrayList<StockProductInDetail>());
		for (Object[] c : temp_result.getResult())
		{
			StockProductInDetail detail = (StockProductInDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((StockProductIn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#save(com.huayin.printmanager.persist.entity.stock.
	 * StockProductIn)
	 */
	@Override
	@Transactional
	public Long save(StockProductIn stockProductIn)
	{
		BoolValue flag = stockProductIn.getIsCheck();				// 标识是否保存并审核
		stockProductIn.setIsCheck(BoolValue.NO);						// 默认未审核
		stockProductIn.setBillType(BillType.STOCK_SPIN);
		stockProductIn.setBillNo(UserUtils.createBillNo(BillType.STOCK_SPIN));
		stockProductIn.setCompanyId(UserUtils.getCompanyId());
		stockProductIn.setCreateTime(new Date());
		
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			stockProductIn.setCreateName(e.getName());
		}
		else
		{
			stockProductIn.setCreateName(UserUtils.getUserName());
		}
		stockProductIn.setCreateEmployeeId(UserUtils.getEmployeeId());
		if (stockProductIn.getEmployeeId() != null)
		{
			stockProductIn.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductIn.getEmployeeId())).getName());
		}
		daoFactory.getCommonDao().saveEntity(stockProductIn);
		for (StockProductInDetail stockProductInDetail : stockProductIn.getDetailList())
		{
			stockProductInDetail.setMasterId(stockProductIn.getId());
			stockProductInDetail.setCompanyId(UserUtils.getCompanyId());
			if (stockProductInDetail.getSourceDetailId() != null)
			{
				WorkProduct workProduct = daoFactory.getCommonDao().getEntity(WorkProduct.class, stockProductInDetail.getSourceDetailId());
				workProduct.setInStockQty(workProduct.getInStockQty() + stockProductInDetail.getQty());
			}
			if (stockProductInDetail.getPrice() == null || "".equals(stockProductInDetail.getPrice()))
			{
				stockProductInDetail.setPrice(new BigDecimal(0));
			}
			if (stockProductInDetail.getMoney() == null || "".equals(stockProductInDetail.getMoney()))
			{
				stockProductInDetail.setMoney(new BigDecimal(0));
			}
			if (stockProductInDetail.getProductId() != null)
			{
				stockProductInDetail.setCode(((Product) UserUtils.getBasicInfo("PRODUCT", stockProductInDetail.getProductId())).getCode());
			}
			daoFactory.getCommonDao().saveEntity(stockProductInDetail);

		}
		if (flag == BoolValue.YES)
		{
			this.check(stockProductIn.getId());
		}
		return stockProductIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.stock.StockProductInService#update(com.huayin.printmanager.persist.entity.stock.
	 * StockProductIn)
	 */
	@Override
	@Transactional
	public Long update(StockProductIn stockProductIn)
	{
		BoolValue flag = stockProductIn.getIsCheck();	// 标识是否保存并审核
		stockProductIn.setIsCheck(BoolValue.NO); 			// 默认为审核
		StockProductIn stockProductIn_ = this.lockHasChildren(stockProductIn.getId());
		// 判断是否已审核
		if (stockProductIn_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, StockProductInDetail> old_detail_map = ConverterUtils.list2Map(stockProductIn_.getDetailList(), "id");
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (StockProductInDetail newItem : stockProductIn.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (StockProductInDetail oldItem : stockProductIn_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(StockProductInDetail.class, id);
				StockProductInDetail old_detail = old_detail_map.get(id);
				if (old_detail.getSourceDetailId() != null)
				{
					WorkProduct workProduct = daoFactory.getCommonDao().getEntity(WorkProduct.class, old_detail.getSourceDetailId());
					workProduct.setInStockQty(workProduct.getInStockQty() - old_detail.getQty());
				}
			}
		}
		if (stockProductIn.getEmployeeId() != null)
		{
			stockProductIn.setEmployeeName(((Employee) UserUtils.getBasicInfo("EMPLOYEE", stockProductIn.getEmployeeId())).getName());
		}
		PropertyClone.copyProperties(stockProductIn_, stockProductIn, false, new String[] { "detailList" }, new String[] { "memo" });// 替换成新内容
		for (StockProductInDetail stockProductInDetail : stockProductIn.getDetailList())
		{
			StockProductInDetail stockProductInDetail_ = this.getDetail(stockProductInDetail.getId());

			if (stockProductInDetail.getSourceDetailId() != null)
			{
				WorkProduct workProduct = daoFactory.getCommonDao().getEntity(WorkProduct.class, stockProductInDetail.getSourceDetailId());
				workProduct.setInStockQty(workProduct.getInStockQty() - stockProductInDetail_.getQty() + stockProductInDetail.getQty());
			}
			if (stockProductInDetail.getPrice() == null || "".equals(stockProductInDetail.getPrice()))
			{
				stockProductInDetail.setPrice(new BigDecimal(0));
			}
			if (stockProductInDetail.getMoney() == null || "".equals(stockProductInDetail.getMoney()))
			{
				stockProductInDetail.setMoney(new BigDecimal(0));
			}
			PropertyClone.copyProperties(stockProductInDetail_, stockProductInDetail, false, null, new String[] { "memo" });
		}
		if (flag == BoolValue.YES)
		{
			this.check(stockProductIn.getId());
		}
		return stockProductIn.getId();
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#get(java.lang.Long)
	 */
	@Override
	public StockProductIn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductIn.class);
		query.eq("id", id);
		StockProductIn stockProductIn = daoFactory.getCommonDao().getByDynamicQuery(query, StockProductIn.class);

		DynamicQuery queryD = new CompanyDynamicQuery(StockProductInDetail.class, "sp");
		queryD.createAlias(Product.class, "p");
		queryD.addProjection(Projections.property("sp, p"));
		queryD.eq("masterId", id);
		queryD.eqProperty("p.id", "sp.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryD, Object[].class);
		List<StockProductInDetail> stockProductInDetailList = new ArrayList<>();
		for (Object[] c : temp_result.getResult())
		{
			StockProductInDetail detail = (StockProductInDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			stockProductInDetailList.add(detail);
		}
		// List<StockProductInDetail> stockProductInDetailList = daoFactory.getCommonDao().findEntityByDynamicQuery(queryD,
		// StockProductInDetail.class);

		if (stockProductIn != null)
		{
			stockProductIn.setDetailList(stockProductInDetailList);
		}
		return stockProductIn;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#getDetail(java.lang.Long)
	 */
	@Override
	public StockProductInDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInDetail.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, StockProductInDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			StockProductIn stockProductIn = this.lockHasChildren(id);
			for (StockProductInDetail stockProductInDetail : stockProductIn.getDetailList())
			{
				if (stockProductInDetail.getSourceDetailId() != null)
				{
					WorkProduct workProduct = daoFactory.getCommonDao().getEntity(WorkProduct.class, stockProductInDetail.getSourceDetailId());
					workProduct.setInStockQty(workProduct.getInStockQty() - stockProductInDetail.getQty());
				}
			}
			daoFactory.getCommonDao().deleteAllEntity(stockProductIn.getDetailList());
			daoFactory.getCommonDao().deleteEntity(stockProductIn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#check(java.lang.Long)
	 */
	@Override
	@Transactional
	public Boolean check(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPIN, id, BoolValue.YES))
		{
			return false;
		}
		StockProductIn stockProductIn = this.get(id);
		for (StockProductInDetail stockProductInDetail : stockProductIn.getDetailList())
		{
			// 库存操作
			StockProduct stockProduct = new StockProduct();
			stockProduct.setProductId(stockProductInDetail.getProductId());
			stockProduct.setQty(stockProductInDetail.getQty());
			stockProduct.setWarehouseId(stockProductIn.getWarehouseId());
			stockProduct.setPrice(stockProductInDetail.getPrice());
			stockProduct.setMoney(stockProductInDetail.getMoney());
			stockProduct.setUpdateTime(new Date());
			serviceFactory.getStockProductService().stock(stockProduct,InOutType.IN);

			// 入库记录
			StockProductLog log = new StockProductLog();
			Product product = daoFactory.getCommonDao().getEntity(Product.class, stockProductInDetail.getProductId());
			log.setBillId(stockProductIn.getId());
			log.setBillType(stockProductIn.getBillType());
			log.setBillNo(stockProductIn.getBillNo());
			log.setSourceId(stockProductInDetail.getId());
			log.setCreateTime(new Date());
			log.setCompanyId(UserUtils.getCompanyId());
			log.setCustomerMaterialCode(product.getCustomerMaterialCode());
			log.setCode(product.getCode());
			log.setProductClassId(product.getProductClassId());
			log.setProductName(product.getName());
			log.setProductId(product.getId());
			log.setSpecifications(product.getSpecifications());
			log.setWarehouseId(stockProductIn.getWarehouseId());
			log.setUnitId(product.getUnitId());
			log.setPrice(stockProductInDetail.getPrice());
			log.setInQty(stockProductInDetail.getQty());
			log.setInMoney(stockProductInDetail.getMoney());
			log.setCustomerName(stockProductInDetail.getCustomerName());
			log.setCustomerId(stockProductInDetail.getCustomerId());
			Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
			log.setStorgeQty(storgeQty);
			daoFactory.getCommonDao().saveEntity(log);
		}

		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#checkBack(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		StockProductIn master = this.get(id);
		// 先判断是否已经反审核
		if (master.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (StockProductInDetail stockProductInDetail : master.getDetailList())
		{// 将库存不足的库存对象存List
			DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
			query.eq("productId", stockProductInDetail.getProductId());
			query.eq("warehouseId", master.getWarehouseId());
			StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
			if (stockProduct == null)
			{
				stockProduct = new StockProduct();
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProductInDetail.getProductId()));
				stockProduct.setQty(0);
			}
			else
			{
				stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
			}

			if (stockProductInDetail.getQty() > stockProduct.getQty())
			{
				list.add(stockProduct);
			}
		}

		// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
		if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
		{
			list.clear();

			if (!serviceFactory.getCommonService().audit(BillType.STOCK_SPIN, id, BoolValue.NO))
			{
				return null;
			}
			// 库存操作
			for (StockProductInDetail stockProductOutDetail : master.getDetailList())
			{
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(stockProductOutDetail.getProductId());
				stockProduct.setQty(stockProductOutDetail.getQty());
				stockProduct.setWarehouseId(master.getWarehouseId());
				stockProduct.setPrice(stockProductOutDetail.getPrice());
				stockProduct.setMoney(stockProductOutDetail.getMoney());
				// TODO 这里应该改为 backStock?
				serviceFactory.getStockProductService().backStock(stockProduct, InOutType.IN);

				DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
				query.eq("billId", id);
				List<StockMaterialLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockMaterialLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}

		}

		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#transmit(java.lang.Long[])
	 */
	@Override
	public List<StockProductInDetail> transmit(Long[] ids)
	{
		List<StockProductInDetail> detailList = new ArrayList<StockProductInDetail>();
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class);
		query.in("id", Arrays.asList(ids));
		List<WorkProduct> workProductlList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkProduct.class);
		for (WorkProduct workProduct : workProductlList)
		{
			if (workProduct.getProduceQty() <= workProduct.getInStockQty())
			{
				continue;
			}
			Work work = daoFactory.getCommonDao().getEntity(Work.class, workProduct.getMasterId());
			StockProductInDetail stockProductInDetail = new StockProductInDetail();
			stockProductInDetail.setSourceBillNo(work.getBillNo());
			stockProductInDetail.setSourceId(work.getId());
			stockProductInDetail.setSourceDetailId(workProduct.getId());
			stockProductInDetail.setSourceBillType(work.getBillType());
			stockProductInDetail.setQty(workProduct.getProduceQty() - workProduct.getInStockQty());
			stockProductInDetail.setSourceQty(workProduct.getProduceQty());
			stockProductInDetail.setPrice(workProduct.getPrice());
			stockProductInDetail.setProductId(workProduct.getProductId());
			stockProductInDetail.setProductName(workProduct.getProductName());
			stockProductInDetail.setSpecifications(workProduct.getStyle());
			stockProductInDetail.setUnitId(workProduct.getUnitId());
			stockProductInDetail.setSaleOrderBillNo(workProduct.getSourceBillNo());
			stockProductInDetail.setCustomerMaterialCode(workProduct.getCustomerMaterialCode());
			stockProductInDetail.setCustomerId(workProduct.getCustomerId());
			stockProductInDetail.setCustomerName(workProduct.getCustomerName());
			stockProductInDetail.setImgUrl(daoFactory.getCommonDao().getEntity(Product.class, workProduct.getProductId()).getImgUrl());
			stockProductInDetail.setProductClassId(daoFactory.getCommonDao().getEntity(Product.class, workProduct.getProductId()).getProductClassId());
			detailList.add(stockProductInDetail);
		}
		return detailList;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#findForTransmitProductIn(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<WorkProduct> findForTransmitProductIn(QueryParam queryParam)
	{
		try
		{
			SearchResult<WorkProduct> result = new SearchResult<WorkProduct>();
			DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "wp");
			query.createAlias(Work.class, "w");
			query.createAlias(Product.class, "p");
			query.addProjection(Projections.property("wp, w, p"));
			query.eqProperty("wp.masterId", "w.id");
			query.eqProperty("wp.productId", "p.id");
			query.add(Restrictions.gtProperty("wp.produceQty", "wp.inStockQty"));

			if (StringUtils.isNotBlank(queryParam.getProductStyle()))
			{
				query.like("wp.style", "%" + queryParam.getProductStyle() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
			{
				query.like("wp.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
			{
				query.like("wp.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
			{
				query.like("wp.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
			}

			if (queryParam.getDateMin() != null)
			{
				query.ge("w.createTime", queryParam.getDateMin());
			}
			if (queryParam.getDateMax() != null)
			{
				query.le("w.createTime", queryParam.getDateMax());
			}

			if (StringUtils.isNotBlank(queryParam.getBillNo()))
			{
				query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getCustomerName()))
			{
				query.like("wp.customerName", "%" + queryParam.getCustomerName() + "%");
			}
			if (StringUtils.isNotBlank(queryParam.getProductName()))
			{
				query.like("wp.productName", "%" + queryParam.getProductName() + "%");
			}
			if (queryParam.getCompleteFlag() == BoolValue.NO)
			{// 搜索非强制完工
				query.eq("wp.isForceComplete", queryParam.getCompleteFlag());
				query.eq("w.isForceComplete", queryParam.getCompleteFlag());
			}
			else
			{// 搜索已强制完工
				query.eq("wp.isForceComplete", BoolValue.YES);
			}
			query.eq("w.isCheck", BoolValue.YES);
			query.eq("w.isOutSource", BoolValue.NO);
			query.eq("wp.companyId", UserUtils.getCompanyId());
			query.desc("w.createTime");
			query.setPageIndex(queryParam.getPageNumber());
			query.setPageSize(queryParam.getPageSize());
			query.setIsSearchTotalCount(true);

			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
			result.setResult(new ArrayList<WorkProduct>());
			for (Object[] c : temp_result.getResult())
			{
				WorkProduct workProduct = (WorkProduct) c[0];
				workProduct.setMaster((Work) c[1]);
				Product product =(Product) c[2];
				workProduct.setImgUrl(product.getImgUrl());
				result.getResult().add(workProduct);
			}
			result.setCount(temp_result.getCount());
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
	 * @see com.huayin.printmanager.service.stock.StockProductInService#lockHasChildren(java.lang.Long)
	 */
	@Override
	public StockProductIn lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductIn.class);
		query.eq("id", id);
		StockProductIn order = daoFactory.getCommonDao().lockByDynamicQuery(query, StockProductIn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(StockProductInDetail.class);
		query_detail.eq("masterId", id);
		List<StockProductInDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, StockProductInDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.stock.StockProductInService#hasCheckAll(java.lang.String, java.lang.Long)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(StockProductInDetail.class, "d");
		query.createAlias(StockProductIn.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		query.addProjection(Projections.property("count(1)"));
		query.setQueryType(QueryType.JDBC);
		query.eq("d.saleOrderBillNo", saleOrderBillNo);
		query.eq("d.productId", productId);
		query.eq("m.isCheck", BoolValue.NO);
		SearchResult<HashMap> maps = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		Integer result = Integer.parseInt(maps.getResult().get(0).get("count(1)").toString());

		// 子查询
		DynamicQuery queryw = new DynamicQuery(WorkProduct.class, "w");
		queryw.createAlias(Work.class, JoinType.LEFTJOIN, "m2", "m2.id=w.masterId");
		queryw.addProjection(Projections.property("sourceBillNo,billNo"));
		queryw.setQueryType(QueryType.JDBC);

		DynamicQuery queryd = new CompanyDynamicQuery(OutSourceArriveDetail.class, "d");
		queryd.createAlias(OutSourceArrive.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		queryd.createAlias(queryw, JoinType.LEFTJOIN, "q", "q.billNo=d.workBillNo");
		queryd.addProjection(Projections.property("count(1)"));
		queryd.eq("q.sourceBillNo", saleOrderBillNo);
		queryd.eq("d.productId", productId);
		queryd.eq("m.isCheck", BoolValue.NO);
		queryd.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapw = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryd, HashMap.class);
		result = result + Integer.parseInt(mapw.getResult().get(0).get("count(1)").toString());

		return result == 0;
	}
}
