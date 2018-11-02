/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:50:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemTransmitService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:50:51, zhengby
 */
@Service
public class OemTransmitServiceImpl extends BaseServiceImpl implements OemTransmitService
{
	@Override
	public SearchResult<OemOrderDetail> findOrderForTransmitDeliver(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class, "a");
		query.addProjection(Projections.property("a, b"));
		query.createAlias(OemOrder.class, "b");
		query.createAlias(Customer.class, "c");
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("c.id", "b.customerId");
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
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("a.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("a.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("b.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
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
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
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
		query.add(Restrictions.gtProperty("a.qty", "a.deliverQty"));

		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(1);
		query.setPageSize(Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.TRANSMIT_ARRIVE_PAGE_SIZE_MAX)));
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemOrderDetail> result = new SearchResult<OemOrderDetail>();
		result.setResult(new ArrayList<OemOrderDetail>());
		for (Object[] c : temp_result.getResult())
		{
			OemOrderDetail detail = (OemOrderDetail) c[0];
			detail.setMaster((OemOrder) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<OemDeliverDetail> findDeliverForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class, "a");
		query.createAlias(OemDeliver.class, "b");
		query.createAlias(Customer.class, "c");
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		query.eqProperty("b.customerId", "c.id");
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
		if (StringUtils.isNotBlank(queryParam.getOemOrderBillNo()))
		{
			query.like("a.oemOrderBillNo", "%" + queryParam.getOemOrderBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.originBillNo", "%" + queryParam.getCustomerBillNo() + "%");
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
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
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
		SearchResult<OemDeliverDetail> result = new SearchResult<OemDeliverDetail>();
		result.setResult(new ArrayList<OemDeliverDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemDeliverDetail detail = (OemDeliverDetail) c[0];
			detail.setMaster((OemDeliver) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<OemReturnDetail> findReturnForTransmitReconcil(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReturnDetail.class, "a");
		query.createAlias(OemReturn.class, "b");
		query.createAlias(Customer.class, "c");
		query.eqProperty("b.customerId", "c.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("a,b"));
		query.eqProperty("a.masterId", "b.id");
		if (queryParam.getCreateTimeMin() != null)
		{
			query.ge("b.createTime", queryParam.getCreateTimeMin());
		}
		if (queryParam.getCreateTimeMax() != null)
		{
			query.le("b.createTime", queryParam.getCreateTimeMax());
		}
		if (StringUtils.isNotBlank(queryParam.getOemOrderBillNo()))
		{
			query.like("a.oemOrderBillNo", "%" + queryParam.getOemOrderBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.originBillNo", "%" + queryParam.getCustomerBillNo() + "%");
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
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
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
		SearchResult<OemReturnDetail> result = new SearchResult<OemReturnDetail>();
		result.setResult(new ArrayList<OemReturnDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemReturnDetail detail = (OemReturnDetail) c[0];
			detail.setMaster((OemReturn) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
}
