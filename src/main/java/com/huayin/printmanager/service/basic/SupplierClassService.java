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
import com.huayin.printmanager.persist.entity.basic.SupplierClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 供应商类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月28日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
public interface SupplierClassService
{
	/**
	 * <pre>
	 * 根据id获取供应商类型
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:45:43, think
	 */
	public SupplierClass get(Long id);
	
	/**
	 * <pre>
	 * 根据供应商类型名查询供应商
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:45:51, think
	 */
	public SupplierClass getByName(String name);
	
	/**
	 * <pre>
	 * 添加供应商类型
	 * </pre>
	 * @param supplierClass
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:45:57, think
	 */
	public SupplierClass save(SupplierClass supplierClass);

	/**
	 * <pre>
	 * 修改供应商类型
	 * </pre>
	 * @param supplierClass
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:46:04, think
	 */
	public SupplierClass update(SupplierClass supplierClass);

	/**
	 * <pre>
	 * 得到全部供应商类型信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:46:12, think
	 */
	public List<SupplierClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 上午9:46:20, think
	 */
	public SearchResult<SupplierClass> findByCondition(QueryParam queryParam);
}
