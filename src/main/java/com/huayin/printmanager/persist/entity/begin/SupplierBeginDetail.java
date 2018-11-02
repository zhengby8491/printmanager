package com.huayin.printmanager.persist.entity.begin;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;

/**
 * <pre>
 * 供应商期初明细表
 * </pre>
 * @author raintear
 * @version 1.0, 2016年8月11日
 */
@Entity
@Table(name = "basic_supplierBegin_detail")
public class SupplierBeginDetail extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商id
	 */
	private Long supplierId;

	/**
	 * 供应商编号
	 */
	private String supplierCode;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 预付款
	 */
	private BigDecimal advanceMoney;

	/**
	 * 应付款
	 */
	private BigDecimal paymentMoney;

	/**
	 * 已付款
	 */
	private BigDecimal paymentedMoney;

	/**
	 * 是否完成付款
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isPaymentOver=BoolValue.NO;
	/**
	 * 收款日期
	 */
	private Date receiveTime;
	
	/**
	 * 币别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	@Transient
	private SupplierBegin master;

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public String getSupplierCode()
	{
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode)
	{
		this.supplierCode = supplierCode;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}


	public BoolValue getIsPaymentOver()
	{
		return isPaymentOver;
	}

	public void setIsPaymentOver(BoolValue isPaymentOver)
	{
		this.isPaymentOver = isPaymentOver;
	}

	public BigDecimal getAdvanceMoney()
	{
		return advanceMoney;
	}

	public void setAdvanceMoney(BigDecimal advanceMoney)
	{
		this.advanceMoney = advanceMoney;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

	public Date getReceiveTime()
	{
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime)
	{
		this.receiveTime = receiveTime;
	}

	public SupplierBegin getMaster()
	{
		return master;
	}

	public void setMaster(SupplierBegin master)
	{
		this.master = master;
	}

	public BigDecimal getPaymentedMoney()
	{
		return paymentedMoney;
	}

	public void setPaymentedMoney(BigDecimal paymentedMoney)
	{
		this.paymentedMoney = paymentedMoney;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}
	public String getCurrencyTypeText(){
		if (currencyType!=null)
		{
			return currencyType.getText();
		}
		return "-";
	}

}
