/**
 * 
 */
package com.huayin.printmanager.pay.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Map工具类
 * 
 * @author mys
 *
 */
public class MapUtils {
	
	 /**
     * Logs the given exception to <code>System.out</code>.
     * <p>
     * This method exists as Jakarta Collections does not depend on logging.
     *
     * @param ex  the exception to log
     */
    protected static void logInfo(final Exception ex) {
        System.out.println("INFO: Exception: " + ex);
    }

	public static Map<String, String> toMap(Class<?> clazz) {
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			try {
				String obj = (String) field.get(clazz);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				logInfo(e);
			} catch (IllegalAccessException e) {
				logInfo(e);
			}
		}
		return map;
	}
}
