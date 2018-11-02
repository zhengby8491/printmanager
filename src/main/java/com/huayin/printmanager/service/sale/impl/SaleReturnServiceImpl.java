/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午18:51:13
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
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
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sale.SaleReturnService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日
 * @version 	   2.0, 2018年2月22日下午6:51:39, zhengby, 代码规范
 */
@Service
public class SaleReturnServiceImpl extends BaseServiceImpl implements SaleReturnService
{
	@Override
	public SaleReturn get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturn.class);
		query.eq("id", id);
		SaleReturn order = daoFactory.getCommonDao().getByDynamicQuery(query, SaleReturn.class);

		// SaleReturn order = daoFactory.getCommonDao().getEntity(SaleReturn.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public SaleReturnDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class);
		query.eq("id", id);
		SaleReturnDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, SaleReturnDetail.class);

		// SaleReturnDetail detail = daoFactory.getCommonDao().getEntity(SaleReturnDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(SaleReturn.class, detail.getMasterId()));
		DynamicQuery imgQuery = new CompanyDynamicQuery(Product.class);
		imgQuery.eq("id", detail.getProductId());
		Product product = daoFactory.getCommonDao().getByDynamicQuery(imgQuery, Product.class);
		detail.setImgUrl(product.getImgUrl());
		return detail;
	}

	@Override
	public SaleReturn lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturn.class);
		query.eq("id", id);
		SaleReturn order = daoFactory.getCommonDao().lockByDynamicQuery(query, SaleReturn.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(SaleReturnDetail.class);
		query_detail.eq("masterId", id);
		List<SaleReturnDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, SaleReturnDetail.class, LockType.LOCK_WAIT);

		/*
		 * SaleReturn order = daoFactory.getCommonDao().lockObject(SaleReturn.class, id); List<SaleReturnDetail> detailList
		 * = this.getDetailList(id); for (SaleReturnDetail detail : detailList) { detail =
		 * daoFactory.getCommonDao().lockObject(SaleReturnDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<SaleReturnDetail> getDetailList(Long id)
	{
		// DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class);
		// query.eq("masterId", id);
		// List<SaleReturnDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query,
		// SaleReturnDetail.class);
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class, "s");
		query.addProjection(Projections.property("s,p"));
		query.createAlias(Product.class, "p");
		query.eq("masterId", id);
		query.eqProperty("p.id", "s.productId");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		List<SaleReturnDetail> detailList = new ArrayList<SaleReturnDetail>();
		for (Object[] c : temp_result.getResult())
		{
			SaleReturnDetail detail = (SaleReturnDetail) c[0];
			Product product = (Product) c[1];
			detail.setImgUrl(product.getImgUrl());
			detailList.add(detail);
		}
		return detailList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SaleReturn order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.SALE_IR);
		order.setBillNo(UserUtils.createBillNo(BillType.SALE_IR));
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
		for (SaleReturnDetail detail : order.getDetailList())
		{
			detail.setReconcilQty(0);
			detail.setReconcilMoney(new BigDecimal(0));
			if (detail.getSpareQty() == null)
			{
				detail.setSpareQty(0);
			}
			detail.setIsForceComplete(BoolValue.NO);
			detail.setSourceBillType(detail.getSourceBillType());
			detail.setSourceBillNo(detail.getSourceBillNo());

			detail.setMasterId(order.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setPercent(taxRate.getPercent());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			// 反写到货单退货数量
			SaleDeliverDetail source = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, detail.getSourceDetailId());
			source.setReturnQty(source.getReturnQty() + detail.getQty());
			source.setReturnMoney(source.getReturnMoney().add(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
			{// 换货：送货数-换货数
				String code = source.getSourceBillType().getCode();
				if ("MO".equals(code))
				{
					WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
					process.setDeliverQty(process.getDeliverQty() - detail.getQty());
					process.setDeliverMoney(process.getDeliverMoney().subtract(detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}

				if ("SO".equals(code))
				{
					SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
					process.setDeliverQty(process.getDeliverQty() - detail.getQty());
					process.setDeliverMoney(process.getDeliverMoney().subtract(detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}

			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			check(order.getId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SaleReturn order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		SaleReturn old_order = this.lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, SaleReturnDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// 要删除的数据
		List<SaleReturnDetail> del_detail = new ArrayList<SaleReturnDetail>();

		for (SaleReturnDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (SaleReturnDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (SaleReturnDetail new_detail : order.getDetailList())
		{
			SaleReturnDetail old_detail = old_detail_map.get(new_detail.getId());
			// 反写送货单退货数量
			SaleDeliverDetail source = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, new_detail.getSourceDetailId());
			if (new_detail.getId() == null)
			{// 新增
				new_detail.setMasterId(order.getId());
				new_detail.setReconcilQty(0);
				new_detail.setIsForceComplete(BoolValue.NO);
				new_detail.setCompanyId(UserUtils.getCompanyId());
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), new_detail.getTaxRateId());
				new_detail.setPercent(taxRate.getPercent());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());

				source.setReturnQty(source.getReturnQty() + new_detail.getQty());
				source.setReturnMoney(source.getReturnMoney().add(new_detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
				if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 换货：送货数-换货数
					String code = source.getSourceBillType().getCode();
					if ("MO".equals(code))
					{
						WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty() - new_detail.getQty());
						process.setDeliverMoney(process.getDeliverMoney().subtract(new_detail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}

					if ("SO".equals(code))
					{
						SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty() - new_detail.getQty());
						process.setDeliverMoney(process.getDeliverMoney().subtract(new_detail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}

				}
				daoFactory.getCommonDao().saveEntity(new_detail);
			}
			else
			// 更新
			{
				// 退换货都需要：更新退货逻辑
				source.setReturnQty(source.getReturnQty() - (old_detail.getQty() - new_detail.getQty()));
				source.setReturnMoney(source.getReturnMoney().subtract(old_detail.getMoney()).add(new_detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);

				if (old_order.getReturnType() == ReturnGoodsType.RETURN)
				{// 老类型=退货
					if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
					{// 新类型=换货：新增换货逻辑

						String code = source.getSourceBillType().getCode();
						if ("MO".equals(code))
						{
							WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() - new_detail.getQty());
							process.setDeliverMoney(process.getDeliverMoney().subtract(new_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

						if ("SO".equals(code))
						{
							SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() - new_detail.getQty());
							process.setDeliverMoney(process.getDeliverMoney().subtract(new_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

					}
				}
				else if (old_order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 老类型=换货
					if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
					{// 新类型=换货：更新换货逻辑

						String code = source.getSourceBillType().getCode();
						if ("MO".equals(code))
						{
							WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() + (old_detail.getQty() - new_detail.getQty()));
							process.setDeliverMoney(process.getDeliverMoney().add(old_detail.getMoney()).subtract(new_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

						if ("SO".equals(code))
						{
							SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() + (old_detail.getQty() - new_detail.getQty()));
							process.setDeliverMoney(process.getDeliverMoney().add(old_detail.getMoney()).subtract(new_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

					}
					else
					{// 新类型=退货:删除换货记录逻辑

						String code = source.getSourceBillType().getCode();
						if ("MO".equals(code))
						{
							WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() + old_detail.getQty());
							process.setDeliverMoney(process.getDeliverMoney().add(old_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

						if ("SO".equals(code))
						{
							SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
							process.setDeliverQty(process.getDeliverQty() + old_detail.getQty());
							process.setDeliverMoney(process.getDeliverMoney().add(old_detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(process);
						}

					}
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
				SaleReturnDetail saleReturnDetail = old_detail_map.get(id);
				// 反写送货单退货数量
				SaleDeliverDetail source = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, saleReturnDetail.getSourceDetailId());
				source.setReturnQty(source.getReturnQty() - saleReturnDetail.getQty());
				source.setReturnMoney(source.getReturnMoney().subtract(saleReturnDetail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
				if (old_order.getReturnType() == ReturnGoodsType.EXCHANGE)
				{// 换货：送货数+换货数

					String code = source.getSourceBillType().getCode();
					if ("MO".equals(code))
					{
						WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty() + saleReturnDetail.getQty());
						process.setDeliverMoney(process.getDeliverMoney().add(saleReturnDetail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}

					if ("SO".equals(code))
					{
						SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
						process.setDeliverQty(process.getDeliverQty() + saleReturnDetail.getQty());
						process.setDeliverMoney(process.getDeliverMoney().add(saleReturnDetail.getMoney()));
						daoFactory.getCommonDao().updateEntity(process);
					}

				}
				del_detail.add(saleReturnDetail);
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
			check(order.getId());
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void check(Long id)
	{
		serviceFactory.getCommonService().audit(BillType.SALE_IR, id, BoolValue.YES);

		// 有库存模块才进行库存操作
		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			SaleReturn master = this.get(id);
			for (SaleReturnDetail saleReturnDetail : master.getDetailList())
			{
				StockProduct stockProduct = new StockProduct();
				stockProduct.setProductId(saleReturnDetail.getProductId());
				stockProduct.setQty(saleReturnDetail.getQty());
				stockProduct.setWarehouseId(saleReturnDetail.getWarehouseId());
				stockProduct.setPrice(saleReturnDetail.getPrice());
				stockProduct.setMoney(saleReturnDetail.getMoney());
				serviceFactory.getStockProductService().stock(stockProduct, InOutType.IN);

				// 入库记录
				StockProductLog log = new StockProductLog();
				log.setBillId(master.getId());
				log.setBillType(master.getBillType());
				log.setBillNo(master.getBillNo());
				log.setSourceId(saleReturnDetail.getId());
				log.setCreateTime(new Date());
				log.setCompanyId(UserUtils.getCompanyId());
				log.setCode(saleReturnDetail.getProductCode());
				log.setProductClassId(((Product) daoFactory.getCommonDao().getEntity(Product.class, saleReturnDetail.getProductId())).getProductClassId());
				log.setCustomerMaterialCode(saleReturnDetail.getCustomerMaterialCode());
				log.setProductName(saleReturnDetail.getProductName());
				log.setProductId(saleReturnDetail.getProductId());
				log.setSpecifications(saleReturnDetail.getStyle());
				log.setWarehouseId(saleReturnDetail.getWarehouseId());
				log.setUnitId(saleReturnDetail.getUnitId());
				log.setPrice(saleReturnDetail.getPrice());
				log.setInQty(saleReturnDetail.getQty() + (saleReturnDetail.getSpareQty() == null ? 0 : saleReturnDetail.getSpareQty()));
				log.setInMoney(saleReturnDetail.getMoney());
				Integer storgeQty = serviceFactory.getStockProductService().getStockQty(stockProduct.getProductId(), stockProduct.getWarehouseId());
				log.setStorgeQty(storgeQty);
				log.setCustomerId(master.getCustomerId());
				log.setCustomerName(master.getCustomerName());
				daoFactory.getCommonDao().saveEntity(log);
			}
		}

	}

	@Override
	@Transactional
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck)
	{
		List<StockProduct> list = new ArrayList<StockProduct>();
		if (UserUtils.hasCompanyPermission("stock:product:list"))
		{
			SaleReturn master = this.get(id);
			// 先判断是否已经反审核
			if (master.getIsCheck() == BoolValue.NO)
			{
				throw new BusinessException("已反审核");
			}
			
			for (SaleReturnDetail saleReturnDetail : master.getDetailList())
			{// 将库存不足的库存对象存List
				DynamicQuery query = new CompanyDynamicQuery(StockProduct.class);
				query.eq("productId", saleReturnDetail.getProductId());
				query.eq("warehouseId", saleReturnDetail.getWarehouseId());
				StockProduct stockProduct = daoFactory.getCommonDao().getByDynamicQuery(query, StockProduct.class);
				if (stockProduct == null)
				{
					stockProduct = new StockProduct();
					stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, saleReturnDetail.getProductId()));
					stockProduct.setQty(0);
				}
				else
				{
					stockProduct.setProduct(daoFactory.getCommonDao().getEntity(Product.class, stockProduct.getProductId()));
				}

				if (saleReturnDetail.getQty() > stockProduct.getQty())
				{
					list.add(stockProduct);
				}
			}

			// 可以强制审核 （v6.8库存出库审核或者反审核时，如果库存不足不做控制，只做提示）
			if (list.size() == 0 || (null != forceCheck && BoolValue.YES == forceCheck))
			{
				list.clear();

				if (!serviceFactory.getCommonService().audit(BillType.SALE_IR, id, BoolValue.NO))
				{
					return null;
				}
				// 库存操作
				for (SaleReturnDetail saleReturnDetail : master.getDetailList())
				{
					StockProduct stockProduct = new StockProduct();
					stockProduct.setProductId(saleReturnDetail.getProductId());
					stockProduct.setQty(saleReturnDetail.getQty());
					stockProduct.setWarehouseId(saleReturnDetail.getWarehouseId());
					stockProduct.setPrice(saleReturnDetail.getPrice());
					stockProduct.setMoney(saleReturnDetail.getMoney());
					serviceFactory.getStockProductService().backStock(stockProduct, InOutType.IN);

					DynamicQuery query = new CompanyDynamicQuery(StockProductLog.class);
					query.eq("billId", id);
					List<StockProductLog> logs = daoFactory.getCommonDao().findEntityByDynamicQuery(query, StockProductLog.class);
					daoFactory.getCommonDao().deleteAllEntity(logs);
				}

			}
		}
		else
		{
			if (!serviceFactory.getCommonService().audit(BillType.SALE_IR, id, BoolValue.NO))
			{
				return null;
			}
		}

		return list;
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		SaleReturn order = this.lock(id);
		List<SaleReturnDetail> detailList = order.getDetailList();
		for (SaleReturnDetail detail : detailList)
		{
			// 反写送货单退货数量
			SaleDeliverDetail source = daoFactory.getCommonDao().lockObject(SaleDeliverDetail.class, detail.getSourceDetailId());
			source.setReturnQty(source.getReturnQty() - detail.getQty());
			source.setReturnMoney(source.getReturnMoney().subtract(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
			if (order.getReturnType() == ReturnGoodsType.EXCHANGE)
			{// 换货：送货数+换货数

				BillType billType = source.getSourceBillType();
				if (BillType.SALE_SO == billType)
				{
					SaleOrderDetail process = daoFactory.getCommonDao().lockObject(SaleOrderDetail.class, source.getSourceDetailId());
					process.setDeliverQty(process.getDeliverQty() + detail.getQty());
					process.setDeliverMoney(process.getDeliverMoney().add(detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}
				if (BillType.PRODUCE_MO == billType)
				{
					WorkProduct process = daoFactory.getCommonDao().lockObject(WorkProduct.class, source.getSourceDetailId());
					process.setDeliverQty(process.getDeliverQty() + detail.getQty());
					process.setDeliverMoney(process.getDeliverMoney().add(detail.getMoney()));
					daoFactory.getCommonDao().updateEntity(process);
				}

			}
		}
		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<SaleReturn> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturn.class, "o");

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
			query.like("o.custBillNo", "%" + queryParam.getCustomerBillNo() + "%");
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReturn.class);
	}

	@Override
	public SearchResult<SaleReturnDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class, "srd");
		query.createAlias(SaleReturn.class, "sr");
		query.addProjection(Projections.property("srd,sr,p.fileName"));
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("srd.masterId", "sr.id");
		query.eqProperty("c.id", "sr.customerId");

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("srd.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("srd.saleOrderBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("srd.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("srd.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("srd.style", "%" + queryParam.getProductStyle() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("p.id", "srd.productId");
		query.eq("sr.isCancel", BoolValue.NO);

		if (queryParam.getDateMin() != null)
		{
			query.ge("sr.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("sr.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("sr.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("srd.productName", "%" + queryParam.getProductName() + "%");
		}
		if (null != queryParam.getWarehouseId())
		{
			query.eq("srd.warehouseId", queryParam.getWarehouseId());
		}
		if (queryParam.getEmployeeId() != null && queryParam.getEmployeeId() != -1)
		{
			query.eq("sr.employeeId", queryParam.getEmployeeId());
		}
		if (null != queryParam.getCustomerClassId())
		{
			query.eq("c.customerClassId", queryParam.getCustomerClassId());
		}
		if (null != queryParam.getProductClassId())
		{
			query.in("p.productClassId", queryParam.getProductClassId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("sr.isCheck", queryParam.getAuditFlag());
		}
		query.eq("sr.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("sr.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<SaleReturnDetail> result = new SearchResult<SaleReturnDetail>();
		result.setResult(new ArrayList<SaleReturnDetail>());

		Product product = new Product();
		for (Object[] c : temp_result.getResult())
		{
			SaleReturnDetail detail = (SaleReturnDetail) c[0];
			product.setFileName((String)c[2]);
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public Integer coutReturnReconcilQty(Long id)
	{
		Integer sum = 0;// 对账数量
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class, "a");
		query.createAlias(SaleReturn.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("a.sourceDetailId", id);
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.returnType", ReturnGoodsType.EXCHANGE);// 换货类型
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		SearchResult<SaleReturnDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReturnDetail.class);
		for (SaleReturnDetail saleReturnDetail : result.getResult())
		{
			sum += saleReturnDetail.getReconcilQty();
		}
		return sum;
	}

	// ==================== 新规范 - 代码重构 ====================
	
	@Override
	public SearchResult<SaleReturnDetail> findAll(BoolValue isCheck, ReturnGoodsType type)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class, "a");
		query.createAlias(SaleReturn.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", isCheck);
		query.eq("b.returnType", type);// 换货类型
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, SaleReturnDetail.class);
	}
	
	@Override
	public SearchResult<SaleReturnDetail> findAll(ReturnGoodsType type)
	{
		return findAll(BoolValue.YES, type);
	}
}
