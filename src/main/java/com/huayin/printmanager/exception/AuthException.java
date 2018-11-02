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
 * 框架  - 操作权限异常类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年11月10日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:00:25, zhengby, 代码规范
 */
public class AuthException extends Exception
{

	private static final long serialVersionUID = 1L;

	public AuthException()
	{
		super();
	}

	public AuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AuthException(String message)
	{
		super(message);
	}

	public AuthException(Throwable cause)
	{
		super(cause);
	}

}
