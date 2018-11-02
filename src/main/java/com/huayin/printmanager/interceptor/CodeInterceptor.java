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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.huayin.printmanager.utils.CaptchaServlet;
import com.huayin.printmanager.utils.JedisUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 验证码拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年7月20日, yishan
 * @version 	   2.0, 2018年2月27日下午3:06:51, zhengby, 代码规范
 */
public class CodeInterceptor implements HandlerInterceptor
{
	private static final String VALIDCODE_CACHE_SYS = "VALIDCODE_CACHE_SYS";

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(CodeInterceptor.class);

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("ThreadLocal StartTime");

	/**
	 * preHandle方法是进行处理器拦截用的，顾名思义，该方法将在Controller处理之前进行调用，SpringMVC中的Interceptor拦截器是链式的，可以同时存在
	 * 多个Interceptor，然后SpringMVC会根据声明的前后顺序一个接一个的执行，而且所有的Interceptor中的preHandle方法都会在
	 * Controller方法调用之前调用。SpringMVC的这种Interceptor链式结构也是可以进行中断的，这种中断方式是令preHandle的返
	 * 回值为false，当preHandle的返回值为false的时候整个请求就结束了。
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		// if (logger.isDebugEnabled())
		// {
		// long beginTime = System.currentTimeMillis();// 1、开始时间

		// logger.debug("开始计时: {} URI: {} ip: {} user: {}",new SimpleDateFormat("hh:mm:ss").format(beginTime),
		// request.getRequestURI(),request.getRemoteAddr(),
		// UserUtils.getUser()!=null?UserUtils.getUser().getUserName():"null");
		// }

		String ip = request.getRemoteAddr();
		String mobile = request.getParameter("mobile");
		String captcha = request.getParameter("captcha");
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）

		// 如果输入的图片验证码不对，不拦截
		if (StringUtils.isNotBlank(captcha) && !CaptchaServlet.validate(request, captcha))
		{
			return true;
		}

		// 每个IP一分钟之内只能发送一次短信验证码
		if (JedisUtils.mapObjectExists(VALIDCODE_CACHE_SYS, ip))
		{
			logger.warn("重复发送验证码: {} URI: {} ip: {} user: {}", new SimpleDateFormat("hh:mm:ss").format(beginTime), request.getRequestURI(), request.getRemoteAddr(), UserUtils.getUser() != null ? UserUtils.getUser().getUserName() : "null");
			return false;
		}

		// 每个手机号码一分钟只能只能发送一次短信验证码
		if (StringUtils.isNotBlank(mobile) && JedisUtils.mapObjectExists(VALIDCODE_CACHE_SYS, mobile))
		{
			logger.warn("重复发送验证码: {} URI: {} ip: {} user: {}", new SimpleDateFormat("hh:mm:ss").format(beginTime), request.getRequestURI(), request.getRemoteAddr(), UserUtils.getUser() != null ? UserUtils.getUser().getUserName() : "null");
			return false;
		}

		// 每次只保存60秒的记录
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ip, ip);
		if (StringUtils.isNotBlank(mobile))
		{
			map.put(mobile, mobile);
		}

		if (!JedisUtils.exists(VALIDCODE_CACHE_SYS))
		{
			JedisUtils.setObjectMap(VALIDCODE_CACHE_SYS, map, 60);
		}
		else
		{
			JedisUtils.mapObjectPut(VALIDCODE_CACHE_SYS, map);
			// JedisUtils.getObjectMap(VALIDCODE_CACHE_SYS).putAll(map);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		if (logger.isDebugEnabled())
		{
			if (modelAndView != null)
			{
				logger.info("viewName: " + modelAndView.getViewName());
			}
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
	{
	}

}
