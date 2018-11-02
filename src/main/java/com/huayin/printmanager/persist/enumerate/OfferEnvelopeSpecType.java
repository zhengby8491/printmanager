/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月7日 下午2:00:59
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 信封成品尺寸类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月7日 下午2:00:59
 */
public enum OfferEnvelopeSpecType
{
	B6("B6（176*125）3号信封", "176*125", 176, 125),
	DL("DL（220*110）5号信封", "220*110", 220, 110),
	ZL("ZL（230*120）6号信封", "230*120", 230, 120),
	C5("C5（229*162）7号信封", "229*162", 229, 162),
	C4("C4（324*229）9号信封", "324*229", 324, 229),
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
	
	OfferEnvelopeSpecType(String text, String style, Integer length, Integer width)
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
