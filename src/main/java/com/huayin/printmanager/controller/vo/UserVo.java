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

import com.huayin.printmanager.persist.entity.sys.User;

/**
 * <pre>
 * 使用者vo
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年10月26日, zhaojt
 * @version 	   2.0, 2018年2月26日上午11:02:34, zhengby, 代码规范
 */
public class UserVo extends User
{
	private static final long serialVersionUID = 1L;

	private Long[] roles;

	private Long[] employeeIds;

	public Long[] getRoles()
	{
		return roles;
	}

	public void setRoles(Long[] roles)
	{
		this.roles = roles;
	}

	public Long[] getEmployeeIds()
	{
		return employeeIds;
	}

	public void setEmployeeIds(Long[] employeeIds)
	{
		this.employeeIds = employeeIds;
	}

}
