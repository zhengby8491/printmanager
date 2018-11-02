/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.PaymentClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础模块 - 付款方式
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:37:56
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
public interface PaymentClassService
{
	/**
	 * <pre>
	 * 根据id获取付款方式
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:03, think
	 */
	public PaymentClass get(Long id);

	/**
	 * <pre>
	 * 根据付款方式名称查询付款方式
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:10, think
	 */
	public PaymentClass getByName(String name);

	/**
	 * <pre>
	 * 添加付款方式
	 * </pre>
	 * @param paymentClass
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:40, think
	 */
	public PaymentClass save(PaymentClass paymentClass);

	/**
	 * <pre>
	 * 修改付款方式
	 * </pre>
	 * @param paymentClass
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:35, think
	 */
	public PaymentClass update(PaymentClass paymentClass);

	/**
	 * <pre>
	 * 得到全部付款方式信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:29, think
	 */
	public List<PaymentClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:49:23, think
	 */
	public SearchResult<PaymentClass> findByCondition(QueryParam queryParam);
}
