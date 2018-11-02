/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月5日 下午5:26:58
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.utils;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * <pre>
 * 外部接口请求方法
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月5日下午5:26:58, zhengby
 */
public class HttpRequest
{
	public static String sendPost(String url, String param)
	{
		HttpClient httpClient = new HttpClient();
		// 获得POST请求方法
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2453.0 Safari/537.36");
		postMethod.addRequestHeader("Content-Type", "application/json; charset=utf-8");
		ByteArrayRequestEntity entity = new ByteArrayRequestEntity(param.getBytes(), "UTF-8");
		postMethod.setRequestEntity(entity);
	
		try
		{
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(4000);
//			String responesStr = postMethod.getResponseBodyAsString();
			httpClient.executeMethod(postMethod);
			InputStream stream = postMethod.getResponseBodyAsStream();
			StringBuilder content = new StringBuilder();
			byte[] b = new byte[1024];
			int lens = -1;
			while ((lens = stream.read(b)) > 0)
			{
				content.append(new String(b, 0, lens));
			}
			String strcont = content.toString();// 内容
			return strcont;
		}
		catch (Exception e)
		{
			System.out.println("请求错误:" + e.getMessage());
		} finally {
			postMethod.releaseConnection();// 释放连接
		}
		return null;
	}
}
