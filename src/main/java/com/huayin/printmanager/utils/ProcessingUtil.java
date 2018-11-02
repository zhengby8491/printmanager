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

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * <pre>
 * 公共 - 进度条管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class ProcessingUtil
{
	public static final Map<String, Integer> PROCESSING = Maps.newHashMap();

	public static void put(String key, Integer num)
	{
		PROCESSING.put(key, num);
	}

	public static void remove(String key)
	{
		PROCESSING.remove(key);
	}

	public static Integer get(String key)
	{
		Integer num = PROCESSING.get(key);
		return num == null ? 0 : num;
	}
}
