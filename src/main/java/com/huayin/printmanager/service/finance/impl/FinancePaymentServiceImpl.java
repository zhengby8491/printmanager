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
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBegin;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.persist.enumerate.PaymentType;
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.service.finance.FinancePaymentService;
import com.huayin.printmanager.service.finance.vo.FinanceCompanyArrearsVo;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinancePaymentServiceImpl extends BaseServiceImpl implements FinancePaymentService
{
	@Override
	public FinancePayment get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class);
		query.eq("id", id);
		FinancePayment order = daoFactory.getCommonDao().getByDynamicQuery(query, FinancePayment.class);

		// Payment order = daoFactory.getCommonDao().getEntity(Payment.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	@Override
	public FinancePaymentDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePaymentDetail.class);
		query.eq("id", id);
		FinancePaymentDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinancePaymentDetail.class);

		// PaymentDetail detail = daoFactory.getCommonDao().getEntity(PaymentDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinancePayment.class, detail.getMasterId()));
		return detail;
	}

	@Override
	@Transactional
	public FinancePayment lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class);
		query.eq("id", id);
		FinancePayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinancePayment.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinancePaymentDetail.class);
		query_detail.eq("masterId", id);
		List<FinancePaymentDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinancePaymentDetail.class, LockType.LOCK_WAIT);

		/*
		 * Payment order = daoFactory.getCommonDao().lockObject(Payment.class, id); List<PaymentDetail> detailList =
		 * this.getDetailList(id); for (PaymentDetail detail : detailList) { detail =
		 * daoFactory.getCommonDao().lockObject(PaymentDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	public List<FinancePaymentDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePaymentDetail.class);
		query.eq("masterId", id);
		List<FinancePaymentDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinancePaymentDetail.class);
		return detailList;
	}

	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinancePayment save(FinancePayment order)
	{
		BoolValue flag = order.getIsCheck();	// 标识是否保存并审核
		order.setIsCheck(BoolValue.NO);				// 默认未审核
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_PAY);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_PAY));
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

		// 账户扣款
		ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.PAYMENT, order.getId().toString(), "供应商付款单据编号[" + order.getBillNo() + "]");
		if (!accountDealResult.getIsSuccess())
		{
			/**
			 * 2018-10-24 09:40
			 * 修改使用BusinessException。		zhengxchn@163.com
			 * */
			//throw new Exception(accountDealResult.getMessage());
			throw new BusinessException(accountDealResult.getMessage());
		}
		// 增加预付款
		if (order.getAdvance().compareTo(new BigDecimal(0)) == 1)
		{// 预付款大于0
			// 创建预付款日志
			FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
			log.setTradeMode(FinanceTradeMode.ADD);
			log.setBillType(order.getBillType());
			log.setBillId(order.getId());
			log.setCompanyId(order.getCompanyId());
			log.setMoney(order.getAdvance());
			log.setSupplierId(order.getSupplierId());
			log.setSupplierName(order.getSupplierName());
			log.setEmployeeId(order.getEmployeeId());
			log.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(log);
			// 增加供应商预付款
			Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, order.getSupplierId());
			supplier.setAdvanceMoney(supplier.getAdvanceMoney().add(order.getAdvance()));
			daoFactory.getCommonDao().updateEntity(supplier);
			UserUtils.clearCacheBasic(BasicType.SUPPLIER);// 清空缓存
		}
		if (order.getDetailList() != null && order.getDetailList().size() > 0)
		{// 付款类型
			order.setPaymentType(PaymentType.PAYMENT);
			List<FinancePaymentDetail> newDetailList = new ArrayList<FinancePaymentDetail>();
			for (FinancePaymentDetail detail : order.getDetailList())
			{
				if (detail.getMoney().compareTo(new BigDecimal(0)) != 0)
				{
					newDetailList.add(detail);
				}
			}
			order.setDetailList(newDetailList);
			// 保存明细
			for (FinancePaymentDetail detail : newDetailList)
			{
				detail.setCompanyId(UserUtils.getCompanyId());
				detail.setMasterId(order.getId());
				detail.setIsForceComplete(BoolValue.NO);
				detail.setUserNo(UserUtils.getUser().getUserNo());
				if (detail.getSourceBillType() == BillType.OUTSOURCE_OC)
				{// 反写发外对账单
					OutSourceReconcilDetail source = daoFactory.getCommonDao().lockObject(OutSourceReconcilDetail.class, detail.getSourceDetailId());
					source.setPaymentMoney(source.getPaymentMoney().add(detail.getMoney()));
					if (source.getPaymentMoney().compareTo(source.getMoney()) != -1)
					{
						source.setIsPaymentOver(BoolValue.YES);
					}
					daoFactory.getCommonDao().updateEntity(source);
				}
				else if (detail.getSourceBillType() == BillType.PURCH_PK)
				{// 反写采购对账单
					PurchReconcilDetail source = daoFactory.getCommonDao().lockObject(PurchReconcilDetail.class, detail.getSourceDetailId());
					source.setPaymentMoney(source.getPaymentMoney().add(detail.getMoney()));
					if (source.getPaymentMoney().compareTo(source.getMoney()) != -1)
					{
						source.setIsPaymentOver(BoolValue.YES);
					}
					daoFactory.getCommonDao().updateEntity(source);
				}
				else if (detail.getSourceBillType() == BillType.BEGIN_SUPPLIER)
				{
					SupplierBeginDetail source = daoFactory.getCommonDao().lockObject(SupplierBeginDetail.class, detail.getSourceDetailId());
					source.setPaymentedMoney(source.getPaymentedMoney().add(detail.getMoney()));
					if (source.getPaymentedMoney().compareTo(source.getPaymentMoney()) != -1)
					{
						source.setIsPaymentOver(BoolValue.YES);
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
		{// 预付款类型
			order.setPaymentType(PaymentType.ADVANCE);
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
		if (serviceFactory.getCommonService().audit(BillType.FINANCE_PAY, id, flag))
		{
			FinancePayment order = serviceFactory.getPaymentService().get(id);

			// 如果是作废审核，则需要减少供应商预付款
			if (order.getIsCancel() == BoolValue.YES && flag == BoolValue.YES)
			{// 预付款大于0
				// 账户退款
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.PAYMENT_CANCEL, order.getId().toString(), "供应商付款作废单据编号[" + order.getBillNo() + "]");
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
					for (FinancePaymentDetail detail : order.getDetailList())
					{
						if (detail.getSourceBillType() == BillType.OUTSOURCE_OC)
						{// 反写发外对账单
							OutSourceReconcilDetail source = daoFactory.getCommonDao().lockObject(OutSourceReconcilDetail.class, detail.getSourceDetailId());
							source.setPaymentMoney(source.getPaymentMoney().subtract(detail.getMoney()));
							if (source.getPaymentMoney().abs().compareTo(source.getMoney().abs()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.PURCH_PK)
						{// 反写采购对账单
							PurchReconcilDetail source = daoFactory.getCommonDao().lockObject(PurchReconcilDetail.class, detail.getSourceDetailId());
							source.setPaymentMoney(source.getPaymentMoney().subtract(detail.getMoney()));
							if (source.getPaymentMoney().abs().compareTo(source.getMoney().abs()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.BEGIN_SUPPLIER)
						{
							// 反应供应商期初
							SupplierBeginDetail source = daoFactory.getCommonDao().lockObject(SupplierBeginDetail.class, detail.getSourceDetailId());
							source.setPaymentedMoney(source.getPaymentedMoney().subtract(detail.getMoney()));
							if (source.getPaymentedMoney().abs().compareTo(source.getPaymentMoney().abs()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
						{
							// 反写财务调整单
							FinanceAdjustDetail source = daoFactory.getCommonDao().lockObject(FinanceAdjustDetail.class, detail.getSourceDetailId());
							source.setReceiveOrPayMoney(source.getReceiveOrPayMoney().subtract(detail.getMoney()));
							if (source.getReceiveOrPayMoney().compareTo(source.getAdjustMoney()) == -1)
							{
								source.setIsReceiveOrPayOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
					}
				}
				if (order.getAdvance().compareTo(new BigDecimal(0)) == 1)
				{
					// 单据作废，创建预付款撤销日志
					FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
					log.setTradeMode(FinanceTradeMode.SUBTRACT);
					log.setBillType(order.getBillType());
					log.setBillId(order.getId());
					log.setCompanyId(order.getCompanyId());
					log.setMoney(order.getAdvance());
					log.setSupplierId(order.getSupplierId());
					log.setSupplierName(order.getSupplierName());
					log.setEmployeeId(order.getEmployeeId());
					log.setCreateTime(new Date());
					daoFactory.getCommonDao().saveEntity(log);
					// 减少供应商预付款
					Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, order.getSupplierId());
					supplier.setAdvanceMoney(supplier.getAdvanceMoney().subtract(order.getAdvance()));
					daoFactory.getCommonDao().updateEntity(supplier);
				}

				UserUtils.clearCacheBasic(BasicType.SUPPLIER);// 清空缓存

			}
			return true;
		}
		else
		{
			return false;
		}

	}

	public boolean checkAll()
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class);
			query.eq("isCheck", BoolValue.NO);
			query.eq("isForceComplete", BoolValue.NO);
			query.eq("isCancel", BoolValue.NO);
			List<FinancePayment> paymentList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinancePayment.class);
			for (FinancePayment payment : paymentList)
			{
				payment.setIsCheck(BoolValue.YES);
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
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class);
		query.eq("id", id);
		FinancePayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinancePayment.class, LockType.LOCK_WAIT).get(0);

		// Payment order = daoFactory.getCommonDao().lockObject(Payment.class, id);
		order.setIsCancel(BoolValue.YES);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	@Override
	@Transactional
	public boolean cancelBack(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class);
		query.eq("id", id);
		FinancePayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinancePayment.class, LockType.LOCK_WAIT).get(0);

		// Payment order = daoFactory.getCommonDao().lockObject(Payment.class, id);
		order.setIsCancel(BoolValue.NO);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	@Override
	public SearchResult<FinancePayment> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class, "p");
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
		if (queryParam.getSupplierName() != null)
		{
			query.like("p.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getIsCancel() != null)
		{
			query.eq("p.isCancel", queryParam.getIsCancel());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("p.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("p.supplierId", queryParam.getSupplierId());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");
		// 单表查询
		if (employes.length <= 0)
		{
			return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinancePayment.class);
		}
		// 多表查询
		else
		{
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

			SearchResult<FinancePayment> result = new SearchResult<FinancePayment>();
			result.setResult(new ArrayList<FinancePayment>());
			for (Object[] c : temp_result.getResult())
			{
				FinancePayment receiver = (FinancePayment) c[0];
				result.getResult().add(receiver);
			}
			result.setCount(temp_result.getCount());
			return result;
		}
	}

	@Override
	public SearchResult<FinancePaymentDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class, "b");
		query.createAlias(FinancePaymentDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
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
		if (queryParam.getSupplierName() != null)
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
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

		SearchResult<FinancePaymentDetail> result = new SearchResult<FinancePaymentDetail>();
		result.setResult(new ArrayList<FinancePaymentDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinancePaymentDetail detail = (FinancePaymentDetail) c[1];
			if (detail == null)
			{
				detail = new FinancePaymentDetail();
				detail.setMasterId(((FinancePayment) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinancePayment) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinancePayment) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<OutSourceReconcilDetail> findOutSourceReconcilDetail(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "a");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(OutSourceReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
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
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}
		if (queryParam.getId() != null && !"".equals(queryParam.getId()))
		{
			query.eq("a.id", queryParam.getId());
		}

		query.eq("b.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isPaymentOver", BoolValue.NO);// 工序是否已完成付款

		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OutSourceReconcilDetail> result = new SearchResult<OutSourceReconcilDetail>();
		result.setResult(new ArrayList<OutSourceReconcilDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceReconcilDetail detail = (OutSourceReconcilDetail) c[0];
			detail.setMaster((OutSourceReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<PurchReconcilDetail> findPurchReconcilDetail(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "a");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(PurchReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
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
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}

		query.eq("b.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isPaymentOver", BoolValue.NO);// 工序是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<PurchReconcilDetail> result = new SearchResult<PurchReconcilDetail>();
		result.setResult(new ArrayList<PurchReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchReconcilDetail detail = (PurchReconcilDetail) c[0];
			detail.setMaster((PurchReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<SupplierBeginDetail> findSupplierBeginDetail(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBeginDetail.class, "a");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(SupplierBegin.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=a.supplierId");
			query.inArray("s.employeeId", employes);
		}
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
		if (queryParam.getSupplierId() != null)
		{
			query.eq("a.supplierId", queryParam.getSupplierId());
		}
		query.eq("b.isCheck", BoolValue.YES);// 已审核
		query.eq("a.isPaymentOver", BoolValue.NO);// 是否已完成付款
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<SupplierBeginDetail> result = new SearchResult<SupplierBeginDetail>();
		result.setResult(new ArrayList<SupplierBeginDetail>());
		for (Object[] c : temp_result.getResult())
		{
			SupplierBeginDetail detail = (SupplierBeginDetail) c[0];
			detail.setMaster((SupplierBegin) c[1]);
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
		//query.createAlias(Supplier.class, "c");
		//query.addProjection(Projections.property("b, c"));
		//query.eqProperty("a.businessId", "c.id");
//		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
//		if (employes.length > 0)
//		{
//			query.inArray("c.employeeId", employes);
//		}
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
		if (null != queryParam.getSupplierId())
		{
			query.eq("a.businessId", queryParam.getSupplierId());
		}
		//query.add(Restrictions.gtProperty("a.adjustMoney", "a.receiveOrPayMoney"));
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
	public SearchResult<PurchReconcilDetail> findPurchShouldPayment(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "a");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(PurchReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");

		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
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
		if (StringUtils.isNotBlank(queryParam.getOrderBillNo()))
		{
			query.like("a.orderBillNo", "%" + queryParam.getOrderBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSupplierName() != null && !"".equals(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getMaterialName() != null && !"".equals(queryParam.getMaterialName()))
		{
			query.like("a.materialName", "%" + queryParam.getMaterialName() + "%");
		}
		if (queryParam.getDateMin() != null && !"".equals(queryParam.getDateMin()))
		{
			query.ge("b.reconcilTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null && !"".equals(queryParam.getDateMax()))
		{
			query.le("b.reconcilTime", queryParam.getDateMax());
		}
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.paymentMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<PurchReconcilDetail> result = new SearchResult<PurchReconcilDetail>();
		result.setResult(new ArrayList<PurchReconcilDetail>());
		for (Object[] c : temp_result.getResult())
		{
			PurchReconcilDetail detail = (PurchReconcilDetail) c[0];
			detail.setMaster((PurchReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@Override
	public SearchResult<OutSourceReconcilDetail> findOutSourceShouldPayment(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "a");
		query.addProjection(Projections.property("a,b"));
		query.createAlias(OutSourceReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eqProperty("a.masterId", "b.id");

		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("a.id", queryParam.getIds());
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("a.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getOutSourceBillNo()))
		{
			query.like("a.outSourceBillNo", "%" + queryParam.getOutSourceBillNo() + "%");
		}

		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("b.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSupplierName() != null && !"".equals(queryParam.getSupplierName()))
		{
			query.like("b.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getProcedureName() != null && !"".equals(queryParam.getProcedureName()))
		{
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
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
		if (queryParam.getId() != null && !"".equals(queryParam.getId()))
		{
			query.eq("a.id", queryParam.getId());
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
		query.add(Restrictions.gtProperty("ABS(a.money)", "ABS(a.paymentMoney)"));
		query.eq("b.isCheck", BoolValue.YES);
		// query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
		query.eq("a.companyId", UserUtils.getCompanyId());
		query.eq("a.isPaymentOver", BoolValue.NO);
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<OutSourceReconcilDetail> result = new SearchResult<OutSourceReconcilDetail>();
		result.setResult(new ArrayList<OutSourceReconcilDetail>());

		for (Object[] c : temp_result.getResult())
		{
			OutSourceReconcilDetail detail = (OutSourceReconcilDetail) c[0];
			detail.setMaster((OutSourceReconcil) c[1]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentShouldMoney(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();
		DynamicQuery query = null;
		// 查询本期应付
		if (queryParam.getSupplierType() == SupplierType.MATERIAL)
		{
			query = new CompanyDynamicQuery(PurchReconcilDetail.class, "prd");
			query.addProjection(Projections.property("pr.supplierId, s.id AS id, s.name as name,SUM(prd.money) AS shouldMoney"));
			query.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "pr", "pr.id=prd.masterId");
		}
		else if (queryParam.getSupplierType() == SupplierType.PROCESS)
		{
			query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "prd");
			query.addProjection(Projections.property("pr.supplierId,s.id AS id, s.name as name,SUM(prd.money) AS shouldMoney"));
			query.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "pr", "pr.id=prd.masterId");
		}
		// 供应商名称使用 left join
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=pr.supplierId");
		if (null != dateMin)
		{
			query.ge("pr.createTime", dateMin);
		}
		query.le("pr.createTime", dateMax);
		if (null != queryParam.getSupplierIdList())
		{
			query.in("pr.supplierId", queryParam.getSupplierIdList());
		}
		query.eq("pr.companyId", UserUtils.getCompanyId());
		query.eq("pr.isCheck", BoolValue.YES);
		query.addGourp("pr.supplierId");
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

		// 本期预付:advance、本期实付（必须有付款明细，否则就是预付余额）:money、本期折扣:discount
		DynamicQuery query = new CompanyDynamicQuery(FinancePayment.class, "pm");
		query.addProjection(Projections.property("pm.supplierId as supplierId,s.id as id, s.name as name, "
				+ "IFNULL(SUM(pm.discount),0) as discount, (IFNULL(SUM(pd.money),0) - IFNULL(SUM(pm.discount),0)) as money,IFNULL(SUM(pm.advance),0) as advance"));
//		query.createAlias(FinancePaymentDetail.class, JoinType.LEFTJOIN, "d", "d.masterId=pm.id");
		// 子查询本期折扣
		DynamicQuery query_sub = new CompanyDynamicQuery(FinancePaymentDetail.class,"d");
		query_sub.addProjection(Projections.property("IFNULL(SUM(d.money),0) as money, d.masterId as masterId"));
		query_sub.addGourp("d.masterId");
		// 供应商名称使用 left join
		query.createAlias(query_sub, JoinType.LEFTJOIN,"pd","pd.masterId=pm.id");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=pm.supplierId");
		
		if (null != dateMin)
		{
			query.ge("pm.createTime", dateMin);
		}
		query.le("pm.createTime", dateMax);
		if (null != queryParam.getSupplierIdList())
		{
			query.in("pm.supplierId", queryParam.getSupplierIdList());
		}
		query.eq("pm.isCheck", BoolValue.YES);
		query.eq("pm.isCancel", BoolValue.NO);
		query.addGourp("pm.supplierId");
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
		DynamicQuery queryWP = new CompanyDynamicQuery(FinanceWriteoffPayment.class, "wp");
		
		queryWP.addProjection(Projections.property("wp.supplierId as supplierId, s.id as id, SUM(wp.money) as money,SUM(wp.discount) as discount"));
		if (null != dateMin)
		{
			queryWP.ge("wp.createTime", dateMin);
		}
		queryWP.le("wp.createTime", dateMax);
		queryWP.eq("wp.companyId", UserUtils.getCompanyId());
		queryWP.eq("wp.isCheck", BoolValue.YES);
		queryWP.eq("wp.isCancel", BoolValue.NO);
		queryWP.addGourp("wp.supplierId");

		// 供应商名称使用 left join
		queryWP.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=wp.supplierId");
		if (null != queryParam.getSupplierIdList())
		{
			queryWP.in("wp.supplierId", queryParam.getSupplierIdList());
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public SearchResult<FinanceShouldSumVo> findCurrentAdjustMoney(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		Date dateMin = queryParam.getDateMin();
		Date dateMax = queryParam.getDateMax();
		DynamicQuery queryWP = new CompanyDynamicQuery(FinanceAdjustDetail.class, "d");
		queryWP.createAlias(FinanceAdjust.class, JoinType.LEFTJOIN, "m", "d.masterId=m.id");
		queryWP.addProjection(Projections.property("d.businessId as supplierId,d.businessId as id, SUM(d.adjustMoney) as adjustMoney"));
		if (null != dateMin)
		{
			queryWP.ge("m.createTime", dateMin);
		}
		queryWP.le("m.createTime", dateMax);
		queryWP.eq("m.companyId", UserUtils.getCompanyId());
		queryWP.eq("m.isCheck", BoolValue.YES);
		queryWP.eq("m.isCancel", BoolValue.NO);
		queryWP.addGourp("d.businessId");

		// 供应商名称使用 left join
//		queryWP.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=wp.supplierId");
		if (null != queryParam.getSupplierIdList())
		{
			queryWP.in("d.businessId", queryParam.getSupplierIdList());
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
	public List<FinanceCompanyArrearsVo> findPurchCompanyArrears(QueryParam queryParam)
	{
		List<FinanceCompanyArrearsVo> resultList = new ArrayList<FinanceCompanyArrearsVo>();
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "pd");
		query.addProjection(Projections.property("p.supplierName as name,s.type as type,0 as receiveMoney,ifnull(SUM(pd.money-pd.paymentMoney),0) as paymentMoney,0 as processMoney"));
		query.createAlias(PurchReconcil.class, JoinType.LEFTJOIN, "p", "p.id=pd.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=p.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("s.name", "%" + queryParam.getSupplierName() + "%");
		}
		query.eq("pd.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		query.addGourp("p.supplierId");
		// 单表查询
		if (employes.length <= 0)
		{
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
		}
		// 多表查询
		else
		{
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

			for (Object[] c : temp_result.getResult())
			{
				OutSourceReconcilDetail receiver = (OutSourceReconcilDetail) c[0];
				try
				{
					Map<String, Object> map = (Map<String, Object>) ObjectHelper.objectToMap(receiver);
					FinanceCompanyArrearsVo vo = ObjectHelper.mapToObject(map, FinanceCompanyArrearsVo.class);
					resultList.add(vo);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return resultList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<FinanceCompanyArrearsVo> findOutSourceCompanyArrears(QueryParam queryParam)
	{
		List<FinanceCompanyArrearsVo> resultList = new ArrayList<FinanceCompanyArrearsVo>();
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "od");
		query.addProjection(Projections.property("o.supplierName as name,s.type as type,0 as receiveMoney,0 as paymentMoney,ifnull(SUM(od.money-od.paymentMoney),0) as processMoney"));
		query.createAlias(OutSourceReconcil.class, JoinType.LEFTJOIN, "o", "o.id=od.masterId");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=o.supplierId");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.inArray("s.employeeId", employes);
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("s.name", "%" + queryParam.getSupplierName() + "%");
		}
		query.eq("od.companyId", UserUtils.getCompanyId());
		query.setQueryType(QueryType.JDBC);
		query.addGourp("o.supplierId");
		// 单表查询
		if (employes.length <= 0)
		{
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
		}
		// 多表查询
		else
		{
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

			for (Object[] c : temp_result.getResult())
			{
				OutSourceReconcilDetail receiver = (OutSourceReconcilDetail) c[0];
				try
				{
					Map<String, Object> map = (Map<String, Object>) ObjectHelper.objectToMap(receiver);
					FinanceCompanyArrearsVo vo = ObjectHelper.mapToObject(map, FinanceCompanyArrearsVo.class);
					resultList.add(vo);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		return resultList;
	}

	@Override
	public List<SupplierBeginDetail> findPaymentMoney(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(SupplierBeginDetail.class, "sbd");
		query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "sbd.supplierId=s.id");
		query.add(Restrictions.or(Restrictions.eq("s.type", queryParam.getSupplierType()), Restrictions.eq("s.type", SupplierType.MATERIAL_AND_PROCESS)));

		if (queryParam.getDateMin() != null && queryParam.getDateMax() != null)
		{
			query.between("sbd.receiveTime", queryParam.getDateMin(), queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getSupplierName()))
		{
			query.like("sbd.supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (null != queryParam.getSupplierId())
		{
			query.eq("sbd.supplierId", queryParam.getSupplierId());
		}

		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		List<SupplierBeginDetail> result = new ArrayList<SupplierBeginDetail>();
		for (Object[] c : temp_result.getResult())
		{
			SupplierBeginDetail detail = (SupplierBeginDetail) c[0];

			result.add(detail);
		}
		return result;
	}

	@Override
	public List<CustomerBeginDetail> findPaymentMoneyByCustomer(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(CustomerBeginDetail.class);
		if (queryParam.getDateMin() != null && queryParam.getDateMax() != null)
		{
			query.between("receiveTime", queryParam.getDateMin(), queryParam.getDateMax());
		}

		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (null != queryParam.getCustomerId())
		{
			query.eq("customerId", queryParam.getCustomerId());
		}
		List<CustomerBeginDetail> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, CustomerBeginDetail.class);
		return list;
	}

	@Override
	public SearchResult<FinanceShouldSumVo> findCompreSupplierPayment(QueryParam queryParam)
	{
		SearchResult<FinanceShouldSumVo> result = new SearchResult<FinanceShouldSumVo>();
		List<FinanceShouldSumVo> list = Lists.newArrayList();
		SearchResult<PurchReconcilDetail> result1 = this.findPurchReconcilDetail(queryParam);
		SearchResult<OutSourceReconcilDetail> result2 = this.findOutSourceReconcilDetail(queryParam);
		for (PurchReconcilDetail d : result1.getResult())
		{
			FinanceShouldSumVo vo = new FinanceShouldSumVo();
			vo.setMoney(d.getMoney());
			vo.setPaymentMoney(d.getPaymentMoney());
			list.add(vo);
		}
		for (OutSourceReconcilDetail o : result2.getResult())
		{
			FinanceShouldSumVo vo = new FinanceShouldSumVo();
			vo.setMoney(o.getMoney());
			vo.setPaymentMoney(o.getPaymentMoney());
			list.add(vo);
		}
		result.setResult(list);
		result.setCount(list.size());
		return result;
	}
}
