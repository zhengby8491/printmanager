/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月19日 下午6:16:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 机台印色
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月19日
 */
public enum MachinePrintColor
{
	COLOR1("单色", "1"),
	COLOR2("双色", "2"),
	COLOR4("四色", "4"),
	COLOR6("六色", "8"),
	COLOR8("八色", "8"),
	COLOR10("十色", "10"),
	COLOR12("十二色", "12"),;
	
	private String text;
	
	private String val;
	
	MachinePrintColor(String text, String val)
	{
		this.text = text;
		this.val = val;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getVal()
	{
		return val;
	}

	public void setVal(String val)
	{
		this.val = val;
	}
}
