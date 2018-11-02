/**
 * 
 */
package com.huayin.printmanager.pay.inface.weixin;

import java.util.Map;

import com.huayin.printmanager.pay.http.HttpSupport;
import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.support.PaySupport;
import com.huayin.printmanager.pay.support.PaySignSupport;

/**
 * 微信公共（抽象）支付接口
 * @author mys
 */
public abstract class PayWeixinInfaceAbs implements PayInface {

	@Override
	public Map<String, Object> go(Map<String, String> params) throws Exception {
		// Step1: 验证参数
		veryify(params);
		// Step2: 生成Sign
		String sign = PaySignSupport.buildRequestSignForWeixin(params, params.remove("key"));
		params.put("sign", sign.toUpperCase());
		// Step3: post验证
		String postUrl = genPostUrl();
		String xmlString = HttpSupport.post(postUrl, params);
		if (xmlString == null && "".equals(xmlString)) {
			throw new Exception("call api " + postUrl + " failed.");
		}
		return PaySupport.getMapFromXML(xmlString);
	}
	
	/**
	 * 验证参数
	 * @param params
	 * @throws Exception
	 */
	abstract void veryify(Map<String, String> params) throws Exception;
	
	/**
	 * 获取post url
	 */
	abstract String genPostUrl();
}
