/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 基础枚举 - 通用的枚举类型(是,否)
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
public enum BoolValue
{
	/**
	 * 是
	 */
	YES("是", true),
	/**
	 * 否
	 */
	NO("否", false);

	private String text;

	private Boolean value;

	BoolValue(String text, Boolean value)
	{
		this.text = text;
		this.value = value;
	}

	public String getText()
	{
		return text;
	}

	public Boolean getValue()
	{
		return value;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setValue(Boolean value)
	{
		this.value = value;
	}

	public static BoolValue valueOf(boolean value)
	{
		if (value)
		{
			return BoolValue.YES;
		}
		else
		{
			return BoolValue.NO;
		}
	}
}