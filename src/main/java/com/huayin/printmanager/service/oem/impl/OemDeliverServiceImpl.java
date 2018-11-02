/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:47:27
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemDeliverService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工送货单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:47:27, zhengby
 */
@Service
public class OemDeliverServiceImpl extends BaseServiceImpl implements OemDeliverService
{
	@Override
	public OemDeliver get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliver.class);
		query.eq("id", id);
		OemDeliver order = daoFactory.getCommonDao().getByDynamicQuery(query, OemDeliver.class);
		order.setDetailList(getDetailList(id));
		return order;
	}

	@Override
	public List<OemDeliverDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class);
		query.eq("masterId", id);
		query.asc("id");
		List<OemDeliverDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OemDeliverDetail.class);
		return detailList;
	}
	
	@Override
	public OemDeliverDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class);
		query.eq("id", id);
		OemDeliverDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemDeliverDetail.class);
		return detail;
	}
	
	@Override
	public OemDeliver getMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliver.class);
		query.eq("id", id);
		OemDeliver order = daoFactory.getCommonDao().getByDynamicQuery(query, OemDeliver.class);
		return order;
	}
	
	@Override
	public OemDeliverDetail getDetailHasMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class);
		query.eq("id", id);
		OemDeliverDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemDeliverDetail.class);
		detail.setMaster(this.getMaster(detail.getMasterId()));
		return detail;
	}
	
	@Override
	@Transactional
	public void save(OemDeliver order)
	{
		BoolValue flag = order.getIsCheck();
		order.setBillType(BillType.OEM_ED);
		order.setBillNo(UserUtils.createBillNo(BillType.OEM_ED));
		order.setUserNo(UserUtils.getUser().getUserNo());
		order.setCompanyId(UserUtils.getCompanyId());
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
		// 先保存主表，得到返回的order
		order = daoFactory.getCommonDao().saveEntity(order);
		// 给明细表的masterId设值
		for (OemDeliverDetail detail : order.getDetailList())
		{
			detail.setMasterId(order.getId());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setOemOrderBillId(detail.getSourceId());
			detail.setOemOrderBillNo(detail.getSourceBillNo());
			detail.setIsForceComplete(BoolValue.NO);
			detail.setReconcilQty(new BigDecimal(0));
			detail.setReconcilMoney(new BigDecimal(0));
			detail.setReturnQty(new BigDecimal(0));
			detail.setReturnMoney(new BigDecimal(0));
			detail.setSourceBillType(BillType.OEM_EO);
			detail.setSourceBillNo(detail.getSourceBillNo());
			TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), detail.getTaxRateId());
			detail.setPercent(taxRate.getPercent());
			
			// 反写代工订单送货数量
			OemOrderDetail orderDetail = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, detail.getSourceDetailId());
			orderDetail.setDeliverQty(orderDetail.getDeliverQty().add(detail.getQty()));
			orderDetail.setDeliverMoney(orderDetail.getDeliverMoney().add(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(orderDetail);
		}
		// 再保存订单明细表
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OEM_ED, order.getId(), BoolValue.YES);
		}
	}

	@Override
	@Transactional
	public void update(OemDeliver order)
	{
		if (order.getIsCheck() == BoolValue.YES)
		{
			order.setCheckTime(new Date());
			Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
			if (e != null)
			{
				order.setCheckUserName(e.getName());
			}
			else
			{
				order.setCheckUserName(UserUtils.getUserName());
			}
		}
		OemDeliver old_order = this.lockHasChildren(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		Map<Long, OemDeliverDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		List<OemDeliverDetail> delList = new ArrayList<>();
		
		// ID集合
		for (OemDeliverDetail newItem : order.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (OemDeliverDetail oldItem : old_order.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		
		for (OemDeliverDetail newOrderDetail : order.getDetailList())
		{
			if (newOrderDetail.getId() != null)
			{ // 更新
				OemDeliverDetail oldOrderDetail = this.getDetail(newOrderDetail.getId());
				// 反写代工订单送货数量
				OemOrderDetail source = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, oldOrderDetail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty().add(newOrderDetail.getQty()).subtract(oldOrderDetail.getQty()));
				source.setDeliverMoney(source.getDeliverMoney().add(newOrderDetail.getMoney()).subtract(oldOrderDetail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
				
				oldOrderDetail.setMemo(newOrderDetail.getMemo());
				PropertyClone.copyProperties(oldOrderDetail, newOrderDetail, false, null, new String[] { "memo","processRequire" });// 替换成新内容
				daoFactory.getCommonDao().updateEntity(oldOrderDetail);
			}
			else
			{// 新增
				newOrderDetail.setMasterId(old_order.getId());
				newOrderDetail.setCompanyId(UserUtils.getCompanyId());
				newOrderDetail.setIsForceComplete(BoolValue.NO);
				newOrderDetail.setUserNo(UserUtils.getUser().getUserNo());
				daoFactory.getCommonDao().saveEntity(newOrderDetail);
				// 反写代工订单送货数量
				OemOrderDetail source = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, newOrderDetail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty().add(newOrderDetail.getQty()));
				source.setDeliverMoney(source.getDeliverMoney().add(newOrderDetail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
			}
		}
		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				OemDeliverDetail deliverDetail = old_detail_map.get(id);
				delList.add(deliverDetail);
				OemOrderDetail source = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, deliverDetail.getSourceDetailId());
				source.setDeliverQty(source.getDeliverQty().subtract(deliverDetail.getQty()));
				source.setDeliverMoney(source.getDeliverMoney().subtract(deliverDetail.getMoney()));
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delList);
		
		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(order.getCurrencyType());
		old_order.setRateId(exchangeRate.getId());
		old_order.setUpdateName(UserUtils.getUserName());
		old_order.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(old_order);
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OemDeliver order = this.get(id);
		List<OemDeliverDetail> detailList = order.getDetailList();
		for (OemDeliverDetail detail : detailList)
		{
			// 反写代工订单的送货量
			OemOrderDetail source = daoFactory.getCommonDao().lockObject(OemOrderDetail.class, detail.getSourceDetailId());
			source.setDeliverQty(source.getDeliverQty().subtract(detail.getQty()));
			source.setDeliverMoney(source.getDeliverMoney().subtract(detail.getMoney()));
			daoFactory.getCommonDao().updateEntity(source);
		}
		
		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OemDeliver> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliver.class, "o");
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
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		if (StringUtils.isNotEmpty(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemDeliver.class);
	}
	
	@Override
	public SearchResult<OemDeliverDetail> findDetailBycondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class, "od");
		query.createAlias(OemDeliver.class, "o");
		query.createAlias(Customer.class, "c");
		query.eqProperty("od.masterId", "o.id");
		query.eqProperty("c.id", "o.customerId");
		query.addProjection(Projections.property("od, o"));
		query.eq("o.isCancel", BoolValue.NO);

		if (StringUtils.isNotBlank(queryParam.getProductStyle()))
		{
			query.like("od.style", "%" + queryParam.getProductStyle() + "%");
		}

		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
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
		if (StringUtils.isNotEmpty(queryParam.getBillNo()))
		{
			query.like("o.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProductName()))
		{
			query.like("od.productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.like("od.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("od.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("od.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (queryParam.getEmployeeId() != null && queryParam.getEmployeeId() != -1)
		{
			query.eq("o.employeeId", queryParam.getEmployeeId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("o.customerId", queryParam.getCustomerId());
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemDeliverDetail> result = new SearchResult<OemDeliverDetail>();
		result.setResult(new ArrayList<OemDeliverDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemDeliver oemOrder = (OemDeliver) c[1];
			OemDeliverDetail detail = (OemDeliverDetail) c[0];
			detail.setMaster(oemOrder);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public OemDeliver lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliver.class);
		query.eq("id", id);
		OemDeliver order = daoFactory.getCommonDao().lockByDynamicQuery(query, OemDeliver.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OemDeliverDetail.class);
		query_detail.eq("masterId", id);
		List<OemDeliverDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OemDeliverDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
	
	@Override
	public SearchResult<OemDeliverDetail> findDeliverRecords(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class, "sdd");
		query.createAlias(OemDeliver.class, "sd");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=sd.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("sdd,sd"));
		query.eqProperty("sdd.masterId", "sd.id");
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
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.like("sdd.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		query.add(Restrictions.gtProperty("sdd.qty", "sdd.reconcilQty"));// 送货数>对账数
		query.add(Restrictions.gtProperty("sdd.qty", "sdd.returnQty"));// 送货数>退/换货数
		query.eq("sd.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		//query.desc("sd.createTime");
		query.asc("sdd.id");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
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
	public SearchResult<OemDeliverDetail> findAll(BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemDeliverDetail.class, "a");
		query.createAlias(OemDeliver.class, "b");
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemDeliverDetail.class);
	}
	
	@Override
	public SearchResult<OemDeliverDetail> findAll()
	{
		return findAll(BoolValue.YES);
	}
}
