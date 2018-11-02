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

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.finance.vo.FinanceCompanyArrearsVo;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinancePaymentService
{
	/**
	 * <pre>
	 * 获取付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:51:22, think
	 */
	public FinancePayment get(Long id);

	/**
	 * <pre>
	 * 获取付款单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:51:27, think
	 */
	public FinancePaymentDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:51:34, think
	 */
	public FinancePayment lock(Long id);

	/**
	 * <pre>
	 * 获取付款单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:51:39, think
	 */
	public List<FinancePaymentDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存付款单
	 * </pre>
	 * @param order
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午1:51:50, think
	 */
	public FinancePayment save(FinancePayment order);

	/**
	 * <pre>
	 * 审核付款单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午1:51:55, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 作废付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:03, think
	 */
	public boolean cancel(Long id);

	/**
	 * <pre>
	 * 反作废付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:12, think
	 */
	public boolean cancelBack(Long id);

	/**
	 * <pre>
	 * 审核所有付款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:19, think
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:28, think
	 */
	public SearchResult<FinancePayment> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:30, think
	 */
	public SearchResult<FinancePaymentDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找发外对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:44, think
	 */
	public SearchResult<OutSourceReconcilDetail> findOutSourceReconcilDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找采购对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:50, think
	 */
	public SearchResult<PurchReconcilDetail> findPurchReconcilDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找供应商期初明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:52:56, think
	 */
	public SearchResult<SupplierBeginDetail> findSupplierBeginDetail(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 查询财务调整单应付款类型
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午2:18:06, zhengby
	 */
	public SearchResult<FinanceAdjustDetail> findFinanceAdjustDetailList(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 采购应收账款明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:03, think
	 */
	public SearchResult<PurchReconcilDetail> findPurchShouldPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 发外应付账款明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:11, think
	 */
	public SearchResult<OutSourceReconcilDetail> findOutSourceShouldPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 供应商应付余额汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:18, think
	 */
	public List<FinanceCompanyArrearsVo> findPurchCompanyArrears(QueryParam queryParam);

	/**
	 * <pre>
	 * 加工商应付余额汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:26, think
	 */
	public List<FinanceCompanyArrearsVo> findOutSourceCompanyArrears(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询期初应付款
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:33, think
	 */
	public List<SupplierBeginDetail> findPaymentMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询期初应收款
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:53:39, think
	 */
	public List<CustomerBeginDetail> findPaymentMoneyByCustomer(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询本期应付
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月21日 下午5:47:27, think
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentShouldMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询本期预付、本期实付、本期折扣
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月21日 下午6:18:26, think
	 */
	SearchResult<FinanceShouldSumVo> findCurrentShouldResidue(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询综合供应商的全部对账单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 上午10:57:38, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCompreSupplierPayment(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询核销单的金额
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 下午3:25:30, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentWriteoffMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询财务调整单的调整金额
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午6:18:00, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentAdjustMoney(QueryParam queryParam);

}
