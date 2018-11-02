/**
 * 
 */
package com.huayin.printmanager.pay.inface.ali;

import java.util.Map;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.support.PaySignSupport;

import com.google.common.collect.Maps;

/**
 * 阿里支付（抽象）支付接口
 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.EVUYbQ&treeId=155&articleId=104778&docType=1
 * @author mys
 *
 */
public abstract class PayAliInfaceAbs implements PayInface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.huayin.printmanager.pay.inface.PayInface#go(java.util.Map)
	 */
	@Override
	public Map<String, Object> go(Map<String, String> params) throws Exception {
		// Step1: 验证参数
		veryify(params);
		params.put("service", genPostUrl());
		// Step2: 生成Sign
		String privateKey = params.get("private_key");
		params.remove("private_key");
		params.remove("public_key");
		String sign = PaySignSupport.buildRequestSignForAli(params, privateKey);
		params.put("sign", sign);
		// Step3: post验证（返回form表单提交）
		String form = PayAliSupport.buildRequest(params, "post", "Submit");
		// Step4: 返回form
		Map<String, Object> ret = Maps.newHashMap();
		ret.put("form", form);
		return ret;
	}
	

	/**
	 * 验证参数
	 * @param params
	 * @throws Exception
	 */
	abstract void veryify(Map<String, String> params) throws Exception;

	/**
	 * 支付接口方法
	 * @return
	 */
	abstract String genPostUrl();
}
