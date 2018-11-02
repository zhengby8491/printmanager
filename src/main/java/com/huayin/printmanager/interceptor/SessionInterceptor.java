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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huayin.printmanager.service.ServiceFactory;

/**
 * <pre>
 * 框架  - session拦截器,拦截用户所有请求
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年12月8日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:10:43, zhengby, 代码规范
 */
public class SessionInterceptor extends HandlerInterceptorAdapter
{
	@Autowired
	public ServiceFactory servieFactory;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		// HttpSession session = request.getSession();
		// servieFactory.getDaoFactory().getSessionDao().update(session);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{

	}

}
