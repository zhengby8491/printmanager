/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

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
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 ：发外管理基础信息类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, liudong
 * @version 	   2.0, 2018年2月23日下午4:00:52, zhengby, 代码规范
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class OutSourceBaseEntity extends BaseBillMasterTableEntity
{

	private static final long serialVersionUID = 4160451619391354908L;

	/**
	 * 加工商Id
	 */
	private Long supplierId;

	/**
	 * 加工商code
	 */
	private String supplierCode;

	/**
	 * 加工商名称
	 */
	private String supplierName;

	/**
	 * 加工商地址
	 */
	private String supplierAddress;

	/**
	 * 联系人
	 */
	private String linkName;

	/**
	 * 联系人手机
	 */
	private String mobile;

	/**
	 * 发外员id
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
	 * 兑换汇率(从ExchangeRate 表获取)
	 */
	@Column(length = 20)
	private String rate;

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
	
	/**
	 * 代工平台id
	 */
	@Column(length = 20)
	private String originCompanyId;

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}

	public String getSupplierName()
	{
		return supplierName;
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

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getSupplierCode()
	{
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode)
	{
		this.supplierCode = supplierCode;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
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

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
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
	
	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

	// ----------------------------------------------------------------
	public String getEmployeeName()
	{

		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";

	}

}
