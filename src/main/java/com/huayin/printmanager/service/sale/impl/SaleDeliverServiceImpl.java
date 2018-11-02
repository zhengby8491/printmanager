/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午18:00:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
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
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sale.SaleDeliverService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售送货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月22日下午6:00:54, zhengby, 代码规范
 */
@Service
public class SaleDeliverServiceImpl extends BaseServiceImpl implements SaleDeliverService
{
	@Override
	public SaleDeliver get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliver.class);
		query.eq("id", id);
		SaleDeliver order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleDeliver.class);

		// SaleDeliver order = daoFactory.getCommonDao().getEntity(SaleDeliver.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public SaleDeliverDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class);
		query.eq("id", id);
		SaleDeliverDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, SaleDeliverDetail.class);

		// SaleDeliverDetail detail = daoFactory.getCommonDao().getEntity(SaleDeliverDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(SaleDeliver.class, detail.getMasterId()));
		DynamicQuery imgQuery = new CompanyDynamicQuery(Product.class);
		imgQuery.eq("id", detail.getProductId());
		Product product = daoFactory.getCommonDao().getByDynamicQuery(imgQuery, Product.class);
		detail.setImgUrl(product.getImgUrl());
		return detail;
	}

	@Override
	public SaleDeliver lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliver.class);
		query.eq("id", id);
		SaleDeliver order = daoFactory.getCommonDao().lockByDynamicQuery(query, SaleDeliver.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(SaleDeliverDetail.class);
		query_detail.eq("masterId", id);
		List<SaleDeliverDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, SaleDeliverDetail.class, LockType.LOCK_WAIT);

		/*
		 * SaleDeliver order = daoFactory.getCommonDao().lockObject(SaleDeliver.class, id); List<SaleDeliverDetail>
		 * detailList = this.getDetailList(id); for (SaleDeliverDetail detail : detailList) { detail =
		 * daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public List<SaleDeliverDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "s");
		query.addProjection(Projections.property("s,p"));
		query.createAlias(Product.class, "p");
		query.eq("masterId", id);
		query.eqProperty("p.id", "s.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<SaleDeliverDetail> detailList = new ArrayList<SaleDeliverDetail>();

		for (Object[] c : temp_result.getResult())
		{
			SaleDeliverDetail detail = (SaleDeliverDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			detailList.add(detail);
		}
		return detailList;
	}

	@Override
	@Transactional
	public ServiceResult<SaleDeliver> save(SaleDeliver order)
	{
		// 是否要审核标识
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.SALE_IV);
		order.setBillNo(UserUtils.createBillNo(BillType.SALE_IV));
		order.setUserNo(UserUtils.getUser().getUserNo());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			order.setCreateName(e.getName());
		}
		else
		{
			order.setCreateName(UserUtils.getUserName());
		}
		order.setCreateTime(new Date());
		order.setCreateEmployeeId(UserUtils.getEmployeeId());
		order.setIsCheck(BoolValue.NO);
		order.setIsForceComplete(BoolValue.NO);
		order = daoFactory.getCommonDao().saveEntity(order);
		for (SaleDeliverDetail detail : order.getDetailList())
		{
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMasterId(order.getId());
			detail.setReconcilQty(0);
			detail.setReconcilMoney(new BigDecimal(0));
			detail.setReturnQty(0);
			detail.setReturnMoney(new BigDecimal(0));
			detail.setIsForceComplete(BoolValue.NO);
			detail.setSourceBillType(detail.getSourceBillType());
			detail.setSourceBillNo(detail.getSourceBillNo());
			detail.setSaleOrderBillNo(detail.getSourceBillNo());// 销售订单单据编号

			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setPercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			// 反写销售订单送货数量

			if (BillType.SALE_SO.equals(detail.getSourceBillType()))
			{
				SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, detail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty() + detail.getQty());
				source.setDeliverMoney(source.getDeliverMoney().add(detail.getMoney()));
				source.setDeliverSpareedQty(source.getDeliverSpareedQty() + detail.getSpareQty());
				daoFactory.getCommonDao().updateEntity(source);
			}
			else
			{
				WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, detail.getSourceDetailId());

				source.setDeliverQty(source.getDeliverQty() == null ? 0 : source.getDeliverQty() + detail.getQty());
				if (source.getDeliverMoney() == null)
				{
					source.setDeliverMoney(new BigDecimal(0));
				}
				source.setDeliverMoney(source.getDeliverMoney().add(detail.getMoney()));
				source.setDeliverSpareedQty(source.getDeliverSpareedQty() == null ? 0 : source.getDeliverSpareedQty() + detail.getSpareQty());
				daoFactory.getCommonDao().updateEntity(source);
			}

		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		// 保存并审核按钮执行审核
		ServiceResult<SaleDeliver> serviceResult = new ServiceResult<SaleDeliver>();
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(order.getId(), order.getForceCheck()));
		}
		serviceResult.setReturnValue(order);
		return serviceResult;
	}

	@Override
	@Transactional
	public ServiceResult<SaleDeliver> update(SaleDeliver order)
	{
		// 是否要审核标识
		BoolValue flag = order.getIsCheck();

		ServiceResult<SaleDeliver> serviceResult = new ServiceResult<SaleDeliver>();

		// 再更新数据
		order.setIsCheck(null);
		SaleDeliver old_order = serviceFactory.getSaleDeliverService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, SaleDeliverDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// 要删除的数据
		List<SaleDeliverDetail> del_detail = new ArrayList<SaleDeliverDetail>();

		for (SaleDeliverDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (SaleDeliverDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (SaleDeliverDetail new_detail : order.getDetailList())
		{
			SaleDeliverDetail old_detail = old_detail_map.get(new_detail.getId());
			if (new_detail.getId() == null)// 新增
			{
				new_detail.setCompanyId(UserUtils.getCompanyId());
				new_detail.setMasterId(order.getId());
				new_detail.setReconcilQty(0);
				new_detail.setReturnQty(0);
				new_detail.setIsForceComplete(BoolValue.NO);
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), new_detail.getTaxRateId());
				new_detail.setPercent(taxRate.getPercent());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());
				// 反写销售订单送货数量
				SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, new_detail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty() + new_detail.getQty());
				source.setDeliverMoney(source.getDeliverMoney().add(new_detail.getMoney()));
				source.setDeliverSpareedQty(source.getDeliverSpareedQty() + new_detail.getSpareQty());
				daoFactory.getCommonDao().saveEntity(new_detail);
			}
			else
			// 更新
			{
				// 根据源单类型判断
				if (BillType.SALE_SO == old_detail.getSourceBillType())
				{
					SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, old_detail.getSourceDetailId());
					source.setDeliverQty(source.getDeliverQty() - (old_detail.getQty() - new_detail.getQty()));
					source.setDeliverMoney(source.getDeliverMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
					source.setDeliverSpareedQty(source.getDeliverSpareedQty() - (old_detail.getSpareQty() - new_detail.getSpareQty()));
					daoFactory.getCommonDao().updateEntity(source);
				}
				else
				{
					WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, old_detail.getSourceDetailId());
					source.setDeliverQty(source.getDeliverQty() - (old_detail.getQty() - new_detail.getQty()));
					source.setDeliverMoney(source.getDeliverMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
					source.setDeliverSpareedQty(source.getDeliverSpareedQty() - (old_detail.getSpareQty() - new_detail.getSpareQty()));
					daoFactory.getCommonDao().updateEntity(source);
				}

				PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo" });// 替换成新内容
				daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
			}

		}
		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))
			{
				SaleDeliverDetail saleDeliverDetail = old_detail_map.get(id);
				if (saleDeliverDetail.getSourceBillType() == BillType.SALE_SO)
				{
					SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, saleDeliverDetail.getSourceDetailId());
					source.setDeliverQty(source.getDeliverQty() - saleDeliverDetail.getQty());
					source.setDeliverMoney(source.getDeliverMoney().subtract(saleDeliverDetail.getMoney()));
					source.setDeliverSpareedQty(source.getDeliverSpareedQty() - saleDeliverDetail.getSpareQty());
					daoFactory.getCommonDao().updateEntity(source);
				} else
				{
					WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, saleDeliverDetail.getSourceDetailId());
					source.setDeliverQty(source.getDeliverQty() - saleDeliverDetail.getQty());
					source.setDeliverMoney(source.getDeliverMoney().subtract(saleDeliverDetail.getMoney()));
					source.setDeliverSpareedQty(source.getDeliverSpareedQty());
					daoFactory.getCommonDao().updateEntity(source);
				}
				del_detail.add(saleDeliverDetail);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(del_detail);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "deliveryAddress" });// 替换新内容
		order.setUpdateName(UserUtils.getUserName());
		order.setUpdateTime(new Date());
		// 必须先保存子表，然后再保存主表
		daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceResult.setReturnObject(this.check(order.getId(), order.getForceCheck()));
		}
		serviceResult.setReturnValue(order);
		return serviceResult;
	}

	@Override
	@Transactional
	public List<StockProduct> check(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();

		// 有库存模块操作
		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			SaleDeliver master = this.get(id);
			// 先判断是否已经审核
			if (master.getIsCheck() == BoolValue.YES)
			{
				throw new BusinessException("已审核");
			}
			
			for (SaleDeliverDetail saleDeliverDetail : master.getDetailList())
			{// 将库存不足的库存对象存List
				DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
				query.eq("productId", saleDeliverDetail.getProductId());
				query.eq("warehouseId", saleDeliverDetail.getWarehouseId());
				StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
				if (stockProduct == null)
				{
					stockProduct = new StockProduct();
					stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, saleDeliverDetail.getProductId()));
					stockProduct.setQty(0);
				}
				else
				{
					stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
				}

				if (saleDeliverDetail.getQty() + saleDeliverDetail.getSpareQty() > stockProduct.getQty())
				{
					list.add(stockProduct);
				}
			}

			// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
			if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
			{
				list.clear();

				if (!serviceFactory.getCommonService().audit(BillType.SALE_IV, id, BoolValue.YES))
				{
					return null;
				}

				// TODO 业务流程【工单-送货-对账-收款】
				// 没有【销售订单列表】，并且存在库存再进行 库存操作，否则不进行库存操作

				// 库存操作
				for (SaleDeliverDetail saleDeliverDetail : master.getDetailList())
				{
					StockProduct stockProduct = new StockProduct();
					stockProduct.setProductId(saleDeliverDetail.getProductId());
					stockProduct.setQty(saleDeliverDetail.getQty() + saleDeliverDetail.getSpareQty());
					stockProduct.setWarehouseId(saleDeliverDetail.getWarehouseId());
					stockProduct.setPrice(saleDeliverDetail.getPrice());
					stockProduct.setMoney(saleDeliverDetail.getMoney());
					serviceFactory.getStockProductService().stock(stockProduct, InOutType.OUT);

					// 出库记录
					StockProductLog log = new StockProductLog();
					log.setBillId(master.getId());
					log.setBillType(master.getBillType());
					log.setBillNo(master.getBillNo());
					log.setSourceId(saleDeliverDetail.getId());
					log.setCreateTime(new Date());
					log.setCompanyId(UserUtils.getCompanyId());
					log.setCode(saleDeliverDetail.getProductCode());
					log.setProductClassId(((Product) daoFactory.getCommonDao().getEntity(Product.class, saleDeliverDetail.getProductId())).getProductClassId());
					log.setProductName(saleDeliverDetail.getProductName());
					log.setProductId(saleDeliverDetail.getProductId());
					log.setCustomerMaterialCode(saleDeliverDetail.getCustomerMaterialCode());
					log.setSpecifications(saleDeliverDetail.getStyle());
					log.setWarehouseId(saleDeliverDetail.getWarehouseId());
					log.setUnitId(saleDeliverDetail.getUnitId());
					log.setPrice(saleDeliverDetail.getPrice());
					log.setOutQty(saleDeliverDetail.getQty() + saleDeliverDetail.getSpareQty());
					log.setOutMoney(saleDeliverDetail.getMoney());
					Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
					log.setStorgeQty(storgeQty);
					log.setCustomerId(master.getCustomerId());
					log.setCustomerName(master.getCustomerName());
					daoFactory.getCommonDao().saveEntity(log);
				}

			}
		}
		else
		{
			if (!serviceFactory.getCommonService().audit(BillType.SALE_IV, id, BoolValue.YES))
			{
				return null;
			}
		}

		return list;
	}

	@Override
	@Transactional
	public Boolean checkBack(Long id)
	{
		if (!serviceFactory.getCommonService().audit(BillType.SALE_IV, id, BoolValue.NO))
		{
			return false;
		}

		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			SaleDeliver saleDeliver = this.get(id);
			for (SaleDeliverDetail saleDeliverDetail : saleDeliver.getDetailList())
			{
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(saleDeliverDetail.getProductId());
				stockProduct.setQty(saleDeliverDetail.getQty() + saleDeliverDetail.getSpareQty());
				stockProduct.setWarehouseId(saleDeliverDetail.getWarehouseId());
				stockProduct.setPrice(saleDeliverDetail.getPrice());
				stockProduct.setMoney(saleDeliverDetail.getMoney());
				serviceFactory.getStockProductService().backStock(stockProduct, InOutType.OUT);

				DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
				query.eq("billId", id);
				List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
				daoFactory.getCommonDao().deleteAllEntity(logs);
			}
		}

		return true;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		SaleDeliver order = this.lock(id);
		List<SaleDeliverDetail> detailList = order.getDetailList();

		// daoFactory.getCommonDao().deleteEntity(SaleDeliver.class, id);
		// List<SaleDeliverDetail> detailList = getDetailList(id);
		for (SaleDeliverDetail detail : detailList)
		{
			BillType billType = detail.getSourceBillType();
			if (BillType.SALE_SO == billType)
			{
				SaleOrderDetail source = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, detail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty() - detail.getQty());
				source.setDeliverSpareedQty(source.getDeliverSpareedQty() - detail.getSpareQty());
				source.setDeliverMoney(source.getDeliverMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
			}

			if (BillType.PRODUCE_MO == billType || BillType.PRODUCE_TURNING == billType || BillType.PRODUCE_SUPPLEMENT == billType)
			{
				WorkProduct source = daoFactory.getCommonDao().lockObject(WorkProduct.class, detail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty() - detail.getQty());
				source.setDeliverSpareedQty(source.getDeliverSpareedQty() - detail.getSpareQty());
				source.setDeliverMoney(source.getDeliverMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
			}

		}
		daoFactory.getCommonDao().deleteAllEntity(detailList);
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<SaleDeliver> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliver.class, "o");
		query.addProjection(Projections.property("o"));
		query.createAlias(Customer.class, "c");
		query.eqProperty("c.id", "o.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("o.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("o.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerBillNo()))
		{
			query.like("o.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleDeliver.class);
	}

	@Override
	public SearchResult<SaleDeliverDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "sdd");
		query.createAlias(SaleDeliver.class, "sd");
		query.addProjection(Projections.property("sdd,sd,p.fileName"));
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("sdd.masterId", "sd.id");
		query.eqProperty("c.id", "sd.customerId");
		query.eqProperty("p.id", "sdd.productId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();

		if (StringUtils.isNotBlank(queryParam.getSaleOrderBillNo()))
		{
			query.like("sdd.saleOrderBillNo", "%" + queryParam.getSaleOrderBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("sdd.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("sdd.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("sdd.style", "%" + queryParam.getProductStyle() + "%");
		}

		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eq("sd.isCancel", BoolValue.NO);

		if (queryParam.getDateMin() != null)
		{
			query.ge("sd.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("sd.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("sd.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("sdd.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getWarehouseId() != null && queryParam.getWarehouseId() != -1)
		{
			query.eq("sdd.warehouseId", queryParam.getWarehouseId());
		}
		if (queryParam.getEmployeeId() != null && queryParam.getEmployeeId() != -1)
		{
			query.eq("sd.employeeId", queryParam.getEmployeeId());
		}
		if (queryParam.getCustomerClassId() != null && queryParam.getCustomerClassId() != -1)
		{
			query.eq("c.customerClassId", queryParam.getCustomerClassId());
		}
		if (queryParam.getProductClassId() != null && queryParam.getProductClassId() != -1)
		{
			query.eq("p.productClassId", queryParam.getProductClassId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("sd.isCheck", queryParam.getAuditFlag());
		}

		query.eq("sd.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("sd.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<SaleDeliverDetail> result = new SearchResult<SaleDeliverDetail>();
		result.setResult(new ArrayList<SaleDeliverDetail>());

		Product product = new Product();
		for (Object[] c : temp_result.getResult())
		{
			SaleDeliverDetail detail = (SaleDeliverDetail) c[0];
			product.setFileName((String)c[2]);
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleDeliver) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<SaleDeliverDetail> findDetailByConditions(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "sdd");
		query.createAlias(SaleDeliver.class, "sd");
		query.createAlias(Product.class, "p");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=sd.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("sdd,sd,p.fileName"));
		query.eqProperty("sdd.masterId", "sd.id");
		query.eqProperty("p.id", "sdd.productId");
		query.eq("sd.isCheck", BoolValue.YES);
		query.eq("sd.isCancel", BoolValue.NO);
		if (queryParam.getDateMin() != null)
		{
			query.ge("sd.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("sd.createTime", queryParam.getDateMax());
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("sd.customerId", queryParam.getCustomerId());
		}

		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("sd.billNo", "%" + queryParam.getBillNo() + "%");
		}
		query.add(Restrictions.gtProperty("sdd.qty", "sdd.reconcilQty"));// 送货数>对账数
		query.add(Restrictions.gtProperty("sdd.qty", "sdd.returnQty"));// 送货数>退/换货数
		query.eq("sd.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("sd.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<SaleDeliverDetail> result = new SearchResult<SaleDeliverDetail>();
		result.setResult(new ArrayList<SaleDeliverDetail>());
		
		Product product = new Product();
		for (Object[] c : temp_result.getResult())
		{
			SaleDeliverDetail detail = (SaleDeliverDetail) c[0];
			product.setFileName((String)c[2]);
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleDeliver) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public void coutReturnReconcilQty(SaleOrderDetail saleOrderDetail)
	{
		Integer sum = 0;// 对账数量
		BigDecimal sumMony = new BigDecimal(0.00);// 已收款
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "a");
		query.createAlias(SaleDeliver.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", saleOrderDetail.getId());
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleDeliverDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleDeliverDetail.class);
		for (SaleDeliverDetail saleDeliverDetail : result.getResult())
		{
			// 退货数量
			 Integer reconcil = serviceFactory.getSaleReturnService().coutReturnReconcilQty(saleDeliverDetail.getId());
			// 对账数量=送货对账数量-退货(换货)对账数量
			sum += saleDeliverDetail.getReconcilQty() - reconcil;
			BigDecimal receiveMoney = serviceFactory.getSaleReconcilService().countReturnReceiveMoney(saleDeliverDetail);
			sumMony = sumMony.add(receiveMoney);
		}
		saleOrderDetail.setReconcilQty(sum);
		saleOrderDetail.setReceiveMoney(sumMony);
	}

	@Override
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "d");
		query.createAlias(SaleDeliver.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		query.addProjection(Projections.property("count(1)"));
		query.setQueryType(QueryType.JDBC);
		query.eq("d.saleOrderBillNo", saleOrderBillNo);
		query.eq("d.productId", productId);
		query.eq("m.isCheck", BoolValue.NO);
		@SuppressWarnings("rawtypes")
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt(map.getResult().get(0).get("count(1)").toString()) == 0;

	}

	// ==================== 新规范 - 代码重构 ====================
	
	@Override
	public SearchResult<SaleDeliverDetail> findAll(BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "a");
		query.createAlias(SaleDeliver.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", isCheck);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleDeliverDetail.class);
	}
	
	@Override
	public SearchResult<SaleDeliverDetail> findAll()
	{
		return findAll(BoolValue.YES);
	}
	
}
