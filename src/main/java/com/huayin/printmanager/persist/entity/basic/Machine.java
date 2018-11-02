/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.MachineType;

/**
 * <pre>
 * 基础设置 - 机台信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_machine")
public class Machine extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 机台名称
	 */
	private String name;

	/**
	 * 规格型号
	 */
	private String code;

	/**
	 * 生产厂商
	 */
	private String manufacturer;

	/**
	 * 机台属性
	 */
	@Enumerated(EnumType.STRING)
	private MachineType machineType;

	/**
	 * 设备金额
	 */
	private BigDecimal money;

	/**
	 * 标准产能
	 */
	private Integer capacity;

	/**
	 * 最大上机规格
	 */
	private String maxStyle;

	/**
	 * 最小上机规格
	 */
	private String minStyle;

	/**
	 * 最大印色
	 */
	private Integer colorQty;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getManufacturer()
	{
		return manufacturer;
	}

	public void setManufacturer(String manufacturer)
	{
		this.manufacturer = manufacturer;
	}

	public MachineType getMachineType()
	{
		return machineType;
	}

	public void setMachineType(MachineType machineType)
	{
		this.machineType = machineType;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Integer getCapacity()
	{
		return capacity;
	}

	public void setCapacity(Integer capacity)
	{
		this.capacity = capacity;
	}

	public String getMaxStyle()
	{
		return maxStyle;
	}

	public void setMaxStyle(String maxStyle)
	{
		this.maxStyle = maxStyle;
	}

	public String getMinStyle()
	{
		return minStyle;
	}

	public void setMinStyle(String minStyle)
	{
		this.minStyle = minStyle;
	}

	public Integer getColorQty()
	{
		return colorQty;
	}

	public void setColorQty(Integer colorQty)
	{
		this.colorQty = colorQty;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getMachineTypeText()
	{
		if (machineType != null)
		{
			return machineType.getText();
		}
		return "-";
	}
}
