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
 * 业务异常类
 * </pre>
 * @author       zhengxchn@163.com
 * @version      1.0, 2018年09月21日, zhengxchn@163.com
 */
public class BusinessException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public BusinessException()
	{
		super();
	}

	public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BusinessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public BusinessException(String message)
	{
		super(message);
	}

	public BusinessException(Throwable cause)
	{
		super(cause);
	}

}
