
package com.huayin.printmanager.pay.factory;

import com.huayin.printmanager.pay.inface.PayInface;
import com.huayin.printmanager.pay.inface.ali.PayAliOrderQueryInface;
import com.huayin.printmanager.pay.inface.ali.PayAliRefundInface;
import com.huayin.printmanager.pay.inface.ali.PayAliRefundQueryInface;
import com.huayin.printmanager.pay.inface.ali.PayAliUnifiedOrderInface;
import com.huayin.printmanager.pay.inface.weixin.PayWeixinOrderQueryInface;
import com.huayin.printmanager.pay.inface.weixin.PayWeixinRefundInface;
import com.huayin.printmanager.pay.inface.weixin.PayWeixinRefundQueryInface;
import com.huayin.printmanager.pay.inface.weixin.PayWeixinUnifiedOrderInface;

/**
 * 支付工厂
 * @author mys
 */
public class PayFactory
{

	/**
	 * 微信公众接口
	 */
	public static final Integer WEIXIN = 1;

	/**
	 * 支付开发平台
	 */
	public static final Integer ALI = 2;

	/**
	 * <pre>
	 * 统一下单
	 * </pre>
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static PayInface createUnifiedOrder(Integer type) throws Exception
	{
		if (WEIXIN == type)
			return new PayWeixinUnifiedOrderInface();
		else if (ALI == type)
			return new PayAliUnifiedOrderInface();
		else
			throw new Exception("unknow type " + type);
	}

	/**
	 * <pre>
	 * 订单查询
	 * </pre>
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static PayInface createOrderQuery(Integer type) throws Exception
	{
		if (WEIXIN == type)
			return new PayWeixinOrderQueryInface();
		else if (ALI == type)
			return new PayAliOrderQueryInface();
		else
			throw new Exception("unknow type " + type);
	}

	/**
	 * <pre>
	 * 退货
	 * </pre>
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static PayInface createRefund(Integer type) throws Exception
	{
		if (WEIXIN == type)
			return new PayWeixinRefundInface();
		else if (ALI == type)
			return new PayAliRefundInface();
		else
			throw new Exception("unknow type " + type);
	}

	/**
	 * <pre>
	 * 退货查询
	 * </pre>
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static PayInface createRefundQuery(Integer type) throws Exception
	{
		if (WEIXIN == type)
			return new PayWeixinRefundQueryInface();
		else if (ALI == type)
			return new PayAliRefundQueryInface();
		else
			throw new Exception("unknow type " + type);
	}
}
