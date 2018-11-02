/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.huayin.printmanager.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.net.HttpHeaders;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;

/**
 * <pre>
 * 公共 - Servlet工具
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class ServletUtils
{

	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	// 静态文件后缀
	private final static String[] staticFiles = StringUtils
			.split(SystemConfigUtil.getConfig(SysConstants.SITE_STATIC_FILE_SUFFIX), ",");

	// 动态映射URL后缀
	// private final static String urlSuffix = Global.getUrlSuffix();

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds)
	{
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setNoCacheHeader(HttpServletResponse response)
	{
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate)
	{
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag)
	{
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改. 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified)
	{
		long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000))
		{
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效. 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * @param etag 内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag)
	{
		String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (headerValue != null)
		{
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue))
			{
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens())
				{
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag))
					{
						conditionSatisfied = true;
					}
				}
			}
			else
			{
				conditionSatisfied = true;
			}

			if (conditionSatisfied)
			{
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName)
	{
		try
		{
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
		}
		catch (UnsupportedEncodingException e)
		{
			e.getMessage();
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils. 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix)
	{
		Validate.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null)
		{
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements())
		{
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre))
			{
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0)
				{
					values = new String[] {};
					// Do nothing, no values found at all.
				}
				else if (values.length > 1)
				{
					params.put(unprefixed, values);
				}
				else
				{
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix)
	{
		StringBuilder queryStringBuilder = new StringBuilder();

		String pre = prefix;
		if (pre == null)
		{
			pre = "";
		}
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
			if (it.hasNext())
			{
				queryStringBuilder.append("&");
			}
		}
		return queryStringBuilder.toString();
	}

	/**
	 * 客户端对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic(String userName, String password)
	{
		String encode = userName + ":" + password;
		return "Basic " + Encodes.encodeBase64(encode.getBytes());
	}

	/**
	 * 获取当前请求对象
	 * @return
	 */
	public static HttpServletRequest getRequest()
	{
		try
		{
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 判断访问URI是否是静态文件请求
	 * @throws Exception
	 */
	public static boolean isStaticFile(String uri)
	{
		if (staticFiles == null)
		{
			try
			{
				throw new Exception("检测到“properties”中没有配置“web.staticFile”属性。配置示例：\n#静态文件后缀\n"
						+ "web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs))
		// && !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
		// return true;
		// }
		if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, ".jsp")
				&& !StringUtils.endsWithAny(uri, ".java"))
		{
			return true;
		}
		return false;
	}

	// 文件下载 主要方法
	public static void download(HttpServletRequest request, HttpServletResponse response, String fileName)
	{
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try
		{
			request.setCharacterEncoding("UTF-8");
			

			// 获取项目根目录
			String ctxPath = request.getSession().getServletContext().getRealPath("");

			// 获取下载文件露肩
			String downLoadPath = ctxPath + "/file/" + fileName;

			// 获取文件的长度
			long fileLength = new File(downLoadPath).length();

			// 设置文件输出类型
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition",
					"attachment; filename=" +URLEncoder.encode(fileName, "UTF-8"));
			// 设置输出长度
			response.setHeader("Content-Length", String.valueOf(fileLength));
			// 获取输入流
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			// 输出流
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
			{
				bos.write(buff, 0, bytesRead);
			}
			// 关闭流
			bis.close();
			bos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bis.close();
				bos.close();
				bis = null;
				bos = null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
