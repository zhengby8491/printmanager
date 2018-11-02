/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 下午1:53:52
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * 订单状态
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日下午1:53:52, zhengby
 */
public enum OrderStatusType
{
	TO_pay("待付款",1),
	TO_deliver("代配送",2),
	TO_confirmDeliver("待确认送货",3),
	To_Evaluate("待评价",4),
	Completed("已完成",5),
	Canceled("已取消",6),
	;
	private String text;
	private Integer value;
	
	OrderStatusType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
	}
	
	public static OrderStatusType setType(Integer value)
	{
		for (OrderStatusType a : OrderStatusType.values())
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
