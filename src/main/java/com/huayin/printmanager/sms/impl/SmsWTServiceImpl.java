/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.sms.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.sms.AbstractGatewayProcessor;

/* @Service("smsShServiceImpl") */
/**
 * <pre>
 * 短信平台 - 海81666短信接口实现
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class SmsWTServiceImpl extends AbstractGatewayProcessor
{
	private final static Log logger = LogFactory.getLog(SmsWTServiceImpl.class);

	private static Map<String, String> resultCodeMap = new HashMap<String, String>();

	static
	{
		resultCodeMap.put("0", "发送成功");
		resultCodeMap.put("101", "无此用户");
		resultCodeMap.put("102", "密码错误");
		resultCodeMap.put("103", "提交过快（提交速度超过流速限制）");
		resultCodeMap.put("104", "系统忙（因平台侧原因，暂时无法处理提交的短信）");
		resultCodeMap.put("105", "敏感短信（短信内容包含敏感词）");
		resultCodeMap.put("106", "消息长度错（>536或<=0）");
		resultCodeMap.put("107", "包含错误的手机号码");
		resultCodeMap.put("108", "手机号码个数错（群发>50000或<=0;单发>200或<=0）");
		resultCodeMap.put("109", "无发送额度（该用户可用短信数已使用完）");
		resultCodeMap.put("110", "不在发送时间内");
		resultCodeMap.put("111", "超出该账户当月发送额度限制");
		resultCodeMap.put("112", "无此产品，用户没有订购该产品");
		resultCodeMap.put("113", "extno格式错（非数字或者长度不对）");
		resultCodeMap.put("115", "自动审核驳回");
		resultCodeMap.put("116", "签名不合法，未带签名（用户必须带签名的前提下）");
		resultCodeMap.put("117", "IP地址认证错,请求调用的IP地址不是系统登记的IP地址");
		resultCodeMap.put("118", "用户没有相应的发送权限");
		resultCodeMap.put("119", "用户已过期");
		resultCodeMap.put("120", "内容不在白名单模板中");
		resultCodeMap.put("121", "相同内容短信超限");
	}

	public SmsWTServiceImpl(ApplicationContext context, SmsPartner partner)
	{
		super(context, partner);
	}

	@Override
	public ServiceResult<SmsLog> sendMessage(SmsLog smsLog)
	{
		ServiceResult<SmsLog> result = new ServiceResult<SmsLog>();
		result.setReturnValue(smsLog);
		String smsporturl = partner.getExtConfigs().get(SMSPORT_URL);
		String account = partner.getExtConfigs().get(SMSPORT_PARTNERID);
		String pswd = partner.getExtConfigs().get(SMSPORT_PWD);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("account", account);
		parameters.put("pswd", pswd);
		parameters.put("mobile", smsLog.getMobile());
		parameters.put("msg", smsLog.getContent());
		parameters.put("needstatus", "false");
		try
		{
			String resultMsg = HttpRequestProxy.doGet(smsporturl, parameters, "UTF-8");
			if (resultMsg != null)
			{
				if (resultMsg.split(",")[1].equals("0"))
				{
					result.setIsSuccess(true);
					result.setMessage("发送成功");
				}
				else
				{
					result.setIsSuccess(false);
					logger.error("短信发送失败，SmsLog{id:" + smsLog.getId() + ",mobile=" + smsLog.getMobile() + "},返回信息{" + resultMsg.split(",")[1] + "}");
				}
			}
			else
			{
				result.setIsSuccess(false);
				logger.error("短信发送失败，SmsLog{id:" + smsLog.getId() + ",mobile=" + smsLog.getMobile() + "}");
			}
		}
		catch (Exception e)
		{
			result.setIsSuccess(false);
			logger.error("短信发送异常，SmsLog{id:" + smsLog.getId() + ",mobile=" + smsLog.getMobile() + "}," + e.getMessage());
		}
		return result;
	}

	public static void main(String[] args)
	{
		// String queryBalance="http://114.55.141.65/msg/QueryBalance?account=yinzhi&pswd=Qazx1234";
		// String
		// sendMsg="http://114.55.141.65/msg/HttpSendSM?account=yinzhi&pswd=Qazx1234&mobile=17198662395&msg=test&&needstatus=false";

		// System.out.println(HttpRequestProxy.doGet(queryBalance, "UTF-8"));
		// System.out.println(HttpRequestProxy.doGet(sendMsg, "UTF-8"));
		// if(true) return ;
		// http://192.168.168.168/msg/HttpSendSM?account=111111&pswd=123456&mobile=18900000000,13800138000&msg=test&needstatus=false
		String smsporturl = "http://114.55.141.65/msg/HttpSendSM";
		String account = "yinzhi";
		String pswd = "yinzhi#2016";
		String mobile = "17198662395";
		String msg = "【印管理】测试2";
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("account", account);
		parameters.put("pswd", pswd);
		parameters.put("mobile", mobile);
		parameters.put("msg", msg);
		parameters.put("needstatus", "false");
		String result = HttpRequestProxy.doGet(smsporturl, parameters, "UTF-8");
		if (result != null)
		{
			System.out.println(result);
			System.out.println(resultCodeMap.get(result.split(",")[1]));
		}
	}
}
