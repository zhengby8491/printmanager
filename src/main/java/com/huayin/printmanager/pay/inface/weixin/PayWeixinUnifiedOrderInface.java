/**
 * 
 */
package com.huayin.printmanager.pay.inface.weixin;

import java.util.Map;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.util.ExceptionUtil;

/**
 * 下单
 * @author mys
 *
 */
public class PayWeixinUnifiedOrderInface extends PayWeixinInfaceAbs implements PayInface {

	/**
	 * Step1: 验证参数
	 * 
	 * @param params
	 * @throws Exception
	 */
	void veryify(Map<String, String> params) throws Exception {
		ExceptionUtil.isNull(params.get("appid"), "The field app_id can not be null.");
		ExceptionUtil.isNull(params.get("mch_id"), "The field mch_id can not be null.");
		ExceptionUtil.isNull(params.get("nonce_str"), "The field nonce_str can not be null.");
//		ExceptionUtil.isNull(params.get("sign"), "The field sign can not be null.");
		ExceptionUtil.isNull(params.get("body"), "The field body can not be null.");
		ExceptionUtil.isNull(params.get("out_trade_no"), "The field out_trade_no can not be null.");
		ExceptionUtil.isNull(params.get("total_fee"), "The field total_fee can not be null.");
		ExceptionUtil.isNull(params.get("spbill_create_ip"), "The field spbill_create_ip can not be null.");
		ExceptionUtil.isNull(params.get("notify_url"), "The field notify_url can not be null.");
		ExceptionUtil.isNull(params.get("trade_type"), "The field trade_type can not be null.");
	}

	@Override
	String genPostUrl() {
		return PayWeixinSupport.URL_UNIFEDORDER;
	}

}
