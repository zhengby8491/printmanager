/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 上午11:20:39
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 专色类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 上午11:20:39
 */
public enum OfferSpotColorType
{
	NOSPOT("无专", 0),
	ONESPOT("一专", 1),
	TWOSPOT("二专", 2),
	THREESPOT("三专", 3),
	FOURSPOT("四专", 4),
	FIVESPOT("五专", 5);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 对应数值
	 */
	private Integer value;
	
	OfferSpotColorType(String text, Integer value)
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
