/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.CustomerClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 客户分类
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
public interface CustomerClassService
{
	/**
	 * <pre>
	 * 根据id获取客户类型
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:04, think
	 */
	public CustomerClass get(Long id);
	
	/**
	 * <pre>
	 * 根据客户分类名查询客户分类
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:12, think
	 */
	public CustomerClass getByName(String name);

	/**
	 * <pre>
	 * 添加客户类型
	 * </pre>
	 * @param customerClass
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:21, think
	 */
	public CustomerClass save(CustomerClass customerClass);

	/**
	 * <pre>
	 * 修改客户类型
	 * </pre>
	 * @param customerClass
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:27, think
	 */
	public CustomerClass update(CustomerClass customerClass);

	/**
	 * <pre>
	 * 得到全部客户类型信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:36, think
	 */
	public List<CustomerClass> findAll();
	
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 上午10:59:42, think
	 */
	public SearchResult<CustomerClass> findByCondition(QueryParam queryParam);
}
