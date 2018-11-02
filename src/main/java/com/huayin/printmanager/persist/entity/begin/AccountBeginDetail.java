package com.huayin.printmanager.persist.entity.begin;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.CurrencyType;

/**
 * <pre>
 * 账户基础详情表
 * </pre>
 * @author raintear
 * @version 1.0, 2016年8月11日
 */
@Entity
@Table(name = "basic_accountBegin_detail")
public class AccountBeginDetail extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 对应账户ID
	 */
	private Long accountId;

	/**
	 * 期初金额
	 */
	private BigDecimal beginMoney;

	/**
	 * 账户
	 */
	@Column(length = 20)
	private String bankNo;
	
	/**
	 * 支行名称
	 */
	@Column(length = 50)
	private String branchName;
	
	/**
	 * 币种
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;
	
	@Transient
	private AccountBegin master;

	public Long getAccountId()
	{
		return accountId;
	}

	public void setAccountId(Long accountId)
	{
		this.accountId = accountId;
	}

	public BigDecimal getBeginMoney()
	{
		return beginMoney;
	}

	public void setBeginMoney(BigDecimal beginMoney)
	{
		this.beginMoney = beginMoney;
	}

	public String getBankNo()
	{
		return bankNo;
	}

	public void setBankNo(String bankNo)
	{
		this.bankNo = bankNo;
	}

	public String getBranchName()
	{
		return branchName;
	}

	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}

	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	}

	public AccountBegin getMaster()
	{
		return master;
	}

	public void setMaster(AccountBegin master)
	{
		this.master = master;
	}

	public String getCurrencyTypeText(){
		if (currencyType!=null)
		{
			return currencyType.getText();
		}
		return "-";
	}
}
