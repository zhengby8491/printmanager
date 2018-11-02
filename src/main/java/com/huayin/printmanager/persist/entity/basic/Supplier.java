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
import com.huayin.printmanager.persist.enumerate.SupplierType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 供应商信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_supplier")
public class Supplier extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 供应商名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 预付款
	 */
	private BigDecimal advanceMoney;

	/**
	 * 供应商简称
	 */
	@Column(length = 20)
	private String shortName;

	/**
	 * 供应商分类
	 */
	@Column(length = 50)
	private Long supplierClassId;

	/**
	 * 供应商属性
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private SupplierType type;

	/**
	 * 汇率表Id
	 */
	private Long exchangeRateId;

	/**
	 * 公司法人
	 */
	@Column(length = 20)
	private String corporate;

	/**
	 * 注册资金(单位:元)
	 */
	private Integer registeredCapital;

	/**
	 * 公司主页
	 */
	private String url;

	/**
	 * 采购员
	 */
	@Column(length = 20)
	private Long employeeId;

	/**
	 * 付款方式
	 */
	private Long paymentClassId;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 结算方式
	 */
	private Long settlementClassId;

	/**
	 * 税率ID
	 */
	private Long taxRateId;

	/**
	 * 是否有效
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isValid = BoolValue.YES;

	/**
	 * 币种
	 */
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 是否期初(默认:否)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isBegin = BoolValue.NO;

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
	 * 客户地址列表
	 */
	@Transient
	private List<SupplierAddress> addressList = new ArrayList<SupplierAddress>();

	/**
	 *付款单位列表
	 */
	@Transient
	private List<SupplierPayer> payerList = new ArrayList<SupplierPayer>();

	/**
	 * 默认地址
	 */
	@Transient
	private SupplierAddress defaultAddress;

	/**
	 * 默认付款单位
	 */
	@Transient
	private SupplierPayer defaultPayer;
	
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

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	public Long getSupplierClassId()
	{
		return supplierClassId;
	}

	public void setSupplierClassId(Long supplierClassId)
	{
		this.supplierClassId = supplierClassId;
	}

	public SupplierType getType()
	{
		return type;
	}

	public void setType(SupplierType type)
	{
		this.type = type;
	}

	public Long getExchangeRateId()
	{
		return exchangeRateId;
	}

	public void setExchangeRateId(Long exchangeRateId)
	{
		this.exchangeRateId = exchangeRateId;
	}

	public BigDecimal getAdvanceMoney()
	{
		return advanceMoney;
	}

	public void setAdvanceMoney(BigDecimal advanceMoney)
	{
		this.advanceMoney = advanceMoney;
	}

	public String getCorporate()
	{
		return corporate;
	}

	public void setCorporate(String corporate)
	{
		this.corporate = corporate;
	}

	public Integer getRegisteredCapital()
	{
		return registeredCapital;
	}

	public void setRegisteredCapital(Integer registeredCapital)
	{
		this.registeredCapital = registeredCapital;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public Long getPaymentClassId()
	{
		return paymentClassId;
	}

	public void setPaymentClassId(Long paymentClassId)
	{
		this.paymentClassId = paymentClassId;
	}

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public Long getSettlementClassId()
	{
		return settlementClassId;
	}

	public void setSettlementClassId(Long settlementClassId)
	{
		this.settlementClassId = settlementClassId;
	}

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
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

	public List<SupplierAddress> getAddressList()
	{
		return addressList;
	}

	public void setAddressList(List<SupplierAddress> addressList)
	{
		this.addressList = addressList;
	}

	public List<SupplierPayer> getPayerList()
	{
		return payerList;
	}

	public void setPayerList(List<SupplierPayer> payerList)
	{
		this.payerList = payerList;
	}

	public SupplierAddress getDefaultAddress()
	{
		return defaultAddress;
	}

	public void setDefaultAddress(SupplierAddress defaultAddress)
	{
		this.defaultAddress = defaultAddress;
	}

	public SupplierPayer getDefaultPayer()
	{
		return defaultPayer;
	}

	public void setDefaultPayer(SupplierPayer defaultPayer)
	{
		this.defaultPayer = defaultPayer;
	}

	public BoolValue getIsBegin()
	{
		return isBegin;
	}

	public void setIsBegin(BoolValue isBegin)
	{
		this.isBegin = isBegin;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public String getEmployeeName()
	{
		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}

	public String getSupplierClassName()
	{
		if (supplierClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.SUPPLIERCLASS.name(), supplierClassId, "name");
		}
		return "-";
	}

	public String getTypeText()
	{
		if (type != null)
		{
			return type.getText();
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

	public String getPaymentClassName()
	{
		if (paymentClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PAYMENTCLASS.name(), paymentClassId, "name");
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

	public String getCurrencyTypeText()
	{
		if (currencyType != null)
		{
			return currencyType.getText();
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
