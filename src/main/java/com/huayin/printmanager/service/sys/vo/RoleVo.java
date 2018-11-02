/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.vo;

import java.io.Serializable;

import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Role;

/**
 * <pre>
 * 角色VO
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年12月19日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:48:28, zhengby, 代码规范
 */
public class RoleVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Role role;

	private Company company;

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public Company getCompany()
	{
		return company;
	}

	public void setCompany(Company company)
	{
		this.company = company;
	}

}
