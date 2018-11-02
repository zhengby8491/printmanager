/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.pay.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.pay.config.AlipayConfig;
import com.huayin.printmanager.pay.factory.PayFactory;
import com.huayin.printmanager.pay.support.PaySupport;
import com.huayin.printmanager.pay.util.HttpUtil;
import com.huayin.printmanager.pay.util.JsonMapper;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.pay.PayService;
import com.huayin.printmanager.utils.JedisUtils;

/**
 * <pre>
 * 支付功能
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月26日上午10:02:17, zhengby, 代码规范
 */
@Service
public class PayServiceImpl extends BaseServiceImpl implements PayService
{
	/*
	 * 支付宝支付回调
	 * @param subject 商品名称
	 * @param total_fee 支付金额
	 * @param body 订单描述
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> alipayByPc(Buy product, Long orderId) throws Exception
	{
		BuyRecord purchaseRecord = daoFactory.getCommonDao().getEntity(BuyRecord.class, orderId);

		String configInfo = SystemConfigUtil.getConfig(SysConstants.ZFB_CONF);
		configInfo = configInfo.replaceAll("&quot;", "\"");
		Map<String, String> map = (Map<String, String>) JsonUtils.jsonToMap(configInfo);

		Map<String, String> params = Maps.newHashMap();
		params.put("partner", map.get("partner"));
		params.put("seller_id", map.get("seller_id"));
		params.put("_input_charset", map.get("_input_charset"));
		params.put("sign_type", map.get("sign_type"));
		params.put("payment_type", map.get("payment_type"));
		params.put("notify_url", map.get("notify_url"));
		params.put("return_url", map.get("return_url"));
		params.put("public_key", map.get("public_key"));
		params.put("private_key", map.get("private_key"));
		params.put("subject", product.getName());
		params.put("out_trade_no", String.valueOf(orderId));
		BigDecimal bigDecimal = product.getPrice().add(purchaseRecord.getTax());
		BigDecimal rate = new BigDecimal(1);
		BigDecimal decimal = bigDecimal.multiply(rate);
		params.put("total_fee", decimal.setScale(2, BigDecimal.ROUND_UP).toString());
		params.put("body", product.getName());
		Map<String, Object> go = PayFactory.createUnifiedOrder(PayFactory.ALI).go(params);
		return go;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public Map<String, Object> weixinByPc(Buy product, String orderNo, String openid, String trade_type, String attach) throws Exception
	{
		Map<String, Object> result = Maps.newHashMap();
		BuyRecord purchaseRecord = null;
		Map<String, Object> prepay = null;
		if ("PC".equals(attach))
		{
			purchaseRecord = serviceFactory.getBuyService().getOrder(Long.parseLong(orderNo));
			prepay = JedisUtils.getObjectMap(purchaseRecord.getId().toString());
			if (prepay != null && prepay.get("code_url")!=null)
			{
				result.put("payStatus", true);
				result=prepay;
				return result;
			}
		}
		if ("WX".equals(attach))
		{
			purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(orderNo);
			prepay = JedisUtils.getObjectMap(purchaseRecord.getBillNo());
			if (prepay != null && prepay.get("code_url").equals(""))
			{
				result.put("payStatus", true);
				result=prepay;
				return result;
			}
		}

		// dongtai billno
		String billNoNew = String.valueOf(System.currentTimeMillis()) + new Random().nextInt(10);

		String configInfo = SystemConfigUtil.getConfig(SysConstants.WX_CONF);
		configInfo = configInfo.replaceAll("&quot;", "\"");
		Map<String, String> map = (Map<String, String>) JsonUtils.jsonToMap(configInfo);

		
		result.put("payStatus", false);
		result.put("payMethod", "1");

		Map<String, String> params = Maps.newHashMap();

		params.put("attach", attach);
		params.put("openid", openid);
		params.put("appid", map.get("appid"));
		params.put("mch_id", map.get("mch_id"));
		params.put("device_info", "WEB");
		params.put("sign_type", map.get("sign_type"));
		params.put("fee_type", "CNY");
		params.put("notify_url", map.get("notify_url"));
		params.put("trade_type", StringUtils.isNotBlank(trade_type) ? trade_type : "NATIVE");
		params.put("key", map.get("key"));
		params.put("body", product.getName());
		params.put("spbill_create_ip", HttpUtil.getHostIp());
		params.put("out_trade_no", purchaseRecord.getBillNo() + "_" + billNoNew);

		BigDecimal amountPaid = product.getPrice().add(purchaseRecord.getTax()); // 应付款
		BigDecimal rate = new BigDecimal(1); // 支付费用
		BigDecimal decimal = amountPaid.multiply(rate); // 应付款* 支付费用 = 实际付费
		BigDecimal scale = decimal.setScale(2, BigDecimal.ROUND_UP); // 进一法 保留2位小数
		BigDecimal wxDecimal = scale.multiply(new BigDecimal(100));
		BigDecimal scale2 = wxDecimal.setScale(0);
		String string = scale2.toString();
		params.put("total_fee", string);
		params.put("product_id", String.valueOf(product.getId()));
		params.put("nonce_str", PaySupport.getRandomStringByLength(32));
		Map<String, Object> go = PayFactory.createUnifiedOrder(PayFactory.WEIXIN).go(params);
		logger.info("weixin pay ret: >>> " + JsonMapper.toJsonString(go));
		if ("SUCCESS".equals(go.get("return_code")))
		{
			if ("SUCCESS".equals(go.get("result_code")))
			{
				result.put("payStatus", true);
				result.put("total_fee", scale.toString());
				result.put("prepay_id", go.get("prepay_id"));
				if("PC".equals(attach))
				{
					result.put("code_url", go.get("code_url"));
					JedisUtils.setObjectMap(purchaseRecord.getId().toString(), result, 20000);
				}
				if ("WX".equals(attach))
				{
					result.put("code_url", "");
					JedisUtils.setObjectMap(purchaseRecord.getBillNo().toString(), result, 20000);
				}
			}
		}
		
		
		
		return result;
	}

	/*
	 * (non-Javadoc) 支付宝支付完成校验
	 * @see com.hy.jyw.app.client.pay.service.PayApi#verifyAlipay(java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Map)
	 */
	@Override
	public boolean verifyAlipay(String out_trade_no, String trade_no, String trade_status, Map<String, String> sparams) throws Exception
	{
		try
		{
			String partner = AlipayConfig.partner;
			String publicKey = AlipayConfig.publicKey;
			boolean isSign = PaySupport.verify(sparams, partner, publicKey);
			if (isSign)
			{
				logger.debug(out_trade_no + ":支付成功 ");
			}
			else
			{
				logger.debug(out_trade_no + ":支付失败 签名验证失败");
			}
			return isSign;
		}
		catch (Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc) 微信支付结果实现
	 * @see com.hy.jyw.app.client.pay.service.PayApi#getWXPayResult(javax.servlet.http.HttpServletRequest)
	 */
	// @Override
	// public String getWXPayResult(String requestString) {
	// LOGGER.debug("开始接收支付结果通知（回调）...");
	// WeChartNotifyResponse responseData = new WeChartNotifyResponse();
	// UnifiedOrderNotifyRequestData payResult =
	// WeixinUtils.castXMLStringToUnifiedOrderNotifyRequestData(requestString);
	// LOGGER.debug("WeChartUnifiedOrder => " + JsonMapper.toJsonString(payResult));
	// responseData.setReturn_code("FAIL");
	// responseData.setReturn_code("invalid sign");
	// if (payResult.getReturn_code() != null && payResult.getResult_code() != null && payResult.getBank_type() != null
	// && payResult.getOut_trade_no() != null && payResult.getTotal_fee() != null
	// && payResult.getTransaction_id() != null) {
	// responseData.setReturn_code("SUCCESS");
	// responseData.setReturn_msg("OK");
	// try {
	// boolean checkOrderPayStatus = checkOrderPayStatus( payResult.getOut_trade_no());
	// if(!checkOrderPayStatus)
	// changPayInfoSuccess( payResult.getOut_trade_no(), payResult.getTransaction_id(),
	// "SUCCESS".equals(payResult.getReturn_code()), payResult.getBank_type());
	// } catch (Exception e) {
	// responseData.setReturn_code("FAIL");
	// responseData.setReturn_msg("important param is null");
	// }
	// } else {
	// responseData.setReturn_code("FAIL");
	// responseData.setReturn_msg("important param is null");
	// }
	// // 3、将要返回的数据转为XML字符串
	// XStream xStream = new XStream();
	// xStream.alias("xml", WeChartNotifyResponse.class);
	// String responseString = xStream.toXML(responseData);
	// return responseString = responseString.replaceAll("__", "_");
	// }

}
