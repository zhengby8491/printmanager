/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月7日 上午11:10:20
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 封套类成品尺寸类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月7日 上午11:10:20
 */
public enum OfferSheathSpecType
{
	ONE("215*290", "215*290", 215, 290), 
	TWO("215*300", "215*300", 215, 300), 
	THREE("215*305", "215*305", 215, 305),
	CUSTOM("自定义展开尺寸", "CUSTOM", 0, 0);

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
	

	OfferSheathSpecType(String text, String style, Integer length, Integer width)
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
