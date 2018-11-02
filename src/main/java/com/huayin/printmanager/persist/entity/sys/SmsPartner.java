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
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.enumerate.SmsPartnerState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;

/**
 * 短信合作商信息表
 * @author zhaojitao
 * @version 2016-1-19
 */
/**
 * <pre>
 * 系统模块 - 短信渠道
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_smspartner")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsPartner extends AbstractEntity
{

	private static final long serialVersionUID = 1933860213831072480L;

	/**
	 * 合作商创建时间
	 */
	private Date createTime;

	/**
	 * 编号
	 */
	@Id
	@Column(length = 20)
	private Long id;

	/**
	 * 密钥
	 */
	@Column(length = 50)
	private String secretKey;

	/**
	 * 名称
	 */
	@Column(length = 20)
	private String name;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 分配的商户编号
	 */
	@Column(length = 20)
	private String partnerId;

	/**
	 * 短信商户配置信息
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "map_key")
	@Column(name = "map_value")
	private Map<String, String> extConfigs;

	/**
	 * 发送优先级
	 */
	@Column(length = 10)
	private Integer priority;

	/**
	 * 短信终端类型
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsSendType smsSendType;

	/**
	 * 合作商状态
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SmsPartnerState state;

	public Date getCreateTime()
	{
		return createTime;
	}

	public String getSecretKey()
	{
		return secretKey;
	}

	public String getName()
	{
		return name;
	}

	public String getPartnerId()
	{
		return partnerId;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public SmsSendType getSmsSendType()
	{
		return smsSendType;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPartnerId(String partnerId)
	{
		this.partnerId = partnerId;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public void setSmsSendType(SmsSendType smsSendType)
	{
		this.smsSendType = smsSendType;
	}

	public Map<String, String> getExtConfigs()
	{
		return extConfigs;
	}

	public void setExtConfigs(Map<String, String> extConfigs)
	{
		this.extConfigs = extConfigs;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public SmsPartnerState getState()
	{
		return state;
	}

	public void setState(SmsPartnerState state)
	{
		this.state = state;
	}
}
