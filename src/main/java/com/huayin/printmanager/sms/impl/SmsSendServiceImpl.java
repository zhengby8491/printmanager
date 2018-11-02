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

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.constants.QueryConstants.LockType;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.common.util.StringHelper;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.entity.sys.SmsPartner;
import com.huayin.printmanager.persist.enumerate.SmsLogState;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.persist.enumerate.SmsSendState;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.sms.SmsSendFactory;
import com.huayin.printmanager.sms.SmsSendService;

/**
 * <pre>
 * 短信平台 - 短信发送
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
@Service
@Lazy
public class SmsSendServiceImpl extends BaseServiceImpl implements SmsSendService
{
	protected static final ExecutorService threadPool = Executors.newCachedThreadPool();

	// 到期时间，首次发送时间延后1天
	private static int expireTime = 1;

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#lockSmsLogList(int, java.lang.Long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<SmsLog> lockSmsLogList(int size, Long partnerId)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.eq("partnerId", partnerId);
		query.eq("state", SmsLogState.WAITING);
		query.add(Restrictions.or(Restrictions.eq("sendState", SmsSendState.IMMEDIATELY), Restrictions.and(Restrictions.eq("sendState", SmsSendState.SCHEDULED), Restrictions.le("scheduledTime", new Date()))));
		query.asc("retryCount");
		query.desc("sendPriority");
		query.setPageSize(size);
		List<SmsLog> result = serviceFactory.getPersistService().lockByDynamicQuery(query, SmsLog.class, LockType.LOCK_PASS);
		for (SmsLog log : result)
		{
			log.setState(SmsLogState.SENDING);
		}
		serviceFactory.getDaoFactory().getCommonDao().updateAllEntity(result);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#release()
	 */
	@Override
	@PreDestroy
	public void release()
	{
		if (threadPool != null)
		{
			if (!threadPool.isShutdown())
			{
				threadPool.shutdown();
			}
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#resend(java.lang.Long[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResult<Boolean> resend(Long[] ids)
	{
		DynamicQuery query = new DynamicQuery(SmsLog.class);
		query.inArray("id", ids);
		List<SmsLog> list = serviceFactory.getPersistService().findByDynamicQuery(SmsLog.class, query);
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		boolean flag = false;
		try
		{
			StringBuffer sbuffer = new StringBuffer(" 其中处理失败的手机号：");
			for (SmsLog smsLog : list)
			{
				// 调用快速通道
				// smsLog.setRetryCount(smsLog.getRetryCount() + 1);
				smsLog.setExpireTime(DateTimeUtil.addDate(new Date(), expireTime));
				boolean sign = serviceFactory.getSmsSendService().sendNow(smsLog);
				if (!sign)
				{
					flag = true;
					sbuffer.append(smsLog.getMobile());
					sbuffer.append(",");
				}
			}
			if (flag)
			{
				result.setReturnObject(sbuffer.toString().replaceAll("/,$/", ""));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendJobBatch()
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void sendJobBatch()
	{
		// 分短信渠道进行批量发.
		DynamicQuery query = new DynamicQuery(SmsPartner.class);
		query.eq("state", SmsPartnerState.OPEN);
		List<SmsPartner> result = serviceFactory.getPersistService().findByDynamicQuery(SmsPartner.class, query);
		for (final SmsPartner partner : result)
		{
			threadPool.execute(new Runnable()
			{
				@Override
				public void run()
				{
					List<SmsLog> result = serviceFactory.getSmsSendService().lockSmsLogList(100, partner.getId());
					if (result.size() > 0)
					{
						for (final SmsLog smsLog : result)
						{
							threadPool.execute(new Runnable()
							{
								public void run()
								{
									try
									{
										serviceFactory.getSmsSendService().sendNow(smsLog, partner);
									}
									catch (Throwable ex)
									{
										logger.error("发送短信异常{" + smsLog.getId() + "}", ex);
										smsLog.setState(SmsLogState.WAITING);
										smsLog.setMessage("发送短信时未知异常");
										serviceFactory.getPersistService().update(smsLog);
									}
								}
							});
						}
					}
				}
			});
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(com.huayin.printmanager.persist.entity.sys.SmsLog)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean sendNow(SmsLog smsLog)
	{
		boolean sendResult = false;
		if (smsLog.getPartnerId() == null)
		{
			smsLog.setPartnerId(Long.parseLong(SystemConfigUtil.getConfig(SysConstants.SMS_PARTNER_DEFAULT)));
		}
		SmsPartner partner = serviceFactory.getDaoFactory().getCommonDao().getEntity(SmsPartner.class, smsLog.getPartnerId());
		if (partner != null)
		{
			return serviceFactory.getSmsSendService().sendNow(smsLog, partner);
		}
		else
		{
			smsLog.setState(SmsLogState.FAILURE);
			smsLog.setMessage("短信通道不存在或者非开启状态");
		}

		return sendResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(com.huayin.printmanager.persist.entity.sys.SmsLog,
	 * com.huayin.printmanager.persist.entity.sys.SmsPartner)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean sendNow(SmsLog smsLog, SmsPartner partner)
	{
		boolean sendResult = false;
		smsLog.setSendTime(new Date());
		if (smsLog.getExpireTime() == null)
		{
			smsLog.setExpireTime(DateTimeUtil.addDate(new Date(), expireTime));
		}
		if (smsLog.getExpireTime().before(new Date()))
		{
			smsLog.setState(SmsLogState.FAILURE);
		}
		else
		{
			try
			{
				if (partner != null && partner.getState() == SmsPartnerState.OPEN)
				{
					smsLog.setSmsSendType(partner.getSmsSendType());
					ServiceResult<SmsLog> result = SmsSendFactory.getGateway(partner.getId()).sendMessage(smsLog);
					if (result != null)
					{
						if (result.getIsSuccess())
						{
							smsLog.setPartnerId(partner.getId());
							smsLog.setState(SmsLogState.SENDED);
							smsLog.setSuccessTime(new Date());
							smsLog.setMessage(result.getMessage());
							sendResult = true;
						}
						else
						{
							// 明确告诉失败的
							smsLog.setState(SmsLogState.WAITING);
							smsLog.setMessage(result.getMessage());
						}
					}
					else
					{
						smsLog.setState(SmsLogState.WAITING);
						smsLog.setMessage("短信通道调用错误");
					}
				}
				else
				{
					smsLog.setState(SmsLogState.FAILURE);
					smsLog.setMessage("短信通道不存在或者非开启状态");
				}
			}
			catch (Throwable ex)
			{
				logger.error("短信发送异常，短信编号{" + smsLog.getId() + "}", ex);
				smsLog.setState(SmsLogState.WAITING);
				smsLog.setMessage("短信发送异常");
			}
			// 增加重发次数
			if (smsLog.getRetryCount() != null)
			{
				smsLog.setRetryCount(smsLog.getRetryCount() + 1);
				if (smsLog.getRetryCount() >= 10)
				{// 最大发送10次
					smsLog.setState(SmsLogState.FAILURE);
				}
			}
			else
			{
				smsLog.setRetryCount(1);
			}
		}

		if (smsLog.getId() != null)
		{
			serviceFactory.getDaoFactory().getCommonDao().updateEntity(smsLog);
		}
		else
		{
			serviceFactory.getDaoFactory().getCommonDao().saveEntity(smsLog);
		}
		return sendResult;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendSystemAlarm(java.lang.String)
	 */
	@Override
	public void sendSystemAlarm(final String content)
	{
		threadPool.execute(new Runnable()
		{
			public void run()
			{
				// 短信报警
				String mobiles = SystemConfigUtil.getConfig(SysConstants.MONITOR_MOBILE);
				if (!StringHelper.isEmpty(mobiles))
				{
					String[] mobilesArray = mobiles.split("\\;");
					if (mobilesArray != null && mobilesArray.length > 0)
					{
						for (String mobile : mobilesArray)
						{
							try
							{
								String[] split = mobile.split(",");
								mobile = split[0];
								serviceFactory.getSmsSendService().sendNow(mobile, content, Long.parseLong(split[1]), SmsType.WARN);
							}
							catch (Throwable e)
							{
								logger.error("告警手机系统参数{MESSAGE_MONITOR_MOBILE}的正确配置方式应该是:" + "手机号码1,通道编号1;手机号码2,通道编号2;...");
							}
						}
					}
				}
			}
		});
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Long, com.huayin.printmanager.persist.enumerate.SmsType)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNow(final String smsPortalAccountId, final String mobile, final String content, Long partnerId, final SmsType type)
	{

		if (StringUtils.isBlank(mobile))
		{
			logger.error("手机号码不能为空");
			return;
		}
		if (StringUtils.isBlank(content))
		{
			logger.error("短信内容不能为空");
			return;
		}
		if (partnerId == null || partnerId == 0l)
		{
			partnerId = Long.parseLong(SystemConfigUtil.getConfig(SysConstants.SMS_PARTNER_DEFAULT));
		}
		final Long _partnerId = partnerId;
		threadPool.execute(new Runnable()
		{
			@Override
			public void run()
			{
				SmsLog smsLog = new SmsLog();
				smsLog.setSmsPortalAccountId(smsPortalAccountId);
				smsLog.setRetryCount(0);
				smsLog.setExpireTime(DateTimeUtil.addDate(new Date(), expireTime));
				smsLog.setContent(content);
				smsLog.setMobile(mobile);
				smsLog.setSendPriority(type.getValue());
				smsLog.setSendState(SmsSendState.IMMEDIATELY);
				smsLog.setState(SmsLogState.SENDING);
				smsLog.setPartnerId(_partnerId);
				smsLog.setType(type);
				serviceFactory.getSmsSendService().sendNow(smsLog);
			}
		});

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(java.lang.String, java.lang.String, java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.SmsType)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNow(final String mobile, final String content, Long partnerId, final SmsType type)
	{
		serviceFactory.getSmsSendService().sendNow(null, mobile, content, partnerId, type);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(java.lang.String, java.lang.String,
	 * com.huayin.printmanager.persist.enumerate.SmsType)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNow(final String mobile, final String content, final SmsType type)
	{
		serviceFactory.getSmsSendService().sendNow(mobile, content, null, type);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#sendNow(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendNow(final String mobile, final String content)
	{
		serviceFactory.getSmsSendService().sendNow(mobile, content, null, SmsType.COMMON);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.sms.SmsSendService#send(java.lang.String, java.lang.String, java.lang.Long,
	 * com.huayin.printmanager.persist.enumerate.SmsType, java.util.Date)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void send(final String mobile, final String content, final Long partnerId, final SmsType type, final Date scheduleTime)
	{
		if (mobile == null || mobile.trim().equals(""))
		{
			logger.error("手机号码不能为空");
			return;
		}
		if (content == null || content.trim().equals(""))
		{
			logger.error("短信内容不能为空");
			return;
		}
		threadPool.execute(new Runnable()
		{
			@Override
			public void run()
			{
				SmsPartner partner = serviceFactory.getDaoFactory().getCommonDao().getEntity(SmsPartner.class, partnerId);
				if (partner != null && partner.getState() == SmsPartnerState.OPEN)
				{
					SmsLog smsLog = new SmsLog();
					smsLog.setRetryCount(0);
					smsLog.setContent(content);
					smsLog.setMobile(mobile);
					smsLog.setSendPriority(type.getValue());
					if (scheduleTime != null)
					{
						smsLog.setScheduledTime(scheduleTime);
						smsLog.setExpireTime(DateTimeUtil.addDate(scheduleTime, expireTime));
						smsLog.setSendState(SmsSendState.SCHEDULED);
					}
					else
					{
						smsLog.setExpireTime(DateTimeUtil.addDate(new Date(), expireTime));
						smsLog.setSendState(SmsSendState.IMMEDIATELY);
					}
					smsLog.setState(SmsLogState.WAITING);
					smsLog.setPartnerId(partner.getId());
					smsLog.setSmsSendType(partner.getSmsSendType());
					serviceFactory.getPersistService().save(smsLog);
				}
				else
				{
					logger.error("短信通道不存在或非开启状态");
				}
			}
		});
	}
}