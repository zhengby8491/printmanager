/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 下午1:46:44
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * 物流运输
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日下午1:46:44, zhengby
 */
public enum DeliveryMethod
{
	Method1("自提", 1),
	Method2("供应商发货", 2);
	
	private String text;
	
	private Integer value;
	
	DeliveryMethod(String text, Integer value)
	{
		this.text = text;
		this.value = value;
	}
	
	public static DeliveryMethod setType(Integer value)
	{
		for (DeliveryMethod a : DeliveryMethod.values())
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
