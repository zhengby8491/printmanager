/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.sys.vo.RegisterVo;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架  - 注册
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年9月3日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:31:51, zhengby, 代码规范
 */
@Controller
public class RegisterController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到注册页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:34:06, zhengby
	 */
	@RequestMapping(value = "${basePath}/register", method = RequestMethod.GET)
	public String register(ModelMap map)
	{
		return "register/register";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到注册提交成功或失败的页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param vo
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:34:22, zhengby
	 */
	@RequestMapping(value = "${basePath}/register_submit", method = RequestMethod.POST)
	@SystemControllerLog(SystemLogType=SystemLogType.REGISTER,Description="注册")
	public String register_submit(HttpServletRequest request,ModelMap map,RegisterVo vo)
	{
		try
		{
			if (Validate.validateObjectsNullOrEmpty(vo,vo.getUserName(),vo.getMobile(),vo.getPassword(),vo.getValidCode()))
			{
				return returnErrorPage(map, "注册信息填写不完整！");
			}
			 
			if(!UserUtils.validateSmsValidCode(vo.getMobile(), vo.getValidCode())){
				return returnErrorPage(map, "验证码不正确!");
			};
			
			User user = serviceFactory.getDaoFactory().getUserDao().getByMobile(vo.getMobile());
			if (user != null)
			{
				return returnErrorPage(map, vo.getMobile()+"手机号码已经存在!");
			}
			
			vo.setIp(request.getRemoteAddr());
			ServiceResult<User> result=serviceFactory.getUserService().register(vo,request);
			
			if(result.getIsSuccess())
			{
				return "redirect:" + basePath;
			}else
			{
				return returnErrorPage(map, "注册系统维护中...");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			UserUtils.clearSmsValidCode();
		}
		return "redirect:" + basePath;
	}

	/**
	 * <pre>
	 * 功能  - 查询名称是否已注册
	 * </pre>
	 * @param userName
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:35:08, zhengby
	 */
	@RequestMapping(value = "${basePath}/register/exist/userName")
	@ResponseBody
	public boolean existUserName(String userName, ModelMap map)
	{
		return !serviceFactory.getUserService().existUserName(userName.trim(), null);
	}
	
	/**
	 * <pre>
	 *  功能  - 查询手机号是否已注册
	 * </pre>
	 * @param mobile
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:35:44, zhengby
	 */
	@RequestMapping(value = "${basePath}/register/exist/mobile")
	@ResponseBody
	public boolean existMobile(String mobile,ModelMap map)
	{
		return !serviceFactory.getUserService().existMobile(mobile.trim(), null);
	}
	
}
