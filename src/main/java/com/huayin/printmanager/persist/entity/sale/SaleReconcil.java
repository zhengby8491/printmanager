/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 销售管理 - 销售对账：销售对账表
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月24日 上午10:44:11
 * @version 	   2.0, 2018年2月22日下午6:27:01, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_reconcil")
public class SaleReconcil extends SaleBaseEntity
{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 对账日期
	 */
	private Date reconcilTime;
	
	/**
	 * 付款单位
	 */
	private Long customerPayerId;
	
	/**
	 * 对账明细
	 */
	@Transient
	private List<SaleReconcilDetail> detailList = new ArrayList<SaleReconcilDetail>();

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

	public List<SaleReconcilDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<SaleReconcilDetail> detailList)
	{
		this.detailList = detailList;
	}
 
}
