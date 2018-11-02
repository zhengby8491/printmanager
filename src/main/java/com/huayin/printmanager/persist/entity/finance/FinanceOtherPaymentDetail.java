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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 财务管理 - 其他付款单明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_otherpayment_detail")
public class FinanceOtherPaymentDetail extends FinanceDetailBaseEntity
{
	private static final long serialVersionUID = 6969029484205744846L;

	/**
	 * 摘要
	 */
	private String summary;

	@Transient
	private FinanceOtherPayment master;
	// 备注已存在继承的父类里 memo
	// 付款金额已存在继承的父类里 money

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public FinanceOtherPayment getMaster()
	{
		return master;
	}

	public void setMaster(FinanceOtherPayment master)
	{
		this.master = master;
	}

}
