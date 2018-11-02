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

import org.springframework.context.ApplicationContext;

import com.huayin.printmanager.persist.entity.sys.SmsPartner;

/**
 * <pre>
 * 短信平台 - 抽象网关处理器
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public abstract class AbstractGatewayProcessor implements SmsGatewayService
{

	/**
	 * 系统参数名-短信接口地址
	 */
	public static final String SMSPORT_URL = "URL";

	/**
	 * 系统参数名-短信接口商户号
	 */
	public static final String SMSPORT_PARTNERID = "PARTNERID";

	/**
	 * 系统参数名-短信接口密码
	 */
	public static final String SMSPORT_PWD = "PWD";
	
	protected ApplicationContext context;

	protected SmsPartner partner;

	public AbstractGatewayProcessor(ApplicationContext context, SmsPartner partner)
	{
		this.context = context;
		this.partner = partner;
	}
	

	public void release()
	{
		
	}
}
