/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月21日 下午4:18:55
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 报价模块 - 工序设置 - 工序类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月21日 下午4:18:55
 */
public enum OfferProcedureType
{
	/**
	 * 印后
	 */
	AFTER("印后工序"),
	/**
	 * 成品
	 */
	FINISHED("成品工序");
	
	private String text;

	OfferProcedureType(String text)
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
