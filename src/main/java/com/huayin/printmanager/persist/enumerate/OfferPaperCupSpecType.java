/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月7日 下午2:31:07
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 纸杯成品尺寸类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月7日 下午2:31:07
 */
public enum OfferPaperCupSpecType
{
	SIXOZ("6.5盎司纸杯（180ml）", "230*75", 230, 75, 49, 73, 75), 
	NINEOZ("9盎司纸杯（250ml）", "240*94", 240, 94, 52, 77, 94),
	CUSTOM("自定义展开尺寸", "CUSTOM",0, 0, 0, 0, 0),;
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
	
	/**
	 * 底部直径
	 */
	private Integer bottomDia;

	/**
	 * 口部直径
	 */
	private Integer topDia;
	
	/**
	 * 纸杯高度
	 */
	private Integer height;
	
	OfferPaperCupSpecType(String text, String style, Integer length, Integer width, Integer bottomDia, Integer topDia, Integer height)
	{
		this.text = text;
		this.style = style;
		this.length = length;
		this.width = width;
		this.bottomDia = bottomDia;
		this.topDia = topDia;
		this.height = height;
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

	public Integer getBottomDia()
	{
		return bottomDia;
	}

	public void setBottomDia(Integer bottomDia)
	{
		this.bottomDia = bottomDia;
	}

	public Integer getTopDia()
	{
		return topDia;
	}

	public void setTopDia(Integer topDia)
	{
		this.topDia = topDia;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}
}
