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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.common.persist.entity.AbstractTableIdEntity;

/**
 * <pre>
 * 系统模块 - 购买信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_buy")
public class Buy extends AbstractTableIdEntity
{

	private static final long serialVersionUID = -1072628977007867594L;

	/**
	 * 产品名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 销售价格
	 */
	private BigDecimal price;

	/**
	 * 原价
	 */
	private BigDecimal originalPrice;

	/**
	 * 购买类型 1 购买  2升级
	 */
	private Integer type = 1;

	/**
	 * 版本類型
	 */
	private Integer versionType;

	/**
	 * 奖金
	 */
	private Integer bonus;

	/**
	 * 排序
	 */
	private Integer sort;

	@Transient
	private List<Long> menuIdList = new ArrayList<Long>();

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public BigDecimal getOriginalPrice()
	{
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice)
	{
		this.originalPrice = originalPrice;
	}

	public List<Long> getMenuIdList()
	{
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList)
	{
		this.menuIdList = menuIdList;
	}

	public Integer getBonus()
	{
		return bonus;
	}

	public void setBonus(Integer bonus)
	{
		this.bonus = bonus;
	}

	public Integer getVersionType()
	{
		return versionType;
	}

	public void setVersionType(Integer versionType)
	{
		this.versionType = versionType;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

}