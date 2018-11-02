/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年11月7日 下午4:42:05
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 纸张类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年11月7日
 */
public enum PaperType
{
	MAGNANIMOUS_PAPER("大度", "889*1194", 1194, 889),
	ARE_DEGREES_PAPER("正度", "787*1092", 1092, 787),;
	
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
	
	PaperType(String text, String style, Integer length, Integer width)
	{
		this.text  = text;
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
