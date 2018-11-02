/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
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
import com.huayin.printmanager.persist.enumerate.PaymentClassType;

/**
 * <pre>
 * 基础模块 - 付款方式
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:37:56
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_payment_class")
public class PaymentClass extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 付款方式名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 付款方式类型（货到付款，月结）
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private PaymentClassType type;

	/**
	 * 天数
	 */
	private Integer dayNum;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PaymentClassType getType()
	{
		return type;
	}

	public void setType(PaymentClassType type)
	{
		this.type = type;
	}

	public Integer getDayNum()
	{
		return dayNum;
	}

	public void setDayNum(Integer dayNum)
	{
		this.dayNum = dayNum;
	}

	public String getTypeText()
	{
		if (type != null)
		{
			return type.getText();
		}
		return "-";
	}
}
