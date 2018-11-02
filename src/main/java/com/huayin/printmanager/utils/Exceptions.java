/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * 公共 - 关于异常的工具类.
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class Exceptions
{
	/**
	 * <pre>
	 * 将CheckedException转换为UncheckedException.
	 * </pre>
	 * @param e
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:32:16, think
	 */
	public static RuntimeException unchecked(Exception e)
	{
		if (e instanceof RuntimeException)
		{
			return (RuntimeException) e;
		}
		else
		{
			return new RuntimeException(e);
		}
	}

	/**
	 * <pre>
	 * 将ErrorStack转化为String.
	 * </pre>
	 * @param e
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:32:22, think
	 */
	public static String getStackTraceAsString(Throwable e)
	{
		if (e == null)
		{
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * <pre>
	 * 判断异常是否由某些底层的异常引起.
	 * </pre>
	 * @param ex
	 * @param causeExceptionClasses
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:32:28, think
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses)
	{
		Throwable cause = ex.getCause();
		while (cause != null)
		{
			for (Class<? extends Exception> causeClass : causeExceptionClasses)
			{
				if (causeClass.isInstance(cause))
				{
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * <pre>
	 * 在request中获取异常类
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:32:34, think
	 */
	public static Throwable getThrowable(HttpServletRequest request)
	{
		Throwable ex = null;
		if (request.getAttribute("exception") != null)
		{
			ex = (Throwable) request.getAttribute("exception");
		}
		else if (request.getAttribute("javax.servlet.error.exception") != null)
		{
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}

}
