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

/**
 * <pre>
 * 注册VO
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年9月4日, zhaojt
 * @version 	   2.0, 2018年2月26日上午9:47:02, zhengby, 代码规范
 */
public class RegisterVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String userName;

	private String mobile;

	private String password;

	private String ip;

	private String validCode;

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getValidCode()
	{
		return validCode;
	}

	public void setValidCode(String validCode)
	{
		this.validCode = validCode;
	}
}
