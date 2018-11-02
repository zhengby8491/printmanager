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
 * 微信 - 收款核销单审核Vo
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WriteoffReceiveCheckVo
{
	private Long id;

	/**
	 * 收款单号
	 */
	private String billNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 收款金额
	 */
	private BigDecimal money;

	/**
	 * 折扣金额
	 */
	private BigDecimal discount;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public WriteoffReceiveCheckVo(Long id, String billNo, String customerName, BigDecimal money, BigDecimal discount)
	{
		super();
		this.id = id;
		this.billNo = billNo;
		this.customerName = customerName;
		this.money = money;
		this.discount = discount;
	}

	public WriteoffReceiveCheckVo()
	{
		super();
	}

}
