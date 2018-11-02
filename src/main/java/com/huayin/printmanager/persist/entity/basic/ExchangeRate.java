package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.CurrencyType;

/**
 * 汇率表
 * @ClassName: ExchangeRate
 * @author zhong
 * @date 2016年5月25日 下午7:07:16
 */
@Entity
@Table(name = "basic_exchangeRate")
public class ExchangeRate extends BaseBasicTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 兑换币别
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CurrencyType currencyType;

	/**
	 * 本位币别
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CurrencyType standardCurrencyType;

	/**
	 * 兑换汇率
	 */
	@Column(length = 20)
	private BigDecimal rate;
	
	public CurrencyType getCurrencyType()
	{
		return currencyType;
	}

	public void setCurrencyType(CurrencyType currencyType)
	{
		this.currencyType = currencyType;
	} 
	
	public CurrencyType getStandardCurrencyType()
	{
		return standardCurrencyType;
	}

	public void setStandardCurrencyType(CurrencyType standardCurrencyType)
	{
		this.standardCurrencyType = standardCurrencyType;
	}

	public BigDecimal getRate()
	{
		return rate;
	}

	public void setRate(BigDecimal rate)
	{
		this.rate = rate;
	}
	
	public String getCurrencyTypeText(){
		if (currencyType!=null)
		{
			return currencyType.getText();
		}
		return "-";
	}
	
	public String getStandardCurrencyTypeText(){
		if (standardCurrencyType!=null)
		{
			return standardCurrencyType.getText();
		}
		return "-";
	}
}
