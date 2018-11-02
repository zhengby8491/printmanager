/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.sms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.sms.impl.NullSmsPartnerService;
import com.huayin.printmanager.sms.impl.SmsWTServiceImpl;
import com.huayin.printmanager.sms.impl.SmsYPServiceImpl;

/**
 * <pre>
 * 短信平台 - 短信发送工厂
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class SmsSendFactory
{
	private static Map<Long, SmsGatewayService> gatewayMap = new HashMap<Long, SmsGatewayService>();

	private static final Log logger = LogFactory.getLog(SmsSendFactory.class);

	/**
	 * <pre>
	 * 通过网关配置信息获取处理器
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月26日 上午11:02:58, think
	 */
	public static SmsGatewayService getGateway(Long id)
	{
		if (gatewayMap.containsKey(id))
		{
			return gatewayMap.get(id);
		}
		else
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 通过网关配置信息获取处理器
	 * </pre>
	 * @param context 服务工厂上下文
	 * @param partner
	 * @return
	 * @since 1.0, 2017年10月26日 上午11:03:09, think
	 */
	public static SmsGatewayService getGateway(ApplicationContext context, SmsPartner partner)
	{
		if (partner == null)
		{
			return null;
		}
		if (gatewayMap.containsKey(partner.getId()))
		{
			return gatewayMap.get(partner.getId());
		}
		else
		{
			if (partner.getState() == SmsPartnerState.OPEN)
			{
				SmsGatewayService initGateway = initGateway(context, partner);
				synchronized (gatewayMap)
				{
					gatewayMap.put(partner.getId(), initGateway);
				}
				return initGateway;
			}
			else
			{
				return null;
			}
		}
	}

	/**
	 * <pre>
	 * 重置短信网关处理器
	 * </pre>
	 * @param partnerId
	 * @since 1.0, 2017年10月26日 上午11:03:23, think
	 */
	public static void clearGateway(Long partnerId)
	{
		logger.info("重置短信网关处理器{" + partnerId + "}...");
		if (gatewayMap.containsKey(partnerId))
		{
			SmsGatewayService processor = gatewayMap.get(partnerId);
			synchronized (gatewayMap)
			{
				gatewayMap.remove(partnerId);
			}
			try
			{
				processor.release();
			}
			catch (Throwable e)
			{
				logger.info("重置短信网关处理器{" + partnerId + "}释放资源异常...");
			}
		}
	}

	/**
	 * <pre>
	 * 初始化网关处理器
	 * </pre>
	 * @param context 服务工厂上下文
	 * @param partner
	 * @return
	 * @since 1.0, 2017年10月26日 上午11:03:34, think
	 */
	private static SmsGatewayService initGateway(ApplicationContext context, SmsPartner partner)
	{
		if (partner == null)
		{
			return new NullSmsPartnerService(context, partner);
		}
		logger.info("初始化短信网关{" + partner.getId() + "}...");
		SmsGatewayService processor = null;
		switch (partner.getSmsSendType())
		{

			case WUOTAO:
				processor = new SmsWTServiceImpl(context, partner);
				break;
			case YUNPIAN:
				processor = new SmsYPServiceImpl(context, partner);
				break;
			default:
				processor = new SmsWTServiceImpl(context, partner);
				break;
		}
		return processor;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午11:03:46, think
	 */
	public static Collection<SmsGatewayService> getAllProcessor()
	{
		synchronized (gatewayMap)
		{
			return new ArrayList<SmsGatewayService>(gatewayMap.values());
		}
	}

}
