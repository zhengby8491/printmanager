/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 职位设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
public interface PositionService
{
	/**
	 * <pre>
	 * 根据id获取职位信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:03, think
	 */
	public Position get(Long id);

	/**
	 * <pre>
	 * 根据职位名称查询职位
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:10, think
	 */
	public Position getByName(String name);

	/**
	 * <pre>
	 * 添加职位信息
	 * </pre>
	 * @param position
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:16, think
	 */
	public Position save(Position position);

	/**
	 * <pre>
	 * 修改职位信息
	 * </pre>
	 * @param position
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:34, think
	 */
	public Position update(Position position);

	/**
	 * <pre>
	 * 根据id删除职位
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年12月26日 下午6:00:40, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 得到全部职位信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:47, think
	 */
	public List<Position> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:00:53, think
	 */
	public SearchResult<Position> findByCondition(QueryParam queryParam);
}
