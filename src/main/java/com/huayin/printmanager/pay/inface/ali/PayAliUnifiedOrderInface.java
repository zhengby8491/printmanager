/**
 * 
 */
package com.huayin.printmanager.pay.inface.ali;

import java.util.Map;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.util.ExceptionUtil;

/**
 * 下单
 * @author mys
 *
 */
public class PayAliUnifiedOrderInface extends PayAliInfaceAbs implements PayInface {

	/**
	 * Step1: 验证参数
	 * @param params
	 * @throws Exception
	 */
	void veryify(Map<String, String> params) throws Exception {
		ExceptionUtil.isNull(params.get("sign_type"), "The field sign_type can not be null.");
	}
	
	@Override
	String genPostUrl() {
		return "create_direct_pay_by_user";
	}
}
