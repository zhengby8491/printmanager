/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.purch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购对账 ： 采购对账表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 下午3:24:14, zhong
 * @version 	   2.0, 2018年2月23日上午11:35:04, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_reconcil")
public class PurchReconcil extends PurchBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 对账日期
	 */
	private Date reconcilTime;

	/**
	 * 付款单位(公司)
	 */
	private Long supplierPayerId;	
	
	@Transient
	private List<PurchReconcilDetail> detailList=new ArrayList<PurchReconcilDetail>();

	public Date getReconcilTime()
	{
		return reconcilTime;
	}

	public void setReconcilTime(Date reconcilTime)
	{
		this.reconcilTime = reconcilTime;
	}

	public Long getSupplierPayerId()
	{
		return supplierPayerId;
	}

	public void setSupplierPayerId(Long supplierPayerId)
	{
		this.supplierPayerId = supplierPayerId;
	}

	
	public List<PurchReconcilDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<PurchReconcilDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getSupplierPayerName()
	{
		if(supplierPayerId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.SUPPLIER.name(), supplierPayerId,"name");
		}
		return "-";
	}
}
