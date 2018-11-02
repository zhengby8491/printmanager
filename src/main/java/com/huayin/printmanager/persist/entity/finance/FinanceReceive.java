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

import com.huayin.printmanager.persist.enumerate.ReceiveType;

/**
 * <pre>
 * 财务管理 - 收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_receive")
public class FinanceReceive extends FinanceBaseEntity
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
	 * 收款类型（收款、预收款）
	 */
	private ReceiveType receiveType;

	@Transient
	List<FinanceReceiveDetail> detailList = new ArrayList<FinanceReceiveDetail>();

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

	public List<FinanceReceiveDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<FinanceReceiveDetail> detailList)
	{
		this.detailList = detailList;
	}

	public ReceiveType getReceiveType()
	{
		return receiveType;
	}

	public void setReceiveType(ReceiveType receiveType)
	{
		this.receiveType = receiveType;
	}

}
