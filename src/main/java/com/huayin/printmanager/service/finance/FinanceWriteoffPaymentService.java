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
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 预付核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceWriteoffPaymentService
{
	/**
	 * <pre>
	 * 获取预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:13, think
	 */
	public FinanceWriteoffPayment get(Long id);

	/**
	 * <pre>
	 * 获取预付核销单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:18, think
	 */
	public FinanceWriteoffPaymentDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:26, think
	 */
	public FinanceWriteoffPayment lock(Long id);

	/**
	 * <pre>
	 * 获取预付核销单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:31, think
	 */
	public List<FinanceWriteoffPaymentDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存预付核销单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:39, think
	 */
	public FinanceWriteoffPayment save(FinanceWriteoffPayment order);

	/**
	 * <pre>
	 * 审核预付核销单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:44, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 作废预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:25:52, think
	 */
	public boolean cancel(Long id);

	/**
	 * <pre>
	 * 反作废预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:26:00, think
	 */
	public boolean cancelBack(Long id);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:26:09, think
	 */
	public SearchResult<FinanceWriteoffPayment> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:26:14, think
	 */
	public SearchResult<FinanceWriteoffPaymentDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找发外对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:26:21, think
	 */
	public SearchResult<OutSourceReconcilDetail> findOutSourceReconcilDetail(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找采购对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:26:28, think
	 */
	public SearchResult<PurchReconcilDetail> findPurchReconcilDetail(QueryParam queryParam);
}
