/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月7日 下午1:36:36
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 联次类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月7日 下午1:36:36
 */
public enum OfferSheetType
{
	ONE("单联", 1),
	TWO("两联", 2),
	THREE("三联", 3),
	FOUR("四联", 4),
	FIVE("五联", 5),
	SIX("六联", 6),
	SEVEN("七联", 7),
	EIGHT("八联", 8),
	NINE("九联", 9);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 对应数值
	 */
	private Integer value;
	
	OfferSheetType(String text, Integer value)
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
