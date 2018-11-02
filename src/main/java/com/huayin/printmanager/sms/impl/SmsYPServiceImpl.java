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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.sms.AbstractGatewayProcessor;
import com.huayin.printmanager.sms.SmsGatewayService;

/*@Service
@Lazy*/
/**
 * <pre>
 * 短信平台 - 云片网络短信接口
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class SmsYPServiceImpl extends AbstractGatewayProcessor
{
	private final static Log logger = LogFactory.getLog(SmsYPServiceImpl.class);

	public SmsYPServiceImpl(ApplicationContext context, SmsPartner partner)
	{
		super(context, partner);
	}

	public ServiceFactory serviceFactory = null;

	/**
	 * 模板方式发送
	 */
	public ServiceResult<SmsLog> sendMessageTpl(SmsLog smsLog)
	{
		ServiceResult<SmsLog> result = new ServiceResult<SmsLog>();
		result.setReturnValue(smsLog);
		if (smsLog.getContent() == null || !smsLog.getContent().contains("\"#company#=") || !smsLog.getContent().contains("\"#code#="))
		{
			smsLog.setMessage("短信内容参数不足");
			result.setMessage("短信内容参数不足");
			result.setIsSuccess(false);
			return result;
		}
		String content = smsLog.getContent();
		String tpl_value = "";
		String[] tpl_value_arr = content.split("\"");
		for (int i = 0; i < tpl_value_arr.length; i++)
		{
			if (i % 2 != 0)
			{
				tpl_value += tpl_value_arr[i];
				if (i < tpl_value_arr.length - 2)
				{
					tpl_value += "&";
				}
			}
		}
		String smsporturl = partner.getExtConfigs().get(SMSPORT_URL);
		String apikey = partner.getExtConfigs().get(SMSPORT_PWD);
		String tpl_id = partner.getExtConfigs().get(SMSPORT_PARTNERID);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("apikey", apikey);
		parameters.put("mobile", smsLog.getMobile());
		parameters.put("tpl_id", tpl_id);
		parameters.put("tpl_value", tpl_value);
		String doPost = HttpRequestProxy.doPost(smsporturl, parameters, "UTF-8");
		Map<String, Object> resultMap = new Gson().fromJson(doPost, new TypeToken<Map<String, Object>>()
		{
		}.getType());
		// 返回信息
		smsLog.setMessage(doPost);
		result.setMessage(doPost);
		Double code = (Double) resultMap.get("code");
		if (code.intValue() == 0)
		{
			logger.info("返回JSON:" + doPost);
			result.setIsSuccess(true);
		}
		else
		{
			logger.error("Remote service invoke error[" + doPost + "]");
			result.setIsSuccess(false);
		}
		return result;
	}

	/**
	 * 内容匹配方式发送
	 */
	@Override
	public ServiceResult<SmsLog> sendMessage(SmsLog smsLog)
	{
		ServiceResult<SmsLog> result = new ServiceResult<SmsLog>();
		result.setReturnValue(smsLog);
		String smsporturl = partner.getExtConfigs().get(SMSPORT_URL);
		String apikey = partner.getExtConfigs().get(SMSPORT_PWD);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("apikey", apikey);
		parameters.put("mobile", smsLog.getMobile());
		parameters.put("text", smsLog.getContent());
		String doPost = HttpRequestProxy.doPost(smsporturl, parameters, "UTF-8");
		Map<String, Object> resultMap = new Gson().fromJson(doPost, new TypeToken<Map<String, Object>>()
		{
		}.getType());
		// 返回信息
		smsLog.setMessage(doPost);
		result.setMessage(doPost);
		Double code = (Double) resultMap.get("code");
		if (code.intValue() == 0)
		{
			logger.info("返回JSON:" + doPost);
			result.setIsSuccess(true);
		}
		else
		{
			logger.error("Remote service invoke error[" + doPost + "]");
			result.setIsSuccess(false);
		}
		return result;
	}

	public static void main2(String[] args)
	{
		SmsGatewayService service = (SmsGatewayService) new SmsYPServiceImpl(null, null);

		SmsLog smsLog = new SmsLog();
		smsLog.setContent("短信测试");
		smsLog.setMobile("13417429607");
		ServiceResult<SmsLog> result = service.sendMessage(smsLog);
		System.out.println(result.getIsSuccess());
	}

	@Override
	public void release()
	{
	}
}
