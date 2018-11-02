/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:47:55
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
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemReconcilService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工对账单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:47:55, zhengby
 */
@Service
public class OemReconcilServiceImpl extends BaseServiceImpl implements OemReconcilService
{
	@Override
	public OemReconcil get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcil.class);
		query.eq("id", id);
		OemReconcil order = daoFactory.getCommonDao().getByDynamicQuery(query, OemReconcil.class);
		order.setDetailList(getDetailList(id));
		return order;
	}

	@Override
	public List<OemReconcilDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class);
		query.eq("masterId", id);
		query.asc("id");
		List<OemReconcilDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, OemReconcilDetail.class);
		return detailList;
	}

	@Override
	public OemReconcilDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class);
		query.eq("id", id);
		OemReconcilDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemReconcilDetail.class);
		return detail;
	}

	@Override
	public OemReconcil getMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcil.class);
		query.eq("id", id);
		OemReconcil order = daoFactory.getCommonDao().getByDynamicQuery(query, OemReconcil.class);
		return order;
	}

	@Override
	public OemReconcilDetail getDetailHasMaster(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class);
		query.eq("id", id);
		OemReconcilDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OemReconcilDetail.class);
		detail.setMaster(this.getMaster(detail.getMasterId()));
		return detail;
	}

	@Override
	@Transactional
	public void save(OemReconcil order)
	{
		BoolValue flag = order.getIsCheck();
		order.setBillType(BillType.OEM_EC);
		order.setBillNo(UserUtils.createBillNo(BillType.OEM_EC));
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
		for (OemReconcilDetail detail : order.getDetailList())
		{
			if (detail.getSourceBillType() == BillType.OEM_ED)
			{
				// 反写送货单对账数量
				OemDeliverDetail deliverDetail = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, detail.getSourceDetailId());
				deliverDetail.setReconcilQty(deliverDetail.getReconcilQty().add(detail.getQty()));
				deliverDetail.setReconcilMoney(deliverDetail.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(deliverDetail);

			}
			else if (detail.getSourceBillType() == BillType.OEM_ER)
			{// 反写退货单对账数量
				OemReturnDetail returnDetail = daoFactory.getCommonDao().lockObject(OemReturnDetail.class, detail.getSourceDetailId());
				returnDetail.setReconcilQty(returnDetail.getReconcilQty().add(detail.getQty().abs()));
				returnDetail.setReconcilMoney(returnDetail.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(returnDetail);
			}

			detail.setMasterId(order.getId());
			detail.setUserNo(UserUtils.getUser().getUserNo());
			detail.setCompanyId(UserUtils.getCompanyId());
		}
		// 再保存订单明细表
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());

		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			serviceFactory.getCommonService().audit(BillType.OEM_EC, order.getId(), BoolValue.YES);
		}
	}

	@Override
	@Transactional
	public void update(OemReconcil order)
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
		OemReconcil old_order = this.lockHasChildren(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}

		Map<Long, OemReconcilDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		List<OemReconcilDetail> delList = new ArrayList<>();

		// ID集合
		for (OemReconcilDetail newItem : order.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (OemReconcilDetail oldItem : old_order.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}

		for (OemReconcilDetail newOrderDetail : order.getDetailList())
		{
			if (newOrderDetail.getId() != null)
			{ // 更新
				OemReconcilDetail oldOrderDetail = this.getDetail(newOrderDetail.getId());
				if (newOrderDetail.getSourceBillType() == BillType.OEM_ED)
				{
					// 反写代工送货单的对账数量
					OemDeliverDetail deliverDetail = serviceFactory.getOemDeliverService().getDetail(newOrderDetail.getSourceDetailId());
					deliverDetail.setReconcilQty(deliverDetail.getReconcilQty().subtract(oldOrderDetail.getQty().subtract(newOrderDetail.getQty())));
					deliverDetail.setReconcilMoney(deliverDetail.getReconcilMoney().subtract(oldOrderDetail.getMoney()).add(newOrderDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(deliverDetail);
				}
				else if (newOrderDetail.getSourceBillType() == BillType.OEM_ER)
				{
					// 反写代工退货单的对账数量
					OemReturnDetail returnDetail = serviceFactory.getOemReturnService().getDetail(newOrderDetail.getSourceDetailId());
					returnDetail.setReconcilQty(returnDetail.getReconcilQty().subtract(oldOrderDetail.getQty().abs().subtract(newOrderDetail.getQty().abs())));
					returnDetail.setReconcilMoney(returnDetail.getReconcilMoney().add(oldOrderDetail.getMoney()).subtract(newOrderDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(returnDetail);
				}
				oldOrderDetail.setMemo(newOrderDetail.getMemo());
				PropertyClone.copyProperties(oldOrderDetail, newOrderDetail, false, null, new String[] { "memo", "processRequire" });// 替换成新内容
			}
			else
			{// 新增
				newOrderDetail.setMasterId(old_order.getId());
				newOrderDetail.setQty(new BigDecimal(0));
				newOrderDetail.setCompanyId(UserUtils.getCompanyId());
				newOrderDetail.setIsForceComplete(BoolValue.NO);
				newOrderDetail.setUserNo(UserUtils.getUser().getUserNo());
				TaxRate taxRate = (TaxRate) UserUtils.getBasicInfo(BasicType.TAXRATE.name(), newOrderDetail.getTaxRateId());
				newOrderDetail.setPercent(taxRate.getPercent());

				if (newOrderDetail.getSourceBillType() == BillType.OEM_ED)
				{
					// 反写代工送货单对账数量
					OemDeliverDetail deliverDetail = serviceFactory.getOemDeliverService().getDetail(newOrderDetail.getSourceDetailId());
					deliverDetail.setReconcilQty(deliverDetail.getReconcilQty().add(newOrderDetail.getQty()));
					deliverDetail.setReconcilMoney(deliverDetail.getReconcilMoney().add(newOrderDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(deliverDetail);

				}
				else if (newOrderDetail.getSourceBillType() == BillType.OEM_ER)
				{
					// 反写代工退货单对账数量
					OemReturnDetail returnDetail = serviceFactory.getOemReturnService().getDetail(newOrderDetail.getSourceDetailId());
					returnDetail.setReconcilQty(returnDetail.getReconcilQty().add(newOrderDetail.getQty()));
					returnDetail.setReconcilMoney(returnDetail.getReconcilMoney().add(newOrderDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(returnDetail);
				}
				daoFactory.getCommonDao().saveEntity(newOrderDetail);
			}

		}
		// 删除操作
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				OemReconcilDetail oemReconcilDetail = old_detail_map.get(id);
				if (oemReconcilDetail.getSourceBillType() == BillType.OEM_ED)
				{
					// 反写送货单对账数量
					OemDeliverDetail sourceArrive = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, oemReconcilDetail.getSourceDetailId());
					sourceArrive.setReconcilQty(sourceArrive.getReconcilQty().subtract(oemReconcilDetail.getQty()));
					sourceArrive.setReconcilMoney(sourceArrive.getReconcilMoney().subtract(oemReconcilDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceArrive);
				}
				else if (oemReconcilDetail.getSourceBillType() == BillType.OEM_ER)
				{
					// 反写退货单对账数量
					OemReturnDetail sourceReturn = daoFactory.getCommonDao().lockObject(OemReturnDetail.class, oemReconcilDetail.getSourceDetailId());
					sourceReturn.setReconcilQty(sourceReturn.getReconcilQty().subtract(oemReconcilDetail.getQty().abs()));
					sourceReturn.setReconcilMoney(sourceReturn.getReconcilMoney().add(oemReconcilDetail.getMoney()));
					daoFactory.getCommonDao().updateEntity(sourceReturn);
				}
				delList.add(oemReconcilDetail);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delList);

		PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "linkName", "mobile", "customerAddress" });// 替换成新内容
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
		OemReconcil order = this.get(id);
		List<OemReconcilDetail> detailList = order.getDetailList();
		for (OemReconcilDetail detail : detailList)
		{
			if (detail.getSourceBillType() == BillType.OEM_ED)
			{
				// 反写代工送货的对账量
				OemDeliverDetail source = daoFactory.getCommonDao().lockObject(OemDeliverDetail.class, detail.getSourceDetailId());
				source.setReconcilQty(source.getReconcilQty().subtract(detail.getQty()));
				source.setReconcilMoney(source.getReconcilMoney().subtract(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
			}
			else if (detail.getSourceBillType() == BillType.OEM_ER)
			{
				// 反写代工退货的对账量
				OemReturnDetail source = daoFactory.getCommonDao().lockObject(OemReturnDetail.class, detail.getSourceDetailId());
				source.setReconcilQty(source.getReconcilQty().subtract(detail.getQty().abs()));
				source.setReconcilMoney(source.getReconcilMoney().add(detail.getMoney()));
				daoFactory.getCommonDao().updateEntity(source);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(order.getDetailList());
		daoFactory.getCommonDao().deleteEntity(order);
	}

	@Override
	public SearchResult<OemReconcil> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcil.class, "o");
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemReconcil.class);
	}

	@Override
	public SearchResult<OemReconcilDetail> findDetailBycondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class, "od");
		query.createAlias(OemReconcil.class, "o");
		query.createAlias(Customer.class, "c");
		query.eqProperty("od.masterId", "o.id");
		query.eqProperty("c.id", "o.customerId");
		query.addProjection(Projections.property("od, o"));
		query.eq("o.isCancel", BoolValue.NO);

		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("o.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
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

		SearchResult<OemReconcilDetail> result = new SearchResult<OemReconcilDetail>();
		result.setResult(new ArrayList<OemReconcilDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OemReconcil oemOrder = (OemReconcil) c[1];
			OemReconcilDetail detail = (OemReconcilDetail) c[0];
			detail.setMaster(oemOrder);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public OemReconcil lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcil.class);
		query.eq("id", id);
		OemReconcil order = daoFactory.getCommonDao().lockByDynamicQuery(query, OemReconcil.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(OemReconcilDetail.class);
		query_detail.eq("masterId", id);
		List<OemReconcilDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, OemReconcilDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
	
	@Override
	public SearchResult<OemReconcilDetail> findAll(BoolValue isCheck)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class, "a");
		query.createAlias(OemReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemReconcilDetail.class);
	}

	@Override
	public SearchResult<OemReconcilDetail> findAll()
	{
		return findAll(BoolValue.YES);
	}
}
