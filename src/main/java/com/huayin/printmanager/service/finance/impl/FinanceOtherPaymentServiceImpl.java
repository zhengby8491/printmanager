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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPaymentDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.finance.FinanceOtherPaymentService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 其他付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class FinanceOtherPaymentServiceImpl extends BaseServiceImpl implements FinanceOtherPaymentService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#get(java.lang.Long)
	 */
	@Override
	public FinanceOtherPayment get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPayment.class);
		query.eq("id", id);
		FinanceOtherPayment order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceOtherPayment.class);

		// Payment order = daoFactory.getCommonDao().getEntity(Payment.class, id);
		order.setDetailList(this.getDetailList(id));
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#getDetail(java.lang.Long)
	 */
	@Override
	public FinanceOtherPaymentDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPaymentDetail.class);
		query.eq("id", id);
		FinanceOtherPaymentDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceOtherPaymentDetail.class);

		// PaymentDetail detail = daoFactory.getCommonDao().getEntity(PaymentDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinanceOtherPayment.class, detail.getMasterId()));
		return detail;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#lock(java.lang.Long)
	 */
	@Override
	@Transactional
	public FinanceOtherPayment lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPayment.class);
		query.eq("id", id);
		FinanceOtherPayment order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceOtherPayment.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceOtherPaymentDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceOtherPaymentDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceOtherPaymentDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#getDetailList(java.lang.Long)
	 */
	@Override
	public List<FinanceOtherPaymentDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPaymentDetail.class);
		query.eq("masterId", id);
		List<FinanceOtherPaymentDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceOtherPaymentDetail.class);
		return detailList;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherPaymentService#save(com.huayin.printmanager.persist.entity.
	 * finance.FinanceOtherPayment)
	 */
	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinanceOtherPayment save(FinanceOtherPayment order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_OTHER_PAY);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_OTHER_PAY));
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
		order.setIsCheck(BoolValue.NO);			// 默认未审核
		order = daoFactory.getCommonDao().saveEntity(order);

		if (order.getDetailList() != null && order.getDetailList().size() != 0)
		{
			for (FinanceOtherPaymentDetail detail : order.getDetailList())
			{
				detail.setMasterId(order.getId());
				detail.setIsForceComplete(BoolValue.NO);
				detail.setUserNo(UserUtils.getUser().getUserNo());
				detail.setCompanyId(UserUtils.getCompanyId());
			}
		}
		daoFactory.getCommonDao().saveAllEntity(order.getDetailList());
		// 保存并审核按钮执行审核
		if (flag == BoolValue.YES)
		{
			// 账户扣款
			ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_PAYMENT, order.getId().toString(), "供应商付款单据编号[" + order.getBillNo() + "]");
			if (!accountDealResult.getIsSuccess())
			{
				/**
				 * 2018-10-24 09:40
				 * 修改使用BusinessException。		zhengxchn@163.com
				 * */
				//throw new Exception(accountDealResult.getMessage());
				throw new BusinessException(accountDealResult.getMessage());
			}
			serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_PAY, order.getId(), BoolValue.YES);
		}
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#audit(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public boolean audit(Long id, BoolValue flag)
	{
		boolean bl = serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_PAY, id, flag);
		if (bl)
		{
			FinanceOtherPayment order = daoFactory.getCommonDao().getEntity(FinanceOtherPayment.class, id);
			if (flag == BoolValue.YES)
			{
				// 账户扣款
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_PAYMENT, order.getId().toString(), "供应商付款单据编号[" + order.getBillNo() + "]");
				if (!accountDealResult.getIsSuccess())
				{
					/**
					 * 2018-10-24 09:35
					 * 修改使用BusinessException。		zhengxchn@163.com
					 * */
					//throw new Exception(accountDealResult.getMessage());
					throw new BusinessException(accountDealResult.getMessage());
				}
			}
			else
			{
				// 账户返回扣掉的款
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_PAYMENT_CANCEL, order.getId().toString(), "供应商付款单据编号[" + order.getBillNo() + "]");
				if (!accountDealResult.getIsSuccess())
				{
					/**
					 * 2018-10-24 09:35
					 * 修改使用BusinessException。		zhengxchn@163.com
					 * */
					//throw new Exception(accountDealResult.getMessage());
					throw new BusinessException(accountDealResult.getMessage());
				}
			}

		}
		return bl;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherPaymentService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceOtherPayment> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPayment.class, "p");
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (queryParam.getId() != null)
		{
			query.eq("p.id", queryParam.getId());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("p.createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("p.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (queryParam.getName() != null)
		{

			query.like("p.recCompany", "%" + queryParam.getName() + "%");
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("p.isCheck", queryParam.getAuditFlag());
		}
		query.eq("p.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("p.createTime");

		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceOtherPayment.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherPaymentService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceOtherPaymentDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherPayment.class, "b");
		query.createAlias(FinanceOtherPaymentDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");

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
		if (CollectionUtils.isNotEmpty(queryParam.getIds()))
		{
			query.in("b.id", queryParam.getIds());
		}
		if (queryParam.getAuditFlag() != null)
		{
			query.eq("b.isCheck", queryParam.getAuditFlag());
		}
		if (queryParam.getName() != null)
		{
			query.like("b.recCompany", "%" + queryParam.getName() + "%");
		}

		query.eq("b.companyId", UserUtils.getCompanyId());
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("b.createTime");
		query.setIsSearchTotalCount(true);
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<FinanceOtherPaymentDetail> result = new SearchResult<FinanceOtherPaymentDetail>();
		result.setResult(new ArrayList<FinanceOtherPaymentDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceOtherPaymentDetail detail = (FinanceOtherPaymentDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceOtherPaymentDetail();
				detail.setMasterId(((FinanceOtherPayment) c[0]).getId());
				detail.setMoney(((FinanceOtherPayment) c[0]).getMoney());
			}
			detail.setMaster((FinanceOtherPayment) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherPaymentService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteEntity(this.get(id));
		daoFactory.getCommonDao().deleteAllEntity(this.getDetailList(id));
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherPaymentService#update(com.huayin.printmanager.persist.entity.
	 * finance.FinanceOtherPayment)
	 */
	@Override
	@Transactional
	public FinanceOtherPayment update(FinanceOtherPayment order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		FinanceOtherPayment old_order = serviceFactory.getOtherPaymentService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, FinanceOtherPaymentDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();

		List<FinanceOtherPaymentDetail> del_detail = new ArrayList<FinanceOtherPaymentDetail>();

		for (FinanceOtherPaymentDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (FinanceOtherPaymentDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (FinanceOtherPaymentDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() == null)
			{// 新增
				new_detail.setIsForceComplete(BoolValue.NO);
				new_detail.setMasterId(order.getId());
				new_detail.setCompanyId(UserUtils.getCompanyId());
				new_detail.setUserNo(UserUtils.getUser().getUserNo());
				daoFactory.getCommonDao().saveEntity(new_detail);
			}
			else
			{
				if (old_detailIds.contains(new_detail.getId()))
				{// 更新

					FinanceOtherPaymentDetail old_detail = old_detail_map.get(new_detail.getId());
					PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo", "money", "summary" });// 替换成新内容
					daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
				}

			}
			// 删除操作
			for (Long id : old_detailIds)
			{
				if (!new_detailIds.contains(id))
				{
					FinanceOtherPaymentDetail old_detail = old_detail_map.get(id);
					del_detail.add(old_detail);
				}
			}
			daoFactory.getCommonDao().deleteAllEntity(del_detail);

			PropertyClone.copyProperties(old_order, order, false, new String[] { "detailList" }, new String[] { "memo", "money", "summary" });// 替换新内容
			order.setUpdateName(UserUtils.getUserName());
			order.setUpdateTime(new Date());
			// 必须先保存子表，然后再保存主表
			daoFactory.getCommonDao().updateEntity(old_order);// 更新主表
			// 保存并审核按钮执行审核
			if (flag == BoolValue.YES)
			{
				serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_PAY, order.getId(), BoolValue.YES);

			}
		}
		// 审核完成后执行账户资金扣款
		if (flag == BoolValue.YES)
		{
			// 账户扣款
			ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_PAYMENT, order.getId().toString(), "供应商付款单据编号[" + order.getBillNo() + "]");
			if (!accountDealResult.getIsSuccess())
			{
				try
				{
					throw new Exception(accountDealResult.getMessage());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return order;
	}
}
