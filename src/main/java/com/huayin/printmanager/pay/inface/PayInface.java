/**
 * 
 */
package com.huayin.printmanager.pay.inface;

import java.util.Map;

/**
 * 支付接口-人口
 * @author mys
 *
 */
public interface PayInface {

	/**
	 * 支付入口
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> go(Map<String, String> params) throws Exception;
}
