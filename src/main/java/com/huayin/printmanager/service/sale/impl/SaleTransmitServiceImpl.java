/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月8日 下午2:23:33
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sale.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sale.SaleTransmitService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月8日下午2:23:33, zhengby
 */
@Service
public class SaleTransmitServiceImpl extends BaseServiceImpl implements SaleTransmitService
{
	@Override
	public SearchResult<SaleOrderDetail> findForTransmitDeliverByWork(QueryParam queryParam)
	{
		SearchResult<SaleOrderDetail> result = new SearchResult<SaleOrderDetail>();
		DynamicQuery query = new CompanyDynamicQuery(WorkProduct.class, "a");
		query.addProjection(Projections.property("a, b, p"));
		query.createAlias(Work.class, "b");
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "a.customerId");
		query.eqProperty("a.productId", "p.id");
		query.eqProperty("a.sourceBillNo", "''");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (queryParam.getCreateTimeMin() != null)
		{
			query.ge("b.createTime", queryParam.getCreateTimeMin());
		}
		if (queryParam.getCreateTimeMax() != null)
		{
			query.le("b.createTime", queryParam.getCreateTimeMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.deliveryTime", (queryParam.getDateMin()));
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.deliveryTime", queryParam.getDateMax());
		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.saleProduceQty", "a.deliverQty"));

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(1);
		query.setPageSize(Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_RETURN_PAGE_SIZE_MAX)));
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<WorkProduct> workProductResult = new SearchResult<WorkProduct>();
		workProductResult.setResult(new ArrayList<WorkProduct>());
		for (Object[] c : temp_result.getResult())
		{
			WorkProduct detail = (WorkProduct) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((Work) c[1]);
			workProductResult.getResult().add(detail);
		}

		// SearchResult<WorkProduct> workProductResult =
		// serviceFactory.getWorkService().findProductByCondition(queryParam);

		result.setCount(temp_result.getCount());

		List<SaleOrderDetail> list = new ArrayList<SaleOrderDetail>();
		for (WorkProduct workProduct : workProductResult.getResult())
		{
			SaleOrderDetail saleOrderDetail = new SaleOrderDetail();

			saleOrderDetail.setId(workProduct.getId());

			SaleOrder master = new SaleOrder();
			master.setCreateTime(workProduct.getMaster().getCreateTime());
			master.setBillNo(workProduct.getMaster().getBillNo());
			master.setCustomerId(workProduct.getCustomerId());

			saleOrderDetail.setDeliveryTime(workProduct.getDeliveryTime());
			saleOrderDetail.setCustomerMaterialCode(workProduct.getCustomerMaterialCode());
			master.setCustomerBillNo(workProduct.getCustomerBillNo());
			master.setCheckUserName(workProduct.getCustomerName());

			saleOrderDetail.setUnitId(workProduct.getUnitId());
			saleOrderDetail.setProductName(workProduct.getProductName());
			saleOrderDetail.setProductId(workProduct.getProductId());
			saleOrderDetail.setStyle(workProduct.getStyle());
			saleOrderDetail.setQty(workProduct.getSaleProduceQty().intValue());
			saleOrderDetail.setSpareQty(workProduct.getSpareProduceQty().intValue());
			saleOrderDetail.setMemo(workProduct.getMemo());
			saleOrderDetail.setDeliverQty(workProduct.getDeliverQty());
			saleOrderDetail.setImgUrl(workProduct.getImgUrl());

			saleOrderDetail.setMaster(master);
			saleOrderDetail.setMasterId(workProduct.getMasterId());
			list.add(saleOrderDetail);
		}
		result.setResult(list);

		return result;
	}
	
	@Override
	public SearchResult<SaleOrderDetail> findForTransmitDeliver(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleOrderDetail.class, "a");
		query.addProjection(Projections.property("a, b, p"));
		query.createAlias(SaleOrder.class, "b");
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.customerId");
		query.eqProperty("a.productId", "p.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getCreateTimeMin() != null)
		{
			query.ge("b.createTime", queryParam.getCreateTimeMin());
		}
		if (queryParam.getCreateTimeMax() != null)
		{
			query.le("b.createTime", queryParam.getCreateTimeMax());
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("b.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("a.deliveryTime", (queryParam.getDateMin()));
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("a.deliveryTime", queryParam.getDateMax());
		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(1);
		query.setPageSize(Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_ARRIVE_PAGE_SIZE_MAX)));
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SaleOrderDetail> result = new SearchResult<SaleOrderDetail>();
		result.setResult(new ArrayList<SaleOrderDetail>());
		for (Object[] c : temp_result.getResult())
		{
			SaleOrderDetail detail = (SaleOrderDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleOrder) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<SaleDeliverDetail> findDeliverForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleDeliverDetail.class, "a");
		query.createAlias(SaleDeliver.class, "b");
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.addProjection(Projections.property("a,b,p"));
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("b.customerId", "c.id");
		query.eqProperty("p.id", "a.productId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (queryParam.getCreateTimeMin() != null)
		{
			query.ge("b.createTime", queryParam.getCreateTimeMin());
		}
		if (queryParam.getCreateTimeMax() != null)
		{
			query.le("b.createTime", queryParam.getCreateTimeMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("a.saleOrderBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));// 送货数>对账数

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(1);
		query.setPageSize(Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_ARRIVE_PAGE_SIZE_MAX)));
		SearchResult<Object[]> temp_result = null;
		try
		{
			temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		SearchResult<SaleDeliverDetail> result = new SearchResult<SaleDeliverDetail>();
		result.setResult(new ArrayList<SaleDeliverDetail>());

		for (Object[] c : temp_result.getResult())
		{
			SaleDeliverDetail detail = (SaleDeliverDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleDeliver) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<SaleReturnDetail> findReturnForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReturnDetail.class, "a");
		query.createAlias(SaleReturn.class, "b");
		query.createAlias(Customer.class, "c");
		query.createAlias(Product.class, "p");
		query.eqProperty("b.customerId", "c.id");
		query.eqProperty("p.id", "a.productId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("a,b,p"));
		query.eqProperty("a.masterId", "b.id");

		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}
		if (queryParam.getCreateTimeMin() != null)
		{
			query.ge("b.createTime", queryParam.getCreateTimeMin());
		}
		if (queryParam.getCreateTimeMax() != null)
		{
			query.le("b.createTime", queryParam.getCreateTimeMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("a.saleOrderBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("a.style", "%" + queryParam.getProductStyle() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getCompleteFlag() == BoolValue.NO)
		{// 搜索非强制完工
			query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		else
		{// 搜索已强制完工
			query.eq("a.isForceComplete", BoolValue.YES);

		}
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.add(Restrictions.gtProperty("a.qty", "a.reconcilQty"));// 退货数>对账数

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(1);
		query.setPageSize(Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_RETURN_PAGE_SIZE_MAX)));
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		;
		SearchResult<SaleReturnDetail> result = new SearchResult<SaleReturnDetail>();
		result.setResult(new ArrayList<SaleReturnDetail>());

		for (Object[] c : temp_result.getResult())
		{
			SaleReturnDetail detail = (SaleReturnDetail) c[0];
			Product product = (Product) c[2];
			detail.setImgUrl(product.getImgUrl());
			detail.setMaster((SaleReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
}
