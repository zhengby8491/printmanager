/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin;

import javax.servlet.http.HttpServletRequest;

import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   2.0, 2018年2月27日下午6:28:01, zhengby, 代码规范
 */
public interface ExpressRequestService
{
	String buildRequest(HttpServletRequest request, UserShareType userShareType);
}
