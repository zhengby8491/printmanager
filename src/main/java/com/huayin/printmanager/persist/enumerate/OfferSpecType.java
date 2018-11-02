/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 上午10:53:58
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 成品尺寸类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 上午10:53:58
 */
public enum OfferSpecType
{
	ZHENG2KAI("正2开（530*760）", "530*760", 530, 760),
	ZHENG4KAI("正4开（530*380）", "530*380", 530, 380),
	ZHENG8KAI("正8开（260*380）", "260*380", 260, 380),
	ZHENG12KAI("正12开（250*250）", "250*250", 250, 250),
	ZHENG16KAI("正16开（185*260）", "185*260", 185, 260),
	ZHENG32KAI("正32开（185*125）", "185*125", 185, 125),
	DA2KAI("大2开（860*580）", "860*580", 860, 580),
	DA4KAI("大4开（420*580）", "420*580", 420, 580),
	DA8KAI("大8开（420*285）", "420*285", 420, 285),
	DA12KAI("大12开（280*280）", "280*280", 280, 280),
	DA16KAI("大16开（210*285）", "210*285", 210, 285),
	DA32KAI("大32开（210*140）", "210*140", 210, 140),
	CUSTOM("自定义", "CUSTOM", 0, 0);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 尺寸
	 */
	private String style;
	
	/**
	 * 尺寸 - 长
	 */
	private Integer length;
	
	/**
	 * 尺寸 - 宽
	 */
	private Integer width;
	
	OfferSpecType(String text, String style, Integer length, Integer width)
	{
		this.text = text;
		this.style = style;
		this.length = length;
		this.width = width;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
	}
}
