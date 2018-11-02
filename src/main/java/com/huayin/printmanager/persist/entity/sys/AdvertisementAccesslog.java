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
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 系统模块 - 广告访问记录
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_advertisement_accesslog")
public class AdvertisementAccesslog extends BaseTableIdEntity
{
	private static final long serialVersionUID = -2397735988036657802L;

	/**
	 * 广告Id
	 */
	private Long advertisementId;

	/**
	 * IP
	 */
	private String ip;

	/**
	 * 访问归属地
	 */
	private String address;

	/**
	 * 内IP
	 */
	private String iip;

	/**
	 * 访问用户Id
	 */
	private Long userId;

	/**
	 * 访问用户名
	 */
	private String userName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public Long getAdvertisementId()
	{
		return advertisementId;
	}

	public void setAdvertisementId(Long advertisementId)
	{
		this.advertisementId = advertisementId;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getIip()
	{
		return iip;
	}

	public void setIip(String iip)
	{
		this.iip = iip;
	}

}