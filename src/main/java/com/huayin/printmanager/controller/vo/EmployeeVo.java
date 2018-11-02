/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.vo;

import com.huayin.printmanager.persist.entity.basic.Employee;

/**
 * <pre>
 * 员工vo
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年11月7日
 * @version 	   2.0, 2018年2月26日上午11:01:26, zhengby, 代码规范
 */
public class EmployeeVo extends Employee
{
	private static final long serialVersionUID = 1L;

	private Long[] employeeIds;

	public Long[] getEmployeeIds()
	{
		return employeeIds;
	}

	public void setEmployeeIds(Long[] employeeIds)
	{
		this.employeeIds = employeeIds;
	}

}
