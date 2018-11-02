/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 下午2:11:07
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 封面P数
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 下午2:11:07
 */
public enum OfferCoverPageType
{
	ZERO("0", 0),
	FOUR("4", 4),
	SIX("6", 6),
	EIGHT("8", 8),
	CUSTOM("自定义",-1);
	/**
	 * 文本描述
	 */
	private String text;
	
	private Integer value;
	
	OfferCoverPageType(String text, Integer value)
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
