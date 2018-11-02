/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin.factory;

import com.huayin.printmanager.expresslogin.ExpressNotifyService;
import com.huayin.printmanager.expresslogin.ExpressRequestService;

/**
 * <pre>
 * 框架  - 第三方快录工厂接口
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   2.0, 2018年2月27日下午6:25:46, zhengby, 代码规范
 */
public interface ExpressLoginCreator
{
	public ExpressRequestService createRequestService();

	public ExpressNotifyService createNotifyService();
}
