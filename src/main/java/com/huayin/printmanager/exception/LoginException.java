/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exception;

/**
 * <pre>
 * 框架 - 登录异常类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年11月10日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:01:10, zhengby, 代码规范
 */
public class LoginException extends Exception
{

	private static final long serialVersionUID = 1L;

	public LoginException()
	{
		super();
	}

	public LoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LoginException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public LoginException(String message)
	{
		super(message);
	}

	public LoginException(Throwable cause)
	{
		super(cause);
	}

}
