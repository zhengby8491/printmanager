/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.begin.CustomerBegin;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.service.begin.CustomerBeginService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 客户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@Service
public class CustomerBeginServiceImpl extends BaseServiceImpl implements CustomerBeginService
{

	@Override
	public CustomerBegin get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBegin.class);
		query.eq("id", id);
		CustomerBegin customerBegin = daoFactory.getCommonDao().getByDynamicQuery(query, CustomerBegin.class);

		if (customerBegin != null)
		{
			customerBegin.setDetailList(this.getDetail(id));
		}
		return customerBegin;
	}

	@Override
	public List<CustomerBeginDetail> getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBeginDetail.class);
		query.eq("masterId", id);
		List<CustomerBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, CustomerBeginDetail.class);
		return detailList;

	}

	@Override
	@Transactional
	public Long save(CustomerBegin customerBegin)
	{
		BoolValue flag = customerBegin.getIsCheck();				// 标识是否保存并审核
		customerBegin.setIsCheck(BoolValue.NO);							// 默认未审核
		customerBegin.setBillType(BillType.BEGIN_CUSTOMER);
		customerBegin.setBillNo(UserUtils.createBillNo(BillType.BEGIN_CUSTOMER));
		customerBegin.setCompanyId(UserUtils.getCompanyId());
		customerBegin.setCreateTime(new Date());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			customerBegin.setCreateName(e.getName());
		}
		else
		{
			customerBegin.setCreateName(UserUtils.getUserName());
		}
		customerBegin.setCreateEmployeeId(UserUtils.getEmployeeId());
		customerBegin.setMemo(customerBegin.getMemo());
		daoFactory.getCommonDao().saveEntity(customerBegin);
		for (CustomerBeginDetail detail : customerBegin.getDetailList())
		{
			detail.setMasterId(customerBegin.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setReceivedMoney(new BigDecimal(0));
			detail.setIsForceComplete(BoolValue.NO);
			if (detail.getAdvanceMoney() == null)
			{
				detail.setAdvanceMoney(new BigDecimal(0));
			}
			daoFactory.getCommonDao().getEntity(Customer.class, detail.getCustomerId()).setIsBegin(BoolValue.YES);
			detail.setMemo(detail.getMemo());
			daoFactory.getCommonDao().saveEntity(detail);
		}
		if (flag == BoolValue.YES)
		{
			this.audit(customerBegin.getId());
		}
		return customerBegin.getId();

	}

	@Override
	@Transactional
	public Long update(CustomerBegin customerBegin)
	{
		CustomerBegin customerBegin_ = this.lockHasChildren(customerBegin.getId());
		// 先判断是否已经审核
		if (customerBegin_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (CustomerBeginDetail newItem : customerBegin.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (CustomerBeginDetail oldItem : customerBegin_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				CustomerBeginDetail customerBeginDetail = daoFactory.getCommonDao().getEntity(CustomerBeginDetail.class, id);

				Customer customer = daoFactory.getCommonDao().getEntity(Customer.class, customerBeginDetail.getCustomerId());
				if (customer != null)
				{
					customer.setIsBegin(BoolValue.NO);
				}
				daoFactory.getCommonDao().deleteEntity(CustomerBeginDetail.class, id);
			}
		}

		customerBegin_.setBeginTime(customerBegin.getBeginTime());
		customerBegin_.setMemo(customerBegin.getMemo());
		for (CustomerBeginDetail detail : customerBegin.getDetailList())
		{
			if (detail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(CustomerBeginDetail.class);
				querySt.eq("id", detail.getId());
				CustomerBeginDetail customerBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, CustomerBeginDetail.class);

				customerBeginDetail.setCustomerId(detail.getCustomerId());
				customerBeginDetail.setCustomerCode(detail.getCustomerCode());
				customerBeginDetail.setCustomerName(detail.getCustomerName());
				customerBeginDetail.setCurrencyType(detail.getCurrencyType());
				if (detail.getAdvanceMoney() == null)
				{
					detail.setAdvanceMoney(new BigDecimal(0));
				}
				customerBeginDetail.setAdvanceMoney(detail.getAdvanceMoney());
				customerBeginDetail.setReceiveMoney(detail.getReceiveMoney());
				customerBeginDetail.setReceiveTime(detail.getReceiveTime());
				customerBeginDetail.setMemo(detail.getMemo());

				Customer customer = daoFactory.getCommonDao().getEntity(Customer.class, customerBeginDetail.getCustomerId());
				if (customer != null)
				{
					customer.setIsBegin(BoolValue.YES);
				}
				daoFactory.getCommonDao().updateEntity(customerBeginDetail);

			}
			else
			{
				// 解决-客户期初修改在增加时，需要设置ReceivedMoney为0。否则会出现NULL（导致收款单不能保存）
				detail.setReceivedMoney(new BigDecimal(0));
				detail.setMasterId(customerBegin.getId());
				detail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().saveEntity(detail);
				daoFactory.getCommonDao().getEntity(Customer.class, detail.getCustomerId()).setIsBegin(BoolValue.YES);
			}
		}
		if (customerBegin.getIsCheck() == BoolValue.YES)
		{
			this.audit(customerBegin.getId());
		}
		return customerBegin.getId();
	}

	@Override
	@Transactional
	public Integer delete(Long id)
	{
		try
		{
			CustomerBegin master = this.lockHasChildren(id);
			for (CustomerBeginDetail detail : master.getDetailList())
			{
				daoFactory.getCommonDao().getEntity(Customer.class, detail.getCustomerId()).setIsBegin(BoolValue.NO);
			}
			daoFactory.getCommonDao().deleteEntity(master);
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
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
	public Boolean audit(Long id)
	{
		CustomerBegin order = this.get(id);
		// 先判断是否已经审核
		if (order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核");
		}
		for (CustomerBeginDetail detail : order.getDetailList())
		{
			// 创建预收款日志
			FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
			log.setTradeMode(FinanceTradeMode.ADD);
			log.setBillType(order.getBillType());
			log.setBillId(detail.getId());
			log.setCompanyId(detail.getCompanyId());
			log.setMoney(detail.getAdvanceMoney());
			log.setCustomerId(detail.getCustomerId());
			log.setCustomerName(detail.getCustomerName());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 增加客户预收款
			Customer customer = daoFactory.getCommonDao().lockObject(Customer.class, detail.getCustomerId());
			if (customer.getAdvanceMoney() == null)
			{
				customer.setAdvanceMoney(new BigDecimal(0));
			}
			customer.setAdvanceMoney(customer.getAdvanceMoney().add(detail.getAdvanceMoney()));
			daoFactory.getCommonDao().updateEntity(customer);
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_CUSTOMER, id, BoolValue.YES);
		return true;
	}
	
	@Override
	@Transactional
	public Boolean auditCancel(Long id)
	{
		CustomerBegin order = this.get(id);
		// 先判断是否已经反审核
		if (order.getIsCheck() == BoolValue.NO)
		{
			throw new BusinessException("已反审核");
		}
		for (CustomerBeginDetail detail : order.getDetailList())
		{
			// 创建预收款日志
			FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
			log.setTradeMode(FinanceTradeMode.SUBTRACT);
			log.setBillType(order.getBillType());
			log.setBillId(detail.getId());
			log.setCompanyId(detail.getCompanyId());
			log.setMoney(detail.getAdvanceMoney());
			log.setCustomerId(detail.getCustomerId());
			log.setCustomerName(detail.getCustomerName());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 增加客户预收款
			Customer customer = daoFactory.getCommonDao().lockObject(Customer.class, detail.getCustomerId());
			if (customer.getAdvanceMoney() == null)
			{
				customer.setAdvanceMoney(new BigDecimal(0));
			}
			customer.setAdvanceMoney(customer.getAdvanceMoney().subtract(detail.getAdvanceMoney()));
			daoFactory.getCommonDao().updateEntity(customer);
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存
		}
		serviceFactory.getCommonService().audit(BillType.BEGIN_CUSTOMER, id, BoolValue.NO);
		return true;
	}
	
	@Override
	public SearchResult<CustomerBegin> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBegin.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, CustomerBegin.class);
	}

	@Override
	public CustomerBegin lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBegin.class);
		query.eq("id", id);
		CustomerBegin order = daoFactory.getCommonDao().lockByDynamicQuery(query, CustomerBegin.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(CustomerBeginDetail.class);
		query_detail.eq("masterId", id);
		List<CustomerBeginDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, CustomerBeginDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	@Override
	public CustomerBeginDetail findCustomerBeginDetail(Long customerId)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBeginDetail.class, "a");
		query.eq("a.customerId", customerId);
		query.addProjection(Projections.property("a"));
		query.createAlias(CustomerBegin.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isReceiveOver", BoolValue.NO);// 是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		CustomerBeginDetail c = daoFactory.getCommonDao().getByDynamicQuery(query, CustomerBeginDetail.class);
		return c;

	}

	@Override
	public List<CustomerBeginDetail> findAll(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBeginDetail.class, "a");
		query.addProjection(Projections.property("a"));
		query.createAlias(CustomerBegin.class, JoinType.LEFTJOIN, "b", "b.id=a.masterId");
		if (null != queryParam.getCustomerIdList())
		{
			query.in("a.customerId", queryParam.getCustomerIdList());
		}
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		// query.eq("a.isPaymentOver", BoolValue.NO);// 是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		SearchResult<CustomerBeginDetail> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, CustomerBeginDetail.class);
		return result.getResult();
	}
}
