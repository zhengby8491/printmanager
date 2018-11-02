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

import com.huayin.printmanager.expresslogin.dto.LoginResultDto;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 框架  - 第三方登录请求服务
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   1.0, 2018年2月27日下午6:27:26, zhengby, 代码规范
 */
public interface ExpressNotifyService
{
	LoginResultDto validataNotify(HttpServletRequest request, UserShareType userShareType);
}
