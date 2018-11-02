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
 * 框架  - QQ账户登录
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月27日
 * @version 	   1.0, 2018年2月27日下午6:29:43, zhengby, 代码规范
 */
public class TencentConfig
{
	public static final String requestUrl = "https://graph.qq.com/oauth2.0/authorize";

	public static final String requestAccessToken = "https://graph.qq.com/oauth2.0/token";

	public static final String requestOpenId = "https://graph.qq.com/oauth2.0/me";
	
	public static final String getUserInfoUrl = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";
}
