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
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 材料分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
public interface MaterialClassService
{
	/**
	 * <pre>
	 * 根据id获取材料分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:38:33, think
	 */
	public MaterialClass get(Long id);

	/**
	 * <pre>
	 * 根据id获取材料分类
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:38:39, think
	 */
	public MaterialClass get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据材料分类名查询材料分类
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:38:47, think
	 */
	public MaterialClass getByName(String name);

	/**
	 * <pre>
	 * 添加材料分类
	 * </pre>
	 * @param materialClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:38:53, think
	 */
	public MaterialClass save(MaterialClass materialClass);

	/**
	 * <pre>
	 * 添加材料分类（快速添加）
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年3月1日 上午9:03:37, think
	 */
	public MaterialClass saveQuick(String name);

	/**
	 * <pre>
	 * 修改材料分类
	 * </pre>
	 * @param materialClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:38:59, think
	 */
	public MaterialClass update(MaterialClass materialClass);

	/**
	 * <pre>
	 * 得到全部材料分类信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:39:10, think
	 */
	public List<MaterialClass> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午1:39:16, think
	 */
	public SearchResult<MaterialClass> findByCondition(QueryParam queryParam);

}
