/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;

/**
 * <pre>
 * 运营分析控制
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年10月09日
 */
/**
 * <pre>
 * 系统模块 - 运营分析
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/analysis")
public class AnalysisController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 在线用户
	 * </pre>
	 * @param name
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:36:37, think
	 */
	@RequestMapping(value = "online")
	@RequiresPermissions("sys:analysis:online")
	public String online(String name, HttpServletRequest request, ModelMap map)
	{
		return "sys/analysis/online";
	}

}
