/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 上午10:51:06
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 印刷颜色类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 上午10:51:06
 */
public enum OfferPrintColorType
{
	NONE("零色", 0),
	ONECOLOR("单色", 1),
	TWOECOLOR("双色", 2),
	THREEECOLOR("三色", 3),
	FOURECOLOR("四色", 4),
	FIVEECOLOR("五色", 5),
	SIXECOLOR("六色", 6);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 对应数值
	 */
	private Integer value;
	
	OfferPrintColorType(String text, Integer value)
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
