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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.exception.ServiceResult;
import com.huayin.printmanager.persist.entity.sys.SmsLog;

/**
 * <pre>
 * 短信平台 - 短信网关
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public interface SmsGatewayService
{
	public static final Log logger = LogFactory.getLog(SmsGatewayService.class);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param smsLog
	 * @return
	 * @since 1.0, 2017年10月26日 上午11:04:22, think
	 */
	public ServiceResult<SmsLog> sendMessage(SmsLog smsLog);

	/**
	 * <pre>
	 * 释放短信网关相关的资源
	 * </pre>
	 * @since 1.0, 2017年10月26日 上午11:04:24, think
	 */
	public void release();

}
