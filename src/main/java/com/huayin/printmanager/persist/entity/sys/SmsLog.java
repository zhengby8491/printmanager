/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseUserTableIdEntity;
import com.huayin.printmanager.persist.enumerate.SmsLogState;
import com.huayin.printmanager.persist.enumerate.SmsSendState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;
import com.huayin.printmanager.persist.enumerate.SmsType;

/**
 * <pre>
 * 系统模块 - 短信日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_smslog")
public class SmsLog extends BaseUserTableIdEntity
{

	private static final long serialVersionUID = -5373578539418642184L;

	/**
	 * 接入商accountId
	 */
	private String smsPortalAccountId;

	/**
	 * 内容
	 */
	@Column(length = 1000)
	private String content;

	/**
	 * 失败原因
	 */
	@Column(length = 100)
	private String message;

	/**
	 * 手机号码
	 */
	@Column(length = 20)
	private String mobile;

	/**
	 * 通道ID
	 */
	@Column(length = 40)
	private Long partnerId;

	/**
	 * 重发次数
	 */
	private Integer retryCount;

	/**
	 * 定时发送时间
	 */
	private Date scheduledTime;// 定时发送时间

	/**
	 * 发送优先级(数值越大，优先级越高)
	 */
	@Column(length = 10)
	private Integer sendPriority;

	/**
	 * 发送类型
	 */
	@Column(length = 30)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsSendState sendState;

	/**
	 * 发送时间
	 */
	private Date sendTime;

	/**
	 * 最迟发送时间
	 */
	private Date expireTime;

	/**
	 * 网关类型
	 */
	@Column(length = 30)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsSendType smsSendType;

	/**
	 * 状态
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsLogState state;

	/**
	 * 成功时间
	 */
	private Date successTime;

	/**
	 * 内容类型
	 */
	@Column(length = 30)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsType type;

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public Long getPartnerId()
	{
		return partnerId;
	}

	public void setPartnerId(Long partnerId)
	{
		this.partnerId = partnerId;
	}

	public Integer getRetryCount()
	{
		return retryCount;
	}

	public void setRetryCount(Integer retryCount)
	{
		this.retryCount = retryCount;
	}

	public Date getScheduledTime()
	{
		return scheduledTime;
	}

	public void setScheduledTime(Date scheduledTime)
	{
		this.scheduledTime = scheduledTime;
	}

	public Integer getSendPriority()
	{
		return sendPriority;
	}

	public void setSendPriority(Integer sendPriority)
	{
		this.sendPriority = sendPriority;
	}

	public SmsSendState getSendState()
	{
		return sendState;
	}

	public void setSendState(SmsSendState sendState)
	{
		this.sendState = sendState;
	}

	public Date getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public Date getExpireTime()
	{
		return expireTime;
	}

	public void setExpireTime(Date expireTime)
	{
		this.expireTime = expireTime;
	}

	public SmsSendType getSmsSendType()
	{
		return smsSendType;
	}

	public void setSmsSendType(SmsSendType smsSendType)
	{
		this.smsSendType = smsSendType;
	}

	public SmsLogState getState()
	{
		return state;
	}

	public void setState(SmsLogState state)
	{
		this.state = state;
	}

	public Date getSuccessTime()
	{
		return successTime;
	}

	public void setSuccessTime(Date successTime)
	{
		this.successTime = successTime;
	}

	public SmsType getType()
	{
		return type;
	}

	public void setType(SmsType type)
	{
		this.type = type;
	}

	public String getSmsPortalAccountId()
	{
		return smsPortalAccountId;
	}

	public void setSmsPortalAccountId(String smsPortalAccountId)
	{
		this.smsPortalAccountId = smsPortalAccountId;
	}
}