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

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huayin.common.constant.Constant;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.exception.AdminAuthException;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 超级管理员拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2014年8月19日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:05:46, zhengby, 代码规范
 */
public class AdminInterceptor extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		if (handler.getClass().isAssignableFrom(HandlerMethod.class))
		{
			AdminAuth auth = ((HandlerMethod) handler).getMethodAnnotation(AdminAuth.class);

			// 没有声明需要权限,或者声明不验证权限
			if (auth == null || auth.validate() == false)
			{
				return true;
			}
			else
			{// 登录用户的管理员权限校验
				if (UserUtils.getUser() != null && !UserUtils.isSystemCompany())
				{
					ResponseBody resBody = ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class);
					if (resBody != null)
					{
						AjaxResponseBody resObj = new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "权限非法");
						response.getWriter().write(JsonUtils.toJson(resObj));
						return false;
					}
					else
					{
						throw new AdminAuthException("权限非法");
					}
				}
				else
				{
					return true;
				}
			}
		}
		else
		{
			return true;
		}
	}

}
