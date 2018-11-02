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

import com.huayin.common.persist.entity.AbstractTableIdEntity;

/**
 * <pre>
 * 系统模块 - 产品对应的菜单权限
 * TODO 需要重命名
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_product_menu")
public class Product_Menu extends AbstractTableIdEntity
{

	private static final long serialVersionUID = -8207888394880450583L;
	
	/**
	 * 菜单id
	 */
	@Column(nullable = false)
	private Long menuId;
	
	/**
	 * 购买产品id
	 */
	private Long productId;

	public Long getMenuId()
	{
		return menuId;
	}

	public void setMenuId(Long menuId)
	{
		this.menuId = menuId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}
}
