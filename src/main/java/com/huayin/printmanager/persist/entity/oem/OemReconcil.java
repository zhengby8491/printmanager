/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月16日 上午9:09:53
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 代工管理  - 代工对账单：主表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月16日上午9:09:53, zhengby
 */
@Entity
@Table(name = "oem_reconcil")
public class OemReconcil extends OemBaseEntity
{
	private static final long serialVersionUID = 2875119250294110444L;
	
	/**
	 * 对账日期
	 */
	private Date reconcilTime;
	
	/**
	 * 付款单位
	 */
	private Long customerPayerId;
	
	@Transient
	private List<OemReconcilDetail> detailList;

	public Date getReconcilTime()
	{
		return reconcilTime;
	}

	public void setReconcilTime(Date reconcilTime)
	{
		this.reconcilTime = reconcilTime;
	}

	public Long getCustomerPayerId()
	{
		return customerPayerId;
	}

	public void setCustomerPayerId(Long customerPayerId)
	{
		this.customerPayerId = customerPayerId;
	}

	public List<OemReconcilDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OemReconcilDetail> detailList)
	{
		this.detailList = detailList;
	}
}
