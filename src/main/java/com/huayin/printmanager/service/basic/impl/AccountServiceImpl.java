/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.constants.ErrorCodeConstants.AccountCode;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.entity.basic.AccountLog;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.service.basic.AccountService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 账号信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl implements AccountService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#get(java.lang.Long)
	 */
	@Override
	public Account get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Account.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Account.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#getByBankNo(java.lang.String)
	 */
	@Override
	public Account getByBankNo(String bankNo)
	{
		DynamicQuery query = new CompanyDynamicQuery(Account.class);
		query.eq("bankNo", bankNo);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Account.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#lock(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Account lock(Long id)
	{
		return serviceFactory.getDaoFactory().getCommonDao().lockObject(Account.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.basic.AccountService#save(com.huayin.printmanager.persist.entity.basic.Account)
	 */
	@Override
	@Transactional
	public Account save(Account account)
	{
		account.setMoney(new BigDecimal(0));
		Account _accountNew = daoFactory.getCommonDao().saveEntity(account);
		UserUtils.clearCacheBasic(BasicType.ACCOUNT);
		return _accountNew;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.basic.AccountService#update(com.huayin.printmanager.persist.entity.basic.Account)
	 */
	@Override
	@Transactional
	public Account update(Account account)
	{
		Account _accountNew = daoFactory.getCommonDao().updateEntity(account);
		UserUtils.clearCacheBasic(BasicType.ACCOUNT);
		return _accountNew;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#addMoney(java.lang.Long, java.math.BigDecimal,
	 * com.huayin.printmanager.persist.enumerate.AccountTransType, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public ServiceResult<Account> addMoney(Long accountId, BigDecimal money, AccountTransType type, String referenceId, String remark)
	{
		if (Validate.validateObjectsNullOrEmpty(accountId))
		{
			return ServiceResultFactory.getServiceResult(AccountCode.ACCOUNT_ISNULL, null, "账户信息不能空，请添加账户信息");
		}
		logger.debug("开始对账户{" + accountId + "}增加金额。增加{" + money + "}");
		Account account = serviceFactory.getAccountService().lock(accountId);

		account.setMoney(account.getMoney().add(money));
		account.setLastTransTime(new Date());
		account = serviceFactory.getDaoFactory().getCommonDao().updateEntity(account);
		AccountLog log = new AccountLog();
		log.setCompanyId(UserUtils.getCompanyId());
		log.setAccountId(accountId);
		log.setTransType(type);
		log.setTradeMode(FinanceTradeMode.ADD);
		log.setTransMoney(money);
		log.setTransTime(new Date());
		log.setRemnantMoney(account.getMoney());
		log.setReferenceId(referenceId);
		log.setMemo(remark);
		serviceFactory.getDaoFactory().getCommonDao().saveEntity(log);
		logger.info("账户{" + accountId + "}金额增加成功");
		return new ServiceResult<Account>(account);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#subtractMoney(java.lang.Long, java.math.BigDecimal,
	 * com.huayin.printmanager.persist.enumerate.AccountTransType, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public ServiceResult<Account> subtractMoney(Long accountId, BigDecimal money, AccountTransType type, String referenceId, String remark)
	{
		if (Validate.validateObjectsNullOrEmpty(accountId))
		{
			return ServiceResultFactory.getServiceResult(AccountCode.ACCOUNT_ISNULL, null, "账户信息不能空，请添加账户信息");
		}
		logger.debug("开始对账户{" + accountId + "}扣除金额。扣除{" + money + "}");
		Account account = serviceFactory.getAccountService().lock(accountId);
		/*
		 * if (account.getMoney().compareTo(money) == -1) {// 金额不足 logger.error("账户{" + account.getBankNo() + "}余额不足，扣款失败");
		 * return ServiceResultFactory.getServiceResult(AccountCode.ACCOUNT_MONEY_NOT_ENOUGH, account, "账户{" + accountId +
		 * "}余额不足，扣款失败"); }
		 */
		account.setMoney(account.getMoney().subtract(money));
		account.setLastTransTime(new Date());
		account = serviceFactory.getDaoFactory().getCommonDao().updateEntity(account);
		AccountLog log = new AccountLog();
		log.setCompanyId(UserUtils.getCompanyId());
		log.setAccountId(accountId);
		log.setTransType(type);
		log.setTradeMode(FinanceTradeMode.SUBTRACT);
		log.setTransMoney(money);
		log.setTransTime(new Date());
		log.setRemnantMoney(account.getMoney());
		log.setReferenceId(referenceId);
		log.setMemo(remark);
		serviceFactory.getDaoFactory().getCommonDao().saveEntity(log);
		logger.info("账户{" + accountId + "}金额扣除成功");
		return new ServiceResult<Account>(account);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.basic.AccountService#quickSelect()
	 */
	@Override
	public List<Account> quickSelect(Boolean isBegin)
	{
		DynamicQuery query = new CompanyDynamicQuery(Account.class);
		if (null != isBegin)
		{
			query.eq("isBegin", isBegin ? BoolValue.YES : BoolValue.NO);
		} 
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Account.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.basic.AccountService#findByCondition(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<Account> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Account.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getBankNo()))
		{
			query.like("bankNo", "%" + queryParam.getBankNo() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Account.class);
	}
}
