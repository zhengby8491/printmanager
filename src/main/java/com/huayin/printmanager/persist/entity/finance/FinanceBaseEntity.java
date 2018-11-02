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

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 -  财务主表基类
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class FinanceBaseEntity extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 账户ID
	 */
	private Long accountId;

	/**
	 * 金额
	 */
	private BigDecimal money;

	/**
	 * 折扣金额
	 */
	private BigDecimal discount;

	/**
	 * 预收/付款
	 */
	private BigDecimal advance;

	/**
	 * 结算方式
	 */
	private Long settlementClassId;

	/**
	 * 收/付款日期
	 */
	private Date billTime;

	/**
	 * 收/付款人
	 */
	private Long employeeId;

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public BigDecimal getAdvance()
	{
		return advance;
	}

	public void setAdvance(BigDecimal advance)
	{
		this.advance = advance;
	}

	public Long getSettlementClassId()
	{
		return settlementClassId;
	}

	public void setSettlementClassId(Long settlementClassId)
	{
		this.settlementClassId = settlementClassId;
	}

	public Date getBillTime()
	{
		return billTime;
	}

	public void setBillTime(Date billTime)
	{
		this.billTime = billTime;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public Long getAccountId()
	{
		return accountId;
	}

	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}
	// --------------------------------------------------------

	public String getEmployeeName()
	{
		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}

	public String getAccountBankNo()
	{
		if (accountId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.ACCOUNT.name(), accountId, "bankNo");
		}
		return "-";
	}
	
	public String getCurrencyType()
	{
		if (accountId != null)
		{
			CurrencyType currencyType = (CurrencyType)UserUtils.getBasicInfoFiledValue(BasicType.ACCOUNT.name(), accountId, "currencyType");
			return currencyType.getText();
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
