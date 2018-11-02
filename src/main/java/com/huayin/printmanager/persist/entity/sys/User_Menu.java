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
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 系统模块 - 用户菜单
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_user_menu")
public class User_Menu extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@Column(nullable = false)
	private Long userId;

	/**
	 * 菜单ID
	 */
	@Column(nullable = false)
	private Long menuId;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getMenuId()
	{
		return menuId;
	}

	public void setMenuId(Long menuId)
	{
		this.menuId = menuId;
	}

}
