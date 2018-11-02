/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin.tencent;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.expresslogin.ExpressLoginUtils;
import com.huayin.printmanager.expresslogin.ExpressRequestService;
import com.huayin.printmanager.expresslogin.tencent.config.WechatConfig;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 框架  - 微信登录接口
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月27日下午6:34:52, zhengby, 代码规范
 */
public class WechatServiceImpl implements ExpressRequestService
{

	@Override
	public String buildRequest(HttpServletRequest request, UserShareType userShareType)
	{
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("appid", SystemConfigUtil.getExpressLoginWechatAppId());
		sParaTemp.put("redirect_uri", SystemConfigUtil.getExpressLoginWechatReturnUrl() 
				+ request.getContextPath() + SystemConfigUtil.getBasePath() + "/public/expressReturn/" + UserShareType.WEIXIN);
		sParaTemp.put("response_type", "code");
		sParaTemp.put("scope", "snsapi_login");
		sParaTemp.put("state", SystemConfigUtil.getExpressLoginWechatAppKey());
		String strButtonName = "submit";
		return ExpressLoginUtils.buildForm(sParaTemp, WechatConfig.requestUrl, "GET", strButtonName);
	}
}
