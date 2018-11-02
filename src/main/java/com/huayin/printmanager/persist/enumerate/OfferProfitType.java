/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 下午1:39:28
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 报价模块 - 利润设置类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日
 */
public enum OfferProfitType
{
	NUMBER("按阶梯数量利润比计算"),
	MONEY("按阶梯金额利润比计算"),;
	
	/**
	 * 文本
	 */
	private String text;
	
	OfferProfitType(String text)
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
