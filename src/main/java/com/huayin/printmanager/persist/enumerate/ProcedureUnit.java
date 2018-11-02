/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月30日 上午10:40:47
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 工序 - 单位
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月30日
 */
public enum ProcedureUnit
{
	UNIT1("元/印张"),
	UNIT2("元/m2"),
	UNIT3("元/指定m2"),
	UNIT4("元/印品"),
	UNIT5("元/贴/手"),
	UNIT6("元/P"),
	UNIT7("元/本"),;
	
	private String text;
	
	ProcedureUnit(String text)
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
