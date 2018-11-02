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
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.finance.vo.FinanceCompanyArrearsVo;
import com.huayin.printmanager.service.finance.vo.FinanceShouldSumVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceReceiveService
{
	/**
	 * <pre>
	 * 获取收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:02, think
	 */
	public FinanceReceive get(Long id);

	/**
	 * <pre>
	 * 获取收款单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:07, think
	 */
	public FinanceReceiveDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:15, think
	 */
	public FinanceReceive lock(Long id);

	/**
	 * <pre>
	 * 获取收款单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:24, think
	 */
	public List<FinanceReceiveDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存收款单
	 * </pre>
	 * @param order
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午2:09:33, think
	 */
	public FinanceReceive save(FinanceReceive order);

	/**
	 * <pre>
	 * 审核收款单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午2:09:39, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 审核所有收款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:46, think
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 作废收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:09:54, think
	 */
	public boolean cancel(Long id);

	/**
	 * <pre>
	 * 反作废收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:05, think
	 */
	public boolean cancelBack(Long id);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:14, think
	 */
	public SearchResult<FinanceReceive> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:21, think
	 */
	public SearchResult<FinanceReceiveDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找销售对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:28, think
	 */
	public SearchResult<SaleReconcilDetail> findSaleReconcilDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找客户期初明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:35, think
	 */
	public SearchResult<CustomerBeginDetail> findCustomerBeginDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 应收账款明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:42, think
	 */
	public SearchResult<SaleReconcilDetail> findSaleShouldReceive(QueryParam queryParam);

	/**
	 * <pre>
	 * 客户应收余额汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:10:49, think
	 */
	public List<FinanceCompanyArrearsVo> findCustomerCompanyArrears(QueryParam queryParam);

	/**
	 * <pre>
	 * 销售明细追踪检查收款单是否全部审核
	 * </pre>
	 * @param saleOrderBillNo
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:11:06, think
	 */
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId);

	/**
	 * <pre>
	 * 客户收款单（有一种情况，直接做期初的收款，不需要做对账单）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:11:12, think
	 */
	List<FinanceReceiveDetail> findShouldReceiveMoneyByCustomer(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询客户应收款
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 上午10:37:54, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentShouldMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询本期预收、本期实收、本期折扣
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 上午11:00:16, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentShouldResidue(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询核销单金额
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月2日 下午2:38:48, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentWriteoffMoney(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询代工对账应收款明细
	 * </pre>
	 * @param query_param
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:57:44, zhengby
	 */
	public SearchResult<OemReconcilDetail> findOemShouldReceive(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询代工对账单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 下午3:55:13, zhengby
	 */
	public SearchResult<OemReconcilDetail> findOemReconcilDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询财务调整单应收款类型明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午1:37:56, zhengby
	 */
	public SearchResult<FinanceAdjustDetail> findFinanceAdjustDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询财务调整单的调整金额
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 下午6:27:25, zhengby
	 */
	public SearchResult<FinanceShouldSumVo> findCurrentAdjustMoney(QueryParam queryParam);

}
