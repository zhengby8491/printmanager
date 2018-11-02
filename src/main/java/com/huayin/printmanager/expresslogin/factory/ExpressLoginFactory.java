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
import com.huayin.printmanager.expresslogin.tencent.TencentNotifyImpl;
import com.huayin.printmanager.expresslogin.tencent.TencentServiceImpl;
import com.huayin.printmanager.expresslogin.tencent.WechatNotifyImpl;
import com.huayin.printmanager.expresslogin.tencent.WechatServiceImpl;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 框架  - 第三方登录工厂
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月28日
 * @version 	   2.0, 2018年2月27日下午6:26:27, zhengby, 代码规范
 */
public class ExpressLoginFactory
{

	public static ExpressLoginCreator getExpressLoginCreator(UserShareType type)
	{
		switch (type)
		{
			case TENCENT:
				return new ExpressLoginCreator()
				{

					public ExpressRequestService createRequestService()
					{
						return new TencentServiceImpl();
					}

					public ExpressNotifyService createNotifyService()
					{
						return new TencentNotifyImpl();
					}
				};
			case WEIXIN:
				return new ExpressLoginCreator()
				{

					public ExpressRequestService createRequestService()
					{
						return new WechatServiceImpl();
					}

					public ExpressNotifyService createNotifyService()
					{
						return new WechatNotifyImpl();
					}
				};
			default:
				throw new RuntimeException("不支持的快捷登录放方式");
		}
	}
}
