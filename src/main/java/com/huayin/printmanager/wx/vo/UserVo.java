/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

/**
 * <pre>
 * 微信 - 用户VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class UserVo
{
	/**
	 * 公司名称
	 */
	private String companyName;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 部门名称
	 */
	private String departmentName;

	/**
	 * 联系人
	 */
	private String linkName;

	/**
	 * 联系方式
	 */
	private String mobile;

	/**
	 * 邮箱
	 */
	private String email;

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public UserVo(String companyName, String userName, String departmentName, String linkName, String mobile, String email)
	{
		super();
		this.companyName = companyName;
		this.userName = userName;
		this.departmentName = departmentName;
		this.linkName = linkName;
		this.mobile = mobile;
		this.email = email;
	}

	public UserVo()
	{
		super();
	}

}
