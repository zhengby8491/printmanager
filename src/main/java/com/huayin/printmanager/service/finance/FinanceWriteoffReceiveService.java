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
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 预收核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceWriteoffReceiveService
{
	/**
	 * <pre>
	 * 获取预收核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:33:42, think
	 */
	public FinanceWriteoffReceive get(Long id);

	/**
	 * <pre>
	 * 获取预收核销单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:33:47, think
	 */
	public FinanceWriteoffReceiveDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取预收核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:33:53, think
	 */
	public FinanceWriteoffReceive lock(Long id);

	/**
	 * <pre>
	 * 获取预收核销单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:33:58, think
	 */
	public List<FinanceWriteoffReceiveDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存预收核销单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:06, think
	 */
	public FinanceWriteoffReceive save(FinanceWriteoffReceive order);

	/**
	 * <pre>
	 * 审核预收核销单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:15, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 作废预收核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:23, think
	 */
	public boolean cancel(Long id);

	/**
	 * <pre>
	 * 反作废预收核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:30, think
	 */
	public boolean cancelBack(Long id);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:39, think
	 */
	public SearchResult<FinanceWriteoffReceive> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:45, think
	 */
	public SearchResult<FinanceWriteoffReceiveDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找销售对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:34:50, think
	 */
	public SearchResult<SaleReconcilDetail> findSaleReconcilDetail(QueryParam queryParam);
}
