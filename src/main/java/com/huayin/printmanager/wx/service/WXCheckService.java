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
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.wx.vo.OutSourceCheckVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PaymentCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.PurchCheckVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.ReceiveCheckVo;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.SaleCheckVo;

/**
 * <pre>
 * 微信 - 订单审核
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXCheckService
{
	/**
	 * <pre>
	 * 销售订单审核（列表）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:05:38, think
	 */
	public SearchResult<SaleCheckVo> findSaleByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 销售明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:05:44, think
	 */
	public SearchResult<SaleCheckDetailVo> findSaleDetailById(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核销售订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:05:53, think
	 */
	public Boolean checkSale(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核销售订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:05:59, think
	 */
	public Boolean checkSaleAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购订单审核（列表）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:07, think
	 */
	public SearchResult<PurchCheckVo> findPurchByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:15, think
	 */
	public SearchResult<PurchCheckDetailVo> findPurchDetailById(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核采购订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:23, think
	 */
	public Boolean checkPurch(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核采购订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:30, think
	 */
	public Boolean checkPurchAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外加工单审核(列表)
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:37, think
	 */
	public SearchResult<OutSourceCheckVo> findOutSourceByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外加工单明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:45, think
	 */
	public SearchResult<OutSourceDetailCheckVo> findOutSourceDetailById(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核发外加工单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:52, think
	 */
	public Boolean checkOutSource(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核发外加工单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:06:58, think
	 */
	public Boolean checkOutSourceAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款审核列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:05, think
	 */
	public SearchResult<ReceiveCheckVo> findReceiveByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:11, think
	 */
	public SearchResult<FinanceReceiveDetail> findReceiveDetailById(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核收款单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:18, think
	 */
	public Boolean checkReceive(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核收款单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:26, think
	 */
	public Boolean checkReceiveAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款审核列表 
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:32, think
	 */
	public SearchResult<PaymentCheckVo> findPaymentByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:39, think
	 */
	public SearchResult<FinancePaymentDetail> findPaymentDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核付款单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:48, think
	 */
	public Boolean checkPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核付款单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:07:55, think
	 */
	public Boolean checkPaymentAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款核销审核列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:03, think
	 */
	public SearchResult<FinanceWriteoffReceive> findWriteoffReceiveByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 收款核销明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:10, think
	 */
	public SearchResult<FinanceWriteoffReceiveDetail> findWriteoffReceiveDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核收款核销单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:19, think
	 */
	public Boolean checkWriteoffReceive(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核收款核销单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:26, think
	 */
	public Boolean checkWriteoffReceiveAll(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款核销审核列表 
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:33, think
	 */
	public SearchResult<FinanceWriteoffPayment> findWriteoffPaymentByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 付款核销明细，结果需要分页所以用queryParam
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:39, think
	 */
	public SearchResult<FinanceWriteoffPaymentDetail> findWriteoffPaymentDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核付款核销单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:48, think
	 */
	public Boolean checkWriteoffPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量审核付款核销单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:08:57, think
	 */
	public Boolean checkWriteoffPaymentAll(QueryParam queryParam);
}
