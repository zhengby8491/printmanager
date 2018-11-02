/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.workbench;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统  - 常用功能
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月16日, raintear
 * @version 	   2.0, 2018年2月27日下午2:13:07, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/workbench/often")
public class WorkbenchOftenController extends BaseController
{
	/**
	 * <pre>
	 * 功能  - 保存常用功能
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:13:53, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public AjaxResponseBody save(@RequestBody QueryParam queryParam)
	{
		if (serviceFactory.getWorkbenchOftenService().save(queryParam.getOftenList()))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("保存失败");
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到常用功能列表查看页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:14:37, zhengby
	 */
	@RequestMapping(value = "oftenMenuList")
	public String oftenMenuList(ModelMap map)
	{
		map.put("menuList", serviceFactory.getWorkbenchOftenService().getMenu());
		map.put("oftenList", serviceFactory.getWorkbenchOftenService().get());
		return "workbench/often/list";
	}
}
