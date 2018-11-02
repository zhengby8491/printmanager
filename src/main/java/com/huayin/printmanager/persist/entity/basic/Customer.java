/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 客户信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_customer")
public class Customer extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 客户代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 客户名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 预收款
	 */
	private BigDecimal advanceMoney;

	/**
	 * 简称
	 */
	@Column(length = 50)
	private String shortName;

	/**
	 * 客户分类Id
	 */
	@Column(length = 50)
	private Long customerClassId;

	/**
	 * 送货方式
	 */
	@Column(length = 50)
	private Long deliveryClassId;

	/**
	 * 付款方式
	 */
	@Column(length = 20)
	private Long paymentClassId;

	/**
	 * 结算方式
	 */
	private Long settlementClassId;

	/**
	 * 税率ID
	 */
	private Long taxRateId;

	/**
	 * 销售员
	 */
	@Column(length = 20)
	private Long employeeId;

	/**
	 * 币种
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 是否有效
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isValid = BoolValue.YES;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 代工平台 - 公司id（用于区别供应商来自公司）
	 */
	@Column(length = 20)
	private String originCompanyId;

	/**
	 * 是否期初
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isBegin = BoolValue.NO;

	/**
	 * 客户地址列表
	 */
	@Transient
	private List<CustomerAddress> addressList = new ArrayList<CustomerAddress>();

	/**
	 * 付款单位列表
	 */
	@Transient
	private List<CustomerPayer> payerList = new ArrayList<CustomerPayer>();

	/**
	 * 客户默认地址
	 */
	@Transient
	private CustomerAddress defaultAddress;

	/**
	 * 默认付款单位
	 */
	@Transient
	private CustomerPayer defaultPayer;

	/**
	 * 基础资料是否存在代工平台供应商（用于代工平台）
	 */
	@Transient
	private BoolValue originCompanyExit = BoolValue.NO;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getAdvanceMoney()
	{
		return advanceMoney;
	}

	public void setAdvanceMoney(BigDecimal advanceMoney)
	{
		this.advanceMoney = advanceMoney;
	}

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	public Long getCustomerClassId()
	{
		return customerClassId;
	}

	public void setCustomerClassId(Long customerClassId)
	{
		this.customerClassId = customerClassId;
	}

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public Long getPaymentClassId()
	{
		return paymentClassId;
	}

	public void setPaymentClassId(Long paymentClassId)
	{
		this.paymentClassId = paymentClassId;
	}

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public BoolValue getIsValid()
	{
		return isValid;
	}

	public void setIsValid(BoolValue isValid)
	{
		this.isValid = isValid;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

	public BoolValue getIsBegin()
	{
		return isBegin;
	}

	public void setIsBegin(BoolValue isBegin)
	{
		this.isBegin = isBegin;
	}

	public List<CustomerAddress> getAddressList()
	{
		return addressList;
	}

	public void setAddressList(List<CustomerAddress> addressList)
	{
		this.addressList = addressList;
	}

	public List<CustomerPayer> getPayerList()
	{
		return payerList;
	}

	public void setPayerList(List<CustomerPayer> payerList)
	{
		this.payerList = payerList;
	}

	public CustomerAddress getDefaultAddress()
	{
		return defaultAddress;
	}

	public void setDefaultAddress(CustomerAddress defaultAddress)
	{
		this.defaultAddress = defaultAddress;
	}

	public CustomerPayer getDefaultPayer()
	{
		return defaultPayer;
	}

	public void setDefaultPayer(CustomerPayer defaultPayer)
	{
		this.defaultPayer = defaultPayer;
	}

	public Long getSettlementClassId()
	{
		return settlementClassId;
	}

	public void setSettlementClassId(Long settlementClassId)
	{
		this.settlementClassId = settlementClassId;
	}

	public String getCustomerClassName()
	{
		if (customerClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.CUSTOMERCLASS.name(), customerClassId, "name");
		}
		return "-";
	}

	public String getDeliveryClassName()
	{
		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
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

	public String getTaxRateName()
	{
		if (taxRateId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.TAXRATE.name(), taxRateId, "name");
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

	public String getIsValidText()
	{
		if (isValid != null)
		{
			return isValid.getText();
		}
		return "-";
	}

	public String getIsBeginText()
	{
		if (isBegin != null)
		{
			return isBegin.getText();
		}
		return "-";
	}

	public BoolValue getOriginCompanyExit()
	{
		return originCompanyExit;
	}

	public void setOriginCompanyExit(BoolValue originCompanyExit)
	{
		this.originCompanyExit = originCompanyExit;
	}
}
