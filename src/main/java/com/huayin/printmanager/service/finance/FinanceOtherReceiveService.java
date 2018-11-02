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
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceiveDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 其他收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface FinanceOtherReceiveService
{
	/**
	 * <pre>
	 * 获取其他收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:44, think
	 */
	public FinanceOtherReceive get(Long id);

	/**
	 * <pre>
	 * 获取其他收款单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:49, think
	 */
	public FinanceOtherReceiveDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取其他收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:56, think
	 */
	public FinanceOtherReceive lock(Long id);

	/**
	 * <pre>
	 * 获取其他收款单明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:45:05, think
	 */
	public List<FinanceOtherReceiveDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存其他收款单
	 * </pre>
	 * @param order
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午1:45:15, think
	 */
	public FinanceOtherReceive save(FinanceOtherReceive order);

	/**
	 * <pre>
	 * 审核其他收款单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月26日 下午1:45:25, think
	 */
	public boolean audit(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 删除其他收款单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月26日 下午1:45:34, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 修改其他收款单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:45:40, think
	 */
	public FinanceOtherReceive update(FinanceOtherReceive order);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:45:46, think
	 */
	public SearchResult<FinanceOtherReceive> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:45:52, think
	 */
	public SearchResult<FinanceOtherReceiveDetail> findDetailByCondition(QueryParam queryParam);
}
