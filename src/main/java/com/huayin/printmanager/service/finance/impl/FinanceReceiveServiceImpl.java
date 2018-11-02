/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.ObjectHelper;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.begin.CustomerBegin;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.persist.enumerate.ReceiveType;
import com.huayin.printmanager.service.finance.FinanceReceiveService;
import com.huayin.printmanager.service.finance.vo.FinanceCompanyArrearsVo;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinanceReceiveServiceImpl extends BaseServiceImpl implements FinanceReceiveService
{

	@Override
	public FinanceReceive get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class);
		query.eq("id", id);
		FinanceReceive order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceReceive.class);

		// Receive order = daoFactory.getCommonDao().getEntity(Receive.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public FinanceReceiveDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceiveDetail.class);
		query.eq("id", id);
		FinanceReceiveDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceReceiveDetail.class);

		// ReceiveDetail detail = daoFactory.getCommonDao().getEntity(ReceiveDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinanceReceive.class, detail.getMasterId()));
		return detail;
	}

	@Override
	@Transactional
	public FinanceReceive lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class);
		query.eq("id", id);
		FinanceReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceReceive.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceReceiveDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceReceiveDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceReceiveDetail.class, LockType.LOCK_WAIT);

		/*
		 * Receive order = daoFactory.getCommonDao().lockObject(Receive.class, id); List<ReceiveDetail> detailList =
		 * this.getDetailList(id); for (ReceiveDetail detail : detailList) { detail =
		 * daoFactory.getCommonDao().lockObject(ReceiveDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<FinanceReceiveDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceiveDetail.class);
		query.eq("masterId", id);
		List<FinanceReceiveDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceReceiveDetail.class);
		return detailList;
	}

	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinanceReceive save(FinanceReceive order)
	{
		BoolValue flag = order.getIsCheck();			// 标识是否保存并审核
		order.setIsCheck(BoolValue.NO);						// 默认未审核
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_REC);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_REC));
		order.setUserNo(UserUtils.getUser().getUserNo());
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
		/* order.setIsCheck(BoolValue.NO); */
		order.setIsForceComplete(BoolValue.NO);
		order.setIsCancel(BoolValue.NO);
		if (order.getDiscount() == null)
		{
			order.setDiscount(new BigDecimal(0));
		}
		order = daoFactory.getCommonDao().saveEntity(order);
		// 账户加钱
		ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.RECEIVE, order.getId().toString(), "客户收款单据编号[" + order.getBillNo() + "]");
		if (!accountDealResult.getIsSuccess())
		{
			/**
			 * 2018-10-24 09:40
			 * 修改使用BusinessException。		zhengxchn@163.com
			 * */
			//throw new Exception(accountDealResult.getMessage());
			throw new BusinessException(accountDealResult.getMessage());
		}
		// 增加预收款
		if (order.getAdvance().compareTo(new BigDecimal(0)) == 1)
		{// 预收款大于0
			// 创建预收款日志
			FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
			log.setTradeMode(FinanceTradeMode.ADD);
			log.setBillType(order.getBillType());
			log.setBillId(order.getId());
			log.setCompanyId(order.getCompanyId());
			log.setMoney(order.getAdvance());
			log.setCustomerId(order.getCustomerId());
			log.setCustomerName(order.getCustomerName());
			log.setEmployeeId(order.getEmployeeId());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 增加客户预收款
			Customer Customer = daoFactory.getCommonDao().lockObject(Customer.class, order.getCustomerId());
			Customer.setAdvanceMoney(Customer.getAdvanceMoney().add(order.getAdvance()));
			daoFactory.getCommonDao().updateEntity(Customer);
			UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存
		}
		if (order.getDetailList() != null && order.getDetailList().size() > 0)
		{// 收款类型
			order.setReceiveType(ReceiveType.RECEIVE);
			List<FinanceReceiveDetail> newDetailList = new ArrayList<FinanceReceiveDetail>();
			for (FinanceReceiveDetail detail : order.getDetailList())
			{
				if (detail.getMoney().compareTo(new BigDecimal(0)) != 0)
				{
					newDetailList.add(detail);
				}
			}
			order.setDetailList(newDetailList);
			// 保存明细
			for (FinanceReceiveDetail detail : newDetailList)
			{
				detail.setCompanyId(UserUtils.getCompanyId());
				detail.setMasterId(order.getId());
				detail.setIsForceComplete(BoolValue.NO);
				detail.setUserNo(UserUtils.getUser().getUserNo());
				if (detail.getSourceBillType() == BillType.SALE_SK)
				{
					// 反写销售对账单
					SaleReconcilDetail source = daoFactory.getCommonDao().lockObject(SaleReconcilDetail.class, detail.getSourceDetailId());
					source.setReceiveMoney(source.getReceiveMoney().add(detail.getMoney()));
					if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) != -1)
					{
						source.setIsReceiveOver(BoolValue.YES);
					}
					daoFactory.getCommonDao().updateEntity(source);
				}
				else if (detail.getSourceBillType() == BillType.BEGIN_CUSTOMER)
				{
					// 反写客户期初
					CustomerBeginDetail source = daoFactory.getCommonDao().lockObject(CustomerBeginDetail.class, detail.getSourceDetailId());
					source.setReceivedMoney(source.getReceivedMoney().add(detail.getMoney()));
					if (source.getReceivedMoney().abs().compareTo(source.getReceiveMoney().abs()) != -1)
					{
						source.setIsReceiveOver(BoolValue.YES);
					}
					daoFactory.getCommonDao().updateEntity(source);
				}
				else if (detail.getSourceBillType() == BillType.OEM_EC)
				{
					// 反写代工对账单
					OemReconcilDetail source = daoFactory.getCommonDao().lockObject(OemReconcilDetail.class, detail.getSourceDetailId());
					source.setReceiveMoney(source.getReceiveMoney().add(detail.getMoney()));
					if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) != -1)
					{
						source.setIsReceiveOver(BoolValue.YES);
					}
					daoFactory.getCommonDao().updateEntity(source);
				} else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
				{
					// 反写财务调整单
					FinanceAdjustDetail source = daoFactory.getCommonDao().lockObject(FinanceAdjustDetail.class, detail.getSourceDetailId());
					source.setReceiveOrPayMoney(source.getReceiveOrPayMoney().add(detail.getMoney()));
					if (source.getReceiveOrPayMoney().compareTo(source.getAdjustMoney()) != -1)
					{
						source.setIsReceiveOrPayOver(BoolValue.YES);
					} 
					daoFactory.getCommonDao().updateEntity(source);
				}
			}

		}
		else
		{// 预收款类型
			order.setReceiveType(ReceiveType.ADVANCE);
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		if (flag == BoolValue.YES)
		{
			this.audit(order.getId(), BoolValue.YES);
		}
		return order;
	}

	@Override
	@Transactional
	public boolean audit(Long id, BoolValue flag)
	{
		if (serviceFactory.getCommonService().audit(BillType.FINANCE_REC, id, flag))
		{
			FinanceReceive order = serviceFactory.getReceiveService().get(id);

			// 如果是作废审核，则需要减少客户预收款
			if (order.getIsCancel() == BoolValue.YES && flag == BoolValue.YES)
			{// 预收款大于0
				// 账户减钱
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.RECEIVE_CANCEL, order.getId().toString(), "客户收款作废单据编号[" + order.getBillNo() + "]");
				if (!accountDealResult.getIsSuccess())
				{
					/**
					 * 2018-10-24 09:40
					 * 修改使用BusinessException。		zhengxchn@163.com
					 * */
					//throw new Exception(accountDealResult.getMessage());
					throw new BusinessException(accountDealResult.getMessage());
				}
				if (order.getDetailList() != null && order.getDetailList().size() > 0)
				{
					for (FinanceReceiveDetail detail : order.getDetailList())
					{
						if (detail.getSourceBillType() == BillType.SALE_SK)
						{
							// 反写销售对账单
							SaleReconcilDetail source = daoFactory.getCommonDao().lockObject(SaleReconcilDetail.class, detail.getSourceDetailId());
							source.setReceiveMoney(source.getReceiveMoney().subtract(detail.getMoney()));
							if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) == -1)
							{
								source.setIsReceiveOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.BEGIN_CUSTOMER)
						{
							// 反写客户期初
							CustomerBeginDetail source = daoFactory.getCommonDao().lockObject(CustomerBeginDetail.class, detail.getSourceDetailId());
							source.setReceivedMoney(source.getReceivedMoney().subtract(detail.getMoney()));
							if (source.getReceivedMoney().abs().compareTo(source.getReceiveMoney().abs()) == -1)
							{
								source.setIsReceiveOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.OEM_EC)
						{
							// 反写代工对账
							OemReconcilDetail source = daoFactory.getCommonDao().lockObject(OemReconcilDetail.class, detail.getSourceDetailId());
							source.setReceiveMoney(source.getReceiveMoney().subtract(detail.getMoney()));
							if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) == -1)
							{
								source.setIsReceiveOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
						{
							// 反写财务调整单
							FinanceAdjustDetail source = daoFactory.getCommonDao().lockObject(FinanceAdjustDetail.class, detail.getSourceDetailId());
							source.setReceiveOrPayMoney(source.getReceiveOrPayMoney().subtract(detail.getMoney()));
							daoFactory.getCommonDao().updateEntity(source);
						}
					}
				}
				if (order.getAdvance().compareTo(new BigDecimal(0)) == 1)
				{
					// 单据作废，创建预收款撤销日志
					FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
					log.setTradeMode(FinanceTradeMode.SUBTRACT);
					log.setBillType(order.getBillType());
					log.setBillId(order.getId());
					log.setCompanyId(order.getCompanyId());
					log.setMoney(order.getAdvance());
					log.setCustomerId(order.getCustomerId());
					log.setCustomerName(order.getCustomerName());
					log.setEmployeeId(order.getEmployeeId());
					log.setCreateTime(new Date());
					daoFactory.getCommonDao().saveEntity(log);
					// 减少客户预收款
					Customer Customer = daoFactory.getCommonDao().lockObject(Customer.class, order.getCustomerId());
					Customer.setAdvanceMoney(Customer.getAdvanceMoney().subtract(order.getAdvance()));
					daoFactory.getCommonDao().updateEntity(Customer);

				}
				UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存

			}
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	@Transactional
	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			List<FinanceReceive> receiveList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceReceive.class);
			for (FinanceReceive receive : receiveList)
			{
				receive.setIsCheck(BoolValue.YES);
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean cancel(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class);
		query.eq("id", id);
		FinanceReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceReceive.class, LockType.LOCK_WAIT).get(0);

		// Receive order = daoFactory.getCommonDao().lockObject(Receive.class, id);
		order.setIsCancel(BoolValue.YES);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	@Override
	@Transactional
	public boolean cancelBack(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class);
		query.eq("id", id);
		FinanceReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceReceive.class, LockType.LOCK_WAIT).get(0);
		// Receive order = daoFactory.getCommonDao().lockObject(Receive.class, id);
		order.setIsCancel(BoolValue.NO);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	@Override
	public SearchResult<FinanceReceive> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class, "p");

		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("p.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}

		if (queryParam.getCustomerName() != null)
		{
			query.like("p.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getIsCancel() != null)
		{
			query.eq("p.isCancel", queryParam.getIsCancel());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("p.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("p.customerId", queryParam.getCustomerId());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=p.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");
		// 单表查询
		if (employes.length <= 0)
		{
			return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceReceive.class);
		}
		// 多表查询
		else
		{
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

			SearchResult<FinanceReceive> result = new SearchResult<FinanceReceive>();
			result.setResult(new ArrayList<FinanceReceive>());
			for (Object[] c : temp_result.getResult())
			{
				FinanceReceive receiver = (FinanceReceive) c[0];
				result.getResult().add(receiver);
			}
			result.setCount(temp_result.getCount());
			return result;
		}
	}

	@Override
	public SearchResult<FinanceReceiveDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class, "b");
		query.createAlias(FinanceReceiveDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}

		if (StringUtils.isNotBlank(queryParam.getSourceBillNo()))
		{
			query.like("a.sourceBillNo", "%" + queryParam.getSourceBillNo() + "%");
		}

		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerName() != null)
		{
			query.like("b.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.eq("a.procedureName", queryParam.getProcedureName());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}

		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<FinanceReceiveDetail> result = new SearchResult<FinanceReceiveDetail>();
		result.setResult(new ArrayList<FinanceReceiveDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceReceiveDetail detail = (FinanceReceiveDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceReceiveDetail();
				detail.setMasterId(((FinanceReceive) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceReceive) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceReceive) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<SaleReconcilDetail> findSaleReconcilDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("a, b"));

		query.eqProperty("a.masterId", "b.id");

		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("b.customerId", queryParam.getCustomerId());
		}

		query.eq("b.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isReceiveOver", BoolValue.NO);// 工序是否强制完工
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SaleReconcilDetail> result = new SearchResult<SaleReconcilDetail>();
		result.setResult(new ArrayList<SaleReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			SaleReconcilDetail detail = (SaleReconcilDetail) c[0];
			detail.setMaster((SaleReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<OemReconcilDetail> findOemReconcilDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class, "a");
		query.createAlias(OemReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=b.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.addProjection(Projections.property("a, b"));

		query.eqProperty("a.masterId", "b.id");

		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("b.customerId", queryParam.getCustomerId());
		}

		query.eq("b.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isReceiveOver", BoolValue.NO);// 是否已完成收款
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemReconcilDetail> result = new SearchResult<OemReconcilDetail>();
		result.setResult(new ArrayList<OemReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			OemReconcilDetail detail = (OemReconcilDetail) c[0];
			detail.setMaster((OemReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<CustomerBeginDetail> findCustomerBeginDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBeginDetail.class, "a");
		query.createAlias(CustomerBegin.class, "b");
		query.addProjection(Projections.property("a, b"));

		query.eqProperty("a.masterId", "b.id");
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("a.customerId", queryParam.getCustomerId());
		}
		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isReceiveOver", BoolValue.NO);// 工序是否强制完工
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<CustomerBeginDetail> result = new SearchResult<CustomerBeginDetail>();
		result.setResult(new ArrayList<CustomerBeginDetail>());
		for (Object[] c : temp_result.getResult())
		{
			CustomerBeginDetail detail = (CustomerBeginDetail) c[0];
			detail.setMaster((CustomerBegin) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<FinanceAdjustDetail> findFinanceAdjustDetailList(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceAdjustDetail.class, "a");
		query.createAlias(FinanceAdjust.class, "b");
		query.addProjection(Projections.property("a, b"));
		query.eqProperty("a.masterId", "b.id");
		query.createAlias(Customer.class, "c");
		query.addProjection(Projections.property("b, c"));
		query.eqProperty("a.businessId", "c.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (queryParam.getDateMin() != null)
		{
			query.ge("b.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("b.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (null != queryParam.getAdjustType())
		{
			query.eq("b.adjustType", queryParam.getAdjustType());
		}
		if (null != queryParam.getAuditFlag())
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}
		if (null != queryParam.getCustomerId())
		{
			query.eq("a.businessId", queryParam.getCustomerId());
		}
		query.eq("a.isReceiveOrPayOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		SearchResult<FinanceAdjustDetail> result = new SearchResult<FinanceAdjustDetail>();
		result.setResult(new ArrayList<FinanceAdjustDetail>());
		result.setCount(temp_result.getCount());
		for (Object[] obj : temp_result.getResult())
		{
			FinanceAdjustDetail detail = (FinanceAdjustDetail) obj[0];
			FinanceAdjust master = (FinanceAdjust) obj[1];
			detail.setMaster(master);
			result.getResult().add(detail);
		}
		return result;
	}
	
	@Override
	public SearchResult<SaleReconcilDetail> findSaleShouldReceive(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "a");
		query.createAlias(SaleReconcil.class, "b");

		query.addProjection(Projections.property("a, b"));
		query.eqProperty("a.masterId", "b.id");

		query.createAlias(Customer.class, "c");
		query.addProjection(Projections.property("b, c"));
		query.eqProperty("b.customerId", "c.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + queryParam.getCustomerBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCustomerMaterialCode()))
		{
			query.like("a.customerMaterialCode", "%" + queryParam.getCustomerMaterialCode() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getSaleBillNo()))
		{
			query.like("a.saleOrderBillNo", "%" + queryParam.getSaleBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerName() != null && !"".equals(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getProductName() != null && !"".equals(queryParam.getProductName()))
		{
			query.like("a.productName", "%" + queryParam.getProductName() + "%");
		}
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.reconcilTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.reconcilTime", queryParam.getDateMax());
		}

		if (queryParam.getCompleteFlag() == BoolValue.YES)
		{
			query.eq("a.isForceComplete", BoolValue.YES);// 工单是否强制完工

		}
		else
		{
			query.eq("a.isForceComplete", BoolValue.NO);// 工单是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.receiveMoney)"));
		query.eq("b.isCheck", BoolValue.YES);

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("a.isReceiveOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SaleReconcilDetail> result = new SearchResult<SaleReconcilDetail>();
		result.setResult(new ArrayList<SaleReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			SaleReconcilDetail detail = (SaleReconcilDetail) c[0];
			detail.setMaster((SaleReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
	
	@Override
	public SearchResult<OemReconcilDetail> findOemShouldReceive(QueryParam query_param)
	{
		DynamicQuery query = new CompanyDynamicQuery(OemReconcilDetail.class, "a");
		query.createAlias(OemReconcil.class, "b");

		query.addProjection(Projections.property("a, b"));
		query.eqProperty("a.masterId", "b.id");

		query.createAlias(Customer.class, "c");
		query.addProjection(Projections.property("b, c"));
		query.eqProperty("b.customerId", "c.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		if (CollectionUtils.isNotEmpty(query_param.getIds()))
		{
			query.in("a.id", query_param.getIds());
		}

		if (StringUtils.isNotBlank(query_param.getCustomerBillNo()))
		{
			query.like("a.customerBillNo", "%" + query_param.getCustomerBillNo() + "%");
		}

		if (StringUtils.isNotBlank(query_param.getOemOrderBillNo()))
		{
			query.like("a.oemOrderBillNo", "%" + query_param.getOemOrderBillNo() + "%");
		}
		if (StringUtils.isNotBlank(query_param.getBillNo()))
		{
			query.like("b.billNo", "%" + query_param.getBillNo() + "%");
		}
		if (query_param.getCustomerName() != null && !"".equals(query_param.getCustomerName()))
		{
			query.like("c.name", "%" + query_param.getCustomerName() + "%");
		}
		if (query_param.getProductName() != null && !"".equals(query_param.getProductName()))
		{
			query.like("a.productName", "%" + query_param.getProductName() + "%");
		}
		if (query_param.getProcedureName() != null && !"".equals(query_param.getProcedureName()))
		{
			query.like("a.procedureName", "%" + query_param.getProcedureName() + "%");
		}
		if (query_param.getDateMin() != null && !"".equals(query_param.getDateMin()))
		{
			query.ge("b.reconcilTime", query_param.getDateMin());
		}
		if (query_param.getDateMax() != null && !"".equals(query_param.getDateMax()))
		{
			query.le("b.reconcilTime", query_param.getDateMax());
		}

		if (query_param.getCompleteFlag() == BoolValue.YES)
		{
			query.eq("a.isForceComplete", BoolValue.YES);// 工单是否强制完工
		}
		else
		{
			query.eq("a.isForceComplete", BoolValue.NO);// 工单是否强制完工
			query.eq("b.isForceComplete", BoolValue.NO);// 工单是否强制完工
		}
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.receiveMoney)"));
		query.eq("b.isCheck", BoolValue.YES);

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("a.isReceiveOver", BoolValue.NO);
		query.setPageIndex(query_param.getPageNumber());
		query.setPageSize(query_param.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OemReconcilDetail> result = new SearchResult<OemReconcilDetail>();
		result.setResult(new ArrayList<OemReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			OemReconcilDetail detail = (OemReconcilDetail) c[0];
			detail.setMaster((OemReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<FinanceCompanyArrearsVo> findCustomerCompanyArrears(QueryParam queryParam)
	{
		List<FinanceCompanyArrearsVo> resultList = new ArrayList<FinanceCompanyArrearsVo>();
		DynamicQuery query = new CompanyDynamicQuery(SaleReconcilDetail.class, "sd");
		query.addProjection(Projections.property("c.name as name,'客户' as type,ifnull(SUM(sd.money-sd.receiveMoney),0) as receiveMoney,0 as paymentMoney,0 as processMoney"));
		query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "s", "s.id=sd.masterId");

		query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=s.customerId");
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("c.name", "%" + queryParam.getSupplierName() + "%");
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("c.employeeId", employes);
		}
		query.eq("sd.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		query.addGourp("s.customerId");
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);

		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				FinanceCompanyArrearsVo vo = ObjectHelper.mapToObject(map, FinanceCompanyArrearsVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		return resultList;
	}

	@Override
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceiveDetail.class, "d");
		query.createAlias(FinanceReceive.class, JoinType.LEFTJOIN, "m", "m.id=d.masterId");
		query.createAlias(SaleReconcilDetail.class, JoinType.LEFTJOIN, "s", "s.id=d.sourceDetailId");
		query.addProjection(Projections.property("count(1)"));
		query.setQueryType(QueryType.JDBC);
		query.eq("s.saleOrderBillNo", saleOrderBillNo);
		query.eq("d.productId", productId);
		query.eq("m.isCheck", BoolValue.NO);
		@SuppressWarnings("rawtypes")
		SearchResult<HashMap> map = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		return Integer.parseInt(map.getResult().get(0).get("count(1)").toString()) == 0;
	}

	@Override
	public List<FinanceReceiveDetail> findShouldReceiveMoneyByCustomer(QueryParam queryParam)
	{
		// DynamicQuery query = new CompanyDynamicQuery(Receive.class);
		// if (queryParam.getDateMin() != null && queryParam.getDateMax() != null)
		// {
		// query.between("createTime", queryParam.getDateMin(), queryParam.getDateMax());
		// }
		//
		// if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		// {
		// query.like("customerName", "%" + queryParam.getCustomerName() + "%");
		// }
		// List<Receive> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Receive.class);
		// return list;

		DynamicQuery query = new CompanyDynamicQuery(FinanceReceiveDetail.class, "b");
		query.createAlias(FinanceReceive.class, JoinType.LEFTJOIN, "a", "a.id=b.masterId");

		if (queryParam.getDateMin() != null && queryParam.getDateMax() != null)
		{
			query.between("a.createTime", queryParam.getDateMin(), queryParam.getDateMax());
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("a.customerName", "%" + queryParam.getCustomerName() + "%");
		}

		if (null != queryParam.getCustomerId())
		{
			query.eq("a.customerId", queryParam.getCustomerId());
		}

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		List<FinanceReceiveDetail> result = Lists.newArrayList();
		for (Object[] c : temp_result.getResult())
		{
			FinanceReceiveDetail detail = (FinanceReceiveDetail) c[0];
			detail.setMaster((FinanceReceive) c[1]);
			result.add(detail);
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentShouldMoney(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();
		DynamicQuery query = null;
		// 查询本期应收
		if (BoolValue.YES.equals(queryParam.getIsOem()))
		{
			query = new CompanyDynamicQuery(OemReconcilDetail.class, "prd");
			query.addProjection(Projections.property("pr.customerId as customerId,s.id as id, s.name as name,SUM(prd.money) AS shouldMoney"));
			query.createAlias(OemReconcil.class, JoinType.LEFTJOIN, "pr", "pr.id=prd.masterId");
		} else 
		{
			query = new CompanyDynamicQuery(SaleReconcilDetail.class, "prd");
			query.addProjection(Projections.property("pr.customerId as customerId,s.id as id, s.name as name,SUM(prd.money) AS shouldMoney"));
			query.createAlias(SaleReconcil.class, JoinType.LEFTJOIN, "pr", "pr.id=prd.masterId");
//			DynamicQuery queryOem =  new CompanyDynamicQuery(OemReconcilDetail.class, "od");
//			queryOem.addProjection(Projections.property("o.customerId as customerId, od.money AS shouldMoney"));
//			query.createAlias(OemReconcil.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
//			
//			query.createAlias(queryOem, JoinType.LEFTJOIN, "oem", "prd.customerId=oem.customerId");
		}
		
		// 客户名称使用 left join
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "s", "s.id=pr.customerId");
		query.ge("pr.createTime", dateMin);
		query.le("pr.createTime", dateMax);
		if (null != queryParam.getCustomerIdList())
		{
			query.in("pr.customerId", queryParam.getCustomerIdList());
		}
		query.eq("pr.companyId", UserUtils.getCompanyId());
		query.eq("s.companyId", UserUtils.getCompanyId()); // 一定要加性能问题
		query.eq("pr.isCheck", BoolValue.YES);
		query.addGourp("pr.customerId");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<FinanceShouldSumVo> resultList = new ArrayList<FinanceShouldSumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				FinanceShouldSumVo vo = ObjectHelper.mapToObject(map, FinanceShouldSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getResult().size());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentShouldResidue(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();

		// 本期预收：advance、本期实收（必须有收款明细，否则就是预收余额）：money、本期折扣：discount
		DynamicQuery query = new CompanyDynamicQuery(FinanceReceive.class, "pm");
		query.addProjection(Projections.property("pm.customerId as customerId, s.id as id, s.name as name, "
				+ "IFNULL(SUM(pm.discount),0) as discount,(IFNULL(SUM(pd.money),0) - IFNULL(SUM(pm.discount),0)) as money,IFNULL(SUM(pm.advance),0) as advance"));
		DynamicQuery query_sub = new CompanyDynamicQuery(FinanceReceiveDetail.class, "d");
		query_sub.addProjection(Projections.property("IFNULL(SUM(d.money),0) as money, d.masterId as masterId"));
		query_sub.addGourp("d.masterId");
		// 客户名称使用 left join
		query.createAlias(query_sub, JoinType.LEFTJOIN,"pd","pd.masterId=pm.id");
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "s", "s.id=pm.customerId");
		query.ge("pm.createTime", dateMin);
		query.le("pm.createTime", dateMax);
		if (null != queryParam.getCustomerIdList())
		{
			query.in("pm.customerId", queryParam.getCustomerIdList());
		}
		// 区分代工账款
//		if (BoolValue.YES.equals(queryParam.getIsOem()))
//		{
//			query.eq("d.sourceBillType", "OEM_OC");
//		}
		// query.eq("pm.companyId", UserUtils.getCompanyId());
		query.eq("s.companyId", UserUtils.getCompanyId()); // 一定要加性能问题
		query.eq("pm.isCheck", BoolValue.YES);
		query.eq("pm.isCancel", BoolValue.NO);
		query.addGourp("pm.customerId");
		query.setIsSearchTotalCount(true);
		query.setQueryType(QueryType.JDBC);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, HashMap.class);
		List<FinanceShouldSumVo> resultList = new ArrayList<FinanceShouldSumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				FinanceShouldSumVo vo = ObjectHelper.mapToObject(map, FinanceShouldSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getResult().size());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentWriteoffMoney(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();
		// 核销
		DynamicQuery queryWP = new CompanyDynamicQuery(FinanceWriteoffReceive.class, "wp");
		queryWP.addProjection(Projections.property("wp.customerId as customerId, s.id as id, SUM(wp.money) as money,SUM(wp.discount) as discount"));
		queryWP.ge("wp.createTime", dateMin);
		queryWP.le("wp.createTime", dateMax);
		queryWP.eq("wp.companyId", UserUtils.getCompanyId());
		queryWP.eq("wp.isCheck", BoolValue.YES);
		queryWP.eq("wp.isCancel", BoolValue.NO);
		queryWP.addGourp("wp.customerId");

		// 供应商名称使用 left join
		queryWP.createAlias(Customer.class, JoinType.LEFTJOIN, "s", "s.id=wp.customerId");
		if (null != queryParam.getCustomerIdList())
		{
			queryWP.in("wp.customerId", queryParam.getCustomerIdList());
		}
		queryWP.setIsSearchTotalCount(true);
		queryWP.setQueryType(QueryType.JDBC);
		queryWP.setPageIndex(queryParam.getPageNumber());
		queryWP.setPageSize(queryParam.getPageSize());
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryWP, HashMap.class);
		List<FinanceShouldSumVo> resultList = new ArrayList<FinanceShouldSumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				FinanceShouldSumVo vo = ObjectHelper.mapToObject(map, FinanceShouldSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getResult().size());
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentAdjustMoney(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();
		DynamicQuery queryWP = new CompanyDynamicQuery(FinanceAdjustDetail.class, "d");
		queryWP.createAlias(FinanceAdjust.class, JoinType.LEFTJOIN, "m", "d.masterId=m.id");
		queryWP.addProjection(Projections.property("d.businessId as customerId, d.businessId as id,SUM(d.adjustMoney) as adjustMoney"));
		queryWP.ge("m.createTime", dateMin);
		queryWP.le("m.createTime", dateMax);
		queryWP.eq("m.companyId", UserUtils.getCompanyId());
		queryWP.eq("m.isCheck", BoolValue.YES);
		queryWP.eq("m.isCancel", BoolValue.NO);
		queryWP.addGourp("d.businessId");

		// 供应商名称使用 left join
//		queryWP.createAlias(Customer.class, JoinType.LEFTJOIN, "s", "s.id=wp.customerId");
		if (null != queryParam.getCustomerIdList())
		{
			queryWP.in("d.businessId", queryParam.getCustomerIdList());
		}
		queryWP.setIsSearchTotalCount(true);
		queryWP.setQueryType(QueryType.JDBC);
		queryWP.setPageIndex(queryParam.getPageNumber());
		queryWP.setPageSize(queryParam.getPageSize());
		SearchResult<HashMap> mapResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(queryWP, HashMap.class);
		List<FinanceShouldSumVo> resultList = new ArrayList<FinanceShouldSumVo>();
		for (Map<String, Object> map : mapResult.getResult())
		{
			try
			{
				FinanceShouldSumVo vo = ObjectHelper.mapToObject(map, FinanceShouldSumVo.class);
				resultList.add(vo);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(resultList);
		result.setCount(mapResult.getResult().size());
		return result;
	}
}
