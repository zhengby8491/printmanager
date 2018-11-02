/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月7日 上午11:06:34
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 兜底类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月7日 上午11:06:34
 */
public enum OfferRevealType
{
	FIFTY("50", 50),
	SIXTY("60", 60),
	SIXTYFIVE("65", 65),
	SEVENTY("70", 70),
	SEVENTYFIVE("75", 75),
	EIGHTY("80", 80);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 值
	 */
	private Integer value;
	
	OfferRevealType(String text, Integer value){
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
