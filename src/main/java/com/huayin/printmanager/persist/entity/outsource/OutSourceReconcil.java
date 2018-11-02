/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 发外管理 - 发外对账:发外对账表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, liudong
 * @version 	   2.0, 2018年2月23日下午4:06:14, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_reconcil")
public class OutSourceReconcil extends OutSourceBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 对账日期
	 */
	private Date reconcilTime;

	/**
	 * 付款单位名称
	 */
	private String supplierPayer;

	@Transient
	List<OutSourceReconcilDetail> detailList = new ArrayList<OutSourceReconcilDetail>();

	public Date getReconcilTime()
	{
		return reconcilTime;
	}

	public void setReconcilTime(Date reconcilTime)
	{
		this.reconcilTime = reconcilTime;
	}

	public List<OutSourceReconcilDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OutSourceReconcilDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getSupplierPayer()
	{
		return supplierPayer;
	}

	public void setSupplierPayer(String supplierPayer)
	{
		this.supplierPayer = supplierPayer;
	}

}
