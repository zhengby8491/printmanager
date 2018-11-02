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

import java.math.BigDecimal;

/**
 * <pre>
 * 微信 - 销售、采购、发外汇总VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SumVo
{
	/**
	 * 当天下单金额
	 */
	private BigDecimal dayTotalMoney;

	/**
	 * 单月下单金额
	 */
	private BigDecimal monthTotalMoney;

	/**
	 * 应收/付金额
	 */
	private BigDecimal Money;

	/**
	 * 到期应收/付金额
	 */
	private BigDecimal expireMoney;

	public BigDecimal getDayTotalMoney()
	{
		return dayTotalMoney;
	}

	public void setDayTotalMoney(BigDecimal dayTotalMoney)
	{
		this.dayTotalMoney = dayTotalMoney;
	}

	public BigDecimal getMonthTotalMoney()
	{
		return monthTotalMoney;
	}

	public void setMonthTotalMoney(BigDecimal monthTotalMoney)
	{
		this.monthTotalMoney = monthTotalMoney;
	}

	public BigDecimal getMoney()
	{
		return Money;
	}

	public void setMoney(BigDecimal money)
	{
		Money = money;
	}

	public BigDecimal getExpireMoney()
	{
		return expireMoney;
	}

	public void setExpireMoney(BigDecimal expireMoney)
	{
		this.expireMoney = expireMoney;
	}

	public SumVo(BigDecimal dayTotalMoney, BigDecimal monthTotalMoney, BigDecimal money, BigDecimal expireMoney)
	{
		super();
		this.dayTotalMoney = dayTotalMoney;
		this.monthTotalMoney = monthTotalMoney;
		Money = money;
		this.expireMoney = expireMoney;
	}

	public SumVo()
	{
		super();
	}

}
