/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.wx.vo.OutSourceScheduleVo;
import com.huayin.printmanager.wx.vo.ProduceScheduleVo;
import com.huayin.printmanager.wx.vo.PurchScheduleVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleScheduleVo;

/**
 * <pre>
 * 微信 - 进度追踪
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXScheduleService
{
	/**
	 * <pre>
	 * 销售订单进度追踪
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:32:40, think
	 */
	public SearchResult<SaleScheduleVo> findSaleScheduleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 生产工单进度追踪
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:32:48, think
	 */
	public SearchResult<ProduceScheduleVo> findWorkScheduleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购订单进度追踪 
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:32:55, think
	 */
	public SearchResult<PurchScheduleVo> findPurchScheduleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外加工进度追踪
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:33:02, think
	 */
	public SearchResult<OutSourceScheduleVo> findOutSourceDetailByCondition(QueryParam queryParam);

}
