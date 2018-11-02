/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.enumerate.WarehouseType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 仓库信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public interface WarehouseService
{
	/**
	 * <pre>
	 * 根据id获取仓库信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:49:59, think
	 */
	public Warehouse get(Long id);

	/**
	 * <pre>
	 * 根据仓库信息名称查询仓库信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:06, think
	 */
	public Warehouse getByName(String name);

	/**
	 * <pre>
	 * 未期初的仓库列表
	 * </pre>
	 * @param warehouseType
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:18, think
	 */
	public List<Warehouse> getByBegin(WarehouseType warehouseType);

	/**
	 * <pre>
	 * 添加仓库信息
	 * </pre>
	 * @param warehouse
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:25, think
	 */
	public Warehouse save(Warehouse warehouse);

	/**
	 * <pre>
	 * 修改仓库信息
	 * </pre>
	 * @param warehouse
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:31, think
	 */
	public Warehouse update(Warehouse warehouse);

	/**
	 * <pre>
	 * 得到全部仓库信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:39, think
	 */
	public List<Warehouse> findAll();

	/**
	 * <pre>
	 * 根据仓库类型查找仓库
	 * </pre>
	 * @param warehouseType
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:45, think
	 */
	public List<Warehouse> findByType(WarehouseType warehouseType);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:50:53, think
	 */
	public SearchResult<Warehouse> findByCondition(QueryParam queryParam);
}
