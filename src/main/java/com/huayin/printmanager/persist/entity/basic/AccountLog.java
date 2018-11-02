/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.AccountTransType;
import com.huayin.printmanager.persist.enumerate.FinanceTradeMode;

/**
 * <pre>
 * 财务管理 - 资金帐户流水信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_accountlog")
public class AccountLog extends BaseTableIdEntity
{
	private static final long serialVersionUID = -7359669634271106954L;

	/**
	 * 账户ID
	 */
	private Long accountId;

	/**
	 * 引用交易的外部流水号
	 */
	@Column(length = 50)
	private String referenceId;

	/**
	 * 交易后金额
	 */
	private BigDecimal remnantMoney;

	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private FinanceTradeMode tradeMode;

	/**
	 * 交易金额
	 */
	private BigDecimal transMoney;

	/**
	 * 交易时间
	 */
	private Date transTime;

	/**
	 * 交易类型
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private AccountTransType transType;

	@Transient
	private Account account;

	public String getReferenceId()
	{
		return referenceId;
	}

	public void setReferenceId(String referenceId)
	{
		this.referenceId = referenceId;
	}

	public Date getTransTime()
	{
		return transTime;
	}

	public void setTransTime(Date transTime)
	{
		this.transTime = transTime;
	}

	public AccountTransType getTransType()
	{
		return transType;
	}

	public void setTransType(AccountTransType transType)
	{
		this.transType = transType;
	}

	public Long getAccountId()
	{
		return accountId;
	}

	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}

	public BigDecimal getRemnantMoney()
	{
		return remnantMoney;
	}

	public void setRemnantMoney(BigDecimal remnantMoney)
	{
		this.remnantMoney = remnantMoney;
	}

	public FinanceTradeMode getTradeMode()
	{
		return tradeMode;
	}

	public void setTradeMode(FinanceTradeMode tradeMode)
	{
		this.tradeMode = tradeMode;
	}

	public BigDecimal getTransMoney()
	{
		return transMoney;
	}

	public void setTransMoney(BigDecimal transMoney)
	{
		this.transMoney = transMoney;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public String getTradeModeText()
	{
		if (tradeMode != null)
		{
			return tradeMode.getText();
		}
		return "-";
	}

	public String getTransTypeText()
	{
		if (transType != null)
		{
			return transType.getText();
		}
		return "-";
	}

}