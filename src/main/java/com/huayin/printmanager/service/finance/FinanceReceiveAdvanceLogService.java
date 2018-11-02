package com.huayin.printmanager.service.finance;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.service.vo.QueryParam;

public interface FinanceReceiveAdvanceLogService
{
	/**
	 * <pre>
	 * 预收款日志
	 * </pre>
	 * @param queryParam
	 * @return
	 */
	public SearchResult<FinanceReceiveAdvanceLog> findByCondition(QueryParam queryParam);
}
