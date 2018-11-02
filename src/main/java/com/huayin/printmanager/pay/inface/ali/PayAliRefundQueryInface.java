/**
 * 
 */
package com.huayin.printmanager.pay.inface.ali;

import java.util.Map;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.util.ExceptionUtil;

/**
 * 退款查询
 * @author mys
 *
 */
public class PayAliRefundQueryInface extends PayAliInfaceAbs implements PayInface {

	/**
	 * Step1: 验证参数
	 * @param params
	 * @throws Exception
	 */
	void veryify(Map<String, String> params) throws Exception {
		ExceptionUtil.isNull(params.get("app_id"), "The field app_id can not be null.");
		ExceptionUtil.isNull(params.get("sign_type"), "The field sign_type can not be null.");
	}
	
	@Override
	String genPostUrl() {
		return "alipay.trade.fastpay.refund.query ";
	}
}
