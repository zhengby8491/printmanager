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

import org.springframework.context.ApplicationContext;

import com.huayin.common.exception.ServiceResult;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.sms.AbstractGatewayProcessor;

/*@Service("nullSmsServiceImpl")
@Lazy*/
/**
 * <pre>
 * 短信平台 - 无操作的短信网关
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class NullSmsPartnerService extends AbstractGatewayProcessor
{
	/* （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsGatewayService#sendMessage(com.huayin.printmanager.persist.entity.sys.SmsLog)
	 */
	@Override
	public ServiceResult<SmsLog> sendMessage(SmsLog smsLog)
	{
		ServiceResult<SmsLog> result = new ServiceResult<SmsLog>();
		result.setReturnValue(smsLog);
		result.setIsSuccess(false);
		return result;
	}

	public NullSmsPartnerService(ApplicationContext context, SmsPartner partner)
	{
		super(context, partner);
	}

	/* （非 Javadoc）
	 * @see com.huayin.printmanager.sms.AbstractGatewayProcessor#release()
	 */
	@Override
	public void release()
	{
	}
}
