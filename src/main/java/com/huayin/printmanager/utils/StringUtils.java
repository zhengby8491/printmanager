/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import com.google.common.collect.Lists;
import com.huayin.common.cis.ComponentContextLoader;

/**
 * <pre>
 * 公共 - 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{
	private static final char SEPARATOR = '_';

	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * <pre>
	 * 转换为字节数组
	 * </pre>
	 * @param str
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:20, think
	 */
	public static byte[] getBytes(String str)
	{
		if (str != null)
		{
			try
			{
				return str.getBytes(CHARSET_NAME);
			}
			catch (UnsupportedEncodingException e)
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 字节数组转字符串
	 * </pre>
	 * @param bytes
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:27, think
	 */
	public static String toString(byte[] bytes)
	{
		try
		{
			return new String(bytes, CHARSET_NAME);
		}
		catch (UnsupportedEncodingException e)
		{
			return EMPTY;
		}
	}

	/**
	 * <pre>
	 * 对象转字符串
	 * </pre>
	 * @param obj
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:33, think
	 */
	public static String toString(Object obj)
	{
		return obj == null ? "" : obj.toString().trim();
	}

	/**
	 * <pre>
	 * TOD是否包含字符串O
	 * </pre>
	 * @param str
	 * @param strs
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:40, think
	 */
	public static boolean inString(String str, String... strs)
	{
		if (str != null)
		{
			for (String s : strs)
			{
				if (str.equals(trim(s)))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 替换掉HTML标签方法
	 * </pre>
	 * @param html
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:46, think
	 */
	public static String replaceHtml(String html)
	{
		if (isBlank(html))
		{
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * <pre>
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * </pre>
	 * @param html
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:42:53, think
	 */
	public static String replaceMobileHtml(String html)
	{
		if (html == null)
		{
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * <pre>
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车。
	 * </pre>
	 * @param txt
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:00, think
	 */
	public static String toHtml(String txt)
	{
		if (txt == null)
		{
			return "";
		}
		return replace(replace(Encodes.escapeHtml(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}

	/**
	 * <pre>
	 * 缩略字符串（不区分中英文字符）
	 * </pre>
	 * @param str
	 * @param length
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:07, think
	 */
	public static String abbr(String str, int length)
	{
		if (str == null)
		{
			return "";
		}
		try
		{
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray())
			{
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3)
				{
					sb.append(c);
				}
				else
				{
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * <pre>
	 * 缩略字符串（不区分中英文字符）
	 * </pre>
	 * @param param
	 * @param length
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:16, think
	 */
	public static String abbr2(String param, int length)
	{
		if (param == null)
		{
			return "";
		}
		StringBuffer result = new StringBuffer();
		int n = 0;
		char temp;
		boolean isCode = false; // 是不是HTML代码
		boolean isHTML = false; // 是不是HTML特殊字符,如&nbsp;
		for (int i = 0; i < param.length(); i++)
		{
			temp = param.charAt(i);
			if (temp == '<')
			{
				isCode = true;
			}
			else if (temp == '&')
			{
				isHTML = true;
			}
			else if (temp == '>' && isCode)
			{
				n = n - 1;
				isCode = false;
			}
			else if (temp == ';' && isHTML)
			{
				isHTML = false;
			}
			try
			{
				if (!isCode && !isHTML)
				{
					n += String.valueOf(temp).getBytes("GBK").length;
				}
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}

			if (n <= length - 3)
			{
				result.append(temp);
			}
			else
			{
				result.append("...");
				break;
			}
		}
		// 取出截取字符串中的HTML标记
		String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
		// 去掉不需要结素标记的HTML标记
		temp_result = temp_result.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>", "");
		// 去掉成对的HTML标记
		temp_result = temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
		// 用正则表达式取出标记
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
		Matcher m = p.matcher(temp_result);
		List<String> endHTML = Lists.newArrayList();
		while (m.find())
		{
			endHTML.add(m.group(1));
		}
		// 补全不成对的HTML标记
		for (int i = endHTML.size() - 1; i >= 0; i--)
		{
			result.append("</");
			result.append(endHTML.get(i));
			result.append(">");
		}
		return result.toString();
	}

	/**
	 * <pre>
	 * 转换为Double类型
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:22, think
	 */
	public static Double toDouble(Object val)
	{
		if (val == null)
		{
			return 0D;
		}
		try
		{
			return Double.valueOf(trim(val.toString()));
		}
		catch (Exception e)
		{
			return 0D;
		}
	}

	/**
	 * <pre>
	 * 转换为Float类型
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:30, think
	 */
	public static Float toFloat(Object val)
	{
		return toDouble(val).floatValue();
	}

	/**
	 * <pre>
	 * 转换为Long类型
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:37, think
	 */
	public static Long toLong(Object val)
	{
		return toDouble(val).longValue();
	}

	/**
	 * <pre>
	 * 转换为Integer类型
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:44, think
	 */
	public static Integer toInteger(Object val)
	{
		return toLong(val).intValue();
	}

	/**
	 * <pre>
	 * 获得i18n字符串
	 * </pre>
	 * @param code
	 * @param args
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:43:51, think
	 */
	public static String getMessage(String code, Object[] args)
	{
		LocaleResolver localLocaleResolver = (LocaleResolver) ComponentContextLoader.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return ComponentContextLoader.getApplicationContext().getMessage(code, args, localLocale);
	}

	/**
	 * <pre>
	 * 驼峰命名法工具
	 * toCamelCase("hello_world") == "helloWorld" 
	 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * toUnderScoreCase("helloWorld") = "hello_world"
	 * </pre>
	 * @param s
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:44:07, think
	 */
	public static String toCamelCase(String s)
	{
		if (s == null)
		{
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);

			if (c == SEPARATOR)
			{
				upperCase = true;
			}
			else if (upperCase)
			{
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			}
			else
			{
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * <pre>
	 * 驼峰命名法工具
	 * toCamelCase("hello_world") == "helloWorld" 
	 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * toUnderScoreCase("helloWorld") = "hello_world"
	 * </pre>
	 * @param s
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:44:32, think
	 */
	public static String toCapitalizeCamelCase(String s)
	{
		if (s == null)
		{
			return null;
		}
		s = toCamelCase(s);
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * <pre>
	 * 驼峰命名法工具
	 * toCamelCase("hello_world") == "helloWorld" 
	 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * toUnderScoreCase("helloWorld") = "hello_world"
	 * </pre>
	 * @param s
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:44:56, think
	 */
	public static String toUnderScoreCase(String s)
	{
		if (s == null)
		{
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1))
			{
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c))
			{
				if (!upperCase || !nextUpperCase)
				{
					sb.append(SEPARATOR);
				}
				upperCase = true;
			}
			else
			{
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * <pre>
	 * 如果不为空，则设置值
	 * </pre>
	 * @param target
	 * @param source
	 * @since 1.0, 2017年10月26日 上午10:45:21, think
	 */
	public static void setValueIfNotBlank(String target, String source)
	{
		if (isNotBlank(source))
		{
			target = source;
		}
	}

	/**
	 * <pre>
	 * 转换为JS获取对象值，生成三目运算返回结果
	 * </pre>
	 * @param objectString 对象串 例如：row.user.id 返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:45:29, think
	 */
	public static String jsGetVal(String objectString)
	{
		StringBuilder result = new StringBuilder();
		StringBuilder val = new StringBuilder();
		String[] vals = split(objectString, ".");
		for (int i = 0; i < vals.length; i++)
		{
			val.append("." + vals[i]);
			result.append("!" + (val.substring(1)) + "?'':");
		}
		result.append(val.substring(1));
		return result.toString();
	}

	/**
	 * <pre>
	 * 将字符xml转换成一个Map
	 * </pre>
	 * @param xmlString
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月26日 上午10:45:44, think
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlString2Map(String xmlString) throws Exception
	{

		Element element = DocumentHelper.parseText(xmlString).getRootElement();
		Map<String, String> params = new HashMap<String, String>();
		List<Element> elements = element.elements();
		for (Element e : elements)
		{
			params.put(e.getName(), e.getStringValue());
		}
		return params;
	}
	
	/**
	 * <pre>
	 * 去除最后一个小数0
	 * </pre>
	 * @param val
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:45:51, think
	 */
	public static String removeLastZero(BigDecimal val)
	{
		String rex =  "^-?[1-9]\\d*$";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(val.toString());
		if (!m.matches())
		{
			String newVal = val.toString().replaceAll("0*$", "");
			String lastChar = newVal.substring(newVal.length() - 1, newVal.length());
			if (".".equals(lastChar))
			{
				newVal = newVal.substring(0, newVal.length() - 1);
			}
			return newVal;
		}
		return val.toString();
	}
	
	/**
	 * <pre>
	 * 获取个随机码
	 * </pre>
	 * @param length
	 * @return
	 * @since 1.0, 2018年7月20日 上午11:33:17, zhengby
	 */
	public static String genRandomCode(int length)
	{
		String base = "0123456789";
		return genRandomCode(base,length);
	}
	
	/**
	 * <pre>
	 * 获取个随机码
	 * </pre>
	 * @param base
	 * @param length
	 * @return
	 * @since 1.0, 2018年7月20日 上午11:37:50, zhengby
	 */
	public static String genRandomCode(String base,int length)
	{
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
