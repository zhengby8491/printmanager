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

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.service.finance.vo.FinanceAccountLogSumVo;
import com.huayin.printmanager.service.finance.vo.FinanceCompanyArrearsVo;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 财务汇总
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月14日
 * @since 	  	 2.0, 2017年12月27日下午3:23:07,zhengby,代码重构
 */
@Controller
@RequestMapping(value = "${basePath}/finance/sum")
public class FinanceSumController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 应付账款汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:27:29, zhengby
	 */
	@RequestMapping(value = "payment")
	@RequiresPermissions("finance:sum:purchPayment")
	public String purchPayment()
	{
		return "finance/sum/purch_payment";
	}

	/**
	 * <pre>
	 * 功能 - 应付账款汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:27:08, zhengby
	 */
	@RequestMapping(value = "paymentList")
	@ResponseBody
	public SearchResult<FinanceShouldSumVo> paymentList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSumService().paymentList(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 应收账款汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:26:50, zhengby
	 */
	@RequestMapping(value = "receive")
	@RequiresPermissions("finance:sum:saleReceive")
	public String saleReceive()
	{
		return "finance/sum/sale_receive";
	}
	
	/**
	 * <pre>
	 * 页面 - 代工应收账款汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月20日 上午9:49:49, zhengby
	 */
	@RequestMapping(value = "oemReceive")
	@RequiresPermissions("finance:sum:oemreceive")
	public String oemReceive()
	{
		return "finance/sum/oem_receive";
	}

	/**
	 * <pre>
	 * 功能 - 应收账款汇总数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:26:35, zhengby
	 */
	@RequestMapping(value = "receiveList")
	@ResponseBody
	public SearchResult<FinanceShouldSumVo> receiveList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSumService().receiveList(queryParam);
	}
	
	/**
	 * <pre>
	 * 页面 - 往来单位欠款
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:25:48, zhengby
	 */
	@RequestMapping(value = "arrears")
	@RequiresPermissions("finance:sum:arrears")
	public String arrears()
	{
		return "finance/sum/arrears";
	}

	/**
	 * <pre>
	 * 功能 - 往来单位欠款数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:25:32, zhengby
	 */
	@RequestMapping(value = "arrearsList")
	@ResponseBody
	public SearchResult<FinanceCompanyArrearsVo> arrearsList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceCompanyArrearsVo> result = new SearchResult<FinanceCompanyArrearsVo>();
		try
		{
			List<FinanceCompanyArrearsVo> resultList = serviceFactory.getPaymentService().findOutSourceCompanyArrears(queryParam);
			resultList.addAll(serviceFactory.getPaymentService().findPurchCompanyArrears(queryParam));
			resultList.addAll(serviceFactory.getReceiveService().findCustomerCompanyArrears(queryParam));
			FinanceCompanyArrearsVo totalVo = new FinanceCompanyArrearsVo();
			BigDecimal totalReceiveMoney = new BigDecimal(0);
			BigDecimal totalPaymentMoney = new BigDecimal(0);
			BigDecimal totalProcessMoney = new BigDecimal(0);
			for (FinanceCompanyArrearsVo companyArrearsVo : resultList)
			{
				totalReceiveMoney = totalReceiveMoney.add(companyArrearsVo.getReceiveMoney());
				totalPaymentMoney = totalPaymentMoney.add(companyArrearsVo.getPaymentMoney());
				totalProcessMoney = totalProcessMoney.add(companyArrearsVo.getProcessMoney());
			}
			totalVo.setName("合计");
			totalVo.setPaymentMoney(totalPaymentMoney);
			totalVo.setProcessMoney(totalProcessMoney);
			totalVo.setReceiveMoney(totalReceiveMoney);
			totalVo.setBalanceMoney(totalReceiveMoney.subtract(totalProcessMoney).subtract(totalPaymentMoney));
			resultList.add(totalVo);
			result.setResult(resultList);
			result.setCount(resultList.size());
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * <pre>
	 * 页面 - 账户资金流水汇总
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:25:17, zhengby
	 */
	@RequestMapping(value = "accountlog")
	@RequiresPermissions("finance:sum:accountlog")
	public String accountlog()
	{
		return "finance/sum/accountlog";
	}

	/**
	 * <pre>
	 * 功能 - 账户资金流水汇总 数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 下午3:25:01, zhengby
	 */
	@RequestMapping(value = "accountlogList")
	@ResponseBody
	public SearchResult<FinanceAccountLogSumVo> accountlogList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceAccountLogSumVo> result = serviceFactory.getAccountLogService().findAccountLogSum(queryParam);
		FinanceAccountLogSumVo vo = new FinanceAccountLogSumVo();

		BigDecimal inTransMoney = new BigDecimal(0);
		BigDecimal outTransMoney = new BigDecimal(0);
		BigDecimal beginMoney = new BigDecimal(0);
		for (FinanceAccountLogSumVo accountLogSumVo : result.getResult())
		{
			beginMoney = beginMoney.add(accountLogSumVo.getBeginMoney());
			inTransMoney = inTransMoney.add(accountLogSumVo.getInTransMoney());
			outTransMoney = outTransMoney.add(accountLogSumVo.getOutTransMoney());
		}
		vo.setBeginMoney(beginMoney);
		vo.setInTransMoney(inTransMoney);
		vo.setOutTransMoney(outTransMoney);
		result.getResult().add(vo);
		return result;
	}
}
