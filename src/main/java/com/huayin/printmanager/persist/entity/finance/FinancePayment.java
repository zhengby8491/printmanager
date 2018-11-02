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

import com.huayin.printmanager.persist.enumerate.PaymentType;

/**
 * <pre>
 * 财务管理 - 付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_payment")
public class FinancePayment extends FinanceBaseEntity
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
	 * 付款类型（付款、预付款）
	 */
	private PaymentType paymentType;

	@Transient
	List<FinancePaymentDetail> detailList = new ArrayList<FinancePaymentDetail>();

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

	public List<FinancePaymentDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<FinancePaymentDetail> detailList)
	{
		this.detailList = detailList;
	}

	public PaymentType getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType)
	{
		this.paymentType = paymentType;
	}

}
