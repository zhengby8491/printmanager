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
 * 微信 - 收款单审核 明细Vo
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WriteoffReceiveCheckDetailVo
{
	private Long id;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 源单金额
	 */
	private BigDecimal sourceMoney;

	/**
	 * 源单余额(付款前的未付款金额)
	 */
	private BigDecimal sourceBalanceMoney;

	/**
	 * 本次收款金额
	 */
	private BigDecimal money;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public BigDecimal getSourceMoney()
	{
		return sourceMoney;
	}

	public void setSourceMoney(BigDecimal sourceMoney)
	{
		this.sourceMoney = sourceMoney;
	}

	public BigDecimal getSourceBalanceMoney()
	{
		return sourceBalanceMoney;
	}

	public void setSourceBalanceMoney(BigDecimal sourceBalanceMoney)
	{
		this.sourceBalanceMoney = sourceBalanceMoney;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public WriteoffReceiveCheckDetailVo(Long id, String productName, BigDecimal sourceMoney, BigDecimal sourceBalanceMoney, BigDecimal money)
	{
		super();
		this.id = id;
		this.productName = productName;
		this.sourceMoney = sourceMoney;
		this.sourceBalanceMoney = sourceBalanceMoney;
		this.money = money;
	}

	public WriteoffReceiveCheckDetailVo()
	{
		super();
	}

}
