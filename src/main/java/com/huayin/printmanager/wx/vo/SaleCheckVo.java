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
 * 微信 - 销售订单审核VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SaleCheckVo
{
	private Long id;

	/**
	 * 销售单号
	 */
	private String billNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 联系人
	 */
	private String linkName;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 金额
	 */
	private BigDecimal money;

	/**
	 * 总税额
	 */
	private BigDecimal tax;

	/**
	 * 金额(不含税）
	 */
	private BigDecimal noTaxMoney;

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

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getTax()
	{
		return tax;
	}

	public void setTax(BigDecimal tax)
	{
		this.tax = tax;
	}

	public BigDecimal getNoTaxMoney()
	{
		return noTaxMoney;
	}

	public void setNoTaxMoney(BigDecimal noTaxMoney)
	{
		this.noTaxMoney = noTaxMoney;
	}

	public SaleCheckVo(Long id, String billNo, String customerName, String linkName, String mobile, BigDecimal money, BigDecimal tax, BigDecimal noTaxMoney)
	{
		super();
		this.id = id;
		this.billNo = billNo;
		this.customerName = customerName;
		this.linkName = linkName;
		this.mobile = mobile;
		this.money = money;
		this.tax = tax;
		this.noTaxMoney = noTaxMoney;
	}

	public SaleCheckVo()
	{
		super();
	}

}
