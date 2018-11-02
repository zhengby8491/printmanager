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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 日志拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2014年8月19日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:09:16, zhengby, 代码规范
 */
public class LogInterceptor implements HandlerInterceptor
{

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

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
		// 这里用于给com.huayin.printmanager.i18n.ResourceBundleMessageSource初始化国际，否则ResourceBundleMessageSource.message有可能失败
		RequestContext requestContext = new RequestContext(request);
		requestContext.getMessage("i18n.frame.project");

		if (logger.isDebugEnabled())
		{
			long beginTime = System.currentTimeMillis();// 1、开始时间
			startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
			logger.debug("开始计时: {} URI: {} ip: {} user: {}", new SimpleDateFormat("hh:mm:ss").format(beginTime), request.getRequestURI(), request.getRemoteAddr(), UserUtils.getUser() != null ? UserUtils.getUser().getUserName() : "null");
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

		// 保存日志
		// LogUtils.saveLog(request, handler, ex, null);

		// 打印JVM信息。
		if (logger.isDebugEnabled())
		{

			long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
			long endTime = System.currentTimeMillis(); // 2、结束时间
			logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m", new SimpleDateFormat("hh:mm:ss").format(endTime), DateUtils.formatDateTime(endTime - beginTime), request.getRequestURI(), Runtime.getRuntime().maxMemory() / 1024 / 1024, Runtime.getRuntime().totalMemory() / 1024 / 1024, Runtime.getRuntime().freeMemory() / 1024 / 1024, (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
		}

	}

}
