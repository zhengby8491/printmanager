/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.finance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 预付款日志
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_payment_advancelog")
public class FinancePaymentAdvanceLog extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商Id
	 */
	private Long supplierId;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 交易模式（付款单保存：加钱；付款单作废并审核通过：减钱）
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private FinanceTradeMode tradeMode;

	/**
	 * 预付款
	 */
	private BigDecimal money;

	/**
	 * 单据类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType billType;

	/**
	 * 单据ID
	 */
	private Long billId;

	/**
	 * 付款人
	 */
	private Long employeeId;

	/**
	 * 创建时间
	 */
	private Date createTime;

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

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Long getBillId()
	{
		return billId;
	}

	public void setBillId(Long billId)
	{
		this.billId = billId;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public FinanceTradeMode getTradeMode()
	{
		return tradeMode;
	}

	public void setTradeMode(FinanceTradeMode tradeMode)
	{
		this.tradeMode = tradeMode;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public String getEmployeeName()
	{
		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}

	public String getBillTypeText()
	{
		if (billType != null)
		{
			return billType.getText();
		}
		return "-";
	}

	public String getTradeModeText()
	{
		if (tradeMode != null)
		{
			return tradeMode.getText();
		}
		return "-";
	}

}
