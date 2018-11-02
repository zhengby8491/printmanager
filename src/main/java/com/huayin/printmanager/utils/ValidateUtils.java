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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * 公共 - 验证数据格式
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class ValidateUtils
{
	final static String mobile_regex = "^(13[0-9]|14[5,7]|15[0-9]|18[0-9]|17[0-9])[0-9]{8}$";

	/**
	 * <pre>
	 * 检查手机号码格式
	 * </pre>
	 * @param mobile
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:49:08, think
	 */
	public static boolean checkMobile(String mobile)
	{
		if (mobile == null || mobile.trim().equals(""))
		{
			return false;
		}
		else
		{
			Pattern p = Pattern.compile(mobile_regex);
			Matcher m = p.matcher(mobile);
			return m.matches();
		}
	}
}
