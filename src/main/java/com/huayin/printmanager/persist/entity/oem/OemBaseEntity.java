/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月6日 下午4:53:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  ： 代工单信息基础类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月6日下午4:53:00, zhengby
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class OemBaseEntity extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = -926931485283368952L;

	/**
	 * 客户公司id
	 */
	@Column(length = 20)
	private String originCompanyId;
	
	/**
	 * 客户id
	 */
	@Column(length = 50)
	private Long customerId;
	
	/**
	 * 客户名称
	 */
	@Transient
	private String customerName;
	
	/**
	 * 销售员ID
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 币别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 兑换汇率id(从ExchangeRate 表获取)
	 */
	@Column(length = 20)
	private Long rateId;

	/**
	 * 送货地址
	 */
	private String deliveryAddress;

	/**
	 * 联系人
	 */
	@Column(length = 50)
	private String linkName;

	/**
	 * 手机
	 */
	@Column(length = 20)
	private String mobile;

	/**
	 * 电话
	 */
	@Column(length = 20)
	private String phone;

	/**
	 * 传真
	 */
	@Column(length = 20)
	private String fax;

	/**
	 * 总金额(含税)
	 */
	private BigDecimal totalMoney;

	/**
	 * 总金额(不含税）
	 */
	private BigDecimal noTaxTotalMoney;

	/**
	 * 总税额
	 */
	private BigDecimal totalTax;

	/**
	 * 付款方式
	 */
	@Column(length = 20)
	private Long paymentClassId;

	/**
	 * 结算方式
	 */
	@Column(length = 20)
	private Long settlementClassId;

	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public Long getRateId()
	{
		return rateId;
	}

	public void setRateId(Long rateId)
	{
		this.rateId = rateId;
	}

	public String getDeliveryAddress()
	{
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress)
	{
		this.deliveryAddress = deliveryAddress;
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

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public BigDecimal getTotalMoney()
	{
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney)
	{
		this.totalMoney = totalMoney;
	}

	public BigDecimal getNoTaxTotalMoney()
	{
		return noTaxTotalMoney;
	}

	public void setNoTaxTotalMoney(BigDecimal noTaxTotalMoney)
	{
		this.noTaxTotalMoney = noTaxTotalMoney;
	}

	public BigDecimal getTotalTax()
	{
		return totalTax;
	}

	public void setTotalTax(BigDecimal totalTax)
	{
		this.totalTax = totalTax;
	}

	public Long getPaymentClassId()
	{
		return paymentClassId;
	}

	public void setPaymentClassId(Long paymentClassId)
	{
		this.paymentClassId = paymentClassId;
	}

	public Long getSettlementClassId()
	{
		return settlementClassId;
	}

	public void setSettlementClassId(Long settlementClassId)
	{
		this.settlementClassId = settlementClassId;
	}

	public String getCustomerCode()
	{
		if (customerId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.CUSTOMER.name(), customerId, "code");
		}
		return "-";
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCustomerName()
	{
		if (this.customerName != null)
		{
			return this.customerName;
		}

		if (customerId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.CUSTOMER.name(), customerId, "name");

		}
		return "-";
	}

	public String getEmployeeName()

	{
		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}

	public String getCurrencyTypeText()
	{
		if (currencyType != null)
		{
			return currencyType.getText();
		}
		return "-";
	}

	public String getRateName()
	{
		if (rateId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.TAXRATE.name(), rateId, "name");
		}
		return "-";
	}

	public String getPaymentClassName()
	{
		if (paymentClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PAYMENTCLASS.name(), paymentClassId, "name");
		}
		return "-";
	}

	public String getSettlementClassName()
	{
		if (settlementClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.SETTLEMENTCLASS.name(), settlementClassId, "name");
		}
		return "-";
	}
}
