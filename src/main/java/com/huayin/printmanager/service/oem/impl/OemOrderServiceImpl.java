/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月7日 下午1:48:03
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.huayin.common.util.ObjectHelper;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.helper.AbsHelper.CountMap;
import com.huayin.printmanager.helper.service.OemDeliverHelper;
import com.huayin.printmanager.helper.service.OemReconcilHelper;
import com.huayin.printmanager.helper.service.OemReturnHelper;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.ExchangeRate;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProgressStatusSale;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemOrderService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月7日下午1:48:03, zhengby
 */
@Service
public class OemOrderServiceImpl extends BaseServiceImpl implements OemOrderService
{
	@Override
	public OemOrder get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrder.class);
		query.eq("id", id);
		OemOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, OemOrder.class);
		order.setDetailList(getDetailList(id));
		return order;
	}

	@Override
	public List<OemOrderDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class);
		query.eq("masterId", id);
		query.asc("id");
		List<OemOrderDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OemOrderDetail.class);
		return detailList;
	}
	
	@Override
	public OemOrderDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class);
		query.eq("id", id);
		OemOrderDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemOrderDetail.class);
		return detail;
	}
	
	@Override
	public OemOrderDetail getDetail(String originCompanyId, Long sourceDetailId)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class);
		query.eq("originCompanyId", originCompanyId);
		query.eq("sourceDetailId", sourceDetailId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OemOrderDetail.class);
	}
	
	@Override
	public OemOrder getMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrder.class);
		query.eq("id", id);
		OemOrder order = daoFactory.getCommonDao().getByDynamicQuery(query, OemOrder.class);
		return order;
	}
	
	@Override
	public OemOrderDetail getDetailHasMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class);
		query.eq("id", id);
		OemOrderDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemOrderDetail.class);
		detail.setMaster(this.getMaster(detail.getMasterId()));
		return detail;
	}
	
	@Override
	@Transactional
	public void save(OemOrder order)
	{
		BoolValue flag = order.getIsCheck();
		order.setBillType(BillType.OEM_EO);
		order.setBillNo(UserUtils.createBillNo(BillType.OEM_EO));
		order.setProgressStatus(ProgressStatusSale.NO_PRODUCE);
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
		for (OemOrderDetail oem : order.getDetailList())
		{
			oem.setMasterId(order.getId());
			oem.setUserNo(UserUtils.getUser().getUserNo());
			oem.setCompanyId(UserUtils.getCompanyId());
		}
		// 再保存订单明细表
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OEM_EO, order.getId(), BoolValue.YES);
		}
	}

	@Override
	@Transactional
	public void update(OemOrder order)
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
		OemOrder old_order = this.lockHasChildren(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (OemOrderDetail newItem : order.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (OemOrderDetail oldItem : old_order.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				daoFactory.getCommonDao().deleteEntity(OemOrderDetail.class, id);
			}
		}
		
		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "supplierAddress" });// 替换成新内容
		ExchangeRate exchangeRate = serviceFactory.getCompanyService().getExchangeRate(order.getCurrencyType());
		old_order.setRateId(exchangeRate.getId());
		old_order.setUpdateName(UserUtils.getUserName());
		old_order.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(old_order);
		
		for (OemOrderDetail oemOrderDetail : order.getDetailList())
		{
			if (oemOrderDetail.getId() != null)
			{
				OemOrderDetail oldOrderDetail = this.getDetail(oemOrderDetail.getId());
				oldOrderDetail.setMemo(oemOrderDetail.getMemo());
				PropertyClone.copyProperties(oldOrderDetail, oemOrderDetail, false, null, new String[] { "memo", "processRequire" });// 替换成新内容
				daoFactory.getCommonDao().updateEntity(oldOrderDetail);
			}
			else
			{
				oemOrderDetail.setMasterId(old_order.getId());
				oemOrderDetail.setCompanyId(UserUtils.getCompanyId());
				oemOrderDetail.setIsForceComplete(BoolValue.NO);
				daoFactory.getCommonDao().saveEntity(oemOrderDetail);
			}
		}
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		OemOrder order = this.get(id);
		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OemOrder> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrder.class, "o");
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemOrder.class);
	}
	
	@Override
	public SearchResult<OemOrderDetail> findDetailBycondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class, "od");
		query.createAlias(OemOrder.class, "o");
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
		if (null != queryParam.getCustomerClassId())
		{
			query.eq("c.customerClassId", queryParam.getCustomerClassId());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("o.isCheck", queryParam.getAuditFlag());
		}
		query.eq("o.companyId", UserUtils.getCompanyId());
		query.eq("c.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("o.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemOrderDetail> result = new SearchResult<OemOrderDetail>();
		result.setResult(new ArrayList<OemOrderDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemOrder oemOrder = (OemOrder) c[1];
			OemOrderDetail detail = (OemOrderDetail) c[0];
			detail.setMaster(oemOrder);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public OemOrder lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrder.class);
		query.eq("id", id);
		OemOrder order = daoFactory.getCommonDao().lockByDynamicQuery(query, OemOrder.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OemOrderDetail.class);
		query_detail.eq("masterId", id);
		List<OemOrderDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OemOrderDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
	
	@Override
	public SearchResult<OemOrderDetail> findFlowByCondition(QueryParam queryParam)
	{
		SearchResult<OemOrderDetail> result = findDetailBycondition(queryParam);
		// 查询所有代工送货数量
		Map<Long, CountMap> deliverReconcilQtyMap = OemDeliverHelper.countAllReconcilQty();
		// 查询所有代工单退货数量
		Map<Long, BigDecimal> returnReconcilQtyMap = OemReturnHelper.countAllReconcilQty();
		// 已收款金额
		Map<Long, BigDecimal> reconcilReceiveMoneyMap = OemReconcilHelper.countAllReceiveMoney();

		for (OemOrderDetail detail : result.getResult())
		{
			// 对账数量 = 送货对账数量 - 退货对账数量
			detail.setReconcilQty(new BigDecimal(0));
			detail.setReceiveMoney(new BigDecimal(0));
			CountMap countMap = OemDeliverHelper.countReconcilQty(deliverReconcilQtyMap, detail.getId());
			if (null != countMap)
			{
				BigDecimal returnReconcilQty = OemReturnHelper.countReconcilQty(returnReconcilQtyMap, countMap.getId());
				if (null == returnReconcilQty)
				{
					returnReconcilQty = new BigDecimal(0);
				}
				detail.setReconcilQty(countMap.getCount().subtract(returnReconcilQty));

				// 已收款金额
				BigDecimal receiveMoney = OemReconcilHelper.countReconcilQty(reconcilReceiveMoneyMap, countMap.getId());
				if (null == receiveMoney)
				{
					receiveMoney = new BigDecimal(0);
				}
				detail.setReceiveMoney(receiveMoney);
			}

		}

		return result;
	}
	
	@Override
	public List<OemOrder> getYearsFromOemOrder()
	{
		DynamicQuery query = new CompanyDynamicQuery(OemOrder.class);
		query.addProjection(Projections.property("distinct DATE_FORMAT(createTime,'%Y') as name,DATE_FORMAT(createTime,'%Y') as value"));
		query.eq("isCheck", BoolValue.YES);
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OemOrder.class);
	}
	
	@Override
	public SearchResult<SumVo> sumOemOrderByCustomer(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class, "d");
		query.addProjection(Projections.property("new com.huayin.printmanager.service.vo.SumVo(" + "d.id," 
				+ (type.equals("name") ? "c.name as name," : type.equals("class") ? "cc.name as name," : "") + "SUM(d.money) as sumMoney ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as january," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as february," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as march," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as april," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as may," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as june," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as july," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as august," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as september,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as october," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as november," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as december" + ")"));
		query.createAlias(OemOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=a.customerId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (type.equals("class"))
		{
			query.createAlias(CustomerClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.customerClassId");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getCustomerClassId() != null)
		{
			query.eq("cc.id", queryParam.getCustomerClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("c.name");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("a.customerId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按客户名称
			query.addGourp("a.customerId");
		}
		else if (type.equals("class"))
		{
			// 按客户分类
			query.addGourp("cc.id");
		}

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		// query.setQueryType(QueryType.JDBC);
		result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, SumVo.class);

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> sumOemOrderByProcedure(QueryParam queryParam, String type)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class, "d");
		query.addProjection(Projections.property(("d.id," 
				+ (type.equals("name") ? "d.procedureName as name," : type.equals("class") ? "cc.name as name," : "")) 
				+ "SUM(d.money) as 'sumMoney' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as 'may' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as 'october' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(OemOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "customer", "customer.id=a.customerId");
			query.inArray("customer.employeeId", employes);
		}
		if (type.equals("class"))
		{
			query.createAlias(Procedure.class, JoinType.LEFTJOIN, "c", "c.id=d.procedureId");
			query.createAlias(ProcedureClass.class, JoinType.LEFTJOIN, "cc", "cc.id=c.procedureClassId");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("d.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (queryParam.getProcedureClassId() != null)
		{
			query.eq("cc.id", queryParam.getProcedureClassId());
		}
		if (type.equals("name"))
		{
			query.isNotNull("d.procedureName");
		}
		else if (type.equals("class"))
		{
			query.isNotNull("d.procedureId");
		}
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		if (type.equals("name"))
		{
			// 按客户名称
			query.addGourp("d.procedureId");
		}
		else if (type.equals("class"))
		{
			// 按客户分类
			query.addGourp("cc.id");
		}

		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<SumVo> sumOemOrderBySeller(QueryParam queryParam)
	{
		SearchResult<SumVo> result = new SearchResult<SumVo>();
		DynamicQuery query = new CompanyDynamicQuery(OemOrderDetail.class, "d");
		query.addProjection(Projections.property("d.id,e.name as name," + "SUM(d.money) as 'sumMoney' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-01' THEN d.money ELSE 0 END ) as 'january' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-02' THEN d.money ELSE 0 END ) as 'february' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-03' THEN d.money ELSE 0 END ) as 'march' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-04' THEN d.money ELSE 0 END ) as 'april' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-05' THEN d.money ELSE 0 END ) as 'may' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-06' THEN d.money ELSE 0 END ) as 'june' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-07' THEN d.money ELSE 0 END ) as 'july' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-08' THEN d.money ELSE 0 END ) as 'august' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-09' THEN d.money ELSE 0 END ) as 'september' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-10' THEN d.money ELSE 0 END ) as 'october' ,"
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-11' THEN d.money ELSE 0 END ) as 'november' ," 
				+ "SUM(CASE DATE_FORMAT(a.createTime,'%Y-%m') WHEN '" + queryParam.getYear() + "-12' THEN d.money ELSE 0 END ) as 'december'"));
		query.createAlias(OemOrder.class, JoinType.LEFTJOIN, "a", "a.id=d.masterId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=a.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.createAlias(Employee.class, JoinType.LEFTJOIN, "e", "e.id=a.employeeId");
		if (queryParam.getEmployeeId() != null)
		{
			query.eq("a.employeeId", queryParam.getEmployeeId());
		}
		query.isNotNull("e.name");
		query.eq("a.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("DATE_FORMAT(a.createTime,'%Y')", queryParam.getYear());
		query.addGourp("a.employeeId");
		query.setIsSearchTotalCount(true);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setQueryType(QueryType.JDBC);
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		List<SumVo> list = new ArrayList<SumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			SumVo vo = new SumVo();
			try
			{
				vo = ObjectHelper.mapToObject(map, SumVo.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(mapResult.getCount());
		return result;
	}
}
