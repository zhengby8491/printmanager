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
import com.huayin.printmanager.wx.vo.OutSourceNotArriveVo;
import com.huayin.printmanager.wx.vo.OutSourceNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotPaymentVo;
import com.huayin.printmanager.wx.vo.PurchNotStockVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleNotDeliveryVo;
import com.huayin.printmanager.wx.vo.SaleNotProductionVo;
import com.huayin.printmanager.wx.vo.SaleNotReceiveVo;

/**
 * <pre>
 * 微信 - 未清预警
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXWarnService
{
	/**
	 * <pre>
	 * 未生产订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:55:44, think
	 */
	public SearchResult<SaleNotProductionVo> findNotProductionSaleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 未送货订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:55:50, think
	 */
	public SearchResult<SaleNotDeliveryVo> findNotDeliverSaleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 未入库采购
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:55:57, think
	 */
	public SearchResult<PurchNotStockVo> findNotStockPurchByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 未到货发外
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:56:04, think
	 */
	public SearchResult<OutSourceNotArriveVo> findNotArriveOutSourceByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 未收款销售
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:56:11, think
	 */
	public SearchResult<SaleNotReceiveVo> findShouldReceive(QueryParam queryParam);

	/**
	 * <pre>
	 * 未付款采购
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:56:17, think
	 */
	public SearchResult<PurchNotPaymentVo> findPurchShouldPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 未付款发外
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:56:30, think
	 */
	public SearchResult<OutSourceNotPaymentVo> findOutSourceShouldPayment(QueryParam queryParam);
}
