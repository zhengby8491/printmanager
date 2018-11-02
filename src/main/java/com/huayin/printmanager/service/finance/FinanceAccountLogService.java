/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.AccountLog;
import com.huayin.printmanager.service.finance.vo.FinanceAccountLogSumVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 资金帐户流水信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceAccountLogService
{
	/**
	 * <pre>
	 * 资金帐户流水信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:28:36, think
	 */
	public SearchResult<AccountLog> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 账户资金流水汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:28:42, think
	 */
	public SearchResult<FinanceAccountLogSumVo> findAccountLogSum(QueryParam queryParam);
}
