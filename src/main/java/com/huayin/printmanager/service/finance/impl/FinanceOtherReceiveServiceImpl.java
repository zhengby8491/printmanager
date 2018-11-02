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
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceiveDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.finance.FinanceOtherReceiveService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

@Service
public class FinanceOtherReceiveServiceImpl extends BaseServiceImpl implements FinanceOtherReceiveService
{

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#get(java.lang.Long)
	 */
	@Override
	public FinanceOtherReceive get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceive.class);
		query.eq("id", id);
		FinanceOtherReceive order = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceOtherReceive.class);

		order.setDetailList(this.getDetailList(id));
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#getDetail(java.lang.Long)
	 */
	@Override
	public FinanceOtherReceiveDetail getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceiveDetail.class);
		query.eq("id", id);
		FinanceOtherReceiveDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, FinanceOtherReceiveDetail.class);

		// PaymentDetail detail = daoFactory.getCommonDao().getEntity(PaymentDetail.class, id);
		detail.setMaster(daoFactory.getCommonDao().getEntity(FinanceOtherReceive.class, detail.getMasterId()));
		return detail;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#lock(java.lang.Long)
	 */
	@Override
	@Transactional
	public FinanceOtherReceive lock(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceive.class);
		query.eq("id", id);
		FinanceOtherReceive order = daoFactory.getCommonDao().lockByDynamicQuery(query, FinanceOtherReceive.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(FinanceOtherReceiveDetail.class);
		query_detail.eq("masterId", id);
		List<FinanceOtherReceiveDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, FinanceOtherReceiveDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#getDetailList(java.lang.Long)
	 */
	@Override
	public List<FinanceOtherReceiveDetail> getDetailList(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceiveDetail.class);
		query.eq("masterId", id);
		List<FinanceOtherReceiveDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, FinanceOtherReceiveDetail.class);
		return detailList;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherReceiveService#save(com.huayin.printmanager.persist.entity.
	 * finance.FinanceOtherReceive)
	 */
	@Override
	@Transactional(rollbackForClassName = { "RuntimeException", "Exception" })
	public FinanceOtherReceive save(FinanceOtherReceive order)
	{
		BoolValue flag = order.getIsCheck();
		order.setCompanyId(UserUtils.getCompanyId());
		order.setBillType(BillType.FINANCE_OTHER_REC);
		order.setBillNo(UserUtils.createBillNo(BillType.FINANCE_OTHER_REC));
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
			for (FinanceOtherReceiveDetail detail : order.getDetailList())
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
			// 账户加款
			ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_RECEIVE, order.getId().toString(), "客户收款单据编号[" + order.getBillNo() + "]");
			if (!accountDealResult.getIsSuccess())
			{
				/**
				 * 2018-10-24 09:40
				 * 修改使用BusinessException。		zhengxchn@163.com
				 * */
				//throw new Exception(accountDealResult.getMessage());
				throw new BusinessException(accountDealResult.getMessage());
			}
			serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_REC, order.getId(), BoolValue.YES);
		}
		return order;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#audit(java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.BoolValue)
	 */
	@Override
	@Transactional
	public boolean audit(Long id, BoolValue flag)
	{
		boolean bl = serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_REC, id, flag);
		if (bl)
		{
			FinanceOtherReceive order = daoFactory.getCommonDao().getEntity(FinanceOtherReceive.class, id);
			if (flag == BoolValue.YES)
			{
				// 账户加款
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_RECEIVE, order.getId().toString(), "客户收款作废单据编号[" + order.getBillNo() + "]");
				if (!accountDealResult.getIsSuccess())
				{
					/**
					 * 2018-10-24 09:40
					 * 修改使用BusinessException。		zhengxchn@163.com
					 * */
					//throw new Exception(accountDealResult.getMessage());
					throw new BusinessException(accountDealResult.getMessage());
				}
			}
			else
			{
				// 账户扣款
				ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().subtractMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_RECEIVE_CANCEL, order.getId().toString(), "客户收款单据编号[" + order.getBillNo() + "]");
				if (!accountDealResult.getIsSuccess())
				{
					/**
					 * 2018-10-24 09:40
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
	 * @see com.huayin.printmanager.service.finance.FinanceOtherReceiveService#delete(java.lang.Long)
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
	 * com.huayin.printmanager.service.finance.FinanceOtherReceiveService#update(com.huayin.printmanager.persist.entity.
	 * finance.FinanceOtherReceive)
	 */
	@Override
	@Transactional
	public FinanceOtherReceive update(FinanceOtherReceive order)
	{
		BoolValue flag = order.getIsCheck();
		order.setIsCheck(BoolValue.NO);
		FinanceOtherReceive old_order = serviceFactory.getOtherReceiveService().lock(order.getId());
		// 判断是否已审核
		if (old_order.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		Map<Long, FinanceOtherReceiveDetail> old_detail_map = ConverterUtils.list2Map(old_order.getDetailList(), "id");

		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();

		List<FinanceOtherReceiveDetail> del_detail = new ArrayList<FinanceOtherReceiveDetail>();

		for (FinanceOtherReceiveDetail detail : old_order.getDetailList())
		{
			old_detailIds.add(detail.getId());
		}
		for (FinanceOtherReceiveDetail new_detail : order.getDetailList())
		{
			if (new_detail.getId() != null)
			{
				new_detailIds.add(new_detail.getId());
			}
		}

		for (FinanceOtherReceiveDetail new_detail : order.getDetailList())
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

					FinanceOtherReceiveDetail old_detail = old_detail_map.get(new_detail.getId());
					PropertyClone.copyProperties(old_detail, new_detail, false, null, new String[] { "memo", "money", "summary" });// 替换成新内容
					daoFactory.getCommonDao().updateEntity(old_detail);// 更新子表
				}

			}
			// 删除操作
			for (Long id : old_detailIds)
			{
				if (!new_detailIds.contains(id))
				{
					FinanceOtherReceiveDetail old_detail = old_detail_map.get(id);
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
				serviceFactory.getCommonService().audit(BillType.FINANCE_OTHER_REC, order.getId(), BoolValue.YES);
			}
		}
		if (flag == BoolValue.YES)
		{
			// 审核后，账户需加上收到的金额
			ServiceResult<Account> accountDealResult = serviceFactory.getAccountService().addMoney(order.getAccountId(), order.getMoney(), AccountTransType.OTHER_RECEIVE, order.getId().toString(), "客户收款作废单据编号[" + order.getBillNo() + "]");
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

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherReceiveService#findByCondition(com.huayin.printmanager.service.
	 * vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceOtherReceive> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceive.class, "p");
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("p.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("p.createTime", queryParam.getDateMax());
		}
		if (queryParam.getId() != null)
		{
			query.eq("p.id", queryParam.getId());
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

		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, FinanceOtherReceive.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.finance.FinanceOtherReceiveService#findDetailByCondition(com.huayin.printmanager.
	 * service.vo.QueryParam)
	 */
	@Override
	public SearchResult<FinanceOtherReceiveDetail> findDetailByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(FinanceOtherReceive.class, "b");
		query.createAlias(FinanceOtherReceiveDetail.class, JoinType.LEFTJOIN, "a", "a.masterId=b.id");

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

		SearchResult<FinanceOtherReceiveDetail> result = new SearchResult<FinanceOtherReceiveDetail>();
		result.setResult(new ArrayList<FinanceOtherReceiveDetail>());
		for (Object[] c : temp_result.getResult())
		{
			FinanceOtherReceiveDetail detail = (FinanceOtherReceiveDetail) c[1];
			if (detail == null)
			{
				detail = new FinanceOtherReceiveDetail();
				detail.setMasterId(((FinanceOtherReceive) c[0]).getId());
				detail.setMoney(((FinanceOtherReceive) c[0]).getMoney());
			}
			detail.setMaster((FinanceOtherReceive) c[0]);
			result.getResult().add(detail);
		}
		result.setCount(temp_result.getCount());
		return result;
	}
}
