/**
 * 
 */
package com.huayin.printmanager.pay.inface.weixin;

/**
 * 微信接口支撑
 * 
 * @author mys
 *
 */
public class PayWeixinSupport {
	
	/**
	 * 接口URL - 微信统一下单
	 */
	protected static final String URL_UNIFEDORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	/**
	 * 接口URL - 微信订单查询
	 */
	protected static final String URL_ORDERQUERY = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	/**
	 * 接口URL - 微信申请退款
	 */
	protected static final String URL_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	
	/**
	 * 接口URL - 微信退款查询
	 */
	protected static final String URL_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";
}
