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
import com.huayin.printmanager.persist.entity.sys.User;

/**
 * <pre>
 * 
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年11月10日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:36:22, zhengby, 代码规范
 */
public class CompanyVo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Company company;

	private User registerUser;

	public User getRegisterUser()
	{
		return registerUser;
	}

	public void setRegisterUser(User registerUser)
	{
		this.registerUser = registerUser;
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
