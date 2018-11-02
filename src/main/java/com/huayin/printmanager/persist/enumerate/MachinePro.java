/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月19日 上午11:23:00
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 机台属性
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月19日
 */
public enum MachinePro
{
	PRO1("全开机", "1"),
	PRO2("对开机", "2"),
	PRO4("四开机", "4"),
	PRO6("六开机", "6"),
	PRO8("八开机", "8"),;
	
	/**
	 * 中文名称
	 */
	private String text;
	
	/**
	 * 值
	 */
	private String val;
	
	MachinePro(String text, String val)
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
