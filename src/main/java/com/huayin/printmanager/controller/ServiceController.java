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
 * 框架  - 登录Controller
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2013年5月31日, zhaojt
 * @version 	   2.0, 2018年2月27日下午2:37:00, zhengby, 代码规范
 */
@Controller
public class ServiceController extends BaseController
{
	/**
	 * <pre>
	 * 服务支持
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:37:04, zhengby
	 */
	@RequestMapping(value = "${basePath}/service")
	public String service(ModelMap map)
	{
		return "service";
	}

}
