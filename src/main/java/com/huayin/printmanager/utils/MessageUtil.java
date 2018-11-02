/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月29日 上午10:19:15
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.text.MessageFormat;
import java.util.Map;

/**
 * <pre>
 * 工具 - message常用
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 */
public class MessageUtil
{
	/**
	 * <pre>
	 * 消息国际化
	 * 例如：{0}，我们得项目可以很强大 = 销售，我们得项目可以很强大
	 * </pre>
	 * @param pattern
	 * @param arguments
	 * @return
	 * @since 1.0, 2017年12月29日 上午10:19:47, think
	 */
	public static String formatter(String pattern, Object ... arguments)
	{
		return MessageFormat.format(pattern, arguments);
	}
	
	/**
	 * <pre>
	 * 消息国际化，
	 * 例如：{saler}，我们得项目可以很{bigger} = 销售，我们得项目可以很厉害
	 * </pre>
	 * @param pattern
	 * @param arguments
	 * @return
	 * @since 1.0, 2017年12月29日 上午10:24:57, think
	 */
	public static String formatter(String pattern, Map<String, Object> arguments)
	{
		String tempPattern = pattern;
		for(String key : arguments.keySet())
		{
			tempPattern = tempPattern.replaceAll("\\{" + key + "\\}", arguments.get(key).toString());
		}
		return tempPattern;
	}
}
