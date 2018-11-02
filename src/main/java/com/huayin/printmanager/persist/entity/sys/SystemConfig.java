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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.CacheType;

/**
 * <pre>
 * 系统模块 - 系统参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = CacheType.SECOND_CACHE_DEFAULT)
public class SystemConfig extends AbstractEntity
{
	private static final long serialVersionUID = -2397735988036657802L;

	/**
	 * 主键
	 */
	@Id
	@Column(length = 50)
	private String id;

	/**
	 * 值
	 */
	@Column(length = 400)
	private String value;

	/**
	 * 描述
	 */
	private String description;

	public String getValue()
	{
		return value;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public Object getId()
	{
		return id;
	}

	public String getDescription()
	{
		return description;
	}

}