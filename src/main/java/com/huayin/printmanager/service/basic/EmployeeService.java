/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.io.InputStream;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 员工信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:11:49
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
public interface EmployeeService
{
	/**
	 * <pre>
	 * 根据员工id获取员工信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:04:06, think
	 */
	public Employee get(Long id);

	/**
	 * <pre>
	 * 根据员工名称查询员工信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:03:47, think
	 */
	public Employee getByName(String name);

	/**
	 * <pre>
	 * 保存员工信息
	 * </pre>
	 * @param employee
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:04:19, think
	 */
	public Employee save(Employee employee);

	/**
	 * <pre>
	 * 修改员工信息
	 * </pre>
	 * @param employee
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:04:26, think
	 */
	public Employee update(Employee employee);

	/**
	 * <pre>
	 * 得到全部员工信息
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:04:42, think
	 */
	public List<Employee> findAll();

	/**
	 * <pre>
	 * 根据部门id查询员工信息
	 * </pre>
	 * @param departmentId
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:04:52, think
	 */
	public List<Employee> findByDepartmentId(Long departmentId);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 下午6:05:38, think
	 */
	public SearchResult<Employee> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 导入excel员工信息
	 * </pre>
	 * @param inputSream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年12月26日 下午6:06:46, think
	 */
	public Integer importFromExcel(InputStream inputSream) throws Exception;
}
