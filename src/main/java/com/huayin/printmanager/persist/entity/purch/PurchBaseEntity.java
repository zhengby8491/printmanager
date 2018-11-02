/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.purch;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusPurch;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 ： 采购模块主表基础类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月15日, zhaojt
 * @version 	   2.0, 2018年2月23日上午11:32:28, zhengby, 代码规范
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchBaseEntity extends BaseBillMasterTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 采购员id
	 */
	@Column(length = 50)
	private Long employeeId;

	/**
	 * 供应商Id
	 */
	@Column(length = 50)
	private Long supplierId;

	/**
	 * 供应商编号
	 */
	private String code;

	/**
	 * 供应商名称
	 */
	@Column(length = 50)
	private String supplierName;

	/**
	 * 供应商地址
	 */
	private String supplierAddress;

	/**
	 * 供应商地址Id
	 */
	private Long supplierAddressId;

	/**
	 * 采购进度状态
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private ProgressStatusPurch progressStatus;

	/**
	 * 采购员名称
	 */
	@SuppressWarnings("unused")
	private String employeeName;

	/**
	 * 联系人
	 */
	@Column(length = 50)
	private String linkName;

	/**
	 * 联系人Id
	 */
	private Long supplierContactId;

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
	 * 币别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 汇率Id
	 */
	private Long rateId;

	/**
	 * 兑换汇率
	 */
	@Column(length = 20)
	private BigDecimal rate;

	/**
	 * 税率Id
	 */
	private Long taxRateId;

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
	@Column(length = 50)
	private Long paymentClassId;

	/**
	 * 结算方式
	 */
	@Column(length = 20)
	private Long settlementClassId;

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public String getSupplierAddress()
	{
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress)
	{
		this.supplierAddress = supplierAddress;
	}

	public Long getSupplierAddressId()
	{
		return supplierAddressId;
	}

	public void setSupplierAddressId(Long supplierAddressId)
	{
		this.supplierAddressId = supplierAddressId;
	}

	public ProgressStatusPurch getProgressStatus()
	{
		return progressStatus;
	}

	public void setProgressStatus(ProgressStatusPurch progressStatus)
	{
		this.progressStatus = progressStatus;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public Long getSupplierContactId()
	{
		return supplierContactId;
	}

	public void setSupplierContactId(Long supplierContactId)
	{
		this.supplierContactId = supplierContactId;
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

	public BigDecimal getRate()
	{
		return rate;
	}

	public void setRate(BigDecimal rate)
	{
		this.rate = rate;
	}

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
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

	// ----------------------------------------------------------------------------------------
	public String getCurrencyTypeText()
	{

		if (currencyType != null)
		{
			return currencyType.getText();
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

	public String getSupplierName()
	{
		if (supplierId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.SUPPLIER.name(), supplierId, "name");
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
