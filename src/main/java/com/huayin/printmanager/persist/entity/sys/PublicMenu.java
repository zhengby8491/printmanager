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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.huayin.common.persist.entity.AbstractEntity;

/**
 * <pre>
 * 系统模块 - 所有公司公共菜单
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_publicmenu")
public class PublicMenu extends AbstractEntity
{
	private static final long serialVersionUID = -4969975632609308675L;

	@Id
	private Long id;
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


}
