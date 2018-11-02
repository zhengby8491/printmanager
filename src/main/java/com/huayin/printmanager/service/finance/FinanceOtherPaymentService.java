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
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPaymentDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 其他付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceOtherPaymentService
{

	/**
	 * <pre>
	 * 获取其他付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:36:18, think
	 */
	public FinanceOtherPayment get(Long id);

	/**
	 * <pre>
	 * 获取其他付款单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:36:24, think
	 */
	public FinanceOtherPaymentDetail getDetail(Long id);

	/**
	 * <pre>
	 * 行级锁并获取其他付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:36:31, think
	 */
	public FinanceOtherPayment lock(Long id);

	/**
	 * <pre>
	 * 获取其他付款单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:37:07, think
	 */
	public List<FinanceOtherPaymentDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存其他付款单
	 * </pre>
	 * @param order
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 上午11:37:25, think
	 */
	public FinanceOtherPayment save(FinanceOtherPayment order);

	/**
	 * <pre>
	 * 审核其他付款单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 上午11:37:31, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 删除其他付款单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月26日 上午11:37:39, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 修改其他付款单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:37:45, think
	 */
	public FinanceOtherPayment update(FinanceOtherPayment order);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:37:52, think
	 */
	public SearchResult<FinanceOtherPayment> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:37:54, think
	 */
	public SearchResult<FinanceOtherPaymentDetail> findDetailByCondition(QueryParam queryParam);

}
