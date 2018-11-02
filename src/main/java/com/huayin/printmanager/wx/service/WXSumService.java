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

import java.math.BigDecimal;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.wx.vo.FinanceDetailSumVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.StockMaterialDetailVo;
import com.huayin.printmanager.wx.vo.StockProductDetailVo;
import com.huayin.printmanager.wx.vo.SupOrCustSumVo;

/**
 * <pre>
 * 微信 - 汇总
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXSumService
{
	/**
	 * <pre>
	 * 下单金额 （根据时间段查询）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:39:10, think
	 */
	public BigDecimal getTotalMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 应收款
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:40:25, think
	 */
	public BigDecimal getReceiveMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 应付款
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:12, think
	 */
	public BigDecimal getPaymentMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 销售汇总 按客户汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:18, think
	 */
	public SearchResult<SupOrCustSumVo> findSaleCusotmerSumByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购或发外汇总 按供应商汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:24, think
	 */
	public SearchResult<SupOrCustSumVo> findPurOrOutSupSumByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 销售明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:31, think
	 */
	public SearchResult<SaleCheckDetailVo> findSaleDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:39, think
	 */
	public SearchResult<PurchCheckDetailVo> findPurchDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:46, think
	 */
	public SearchResult<OutSourceDetailCheckVo> findOutSourceDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 查总支出
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:52, think
	 */
	public BigDecimal getSumPaymentMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查总收入
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:46:58, think
	 */
	public BigDecimal getSumReceiveMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查财务汇总收款/付款明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:10, think
	 */
	public SearchResult<FinanceDetailSumVo> findFinanceSumDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取成品库存总数量
	 * </pre>
	 * @param companyId
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:18, think
	 */
	public Integer getProductStockQty(String companyId, String name);

	/**
	 * <pre>
	 * 获取成品库存总金额
	 * </pre>
	 * @param companyId
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:24, think
	 */
	public BigDecimal getProductStockMoney(String companyId, String name);

	/**
	 * <pre>
	 * 获取材料库存总数量
	 * </pre>
	 * @param companyId
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:33, think
	 */
	public BigDecimal getMaterialStockQty(String companyId, String name);

	/**
	 * <pre>
	 * 获取材料库存总金额
	 * </pre>
	 * @param companyId
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:39, think
	 */
	public BigDecimal getMaterialStockMoney(String companyId, String name);

	/**
	 * <pre>
	 * 成品库存明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:46, think
	 */
	public SearchResult<StockProductDetailVo> findStockProductList(QueryParam queryParam);

	/**
	 * <pre>
	 * 材料库存明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:53, think
	 */
	public SearchResult<StockMaterialDetailVo> findStockMaterialList(QueryParam queryParam);

}
