/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月17日 下午4:54:04
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月17日下午4:54:04, zhengby
 */
public enum PurchOrderStatusType
{
	TO_STOCK("待入库", 103),
	PART_STOCK("部分入库", 104),
	ALL_STOCK("全部入库", 105),
	;
	private String text;
	
	private Integer value;
	
	PurchOrderStatusType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
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
