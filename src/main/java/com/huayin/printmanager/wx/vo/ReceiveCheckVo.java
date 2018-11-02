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
import java.util.Date;

/**
 * <pre>
 * 微信 - 收款单审核Vo
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class ReceiveCheckVo
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
	 * 结算方式
	 */
	private String settlementClassName;

	/**
	 * 收/付款日期
	 */
	private Date billTime;

	/**
	 * 收/付款天数
	 */
	private Integer billDay;

	/**
	 * 收款金额
	 */
	private BigDecimal money;

	/**
	 * 折扣金额
	 */
	private BigDecimal discount;

	/**
	 * 预收/付款
	 */
	private BigDecimal advance;

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

	public String getSettlementClassName()
	{
		return settlementClassName;
	}

	public void setSettlementClassName(String settlementClassName)
	{
		this.settlementClassName = settlementClassName;
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

	public BigDecimal getAdvance()
	{
		return advance;
	}

	public void setAdvance(BigDecimal advance)
	{
		this.advance = advance;
	}

	public Date getBillTime()
	{
		return billTime;
	}

	public void setBillTime(Date billTime)
	{
		this.billTime = billTime;
	}

	public ReceiveCheckVo(Long id, String billNo, String customerName, String settlementClassName, Date billTime, BigDecimal money, BigDecimal discount, BigDecimal advance)
	{
		super();
		this.id = id;
		this.billNo = billNo;
		this.customerName = customerName;
		this.settlementClassName = settlementClassName;
		this.billTime = billTime;
		this.money = money;
		this.discount = discount;
		this.advance = advance;
	}

	public ReceiveCheckVo()
	{
		super();
	}

	public Integer getBillDay()
	{
		return billDay;
	}

	public void setBillDay(Integer billDay)
	{
		this.billDay = billDay;
	}

}
