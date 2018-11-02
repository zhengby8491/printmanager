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
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 预收核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinanceWriteoffReceiveServiceImpl extends BaseServiceImpl implements FinanceWriteoffReceiveService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#get(java.lang.Long)
	 */
	@Override
	public FinanceWriteoffReceive get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class);
		query.eq("id", id);
		FinanceWriteoffReceive order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffReceive.class);

		// WriteoffReceive order = daoFactory.getCommonDao().getEntity(WriteoffReceive.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#getDetail(java.lang.Long)
	 */
	@Override
	public FinanceWriteoffReceiveDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceiveDetail.class);
		query.eq("id", id);
		FinanceWriteoffReceiveDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceWriteoffReceiveDetail.class);

		// WriteoffReceiveDetail detail = daoFactory.getCommonDao().getEntity(WriteoffReceiveDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinanceWriteoffReceive.class, detail.getMasterId()));
		return detail;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#lock(java.lang.Long)
	 */
	@Override
	@Transactional
	public FinanceWriteoffReceive lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class);
		query.eq("id", id);
		FinanceWriteoffReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffReceive.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceWriteoffReceiveDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceWriteoffReceiveDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceWriteoffReceiveDetail.class, LockType.LOCK_WAIT);

		/*
		 * WriteoffReceive order = daoFactory.getCommonDao().lockObject(WriteoffReceive.class, id);
		 * List<WriteoffReceiveDetail> detailList = this.getDetailList(id); for (WriteoffReceiveDetail detail : detailList)
		 * { detail = daoFactory.getCommonDao().lockObject(WriteoffReceiveDetail.class, detail.getId()); }
		 */
		order.setDetailList(detailList);
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#getDetailList(java.lang.Long)
	 */
	public List<FinanceWriteoffReceiveDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceiveDetail.class);
		query.eq("masterId", id);
		List<FinanceWriteoffReceiveDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceWriteoffReceiveDetail.class);
		return detailList;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#save(com.huayin.printmanager.persist.entity.
	 * finance.FinanceWriteoffReceive)
	 */
	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinanceWriteoffReceive save(FinanceWriteoffReceive order)
	{
		BoolValue flag = order.getIsCheck();	// 标识是否保存并审核
		order.setIsCheck(BoolValue.NO);				// 默认未审核
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_WRITEOFF_RC);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_WRITEOFF_RC));
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
		// 扣除收收款
		// 创建预收款日志
		FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
		log.setTradeMode(FinanceTradeMode.SUBTRACT);
		log.setBillType(order.getBillType());
		log.setBillId(order.getId());
		log.setCompanyId(order.getCompanyId());
		log.setMoney(order.getMoney());
		log.setCustomerId(order.getCustomerId());
		log.setCustomerName(order.getCustomerName());
		log.setEmployeeId(order.getEmployeeId());
		log.setCreateTime(new Date());
		daoFactory.getCommonDao().saveEntity(log);
		// 减少客户预收款
		Customer customer = daoFactory.getCommonDao().lockObject(Customer.class, order.getCustomerId());
		customer.setAdvanceMoney(customer.getAdvanceMoney().subtract(order.getMoney()));
		daoFactory.getCommonDao().updateEntity(customer);
		UserUtils.clearCacheBasic(BasicType.CUSTOMER);// 清空缓存

		if (order.getDetailList() != null && order.getDetailList().size() > 0)
		{
			List<FinanceWriteoffReceiveDetail> newDetailList = new ArrayList<FinanceWriteoffReceiveDetail>();
			for (FinanceWriteoffReceiveDetail detail : order.getDetailList())
			{
				if (detail.getMoney().compareTo(new BigDecimal(0)) != 0)
				{
					newDetailList.add(detail);
				}
			}
			order.setDetailList(newDetailList);

			// 保存明细
			for (FinanceWriteoffReceiveDetail detail : newDetailList)
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
				else if (detail.getSourceBillType() == BillType.FINANCE_ADJUST)
				{//反写财务调整单
					FinanceAdjustDetail source = daoFactory.getCommonDao().lockObject(FinanceAdjustDetail.class, detail.getSourceDetailId());
					source.setReceiveOrPayMoney(source.getReceiveOrPayMoney().add(detail.getMoney()));
					if (source.getReceiveOrPayMoney().abs().compareTo(source.getAdjustMoney().abs()) != -1)
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
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#audit(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public boolean audit(Long id, BoolValue flag)
	{
		if (serviceFactory.getCommonService().audit(BillType.FINANCE_WRITEOFF_RC, id, flag))
		{
			FinanceWriteoffReceive order = serviceFactory.getWriteoffReceiveService().get(id);

			// 如果是作废审核，则需要增加客户预收款
			if (order.getIsCancel() == BoolValue.YES && flag == BoolValue.YES)
			{
				if (order.getDetailList() != null && order.getDetailList().size() > 0)
				{
					for (FinanceWriteoffReceiveDetail detail : order.getDetailList())
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
						}	else if (detail.getSourceBillType() == BillType.OEM_EC)
						{
							// 反写代工对账单
							OemReconcilDetail source = daoFactory.getCommonDao().lockObject(OemReconcilDetail.class, detail.getSourceDetailId());
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
					// 预收款大于0
					// 单据作废，创建预收款撤销日志
					FinanceReceiveAdvanceLog log = new FinanceReceiveAdvanceLog();
					log.setTradeMode(FinanceTradeMode.ADD);
					log.setBillType(order.getBillType());
					log.setBillId(order.getId());
					log.setCompanyId(order.getCompanyId());
					log.setMoney(order.getMoney());
					log.setCustomerId(order.getCustomerId());
					log.setCustomerName(order.getCustomerName());
					log.setEmployeeId(order.getEmployeeId());
					log.setCreateTime(new Date());
					daoFactory.getCommonDao().saveEntity(log);
					// 增加客户预收款
					Customer Customer = daoFactory.getCommonDao().lockObject(Customer.class, order.getCustomerId());
					Customer.setAdvanceMoney(Customer.getAdvanceMoney().add(order.getMoney()));
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

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#cancel(java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean cancel(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class);
		query.eq("id", id);
		FinanceWriteoffReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffReceive.class, LockType.LOCK_WAIT).get(0);

		// WriteoffReceive order = daoFactory.getCommonDao().lockObject(WriteoffReceive.class, id);
		order.setIsCancel(BoolValue.YES);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#cancelBack(java.lang.Long)
	 */
	@Override
	@Transactional
	public boolean cancelBack(Long id)
	{

		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class);
		query.eq("id", id);
		FinanceWriteoffReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceWriteoffReceive.class, LockType.LOCK_WAIT).get(0);

		// WriteoffReceive order = daoFactory.getCommonDao().lockObject(WriteoffReceive.class, id);
		order.setIsCancel(BoolValue.NO);
		daoFactory.getCommonDao().updateEntity(order);
		return true;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#findByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceWriteoffReceive> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class, "w");
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("w.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("w.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("w.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getCustomerName() != null)
		{
			query.like("w.customerName", "%" + queryParam.getCustomerName() + "%");
		}
		if (queryParam.getIsCancel() != null)
		{
			query.eq("w.isCancel", queryParam.getIsCancel());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("w.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getCustomerId() != null)
		{
			query.eq("w.customerId", queryParam.getCustomerId());
		}
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Customer.class, JoinType.LEFTJOIN, "c", "c.id=w.customerId");
			query.inArray("c.employeeId", employes);
		}
		query.eq("w.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("w.createTime");
		// 单表查询
		if (employes.length <= 0)
		{
			return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceWriteoffReceive.class);
		}
		// 多表查询
		else
		{
			SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

			SearchResult<FinanceWriteoffReceive> result = new SearchResult<FinanceWriteoffReceive>();
			result.setResult(new ArrayList<FinanceWriteoffReceive>());
			for (Object[] c : temp_result.getResult())
			{
				FinanceWriteoffReceive receiver = (FinanceWriteoffReceive) c[0];
				result.getResult().add(receiver);
			}
			result.setCount(temp_result.getCount());
			return result;
		}
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#findDetailByCondition(com.huayin.printmanager
	 * .service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceWriteoffReceiveDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceWriteoffReceive.class, "b");
		query.createAlias(FinanceWriteoffReceiveDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");
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
		SearchResult<FinanceWriteoffReceiveDetail> result = new SearchResult<FinanceWriteoffReceiveDetail>();
		result.setResult(new ArrayList<FinanceWriteoffReceiveDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceWriteoffReceiveDetail detail = (FinanceWriteoffReceiveDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceWriteoffReceiveDetail();
				detail.setMasterId(((FinanceWriteoffReceive) c[0]).getId());
				detail.setSourceMoney(new BigDecimal(0));
				detail.setMoney(((FinanceWriteoffReceive) c[0]).getMoney());
				detail.setSourceBalanceMoney(new BigDecimal(0));
			}
			detail.setMaster((FinanceWriteoffReceive) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceWriteoffReceiveService#findSaleReconcilDetail(com.huayin.
	 * printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<SaleReconcilDetail> findSaleReconcilDetail(QueryParam queryParam)
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

		query.eq("a.isForceComplete", BoolValue.NO);// 工序是否强制完工
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

}
