/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 基础枚举 - 工序类型
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public enum ProcedureType
{
	/**
	 * 印前
	 */
	BEFORE("印前工序"),
	/**
	 * 印刷
	 */
	PRINT("印刷工序"),
	/**
	 * 印后
	 */
	AFTER("印后工序"),
	/**
	 * 成品
	 */
	FINISHED("成品工序");

	private String text;

	ProcedureType(String text)
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
