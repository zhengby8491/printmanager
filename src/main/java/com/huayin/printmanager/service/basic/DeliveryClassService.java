/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.DeliveryClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 送货方式
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
public interface DeliveryClassService
{
	/**
	 * <pre>
	 * 根据id获取送货方式
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:14, think
	 */
	public DeliveryClass get(Long id);

	/**
	 * <pre>
	 * 根据送货方式名称查询送货方式
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:21, think
	 */
	public DeliveryClass getByName(String name);

	/**
	 * <pre>
	 * 添加送货方式
	 * </pre>
	 * @param deliveryClass
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:28, think
	 */
	public DeliveryClass save(DeliveryClass deliveryClass);

	/**
	 * <pre>
	 * 修改送货方式
	 * </pre>
	 * @param deliveryClass
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:33, think
	 */
	public DeliveryClass update(DeliveryClass deliveryClass);

	/**
	 * <pre>
	 * 得到全部送货方式信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:40, think
	 */
	public List<DeliveryClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:56:47, think
	 */
	public SearchResult<DeliveryClass> findByCondition(QueryParam queryParam);
}
