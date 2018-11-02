/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin.tencent.config;

/**
 * <pre>
 * 框架  - 微信账户登录配置
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   2.0, 2018年2月27日下午6:30:25, zhengby, 代码规范
 */
public class WechatConfig
{
	public static final String requestUrl = "https://open.weixin.qq.com/connect/qrconnect";

	public static final String requestAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token";

	public static final String requestOpenId = "https://api.weixin.qq.com/sns/userinfo";
	
	public static final String getUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
}
