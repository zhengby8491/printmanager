/**
 * 
 */
package com.huayin.printmanager.pay.inface.weixin;

import java.util.Map;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.util.ExceptionUtil;

/**
 * 订单查询
 * 
 * @author mys
 *
 */
public class PayWeixinOrderQueryInface extends PayWeixinInfaceAbs implements PayInface {

	/**
	 * Step1: 验证参数
	 * @param params
	 * @throws Exception
	 */
	void veryify(Map<String, String> params) throws Exception {
		ExceptionUtil.isNull(params.get("app_id"), "The field app_id can not be null.");
		ExceptionUtil.isNull(params.get("mch_id"), "The field mch_id can not be null.");
		ExceptionUtil.isNull(params.get("out_trade_no"), "The field out_trade_no can not be null.");
		ExceptionUtil.isNull(params.get("nonce_str"), "The field nonce_str can not be null.");
		ExceptionUtil.isNull(params.get("sign"), "The field sign can not be null.");
	}

	@Override
	String genPostUrl() {
		return PayWeixinSupport.URL_UNIFEDORDER;
	}

}
