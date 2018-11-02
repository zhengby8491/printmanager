/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 基础枚举 - 材料信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
public enum MaterialType
{

	INGREDIENTS("主料"),

	ACCESSORIES("辅料");

	private String text;

	MaterialType(String text)
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
