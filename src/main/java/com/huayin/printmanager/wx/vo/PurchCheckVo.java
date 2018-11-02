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
 * 采购进度VO
 * </pre>
 * @author raintear
 * @version 1.0, 2016年11月7日
 */
public class PurchCheckVo
{
	private Long id;
	
	/**
	 * 采购单号
	 */
	private String billNo;
	
	/**
	 * 供应商名称
	 */
	private String supplierName;
	
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

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
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

	public PurchCheckVo()
	{
		super();
	}

	public PurchCheckVo(Long id, String billNo, String supplierName, String linkName, String mobile, BigDecimal money,
			BigDecimal tax, BigDecimal noTaxMoney)
	{
		super();
		this.id = id;
		this.billNo = billNo;
		this.supplierName = supplierName;
		this.linkName = linkName;
		this.mobile = mobile;
		this.money = money;
		this.tax = tax;
		this.noTaxMoney = noTaxMoney;
	}
	
	
}
