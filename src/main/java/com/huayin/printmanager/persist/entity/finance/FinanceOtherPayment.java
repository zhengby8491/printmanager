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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 财务管理 - 其他付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_otherpayment")
public class FinanceOtherPayment extends FinanceBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 收款单位
	 */
	private String recCompany;

	@Transient
	List<FinanceOtherPaymentDetail> detailList = new ArrayList<>();

	public List<FinanceOtherPaymentDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<FinanceOtherPaymentDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getRecCompany()
	{
		return recCompany;
	}

	public void setRecCompany(String recCompany)
	{
		this.recCompany = recCompany;
	}

}
