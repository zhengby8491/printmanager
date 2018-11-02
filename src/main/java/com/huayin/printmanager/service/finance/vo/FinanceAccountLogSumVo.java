/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 财务管理 - 账户资金汇总VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class FinanceAccountLogSumVo
{
	/**
	 * 账户号
	 */
	private String bankNo;

	/**
	 * 账户收入
	 */
	private BigDecimal inTransMoney;

	/**
	 * 账户支出
	 */
	private BigDecimal outTransMoney;

	/**
	 * 期初金额
	 */
	private BigDecimal beginMoney;

	public String getBankNo()
	{
		return bankNo;
	}

	public void setBankNo(String bankNo)
	{
		this.bankNo = bankNo;
	}

	public BigDecimal getInTransMoney()
	{
		return inTransMoney;
	}

	public void setInTransMoney(BigDecimal inTransMoney)
	{
		this.inTransMoney = inTransMoney;
	}

	public BigDecimal getOutTransMoney()
	{
		return outTransMoney;
	}

	public void setOutTransMoney(BigDecimal outTransMoney)
	{
		this.outTransMoney = outTransMoney;
	}

	public BigDecimal getBeginMoney()
	{
		return beginMoney;
	}

	public void setBeginMoney(BigDecimal beginMoney)
	{
		this.beginMoney = beginMoney;
	}

}
