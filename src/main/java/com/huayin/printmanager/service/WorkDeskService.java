/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import java.math.BigDecimal;
import java.util.Map;

import com.huayin.printmanager.persist.entity.sys.SystemVersionNotice;

/**
 * <pre>
 * 框架 - 常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
public interface WorkDeskService
{
	/**
	 * <pre>
	 * 查销售总量
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:10:15, think
	 */
	public Integer getTotalSaleQty(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查销售总额
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:10:57, think
	 */
	public BigDecimal getTotalSaleMoney(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查采购总量
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:03, think
	 */
	public BigDecimal getTotalPurchQty(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查采购总额
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:09, think
	 */
	public BigDecimal getTotalPurchMoney(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查客户欠款
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:16, think
	 */
	public BigDecimal getCustomerDebt(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查供应商欠款
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:23, think
	 */
	public BigDecimal getSupplierDebt(String dateMin, String dateMax);

	/**
	 * <pre>
	 * 查成品库存总量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:29, think
	 */
	public Integer getProductStockQty();

	/**
	 * <pre>
	 * 查成品库存总成本
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:36, think
	 */
	public BigDecimal getProductStockMoney();

	/**
	 * <pre>
	 * 查材料库存总量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:43, think
	 */
	public BigDecimal getMaterialStockQty();

	/**
	 * <pre>
	 * 查材料库存总成本
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:50, think
	 */
	public BigDecimal getMaterialStockMoney();

	/**
	 * <pre>
	 * 材料最小库存预警
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:11:56, think
	 */
	public Integer getMaterialMinStock();

	/**
	 * <pre>
	 * 3日未到货发外
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:02, think
	 */
	public Integer getNotArriveOutSource();

	/**
	 * <pre>
	 * 3日未入库采购
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:08, think
	 */
	public Integer getNotStockPurch();

	/**
	 * <pre>
	 * 3日未送货订单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:32, think
	 */
	public Integer getNotDeliveSale();

	/**
	 * <pre>
	 * 3日未付款账单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:38, think
	 */
	public Integer getNotPayment();

	/**
	 * <pre>
	 * 3日内未收款账单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:44, think
	 */
	public Integer getNotReceiveOrder();

	/**
	 * <pre>
	 * 销售订单待审核
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:51, think
	 */
	public Integer getNotCheckSale();

	/**
	 * <pre>
	 * 待审核采购订单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:12:57, think
	 */
	public Integer getNotCheckPurch();

	/**
	 * <pre>
	 * 待审核生产工单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:03, think
	 */
	public Integer getNotCheckWork();

	/**
	 * <pre>
	 * 待审核付款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:09, think
	 */
	public Integer getNotCheckPayment();

	/**
	 * <pre>
	 * 待审核付款核销单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:16, think
	 */
	public Integer getNotCheckWriteoffPayment();

	/**
	 * <pre>
	 * 待审核发外加工单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:22, think
	 */
	public Integer getNotCheckOutSource();

	/**
	 * <pre>
	 * 待审核收款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:28, think
	 */
	public Integer getNotCheckReceive();

	/**
	 * <pre>
	 * 待审核收款核销单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:34, think
	 */
	public Integer getNotCheckWriteoffReceive();

	/**
	 * <pre>
	 * 首页趋势图查每天或每月销售总金额
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param timeType
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:41, think
	 */
	public Map<String, BigDecimal> getTotalSaleMoneyByTimeType(String dateMin, String dateMax, String timeType);

	/**
	 * <pre>
	 * 首页趋势图查每天或每月材料总金额
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param timeType
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:48, think
	 */
	public Map<String, BigDecimal> getTotalMaterialMoneyByTimeType(String dateMin, String dateMax, String timeType);

	/**
	 * <pre>
	 * 首页趋势图查每天或每月外发总金额
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param timeType
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:13:54, think
	 */
	public Map<String, BigDecimal> getTotalOutSourceMoneyByTimeType(String dateMin, String dateMax, String timeType);

	/**
	 * <pre>
	 * 未生产订单数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:01, think
	 */
	public Integer getWorkSaleSumQty();

	/**
	 * <pre>
	 * 未送货订单数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:06, think
	 */
	public Integer getDeliverSumQty();

	/**
	 * <pre>
	 * 未送货订单数量（工单）
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:14, think
	 */
	public Integer getDeliverWorkSumQty();

	/**
	 * <pre>
	 * 未入库采购数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:20, think
	 */
	public Integer getPurchSumQty();

	/**
	 * <pre>
	 * 未到货发外数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:49, think
	 */
	public Integer getArriveSumQty();

	/**
	 * <pre>
	 * 未收款销售数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:14:55, think
	 */
	public Integer getReceiveSumQty();

	/**
	 * <pre>
	 * 未付款采购数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:01, think
	 */
	public Integer getPaymentPurchSumQty();

	/**
	 * <pre>
	 * 付款发外数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:07, think
	 */
	public Integer getPaymentOutSourceSumQty();

	/**
	 * <pre>
	 * 待采购物料数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:12, think
	 */
	public Integer getWorkPurchQty();

	/**
	 * <pre>
	 * 待领料物料数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:17, think
	 */
	public Integer getWorkTakeQty();

	/**
	 * <pre>
	 * 待采购对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:23, think
	 */
	public Integer getPurchReconcilQty();

	/**
	 * <pre>
	 * 待销售对战数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:30, think
	 */
	public Integer getSaleReconcilQty();

	/**
	 * <pre>
	 * 待加工对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:15:37, think
	 */
	public Integer getProcessReconcilQty();

	/**
	 * <pre>
	 * 待入库工单数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:16:02, think
	 */
	public Integer getWorkStockQty();

	/**
	 * <pre>
	 * 查是否需要弹出到期通知
	 * </pre>
	 * @return 0：正常用户，不需要提醒,1：正式用户7天到期通知，2：体验用户3天到期通知，3：正式用户到期通知，4：体验用户到期通知
	 * @since 1.0, 2018年1月11日 下午4:16:08, think
	 */
	public String queryExpire();

	/**
	 * <pre>
	 * 获取最新一条版本公告记录
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月11日 下午4:16:18, think
	 */
	public SystemVersionNotice getVersionNotice();
}
