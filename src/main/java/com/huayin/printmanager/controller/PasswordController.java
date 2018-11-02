/**
 * <pre>
 * Author:		   think
 * Create:	 	   2013年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.exception.ServiceResultFactory;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.constants.ErrorCodeConstants.CommonCode;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.utils.CaptchaServlet;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 密码找回/修改
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年9月3日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:54:14, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/password")
public class PasswordController extends BaseController
{
	public String RECOVER_SESSION_KEY = "recover_password_step";

	/**
	 * <pre>
	 * 找回密码：第一步
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:55:37, zhengby
	 */
	@RequestMapping(value = "recover/step1")
	public String recover_pwd_step1(HttpServletRequest request)
	{
		request.getSession().removeAttribute(RECOVER_SESSION_KEY);
		request.getSession().setAttribute(RECOVER_SESSION_KEY, "step1");
		return "password/recover_pwd_step1";
	}

	/**
	 * <pre>
	 * 找回密码：第二步
	 * </pre>
	 * @param request
	 * @param searchContent
	 * @param captcha
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:55:46, zhengby
	 */
	@RequestMapping(value = "recover/step2")
	public String recover_pwd_step2(HttpServletRequest request, String searchContent, String captcha, ModelMap map)
	{
		Object step = request.getSession().getAttribute(RECOVER_SESSION_KEY);
		if (step == null || !step.equals("step1"))
		{
			return redirect(basePath + "/password/recover/step1");
		}
		if (CaptchaServlet.validate(request, captcha))
		{
			User user = serviceFactory.getDaoFactory().getUserDao().getByUserName(searchContent);
			
			if (user == null)
			{
				map.put("message", "用户不存在");
				map.put("searchContent", searchContent);
				return "password/recover_pwd_step1";
			}
			else
			{
				if (user.getCompanyId().equals("1"))
				{
					map.put("message", "权限非法");
					map.put("searchContent", searchContent);
					return "password/recover_pwd_step1";
				}
				request.getSession().setAttribute(RECOVER_SESSION_KEY, "step2_" + user.getId());
				// map.put("mobile", user.getMobile());
				return "password/recover_pwd_step2";
			}
		}
		else
		{
			map.put("message", "验证码错误");
			map.put("searchContent", searchContent);
			return "password/recover_pwd_step1";
		}
	}

	/**
	 * <pre>
	 * 找回密码：第三步
	 * </pre>
	 * @param request
	 * @param mobile
	 * @param validCode
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:55:56, zhengby
	 */
	@RequestMapping(value = "recover/step3")
	public String recover_pwd_step3(HttpServletRequest request, String mobile,String validCode, ModelMap map)
	{
		//验证码校验失败
		if (!UserUtils.validateSmsValidCode(mobile, validCode)){
			return returnErrorPage(map, mobile+"("+validCode+")验证码错误");
		}

		Object step = request.getSession().getAttribute(RECOVER_SESSION_KEY);
		if (step == null || !step.toString().startsWith("step2_"))
		{
			return redirect(basePath + "/password/recover/step1");
		}
		String userId = step.toString().split("_")[1].toString();
		User user = serviceFactory.getDaoFactory().getUserDao().get(Long.parseLong(userId));
		if(user.getState()!=State.NORMAL){
			return returnErrorPage(map, "用户状态异常("+user.getState().getText()+")，请联系管理员!");
		}
		if (!user.getMobile().equals(mobile))
		{
			return returnErrorPage(map, "手机号码错误");
		}
		request.getSession().setAttribute(RECOVER_SESSION_KEY, "step3_" + userId);

		return "password/recover_pwd_step3";
	}

	/**
	 * <pre>
	 * 重置密码
	 * </pre>
	 * @param request
	 * @param password
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:56:04, zhengby
	 */
	@RequestMapping(value = "recover/reset")
	@ResponseBody
	@SystemControllerLog(SystemLogType=SystemLogType.FINDPWD,Description="找回密码")
	public ServiceResult<Boolean> reset(HttpServletRequest request, String password, ModelMap map)
	{

		try
		{
			if (Validate.validateObjectsNullOrEmpty(password))
			{
				return ServiceResultFactory.getServiceResult(CommonCode.COMMON_DATA_NOT_VALIDATA, false, "提交数据不完整");
			}

			Object step = request.getSession().getAttribute(RECOVER_SESSION_KEY);
			if (step == null || !step.toString().startsWith("step3_"))
			{
				return ServiceResultFactory.getServiceResult(CommonCode.COMMON_UKNOW_ERROR, false, "操作非法");
			}
			return serviceFactory.getUserService().findPwd(request, Long.parseLong(step.toString().split("_")[1]),
					password);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return ServiceResultFactory.getServiceResult(CommonCode.COMMON_UKNOW_ERROR, false, "操作异常");
		}
		finally
		{
			request.getSession().removeAttribute(RECOVER_SESSION_KEY);
			UserUtils.clearSmsValidCode();
		}
	}
}
