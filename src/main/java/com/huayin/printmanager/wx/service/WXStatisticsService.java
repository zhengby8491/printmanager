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

import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 单据数量统计
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXStatisticsService
{
	/**
	 * <pre>
	 * 未生产订单数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:38:03, think
	 */
	public Integer getWorkSaleSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 未送货订单数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:42:51, think
	 */
	public Integer getDeliverSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 未入库采购数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:42:58, think
	 */
	public Integer getPurchSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 未到货发外数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:43:29, think
	 */
	public Integer getArriveSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 未收款销售数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:43:58, think
	 */
	public Integer getReceiveSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 未付款采购数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:04, think
	 */
	public Integer getPaymentPurchSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款发外数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:11, think
	 */
	public Integer getPaymentOutSourceSumQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 销售订单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:18, think
	 */
	public Integer getSaleOrderCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购订单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:25, think
	 */
	public Integer getPurchOrderCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外加工代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:31, think
	 */
	public Integer getOutSourceCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:39, think
	 */
	public Integer getReceiveCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:46, think
	 */
	public Integer getPaymentCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款核销单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:44:54, think
	 */
	public Integer getReceiveWriteCheckQty(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款核销单代审核数量
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:45:01, think
	 */
	public Integer getPaymentWriteCheckQty(QueryParam queryParam);

}
