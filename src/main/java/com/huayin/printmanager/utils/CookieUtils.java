/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 公共 - Cookie工具类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class CookieUtils
{
	/**
	 * <pre>
	 * 设置 Cookie（生成时间为1天）
	 * </pre>
	 * @param response
	 * @param name
	 * @param value
	 * @since 1.0, 2017年10月26日 上午9:36:16, think
	 */
	public static void setCookie(HttpServletResponse response, String name, String value)
	{
		setCookie(response, name, value, 60 * 60 * 24);
	}

	/**
	 * <pre>
	 * 设置 Cookie
	 * </pre>
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @since 1.0, 2017年10月26日 上午9:36:22, think
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path)
	{
		setCookie(response, name, value, path, 60 * 60 * 24);
	}

	/**
	 * <pre>
	 * 设置 Cookie
	 * </pre>
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @since 1.0, 2017年10月26日 上午9:36:29, think
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge)
	{
		setCookie(response, name, value, "/", maxAge);
	}

	/**
	 * <pre>
	 * 设置 Cookie
	 * </pre>
	 * @param response
	 * @param name
	 * @param value
	 * @param path
	 * @param maxAge
	 * @since 1.0, 2017年10月26日 上午9:36:37, think
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge)
	{
		Cookie cookie = new Cookie(name, null);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		try
		{
			cookie.setValue(URLEncoder.encode(value, "utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}

	/**
	 * <pre>
	 * 获得指定Cookie的值
	 * </pre>
	 * @param request
	 * @param name
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:36:44, think
	 */
	public static String getCookie(HttpServletRequest request, String name)
	{
		return getCookie(request, null, name, false);
	}

	/**
	 * <pre>
	 * 获得指定Cookie的值，并删除。
	 * </pre>
	 * @param request
	 * @param response
	 * @param name
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:36:50, think
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name)
	{
		return getCookie(request, response, name, true);
	}

	/**
	 * <pre>
	 * 获得指定Cookie的值
	 * </pre>
	 * @param request
	 * @param response
	 * @param name
	 * @param isRemove
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:36:59, think
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove)
	{
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals(name))
				{
					try
					{
						value = URLDecoder.decode(cookie.getValue(), "utf-8");
					}
					catch (UnsupportedEncodingException e)
					{
						e.printStackTrace();
					}
					if (isRemove)
					{
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}
}
