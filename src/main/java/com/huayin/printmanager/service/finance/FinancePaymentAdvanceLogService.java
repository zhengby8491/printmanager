package com.huayin.printmanager.service.finance;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.service.vo.QueryParam;

public interface FinancePaymentAdvanceLogService
{
	/**
	 * <pre>
	 * 预付款日志
	 * </pre>
	 * param supplierName 供应商名称
	 * @return
	 */
	public SearchResult<FinancePaymentAdvanceLog> findByCondition(QueryParam queryParam);
}
