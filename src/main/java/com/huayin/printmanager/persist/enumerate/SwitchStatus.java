/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月30日 下午3:05:19
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 启用禁用
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月30日
 */
public enum SwitchStatus
{
	ENABLED("启用"),
	DISABLED("禁用"),;
	
	private String text;
	
	SwitchStatus(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
