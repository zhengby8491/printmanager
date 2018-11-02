/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

/**
 * <pre>
 * 微信 - 财务付/收款汇总VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class FinanceDetailSumVo
{
	private String name;

	private String money;

	private String discount;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMoney()
	{
		return money;
	}

	public void setMoney(String money)
	{
		this.money = money;
	}

	public String getDiscount()
	{
		return discount;
	}

	public void setDiscount(String discount)
	{
		this.discount = discount;
	}

	public FinanceDetailSumVo(String name, String money, String discount)
	{
		super();
		this.name = name;
		this.money = money;
		this.discount = discount;
	}

	public FinanceDetailSumVo()
	{
		super();
	}
}
