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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <pre>
 * 框架  - 新手指南
 * </pre>
 * @author       zhengby
 * @version      1.0, 2013年5月31日, zhaojt
 * @version 	   1.0, 2018年2月27日下午2:38:25, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/guide")
public class GuideController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到新手指南页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:39:32, zhengby
	 */
	@RequestMapping(value = "begin_guide")
	public String guide(ModelMap map)
	{
		return "guide/begin_guide";
	}

}
