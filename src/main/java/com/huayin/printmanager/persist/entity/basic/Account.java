/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.AccountType;
import com.huayin.printmanager.persist.enumerate.BankType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CurrencyType;

/**
 * <pre>
 * 基础设置 - 账号信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_account")
public class Account extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 账户金额
	 */
	private BigDecimal money;

	/**
	 * 是否期初(默认:否)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isBegin = BoolValue.NO;

	/**
	 * 银行卡类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BankType bankType;

	/**
	 * 支行名称
	 */
	@Column(length = 50)
	private String branchName;

	/**
	 * 账户
	 */
	@Column(length = 20)
	private String bankNo;

	/**
	 * 币种
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 账号类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private AccountType accountType;

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
	 * 最近交易日期
	 */
	private Date lastTransTime;

	public BankType getBankType()
	{
		return bankType;
	}

	public void setBankType(BankType bankType)
	{
		this.bankType = bankType;
	}

	public String getBranchName()
	{
		return branchName;
	}

	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}

	public String getBankNo()
	{
		return bankNo;
	}

	public void setBankNo(String bankNo)
	{
		this.bankNo = bankNo;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public AccountType getAccountType()
	{
		return accountType;
	}

	public void setAccountType(AccountType accountType)
	{
		this.accountType = accountType;
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

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Date getLastTransTime()
	{
		return lastTransTime;
	}

	public void setLastTransTime(Date lastTransTime)
	{
		this.lastTransTime = lastTransTime;
	}

	public BoolValue getIsBegin()
	{
		return isBegin;
	}

	public void setIsBegin(BoolValue isBegin)
	{
		this.isBegin = isBegin;
	}

	public String getBankTypeText()
	{
		if (bankType != null)
		{
			return bankType.getText();
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

	public String getCurrencyTypeText()
	{
		if (currencyType != null)
		{
			return currencyType.getText();
		}
		return "-";
	}

	public String getAccountTypeText()
	{
		if (accountType != null)
		{
			return accountType.getText();
		}
		return "-";
	}
}
