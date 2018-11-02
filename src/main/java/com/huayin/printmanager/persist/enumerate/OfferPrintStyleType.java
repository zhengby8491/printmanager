/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 上午10:57:09
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动 报价 - 印刷方式类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 上午10:57:09
 */
public enum OfferPrintStyleType
{
	SINGLE("单面印刷",1),
	DOUBLE("双面印刷",2),
	HEADTAIL("正反印刷", 2),
	CHAOS("自翻印刷", 1);
	
	/**
	 * 文本描述
	 */
	private  String text;
	/**
	 * 值
	 */
	private Integer value;
	
	OfferPrintStyleType(String text, Integer value){
		this.text = text;
		this.value = value;
	};
	
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
