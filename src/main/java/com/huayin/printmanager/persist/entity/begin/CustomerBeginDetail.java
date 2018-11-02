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
 * 客户期初子表
 * </pre>
 * @author raintear
 * @version 1.0, 2016年8月11日
 */
@Entity
@Table(name = "basic_customerBegin_detail")
public class CustomerBeginDetail extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;
	/**
	 * 客户id
	 */
	private Long customerId;
	
	/**
	 * 客户编号
	 */
	private String customerCode;
	
	/**
	 * 客户名称
	 */
	private String customerName;
	
	/**
	 * 币别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;
	
	/**
	 * 预收款(订金)
	 */
	private BigDecimal advanceMoney;

	/**
	 * 应收款
	 */
	private BigDecimal receiveMoney;
	
	/**
	 * 已收款
	 */
	private BigDecimal receivedMoney;
	
	/**
	 * 是否付完款
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isReceiveOver=BoolValue.NO;

	/**
	 * 收款日期
	 */
	private Date receiveTime;

	@Transient
	private CustomerBegin master;

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public BigDecimal getAdvanceMoney()
	{
		return advanceMoney;
	}

	public void setAdvanceMoney(BigDecimal advanceMoney)
	{
		this.advanceMoney = advanceMoney;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public Date getReceiveTime()
	{
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime)
	{
		this.receiveTime = receiveTime;
	}

	public CustomerBegin getMaster()
	{
		return master;
	}

	public void setMaster(CustomerBegin master)
	{
		this.master = master;
	}

	public String getCustomerCode()
	{
		return customerCode;
	}

	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public BoolValue getIsReceiveOver()
	{
		return isReceiveOver;
	}

	public void setIsReceiveOver(BoolValue isReceiveOver)
	{
		this.isReceiveOver = isReceiveOver;
	}

	public BigDecimal getReceivedMoney()
	{
		return receivedMoney;
	}

	public void setReceivedMoney(BigDecimal receivedMoney)
	{
		this.receivedMoney = receivedMoney;
	}
	
	public String getCurrencyTypeText(){
		if (currencyType!=null)
		{
			return currencyType.getText();
		}
		return "-";
	}
	
}
