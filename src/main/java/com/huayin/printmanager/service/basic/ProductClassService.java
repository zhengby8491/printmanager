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
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 产品分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
public interface ProductClassService
{
	/**
	 * <pre>
	 * 根据id获取产品分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:36:44, think
	 */
	public ProductClass get(Long id);

	/**
	 * <pre>
	 * 根据id获取产品分类
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:36:50, think
	 */
	public ProductClass get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据产品分类名查询产品分类
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:36:56, think
	 */
	public ProductClass getByName(String name);

	/**
	 * <pre>
	 * 添加产品分类
	 * </pre>
	 * @param productClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:37:03, think
	 */
	public ProductClass save(ProductClass productClass);
	
	/**
	 * <pre>
	 * 添加产品分类（快速添加）
	 * </pre>
	 * @param productType
	 * @param name
	 * @return
	 * @since 1.0, 2018年2月28日 下午5:22:17, think
	 */
	public ProductClass saveQuick(ProductType productType, String name);

	/**
	 * <pre>
	 * 修改产品分类
	 * </pre>
	 * @param productClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:37:09, think
	 */
	public ProductClass update(ProductClass productClass);

	/**
	 * <pre>
	 * 得到全部产品分类信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:37:19, think
	 */
	public List<ProductClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:37:26, think
	 */
	public SearchResult<ProductClass> findByCondition(QueryParam queryParam);

}
