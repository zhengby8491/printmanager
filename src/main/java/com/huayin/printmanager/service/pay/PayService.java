/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.pay;

import java.util.Map;

import com.huayin.printmanager.persist.entity.sys.Buy;

/**
 * <pre>
 * 支付接口
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月26日上午10:01:16, zhengby, 代码规范
 */
public interface PayService
{

	public static final String KEY_QRCODE = "QRCODE_URL_";

	public static final String KEY_TOTAL = "KEY_TOTAL";

	/**
	 * 支付宝PC支付
	 * @param loginName
	 * @param token
	 * @param out_trade_no
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> alipayByPc(Buy product,Long orderId) throws Exception;
	

	/**
	 * 支付宝 验证回调接口
	 * @param out_trade_no
	 * @param trade_no
	 * @param trade_status
	 * @param params
	 * @return
	 * @throws RpcException
	 */
	boolean verifyAlipay(String out_trade_no, String trade_no, String trade_status, Map<String, String> params)throws Exception;

	/**
	 * 微信PC支付
	 * @param orderNo
	 * @param payAmount
	 * @param subject
	 * @param userIp
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> weixinByPc(Buy product,String orderNo,String openid,String trade_type,String attach)
			throws Exception;

	/**
	 * 微信支付回调方法
	 * @param requestString
	 * @return
	 */
	// String getWXPayResult(String requestString);
}
