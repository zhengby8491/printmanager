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
 * 财务管理 - 预收款日志
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_receive_advancelog")
public class FinanceReceiveAdvanceLog extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 客户Id
	 */
	private Long customerId;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 交易模式（收款单保存：加钱；收款单作废并审核通过：减钱）
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private FinanceTradeMode tradeMode;

	/**
	 * 预收款
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
	 * 收款人
	 */
	private Long employeeId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
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
