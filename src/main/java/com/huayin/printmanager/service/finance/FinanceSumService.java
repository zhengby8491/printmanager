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
public interface FinanceSumService
{
	/**
	 * <pre>
	 * 应付账款汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午1:53:37, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> paymentList(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 应收账款汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午1:55:26, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> receiveList(QueryParam queryParam);
}
