/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin.dto;

import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 框架  - 第三方登录Dto
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   2.0, 2018年2月27日下午6:24:40, zhengby, 代码规范
 */
public class LoginResultDto
{
	private Boolean isSuccess;

	private String userId;

	private String email;

	private String realName;
	
	private String accessToken;

	private UserShareType userShareType;

	public LoginResultDto()
	{

	}

	public LoginResultDto(Boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public LoginResultDto(Boolean isSuccess, UserShareType userShareType)
	{
		this(isSuccess);
		this.userShareType = userShareType;
	}

	public Boolean getIsSuccess()
	{
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess)
	{
		this.isSuccess = isSuccess;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}
	
	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public UserShareType getUserShareType()
	{
		return userShareType;
	}

	public void setUserShareType(UserShareType userShareType)
	{
		this.userShareType = userShareType;
	}
}
