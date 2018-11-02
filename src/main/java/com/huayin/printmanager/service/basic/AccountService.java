/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.math.BigDecimal;
import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Account;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 账号信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
public interface AccountService
{
	/**
	 * <pre>
	 * 根据id获取账户信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:18:44, think
	 */
	public Account get(Long id);

	/**
	 * <pre>
	 * 根据账户号码查询账号信息
	 * </pre>
	 * @param bankNo
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:18:51, think
	 */
	public Account getByBankNo(String bankNo);

	/**
	 * <pre>
	 * 锁定账户（行级锁）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:19:47, think
	 */
	public Account lock(Long id);

	/**
	 * <pre>
	 * 添加公司账户
	 * </pre>
	 * @param Account
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:18:57, think
	 */
	public Account save(Account Account);

	/**
	 * <pre>
	 * 修改公司账户
	 * </pre>
	 * @param Account
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:19:03, think
	 */
	public Account update(Account Account);

	/**
	 * <pre>
	 * 加钱
	 * </pre>
	 * @param accountId 账户ID
	 * @param money 金额
	 * @param type 交易类型
	 * @param referenceId 引用ID
	 * @param remark 备注
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:19:13, think
	 */
	public ServiceResult<Account> addMoney(Long accountId, BigDecimal money, AccountTransType type, String referenceId, String remark);

	/**
	 * <pre>
	 * 减钱
	 * </pre>
	 * @param accountId 账户ID
	 * @param money 金额
	 * @param type 交易类型
	 * @param referenceId 引用ID
	 * @param remark 备注
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:19:35, think
	 */
	public ServiceResult<Account> subtractMoney(Long accountId, BigDecimal money, AccountTransType type, String referenceId, String remark);

	/**
	 * <pre>
	 * 快速选择
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:20:06, think
	 */
	public List<Account> quickSelect(Boolean isBegin);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:20:00, think
	 */
	public SearchResult<Account> findByCondition(QueryParam queryParam);

}
