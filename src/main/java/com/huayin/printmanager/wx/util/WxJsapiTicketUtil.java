/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.utils.JedisUtils;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 框架 - V型知识库 www.vxzsk.com
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WxJsapiTicketUtil
{
	/**
	 * 微信扫一扫缓存access_token（access_token的有效期目前为2个小时）
	 */
	private static final String ACCESS_TOKEN_CACHE = "WX_SCAN_ACCESS_TOKEN_CACHE";

	/**
	 * 微信扫一扫缓存ticket（jsapi_ticket的有效期为7200秒）
	 */
	private static final String JSAPI_TICKET = "WX_SCAN_JSAPI_TICKET";

	/**
	 * 1小时58分
	 */
	private static final int hour2 = 2 * 59 * 60;

	/**
	 * 7100秒
	 */
	private static final int seconds7200 = 7100;

	/***
	 * 模拟get请求
	 * 
	 * @param url
	 * @param charset
	 * @param timeout
	 * @return
	 */
	public static String sendGet(String url, String charset, int timeout)
	{
		String result = "";
		try
		{
			URL u = new URL(url);
			try
			{
				URLConnection conn = u.openConnection();
				conn.connect();
				conn.setConnectTimeout(timeout);
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
				String line = "";
				while ((line = in.readLine()) != null)
				{

					result = result + line;
				}
				in.close();
			}
			catch (IOException e)
			{
				return result;
			}
		}
		catch (MalformedURLException e)
		{
			return result;
		}

		return result;
	}

	/***
	 * 获取acess_token 来源www.vxzsk.com
	 * 
	 * @return
	 */
	public static String getAccessToken()
	{

		// 缓存中获取access_token
		String accessTokenCache = JedisUtils.get(ACCESS_TOKEN_CACHE);
		if (StringUtils.isNotEmpty(accessTokenCache))
			return accessTokenCache;

		// 去微信服务中心获取
		String appid = SystemConfigUtil.getConfig(SysConstants.WX_APPID);// 应用ID
		String appSecret = SystemConfigUtil.getConfig(SysConstants.WX_APPSECRET);// (应用密钥)
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + appSecret + "";
		String backData = sendGet(url, "utf-8", 10000);
		String accessToken = (String) new JSONObject(backData).get("access_token");
		// 缓存到redis中
		JedisUtils.set(ACCESS_TOKEN_CACHE, accessToken, hour2);
		return accessToken;
	}

	/***
	 * 获取jsapiTicket 来源 www.vxzsk.com
	 * 
	 * @return
	 */
	public static String getJSApiTicket()
	{

		// 缓存中获取access_token
		String ticketCache = JedisUtils.get(JSAPI_TICKET);
		if (StringUtils.isNotEmpty(ticketCache))
			return ticketCache;

		String acess_token = WxJsapiTicketUtil.getAccessToken();
		String urlStr = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acess_token + "&type=jsapi";
		String backData = sendGet(urlStr, "utf-8", 10000);
		String ticket = (String) new JSONObject(backData).get("ticket");
		// 缓存到redis中
		JedisUtils.set(JSAPI_TICKET, ticket, seconds7200);
		return ticket;

	}

	public static void main(String[] args)
	{
		String jsapiTicket = WxJsapiTicketUtil.getJSApiTicket();
		System.out.println("调用微信jsapi的凭证票为：" + jsapiTicket);
	}
}