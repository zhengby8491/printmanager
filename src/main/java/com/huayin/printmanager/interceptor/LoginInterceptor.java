/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.huayin.printmanager.exception.LoginException;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 登录拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年12月08日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:08:09, zhengby, 代码规范
 */
public class LoginInterceptor extends SessionInterceptor
{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		super.preHandle(request, response, handler);
		if (UserUtils.getUser() != null)
		{
			return true;
		}
		else
		{
			throw new LoginException();
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		// super(request,response,handler,modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		// super(request, response, handler, ex);
	}

}
