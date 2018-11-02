package com.huayin.printmanager.expresslogin.tencent;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.GsonBuilder;
import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.expresslogin.ExpressNotifyService;
import com.huayin.printmanager.expresslogin.dto.LoginResultDto;
import com.huayin.printmanager.expresslogin.tencent.config.WechatConfig;
import com.huayin.printmanager.expresslogin.tencent.dto.WechatAccessTokenDto;
import com.huayin.printmanager.expresslogin.tencent.dto.WechatUserInfoDto;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * 
 * <pre>
 * 框架  - 微信用户登录验证
 * </pre>
 * @author mys
 * @version 1.0, 2017年2月27日
 */
public class WechatNotifyImpl implements ExpressNotifyService
{
	private static final Log log = LogFactory.getLog(WechatNotifyImpl.class);

	private static final GsonBuilder gsonBuilder = new GsonBuilder();

	@Override
	public LoginResultDto validataNotify(HttpServletRequest request, UserShareType userShareType)
	{
		LoginResultDto dto = new LoginResultDto(false, UserShareType.WEIXIN);

		String code = request.getParameter("code");
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("grant_type", "authorization_code");
		parameters.put("appid", SystemConfigUtil.getExpressLoginWechatAppId());
		parameters.put("secret", SystemConfigUtil.getExpressLoginWechatAppKey());
		parameters.put("code", code);
		try
		{
			String text = HttpRequestProxy.doGet(WechatConfig.requestAccessToken, parameters, "utf-8");
			WechatAccessTokenDto accessToken = gsonBuilder.create().fromJson(text, WechatAccessTokenDto.class);
			dto.setAccessToken(accessToken.getAccess_token());
			Map<String, String> param = new HashMap<String, String>();
			param.put("openid", SystemConfigUtil.getExpressLoginWechatAppId());
			param.put("access_token", accessToken.getAccess_token());
			String userinfo = HttpRequestProxy.doGet(WechatConfig.requestOpenId, param, "utf-8");
			WechatUserInfoDto user = gsonBuilder.create().fromJson(userinfo, WechatUserInfoDto.class);
			if (user != null && !user.getOpenid().equals(""))
			{
				dto.setIsSuccess(true);
				dto.setUserId(user.getOpenid().trim());
			}
		}
		catch (Exception ex)
		{
			log.error("微信登陆验证失败", ex);
		}
		return dto;
	}
}
