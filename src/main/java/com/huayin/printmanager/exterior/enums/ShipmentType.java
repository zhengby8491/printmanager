/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 下午1:32:43
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * 配送类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日下午1:32:43, zhengby
 */
public enum ShipmentType
{
	PAY1("立即支付", 1), PAY2("延期支付", 2), PAY3("分期支付", 3), PAY4("定金支付", 4), PAY5("新延期支付", 5), PAY6("授信支付", 6),;
	private String text;

	private Integer value;

	ShipmentType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
	}

	public static ShipmentType setType(Integer value)
	{
		for (ShipmentType a : ShipmentType.values())
		{
			if (a.getValue() == value)
			{
				return a;
			}
		}
		return null;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}

}
