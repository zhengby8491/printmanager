/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.finance;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.basic.AccountLog;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentAdvanceLog;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveAdvanceLog;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 资金帐户流水信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/log")
public class FinanceLogController extends BaseController
{
	/**
	 * <pre>
	 * 数据 - 资金帐户流水信息列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:27:19, think
	 */
	@RequestMapping(value = "ajaxAccountLogList")
	@RequiresPermissions("finance:log:accountLog")
	@ResponseBody
	public SearchResult<AccountLog> ajaxAccountLogList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getAccountLogService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 预付款日志列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:27:45, think
	 */
	@RequestMapping(value = "ajaxPaymentAdvanceLogList")
	@RequiresPermissions("finance:log:paymentAdvanceLog")
	@ResponseBody
	public SearchResult<FinancePaymentAdvanceLog> ajaxPaymentAdvanceLogList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getPaymentAdvanceLogService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 数据 - 预收款日志列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:27:59, think
	 */
	@RequestMapping(value = "ajaxReceiveAdvanceLogList")
	@RequiresPermissions("finance:log:paymentAdvanceLog")
	@ResponseBody
	public SearchResult<FinanceReceiveAdvanceLog> ajaxReceiveAdvanceLogList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getReceiveAdvanceLogService().findByCondition(queryParam);
	}
}
