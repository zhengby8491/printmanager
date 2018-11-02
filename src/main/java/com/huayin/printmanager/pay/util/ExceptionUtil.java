package com.huayin.printmanager.pay.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 异常帮助类
 * @author mys
 *
 */
public class ExceptionUtil {
	
	public static void isNull(Map<String, ?> map, String msg) throws Exception {
		if(null == map)
			throw new Exception(msg);
	}
	
	public static void isNull(Integer i, String msg) throws Exception {
		if(null == i)
			throw new Exception(msg);
	}
	
	public static void isNull(String str, String msg) throws Exception {
		if(StringUtils.isEmpty(str))
			throw new Exception(msg);
	}
	
	public static void isNull(Collection<?> collection, String msg) throws Exception {
		if(null == collection || collection.size() == 0)
			throw new Exception(msg);
	}
	
	public static void isTrue(boolean expression, String msg) throws Exception {
		if(!expression)
			throw new Exception(msg);
	}
}
