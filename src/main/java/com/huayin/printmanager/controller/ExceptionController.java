/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年9月12日 上午8:47:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.huayin.common.constant.Constant;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.exception.AdminAuthException;
import com.huayin.printmanager.exception.AuthException;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.exception.LoginException;

/**
 * <pre>
 * 拦截所有异常处理
 * </pre>
 * @author       think
 * @since        1.0, 2018年9月12日
 */
@ControllerAdvice
public class ExceptionController
{
//	@Autowired
//	private CookieLocaleResolver resolver;
	
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * <pre>
	 * 参数绑定异常
	 * </pre>
	 * @return
	 * @since 1.0, 2018年9月12日 上午9:02:00, think
	 */
	@ExceptionHandler({ BindException.class, ConstraintViolationException.class, ValidationException.class })
	public String bindException()
	{
		return "error/400";
	}

	/**
	 * <pre>
	 * 拦截所有异常
	 * </pre>
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 * @since 1.0, 2018年9月12日 上午9:01:46, think
	 */
	@ExceptionHandler
	public String exp(HttpServletRequest request, HttpServletResponse response, Exception ex)
	{
		String requestType = request.getHeader("X-Requested-With");

		if (requestType != null && requestType.equals("XMLHttpRequest"))
		{
			try
			{
				if (ex instanceof LoginException)
				{
					response.getWriter().write(JsonUtils.toJson(new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "请先登录!")));
					// logger.error(ex.getMessage(), ex);
				}
				else if (ex instanceof AdminAuthException)
				{
					response.getWriter().write(JsonUtils.toJson(new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "权限非法!")));
				}
				else if (ex instanceof AuthException)
				{
					response.getWriter().write(JsonUtils.toJson(new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "无操作权限，请联系管理员!")));
					// logger.error(ex.getMessage(), ex);
				}
				else if (ex instanceof BusinessException)		// 业务异常拦截
				{
					response.getWriter().write(JsonUtils.toJson(new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, ex.getMessage())));
					// logger.error(ex.getMessage(), ex);
				}
				else
				{
					response.getWriter().write(JsonUtils.toJson(new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "系统发生错误，请联系管理员处理!")));
					logger.error(ex.getMessage(), ex);
				}
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
			return null;
		}
		else
		{
			if (ex instanceof LoginException)
			{
				return "login";
			}
			else
			{
				if (ex instanceof AdminAuthException)
				{
					request.setAttribute("message", "权限非法!");
				}
				else if (ex instanceof AuthException)
				{
					request.setAttribute("message", "无操作权限，请联系管理员!");
				}
				else
				{
					request.setAttribute("message", "数据异常！");
					logger.error(ex.getMessage(), ex);
				}
				return "error/error";
			}
		}
	}
	
	/**
	 * <pre>
	 * 切换语言 （还要判断是否自己选中得国际化，优先用户选中国际化为主）
	 * </pre>
	 * @param request
	 * @param response
	 * @since 1.0, 2017年12月29日 上午11:16:11, think
	 */
//	protected void switchLanguage(HttpServletRequest request, HttpServletResponse response)
//	{
//		String defaultI18n = SystemConfigUtil.getDefaultI18n();
//		if (StringUtils.isNoneBlank(defaultI18n))
//		{
//			String [] lang = defaultI18n.split("_");
//			if(lang.length == 2)
//			{
//				resolver.setLocale(request, response, new Locale(lang[0], lang[1]));
//			}
//			else
//			{
//				resolver.setLocale(request, response, new Locale(lang[0]));
//			}
//		}
//	}
}
