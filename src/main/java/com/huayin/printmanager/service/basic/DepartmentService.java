/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置  - 部门信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月22日
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
public interface DepartmentService
{
	/**
	 * <pre>
	 * 根据id查询部门信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:46:26, think
	 */
	public Department get(Long id);
	
	/**
	 * <pre>
	 * 根据name查询部门信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:46:34, think
	 */
	public Department getByName(String name);

	/**
	 * <pre>
	 * 添加部门信息
	 * </pre>
	 * @param department
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:46:43, think
	 */
	public Department save(Department department);

	/**
	 * <pre>
	 * 修改部门信息
	 * </pre>
	 * @param department
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:47:15, think
	 */
	public Department update(Department department);

	/**
	 * <pre>
	 * 查询全部部门信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:47:25, think
	 */
	public List<Department> findAll();
	
	/**
	 * <pre>
	 * 根据QueryParam查询部门信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月22日 下午5:47:33, think
	 */
	public SearchResult<Department> findByCondition(QueryParam queryParam);
}
