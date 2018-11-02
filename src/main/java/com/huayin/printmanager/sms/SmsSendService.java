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

import java.util.Date;
import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsType;

/**
 * <pre>
 * 短信平台 - 短信发送
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public interface SmsSendService
{
	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param size
	 * @param partnerId
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:54:48, think
	 */
	public List<SmsLog> lockSmsLogList(int size, Long partnerId);

	/**
	 * <pre>
	 * 从数据库批量读取短信发送
	 * </pre>
	 * @since 1.0, 2017年10月26日 上午10:55:04, think
	 */
	public void sendJobBatch();

	/**
	 * <pre>
	 * 通过构造现有的或者新建的短信记录立即发送
	 * 注意:大部分情况下应该调用其他接口来发送.
	 * </pre>
	 * @param smsLog
	 * @param partner
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:55:10, think
	 */
	public boolean sendNow(SmsLog smsLog, SmsPartner partner);

	/**
	 * <pre>
	 * 通过构造现有的或者新建的短信记录立即发送
	 * 注意:大部分情况下应该调用其他接口来发送.
	 * </pre>
	 * @param smsLog
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:55:23, think
	 */
	public boolean sendNow(SmsLog smsLog);

	/**
	 * <pre>
	 * 通过短信记录编号重新发送短信
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:55:30, think
	 */
	public ServiceResult<Boolean> resend(Long[] ids);

	/**
	 * <pre>
	 * 发送短信
	 * </pre>
	 * @param mobile 手机号码
	 * @param content 短信内容
	 * @param partnerId 短信渠道
	 * @param type 短信类型,决定了优先级
	 * @param scheduleTime 定时发送时间,为空则立即发送
	 * @since 1.0, 2017年10月26日 上午10:55:37, think
	 */
	public void send(String mobile, String content, Long partnerId, SmsType type, Date scheduleTime);

	/**
	 * <pre>
	 * 立即发送短信
	 * </pre>
	 * @param smsPortalAccountId 接入商编号
	 * @param mobile 手机号码
	 * @param content 短信内容
	 * @param partnerId 短信渠道
	 * @param type 短信类型,决定了优先级
	 * @since 1.0, 2017年10月26日 上午10:55:53, think
	 */
	public void sendNow(String smsPortalAccountId, String mobile, String content, Long partnerId, SmsType type);

	/**
	 * <pre>
	 * 立即发送短信
	 * </pre>
	 * @param mobile 手机号码
	 * @param content 短信内容
	 * @param partnerId 短信渠道
	 * @param type 短信类型,决定了优先级
	 * @since 1.0, 2017年10月26日 上午10:56:09, think
	 */
	public void sendNow(String mobile, String content, Long partnerId, SmsType type);

	/**
	 * <pre>
	 * 立即发送短信
	 * </pre>
	 * @param mobile 手机号码
	 * @param content 短信内容
	 * @param type 短信类型,决定了优先级
	 * @since 1.0, 2017年10月26日 上午10:56:22, think
	 */
	public void sendNow(String mobile, String content, SmsType type);

	/**
	 * <pre>
	 * 立即发送短信
	 * </pre>
	 * @param mobile 手机号码
	 * @param content 短信内容
	 * @since 1.0, 2017年10月26日 上午10:56:32, think
	 */
	public void sendNow(final String mobile, final String content);

	/**
	 * <pre>
	 * 线程发送告警短信
	 * </pre>
	 * @param content
	 * @since 1.0, 2017年10月26日 上午10:56:43, think
	 */
	public void sendSystemAlarm(final String content);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @since 1.0, 2017年10月26日 上午10:56:49, think
	 */
	public void release();
}
