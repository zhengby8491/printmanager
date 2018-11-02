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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.PermissionType;

/**
 * <pre>
 * 系统模块 - 菜单管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_menu")
public class Menu extends AbstractEntity
{
	private static final long serialVersionUID = -4969975632609308675L;

	@Id
	private Long id;

	/**
	 * 名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 类型
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(length = 10, nullable = false)
	private PermissionType type;

	/**
	 * 链接
	 */
	@Column(length = 255)
	private String url;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 页签标记
	 */
	// private String tab;
	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 是否点击立即刷新
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue refresh;

	/**
	 * 是否基础版本
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isBase;

	/**
	 * 权限标识符
	 */
	private String identifier;

	/**
	 * 父ID
	 */
	@Column(name = "parent_id")
	private Long parentId;

	@Transient
	private Menu parent;

	@Transient
	private List<Menu> childrens;

	@Override
	public boolean equals(Object obj)
	{
		return getId().equals(((Menu) obj).getId());
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PermissionType getType()
	{
		return type;
	}

	public void setType(PermissionType type)
	{
		this.type = type;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public List<Menu> getChildrens()
	{
		return childrens;
	}

	public void setChildrens(List<Menu> childrens)
	{
		this.childrens = childrens;
	}

	public Menu getParent()
	{
		return parent;
	}

	public void setParent(Menu parent)
	{
		this.parent = parent;
	}

	public BoolValue getRefresh()
	{
		return refresh;
	}

	public void setRefresh(BoolValue refresh)
	{
		this.refresh = refresh;
	}

	public BoolValue getIsBase()
	{
		return isBase;
	}

	public void setIsBase(BoolValue isBase)
	{
		this.isBase = isBase;
	}
}
