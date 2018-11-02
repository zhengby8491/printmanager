/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 财务管理 - 往来单位欠款表VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class FinanceCompanyArrearsVo
{
	private String name;
	
	private String type;
	
	/**
	 * 应收款余额
	 */
	private BigDecimal receiveMoney;
	
	/**
	 * 应付款余额
	 */
	private BigDecimal paymentMoney;
	
	/**
	 * 应付加工余额
	 */
	private BigDecimal processMoney;
	
	/**
	 * 结余
	 */
	private BigDecimal balanceMoney;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

	public BigDecimal getProcessMoney()
	{
		return processMoney;
	}

	public void setProcessMoney(BigDecimal processMoney)
	{
		this.processMoney = processMoney;
	}

	public BigDecimal getBalanceMoney()
	{
		return balanceMoney;
	}

	public void setBalanceMoney(BigDecimal balanceMoney)
	{
		this.balanceMoney = balanceMoney;
	}
	
	
}
