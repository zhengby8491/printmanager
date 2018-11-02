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
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 预付核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinanceWriteoffPaymentServiceImpl extends BaseServiceImpl implements FinanceWriteoffPaymentService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#get(java.lang.Long)
	 */
	@Override
	public FinanceWriteoffPayment get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class);
		query.eq("id", id);
		FinanceWriteoffPayment order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffPayment.class);

		// WriteoffPayment order = daoFactory.getCommonDao().getEntity(WriteoffPayment.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#getDetail(java.lang.Long)
	 */
	@Override
	public FinanceWriteoffPaymentDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPaymentDetail.class);
		query.eq("id", id);
		FinanceWriteoffPaymentDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffPaymentDetail.class);

		// WriteoffPaymentDetail detail = daoFactory.getCommonDao().getEntity(WriteoffPaymentDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinanceWriteoffPayment.class, detail.getMasterId()));
		return detail;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#lock(java.lang.Long)
	 */
	@Override
	@Transactional
	public FinanceWriteoffPayment lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class);
		query.eq("id", id);
		FinanceWriteoffPayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffPayment.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceWriteoffPaymentDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceWriteoffPaymentDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceWriteoffPaymentDetail.class, LockType.LOCK_WAIT);

		/*
		 * WriteoffPayment order = daoFactory.getCommonDao().lockObject(WriteoffPayment.class, id);
		 * List<WriteoffPaymentDetail> detailList = this.getDetailList(id); for (WriteoffPaymentDetail detail : detailList)
		 * { detail = daoFactory.getCommonDao().lockObject(WriteoffPaymentDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#getDetailList(java.lang.Long)
	 */
	public List<FinanceWriteoffPaymentDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPaymentDetail.class);
		query.eq("masterId", id);
		List<FinanceWriteoffPaymentDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffPaymentDetail.class);
		return detailList;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#save(com.huayin.printmanager.persist.entity.
	 * finance.FinanceWriteoffPayment)
	 */
	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinanceWriteoffPayment save(FinanceWriteoffPayment order)
	{
		BoolValue flag = order.getIsCheck();	// 标识是否保存并审核
		order.setIsCheck(BoolValue.NO);				// 默认未审核
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_WRITEOFF_PAY);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_WRITEOFF_PAY));
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
		// 扣除预付款

		// 创建预付款日志
		FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
		log.setTradeMode(FinanceTradeMode.SUBTRACT);
		log.setBillType(order.getBillType());
		log.setBillId(order.getId());
		log.setCompanyId(order.getCompanyId());
		log.setMoney(order.getMoney());
		log.setSupplierId(order.getSupplierId());
		log.setSupplierName(order.getSupplierName());
		log.setEmployeeId(order.getEmployeeId());
		log.setCreateTime(new Date());
		daoFactory.getCommonDao().saveEntity(log);
		// 减少供应商预付款
		Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, order.getSupplierId());
		supplier.setAdvanceMoney(supplier.getAdvanceMoney().subtract(order.getMoney()));
		daoFactory.getCommonDao().updateEntity(supplier);
		UserUtils.clearCacheBasic(BasicType.SUPPLIER);// 清空缓存

		if (order.getDetailList() != null && order.getDetailList().size() > 0)
		{
			List<FinanceWriteoffPaymentDetail> newDetailList = new ArrayList<FinanceWriteoffPaymentDetail>();
			for (FinanceWriteoffPaymentDetail detail : order.getDetailList())
			{
				if (detail.getMoney().compareTo(new BigDecimal(0)) != 0)
				{
					newDetailList.add(detail);
				}
			}
			order.setDetailList(newDetailList);
			// 保存明细
			for (FinanceWriteoffPaymentDetail detail : newDetailList)
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
				}
				else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
				{
					//反写财务调整单
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
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		if (flag == BoolValue.YES)
		{
			this.audit(order.getId(), BoolValue.YES);
		}
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#audit(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public boolean audit(Long id, BoolValue flag)
	{
		if (serviceFactory.getCommonService().audit(BillType.FINANCE_WRITEOFF_PAY, id, flag))
		{
			FinanceWriteoffPayment order = serviceFactory.getWriteoffPaymentService().get(id);

			// 如果是作废审核，则需要增加供应商预付款
			if (order.getIsCancel() == BoolValue.YES && flag == BoolValue.YES)
			{

				if (order.getDetailList() != null && order.getDetailList().size() > 0)
				{
					for (FinanceWriteoffPaymentDetail detail : order.getDetailList())
					{
						if (detail.getSourceBillType() == BillType.OUTSOURCE_OC)
						{// 反写发外对账单
							OutSourceReconcilDetail source = daoFactory.getCommonDao().lockObject(OutSourceReconcilDetail.class, detail.getSourceDetailId());
							source.setPaymentMoney(source.getPaymentMoney().subtract(detail.getMoney()));
							// 发外退货的情况，compareTo的结果是1（0 大于 负数）【BUG-3618】
							if (source.getSourceBillType() == BillType.OUTSOURCE_OR && source.getPaymentMoney().compareTo(source.getMoney()) == 1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							// 其他情况compareTo的结果是-1
							else if (source.getPaymentMoney().compareTo(source.getMoney()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.PURCH_PK)
						{// 反写采购对账单
							PurchReconcilDetail source = daoFactory.getCommonDao().lockObject(PurchReconcilDetail.class, detail.getSourceDetailId());
							source.setPaymentMoney(source.getPaymentMoney().subtract(detail.getMoney()));
							// 采购退货的情况，compareTo的结果是1（0 大于 负数）【BUG-3618】
							if (source.getSourceBillType() == BillType.PURCH_PR && source.getPaymentMoney().compareTo(source.getMoney()) == 1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							// 其他情况compareTo的结果是-1
							else if (source.getPaymentMoney().compareTo(source.getMoney()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.BEGIN_SUPPLIER)
						{
							//
							SupplierBeginDetail source = daoFactory.getCommonDao().lockObject(SupplierBeginDetail.class, detail.getSourceDetailId());
							source.setPaymentedMoney(source.getPaymentedMoney().subtract(detail.getMoney()));
							if (source.getPaymentedMoney().abs().compareTo(source.getPaymentMoney().abs()) == -1)
							{
								source.setIsPaymentOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}
						else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
						{//反写财务调整单
							FinanceAdjustDetail source = daoFactory.getCommonDao().lockObject(FinanceAdjustDetail.class, detail.getSourceDetailId());
							source.setReceiveOrPayMoney(source.getReceiveOrPayMoney().subtract(detail.getMoney()));
							if (source.getReceiveOrPayMoney().abs().compareTo(source.getAdjustMoney().abs()) == -1)
							{
								source.setIsReceiveOrPayOver(BoolValue.NO);
							}
							daoFactory.getCommonDao().updateEntity(source);
						}	
					}
				}
				if (order.getMoney().compareTo(new BigDecimal(0)) == 1)
				{

					// 预付款大于0
					// 单据作废，创建预付款撤销日志
					FinancePaymentAdvanceLog log = new FinancePaymentAdvanceLog();
					log.setTradeMode(FinanceTradeMode.ADD);
					log.setBillType(order.getBillType());
					log.setBillId(order.getId());
					log.setCompanyId(order.getCompanyId());
					log.setMoney(order.getMoney());
					log.setSupplierId(order.getSupplierId());
					log.setSupplierName(order.getSupplierName());
					log.setEmployeeId(order.getEmployeeId());
					log.setCreateTime(new Date());
					daoFactory.getCommonDao().saveEntity(log);

					// 增加供应商预付款
					Supplier supplier = daoFactory.getCommonDao().lockObject(Supplier.class, order.getSupplierId());
					supplier.setAdvanceMoney(supplier.getAdvanceMoney().add(order.getMoney()));
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#cancel(java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean cancel(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class);
		query.eq("id", id);
		FinanceWriteoffPayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffPayment.class, LockType.LOCK_WAIT).get(0);

		// WriteoffPayment order = daoFactory.getCommonDao().lockObject(WriteoffPayment.class, id);
		order.setIsCancel(BoolValue.YES);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#cancelBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean cancelBack(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class);
		query.eq("id", id);
		FinanceWriteoffPayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffPayment.class, LockType.LOCK_WAIT).get(0);

		// WriteoffPayment order = daoFactory.getCommonDao().lockObject(WriteoffPayment.class, id);
		order.setIsCancel(BoolValue.NO);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#findByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceWriteoffPayment> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getSupplierName() != null)
		{
			query.like("supplierName", "%" + queryParam.getSupplierName() + "%");
		}
		if (queryParam.getIsCancel() != null)
		{
			query.eq("isCancel", queryParam.getIsCancel());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getSupplierId() != null)
		{
			query.eq("supplierId", queryParam.getSupplierId());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceWriteoffPayment.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#findDetailByCondition(com.huayin.printmanager
	 * .service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceWriteoffPaymentDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffPayment.class, "b");
		query.createAlias(FinanceWriteoffPaymentDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");

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
			query.like("a.procedureName", "%" + queryParam.getProcedureName() + "%");
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
		SearchResult<FinanceWriteoffPaymentDetail> result = new SearchResult<FinanceWriteoffPaymentDetail>();
		result.setResult(new ArrayList<FinanceWriteoffPaymentDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceWriteoffPaymentDetail detail = (FinanceWriteoffPaymentDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceWriteoffPaymentDetail();
				detail.setMasterId(((FinanceWriteoffPayment) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceWriteoffPayment) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceWriteoffPayment) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#findOutSourceReconcilDetail(com.huayin.
	 * printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<OutSourceReconcilDetail> findOutSourceReconcilDetail(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(OutSourceReconcilDetail.class, "a");
		query.createAlias(OutSourceReconcil.class, "b");
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, JoinType.LEFTJOIN, "s", "s.id=b.supplierId");
			query.inArray("s.employeeId", employes);
		}
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
		if (queryParam.getSupplierId() != null)
		{
			query.eq("b.supplierId", queryParam.getSupplierId());
		}

		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffPaymentService#findPurchReconcilDetail(com.huayin.
	 * printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<PurchReconcilDetail> findPurchReconcilDetail(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(PurchReconcilDetail.class, "a");
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

		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
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

}
