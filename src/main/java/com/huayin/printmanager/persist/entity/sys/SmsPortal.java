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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.huayin.common.persist.entity.AbstractTableIdEntity;
import com.huayin.printmanager.persist.enumerate.State;

/**
 * <pre>
 * 系统模块 - 短信供应商
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_smsportal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SmsPortal extends AbstractTableIdEntity
{
	private static final long serialVersionUID = 1933860213831072480L;

	/**
	 * 接入编号
	 */
	@Column(length = 30)
	private String accountId;

	/**
	 * 名称
	 */
	@Column(length = 20)
	private String name;

	/**
	 * 密钥
	 */
	@Column(length = 50)
	private String secretkey;

	/**
	 * 签名:【印管家】
	 */
	@Column(length = 30)
	private String sign;

	/**
	 * 发送优先级
	 */
	@Column(length = 10)
	private Integer priority;

	/**
	 * 通道ID
	 */
	@Column(length = 40)
	private Long partnerId;

	/**
	 * 状态
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private State state;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建用户
	 */
	private String createName;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 更新用户
	 */
	private String updateName;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getSecretkey()
	{
		return secretkey;
	}

	public void setSecretkey(String secretkey)
	{
		this.secretkey = secretkey;
	}

	public Long getPartnerId()
	{
		return partnerId;
	}

	public void setPartnerId(Long partnerId)
	{
		this.partnerId = partnerId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

}
