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
import com.huayin.printmanager.domain.annotation.Logical;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.exception.AuthException;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 权限拦截器
 * </pre>
 * @author       zhengby
 * @version 	   1.0,	2014年8月19日, zhaojt
 * @version 	   2.0, 2018年2月27日下午3:09:58, zhengby, 代码规范
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		if (handler.getClass().isAssignableFrom(HandlerMethod.class))
		{
			RequiresPermissions permissions = ((HandlerMethod) handler).getMethodAnnotation(RequiresPermissions.class);

			// 没有声明需要权限,或者声明不验证权限
			if (permissions == null || permissions.value() == null)
			{
				return true;
			}
			else
			{// 登录用户的权限校验
				boolean validate = true;
				if (UserUtils.getUser() != null)
				{
					String[] stringPermissions = permissions.value();
					if (stringPermissions != null && stringPermissions.length > 0)
					{
						if (permissions.logical() == Logical.AND)
						{
							validate = true;
							for (String permission : stringPermissions)
							{
								if (!UserUtils.hasPermission(permission))
								{
									validate = false;
									break;
								}
							}
						}
						else if (permissions.logical() == Logical.OR)
						{
							validate = false;
							for (String permission : stringPermissions)
							{
								if (UserUtils.hasPermission(permission))
								{
									// 只要存在一个权限就跳出循环 用户具备权限
									validate = true;
									break;
								}
							}
						}
						else
						{
							throw new Exception("系统发生异常!");
						}
					}

				}
				else
				{
					validate = false;
				}
				if (!validate)
				{
					ResponseBody resBody = ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class);
					if (resBody != null)
					{
						AjaxResponseBody resObj = new AjaxResponseBody(Constant.TRANSACTION_RESPONSE_CODE_UNKOWN, "无操作权限，请联系管理员");
						response.getWriter().write(JsonUtils.toJson(resObj));
						return false;
					}
					else
					{
						throw new AuthException("无操作权限，请联系管理员");
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
