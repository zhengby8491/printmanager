/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月26日 上午10:21:06
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.i18n;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.utils.MessageUtil;

/**
 * <pre>
 * 国际化框架 - 初始化
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
public class ResourceBundleMessageSource extends org.springframework.context.support.ResourceBundleMessageSource
{
	private static ResourceBundle current = null;

	@Override
	protected ResourceBundle getResourceBundle(String basename, Locale locale)
	{
		ResourceBundle resourceBundle = super.getResourceBundle(basename, locale);
		current = resourceBundle;
		reload();
		return resourceBundle;
	}

	private void reload()
	{
		I18nResource.load();
		BasicI18nResource.load();
	}

	/**
	 * <pre>
	 * 根据code获取国际化，如果没有则使用code作为结果
	 * </pre>
	 * @param code
	 * @return
	 * @since 1.0, 2018年1月3日 上午10:17:59, think
	 */
	public static String getMessageExist(String code)
	{
		String value = code;
		if (hasMessage(code))
		{
			value = getMessage(code);
		}
		return value;
	}

	/**
	 * <pre>
	 * 根据code获取国际化，如果没有则返回null
	 * </pre>
	 * @param code
	 * @return
	 * @since 1.0, 2018年1月3日 上午10:22:44, think
	 */
	public static String getMessageNull(String code)
	{
		String value = null;
		if (hasMessage(code))
		{
			value = getMessage(code);
		}
		return value;
	}

	/**
	 * <pre>
	 * 根据item获取枚举国际化，如果没有则返回null
	 * </pre>
	 * @param item
	 * @return
	 * @since 1.0, 2018年1月3日 上午10:34:36, think
	 */
	public static String getMessageEnum(Object item)
	{
		String label = null;
		if (item instanceof Enum<?>)
		{
			String value = ((Enum<?>) item).name();
			String i18nCode = "i18n.enum." + item.getClass().getSimpleName().toLowerCase() + "." + value;
			label = getMessageNull(i18nCode);
		}
		return label;
	}
	
	/**
	 * <pre>
	 * 根据item获取枚举国际化，如果没有则返回defaultValue
	 * </pre>
	 * @param item
	 * @param defaultValue
	 * @return
	 * @since 1.0, 2018年1月3日 上午11:08:38, think
	 */
	public static String getMessageEnum(Object item, String defaultValue)
	{
		String value = getMessageEnum(item);
		if(value == null)
		{
			value = defaultValue;
		}
		return value;
	}

	/**
	 * <pre>
	 * 根据code获取国际化
	 * </pre>
	 * @param code
	 * @return
	 * @since 1.0, 2018年1月3日 上午10:18:24, think
	 */
	public static String getMessage(String code)
	{
		return current.getString(code);
	}

	/**
	 * <pre>
	 * 是否存在国际化code
	 * </pre>
	 * @param code
	 * @return
	 * @since 1.0, 2018年1月3日 上午10:18:39, think
	 */
	public static boolean hasMessage(String code)
	{
		return current.containsKey(code);
	}

	/**
	 * <pre>
	 * 国际化格式化
	 * 例如：[{0}，我们得项目可以很强大] == [销售，我们得项目可以很强大]
	 * </pre>
	 * @param pattern
	 * @param arguments
	 * @return
	 * @since 1.0, 2017年12月29日 上午9:58:49, think
	 */
	public static String i18nFormatter(String pattern, Object... arguments)
	{
		return MessageUtil.formatter(pattern, arguments);
	}

	/**
	 * <pre>
	 * 国际化格式化
	 * 例如：[{saler}，我们得项目可以很{bigger}] == [销售，我们得项目可以很厉害]
	 * </pre>
	 * @param pattern
	 * @param arguments
	 * @return
	 * @since 1.0, 2017年12月29日 上午10:26:54, think
	 */
	public static String i18nFormatter(String pattern, Map<String, Object> arguments)
	{
		return MessageUtil.formatter(pattern, arguments);
	}
}
