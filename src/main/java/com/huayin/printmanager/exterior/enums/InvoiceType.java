/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 下午1:39:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * 发票类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日下午1:39:51, zhengby
 */
public enum InvoiceType
{
	NO("不开发票",1),
	NORmal("普通发票",2),
	TAX("增值税发票",3)
	;
	private String text;
	
	private Integer value;
	
	InvoiceType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
	}

	public static InvoiceType setType(Integer value)
	{
		for (InvoiceType a : InvoiceType.values())
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
