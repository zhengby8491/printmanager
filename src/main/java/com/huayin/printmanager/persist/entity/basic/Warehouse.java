/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.WarehouseType;

/**
 * <pre>
 * 基础设置 - 仓库信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_warehouse")
public class Warehouse extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 仓库类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private WarehouseType warehouseType;

	/**
	 * 是否不良(默认：否)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isBad = BoolValue.NO;

	/**
	 * 是否期初(默认：否)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isBegin = BoolValue.NO;

	public String getName()
	{
		return name;
	}

	public WarehouseType getWarehouseType()
	{
		return warehouseType;
	}

	public void setWarehouseType(WarehouseType warehouseType)
	{
		this.warehouseType = warehouseType;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BoolValue getIsBad()
	{
		return isBad;
	}

	public void setIsBad(BoolValue isBad)
	{
		this.isBad = isBad;
	}

	public BoolValue getIsBegin()
	{
		return isBegin;
	}

	public void setIsBegin(BoolValue isBegin)
	{
		this.isBegin = isBegin;
	}

	public String getWarehouseTypeText()
	{
		if (warehouseType != null)
		{
			return warehouseType.getText();
		}
		return "-";
	}

	public String getIsBadText()
	{
		if (isBad != null)
		{
			return isBad.getText();
		}
		return "-";
	}

	public String getIsBeginText()
	{
		if (isBegin != null)
		{
			return isBegin.getText();
		}
		return "-";
	}
}
