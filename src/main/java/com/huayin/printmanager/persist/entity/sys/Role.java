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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.huayin.printmanager.persist.CacheType;
import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 系统模块 - 角色管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = CacheType.SECOND_CACHE_DEFAULT)
public class Role extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1375284442850617084L;

	/**
	 * 角色名称
	 */
	@Column(length = 30)
	private String name;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	@Transient
	private List<Long> menuIdList = new ArrayList<Long>();

	@Override
	public boolean equals(Object obj)
	{
		return getId().equals(((Role) obj).getId());
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
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

	public List<Long> getMenuIdList()
	{
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList)
	{
		this.menuIdList = menuIdList;
	}

}