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

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.common.persist.entity.AbstractTableIdEntity;
import com.huayin.printmanager.persist.enumerate.AgentType;

/**
 * <pre>
 * 系统模块 - 代理商管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_agent_quotient")
public class AgentQuotient extends AbstractTableIdEntity
{
	private static final long serialVersionUID = -2397735988036657802L;

	// 名称，联系人，联系电话，地址，区域，LOGO，代理商属性（代理，广告，综合）
	/**
	 * 名称
	 */
	private String name;

	/**
	 * 联系人
	 */
	private String linkName;

	/**
	 * 联系电话
	 */
	private String telNum;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 区域
	 */
	private String area;

	/**
	 * 代理商属性
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private AgentType agentType;

	/**
	 * 图片
	 */
	private String photoUrl;

	/**
	 * 创建人
	 */
	private String createName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPhotoUrl()
	{
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl)
	{
		this.photoUrl = photoUrl;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getTelNum()
	{
		return telNum;
	}

	public void setTelNum(String telNum)
	{
		this.telNum = telNum;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public AgentType getAgentType()
	{
		return agentType;
	}

	public String getAgentTypeText()
	{
		if (agentType != null)
		{
			return agentType.getText();
		}
		return null;
	}

	public void setAgentType(AgentType agentType)
	{
		this.agentType = agentType;
	}
}