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
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 单位信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
public interface UnitService
{
	/**
	 * <pre>
	 * 根据id获取单位信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:12:26, think
	 */
	public Unit get(Long id);

	/**
	 * <pre>
	 * 根据单位信息名称查询单位信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:12:33, think
	 */
	public Unit getByName(String name);

	/**
	 * <pre>
	 * 添加单位信息
	 * </pre>
	 * @param unit
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:12:40, think
	 */
	public Unit save(Unit unit);

	/**
	 * <pre>
	 * 修改单位信息
	 * </pre>
	 * @param unit
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:12:46, think
	 */
	public Unit update(Unit unit);

	/**
	 * <pre>
	 * 得到全部单位信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:12:56, think
	 */
	public List<Unit> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:13:07, think
	 */
	public SearchResult<Unit> findByCondition(QueryParam queryParam);
}
