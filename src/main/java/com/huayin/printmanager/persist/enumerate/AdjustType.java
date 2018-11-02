/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午11:13:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 商家类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午11:13:51, zhengby
 */
public enum AdjustType
{
	RECEIVE("RECEIVE", "应收款", "客户"),
	PAY("PAY", "应付款", "供应商"),
	ACCOUNT("ACCOUNT", "账户", "账户");
	
	private String value;
	
	private String text;
	
	private String target;
	
	AdjustType(String value, String text, String target)
	{
		this.value = value;
		this.text = text;
		this.target = target;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getTarget()
	{
		return target;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}
}
