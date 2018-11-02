/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.begin.AccountBegin;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.begin.AccountBeginService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 账户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@Service
public class AccountBeginServiceImpl extends BaseServiceImpl implements AccountBeginService
{
	@Override
	public AccountBegin get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(AccountBegin.class);
		query.eq("id", id);
		AccountBegin accountBegin = daoFactory.getCommonDao().getByDynamicQuery(query, AccountBegin.class);

		if (accountBegin != null)
		{
			accountBegin.setDetailList(this.getDetail(id));
		}
		return accountBegin;
	}

	@Override
	public List<AccountBeginDetail> getDetail(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(AccountBeginDetail.class);
		query.eq("masterId", id);
		List<AccountBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, AccountBeginDetail.class);
		return detailList;

	}

	@Override
	@Transactional
	public Long save(AccountBegin accountBegin)
	{
		BoolValue flag = accountBegin.getIsCheck();					// 标识是否保存并审核
		accountBegin.setIsCheck(BoolValue.NO);							// 默认未审核
		accountBegin.setBillType(BillType.BEGIN_ACCOUNT);
		accountBegin.setBillNo(UserUtils.createBillNo(BillType.BEGIN_ACCOUNT));
		accountBegin.setCompanyId(UserUtils.getCompanyId());
		accountBegin.setCreateTime(new Date());
		// 创建人优先取员工姓名
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			accountBegin.setCreateName(e.getName());
		}
		else
		{
			accountBegin.setCreateName(UserUtils.getUserName());
		}
		accountBegin.setCreateEmployeeId(UserUtils.getEmployeeId());
		accountBegin.setMemo(accountBegin.getMemo());
		daoFactory.getCommonDao().saveEntity(accountBegin);
		for (AccountBeginDetail detail : accountBegin.getDetailList())
		{
			Account account = daoFactory.getCommonDao().getEntity(Account.class, detail.getAccountId());
			account.setIsBegin(BoolValue.YES);
			detail.setMasterId(accountBegin.getId());
			detail.setCompanyId(UserUtils.getCompanyId());
			detail.setMemo(detail.getMemo());
			daoFactory.getCommonDao().saveEntity(detail);
		}
		if (flag == BoolValue.YES)
		{
			this.audit(accountBegin.getId());
		}
		return accountBegin.getId();

	}

	@Override
	@Transactional
	public Long update(AccountBegin accountBegin)
	{
		AccountBegin accountBegin_ = this.lockHasChildren(accountBegin.getId());
		// 先判断是否已经审核
		if (accountBegin_.getIsCheck() == BoolValue.YES)
		{
			throw new BusinessException("已审核，不能修改保存");
		}
		
		// 删除记录
		List<Long> old_detailIds = new ArrayList<Long>();
		List<Long> new_detailIds = new ArrayList<Long>();
		// ID集合
		for (AccountBeginDetail newItem : accountBegin.getDetailList())
		{
			new_detailIds.add(newItem.getId());
		}
		for (AccountBeginDetail oldItem : accountBegin_.getDetailList())
		{
			old_detailIds.add(oldItem.getId());
		}
		for (Long id : old_detailIds)
		{
			if (!new_detailIds.contains(id))// 新记录里找老记录的某条记录
			{
				Long accountId = daoFactory.getCommonDao().getEntity(AccountBeginDetail.class, id).getAccountId();
				Account account = daoFactory.getCommonDao().getEntity(Account.class, accountId);
				account.setIsBegin(BoolValue.NO);
				daoFactory.getCommonDao().deleteEntity(AccountBeginDetail.class, id);
			}
		}

		accountBegin_.setBeginTime(accountBegin.getBeginTime());
		accountBegin_.setMemo(accountBegin.getMemo());
		for (AccountBeginDetail detail : accountBegin.getDetailList())
		{
			if (detail.getId() != null)
			{
				DynamicQuery querySt = new CompanyDynamicQuery(AccountBeginDetail.class);
				querySt.eq("id", detail.getId());
				AccountBeginDetail accountBeginDetail = daoFactory.getCommonDao().getByDynamicQuery(querySt, AccountBeginDetail.class);
				accountBeginDetail.setAccountId(detail.getAccountId());
				accountBeginDetail.setBeginMoney(detail.getBeginMoney());
				accountBeginDetail.setBankNo(detail.getBankNo());
				accountBeginDetail.setBranchName(detail.getBranchName());
				accountBeginDetail.setCurrencyType(detail.getCurrencyType());
				accountBeginDetail.setMemo(detail.getMemo());
				daoFactory.getCommonDao().getEntity(Account.class, accountBeginDetail.getAccountId()).setIsBegin(BoolValue.YES);
				daoFactory.getCommonDao().updateEntity(accountBeginDetail);
			}
			else
			{
				detail.setMasterId(accountBegin.getId());
				detail.setCompanyId(UserUtils.getCompanyId());
				daoFactory.getCommonDao().getEntity(Account.class, detail.getAccountId()).setIsBegin(BoolValue.YES);
				daoFactory.getCommonDao().saveEntity(detail);
			}
		}
		if (accountBegin.getIsCheck() == BoolValue.YES)
		{
			this.audit(accountBegin.getId());
		}
		return accountBegin.getId();

	}

	@Override
	@Transactional
	public Boolean delete(Long id)
	{
		try
		{
			AccountBegin master = this.get(id);
			for (AccountBeginDetail detail : master.getDetailList())
			{
				Account account = daoFactory.getCommonDao().getEntity(Account.class, detail.getAccountId());
				account.setIsBegin(BoolValue.NO);
			}
			daoFactory.getCommonDao().deleteAllEntity(master.getDetailList());
			daoFactory.getCommonDao().deleteEntity(master);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Boolean audit(Long id)
	{
		AccountBegin accountBegin = this.get(id);
		if (!serviceFactory.getCommonService().audit(BillType.BEGIN_ACCOUNT, id, BoolValue.YES))
		{
			return false;
		}
		for (AccountBeginDetail detail : accountBegin.getDetailList())
		{
			serviceFactory.getAccountService().addMoney(detail.getAccountId(), detail.getBeginMoney(), AccountTransType.BEGIN, null, "");
		}

		return true;

	}

	@Override
	@Transactional
	public Boolean auditCancel(Long id)
	{
		AccountBegin accountBegin = this.get(id);
		if (!serviceFactory.getCommonService().audit(BillType.BEGIN_ACCOUNT, id, BoolValue.NO))
		{
			return false;
		}
		for (AccountBeginDetail detail : accountBegin.getDetailList())
		{
			serviceFactory.getAccountService().subtractMoney(detail.getAccountId(), detail.getBeginMoney(), AccountTransType.BEGIN, null, "");
		}
		return true;

	}
	
	@Override
	public SearchResult<AccountBegin> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(AccountBegin.class);
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
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, AccountBegin.class);
	}

	@Override
	public List<Long> getAccountIdList()
	{
		List<Long> result = new ArrayList<Long>();
		DynamicQuery query = new CompanyDynamicQuery(AccountBeginDetail.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.desc("createTime");
		List<AccountBeginDetail> detailList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, AccountBeginDetail.class);
		for (AccountBeginDetail accountBegin : detailList)
		{
			result.add(accountBegin.getAccountId());
		}
		return result;
	}

	@Override
	public AccountBegin lockHasChildren(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(AccountBegin.class);
		query.eq("id", id);
		AccountBegin order = daoFactory.getCommonDao().lockByDynamicQuery(query, AccountBegin.class, LockType.LOCK_WAIT).get(0);
		DynamicQuery query_detail = new CompanyDynamicQuery(AccountBeginDetail.class);
		query_detail.eq("masterId", id);
		List<AccountBeginDetail> detailList = daoFactory.getCommonDao().lockByDynamicQuery(query_detail, AccountBeginDetail.class, LockType.LOCK_WAIT);
		order.setDetailList(detailList);
		return order;
	}
}
