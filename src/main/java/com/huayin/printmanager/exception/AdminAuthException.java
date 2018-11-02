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
 * 框架  - 系统管理员权限异常类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年11月10日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:59:01, zhengby, 代码规范
 */
public class AdminAuthException extends Exception
{

	private static final long serialVersionUID = 1L;

	public AdminAuthException()
	{
		super();
	}

	public AdminAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AdminAuthException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AdminAuthException(String message)
	{
		super(message);
	}

	public AdminAuthException(Throwable cause)
	{
		super(cause);
	}

}
