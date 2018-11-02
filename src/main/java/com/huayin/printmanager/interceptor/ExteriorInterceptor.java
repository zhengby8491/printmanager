/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月3日 上午10:05:17
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;

/**
 * <pre>
 * 外部接口拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月3日上午10:05:17, zhengby
 */
public class ExteriorInterceptor implements HandlerInterceptor
{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		// 检测是否开放印刷家接口
		if (SystemConfigUtil.getConfig(SysConstants.SWITCH_FOR_YSJ).equals("YES"))
		{
			return true;
		} else
		{
			logger.info("接口未开放");
			return false;
		}
	}

	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
	}

	/* （非 Javadoc）
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
		// TODO Auto-generated method stub
	}

}
