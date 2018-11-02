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
 * 基础枚举 - 币别
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
public enum CurrencyType
{
	/**
	 * 人民币
	 */
	RMB("rmb", "人民币", 1),
	/**
	 * 美元
	 */
	USD("usd", "美元", 2),
	/**
	 * 英镑
	 */
	GBP("gbp", "英镑", 3),
	/**
	 * 欧元
	 */
	ERU("eru", "欧元", 4),
	/**
	 * 港元
	 */
	HKD("hkd", "港元", 5),
	/**
	 * 日元
	 */
	JPY("jpy", "日元", 7),
	/**
	 * 韩元
	 */
	KRW("krw", "韩元", 6);
	private String value;

	private String text;

	private int sort;

	CurrencyType(String value, String text, int sort)
	{
		this.text = text;
		this.value = value;
		this.sort = sort;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}
}
