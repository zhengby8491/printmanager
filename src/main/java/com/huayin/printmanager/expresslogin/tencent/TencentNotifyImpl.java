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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.expresslogin.ExpressNotifyService;
import com.huayin.printmanager.expresslogin.dto.LoginResultDto;
import com.huayin.printmanager.expresslogin.tencent.config.TencentConfig;
import com.huayin.printmanager.persist.enumerate.UserShareType;
import com.huayin.printmanager.utils.HttpRequest;

/**
 * <pre>
 * 框架  - 腾讯QQ登录验证
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月27日下午6:32:54, zhengby, 代码规范
 */
public class TencentNotifyImpl implements ExpressNotifyService
{
	private static final Log log = LogFactory.getLog(TencentNotifyImpl.class);

	@Override
	public LoginResultDto validataNotify(HttpServletRequest request, UserShareType userShareType)
	{
		LoginResultDto dto = new LoginResultDto(false, UserShareType.TENCENT);

		String code = request.getParameter("code");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("grant_type", "authorization_code");
		parameters.put("client_id", SystemConfigUtil.getExpressLoginTencentAppId());
		parameters.put("client_secret", SystemConfigUtil.getExpressLoginTencentAppKey());
		parameters.put("code", code);
		parameters.put("redirect_uri", SystemConfigUtil.getExpressLoginTencentReturnUrl());
		try
		{
			String text = HttpRequestProxy.doGet(TencentConfig.requestAccessToken, parameters, "utf-8");
			String access_token = HttpRequest.getParameter(text, "access_token");
			dto.setAccessToken(access_token);
			// String expires_in = MessageHelper.getParameter(text, "expires_in");
			Map<String, String> param = new HashMap<String, String>();
			param.put("access_token", access_token);
			text = HttpRequestProxy.doGet(TencentConfig.requestOpenId, param, "utf-8");
			int start = text.indexOf("\"openid\":\"");
			String openId = text.substring(start + 10, start + 10 + 32).trim();
			if (openId != null && !openId.equals(""))
			{
				dto.setIsSuccess(true);
				dto.setUserId(openId);
			}
		}
		catch (Exception ex)
		{
			log.error("QQ登陆验证失败", ex);
		}
		return dto;
	}
}
