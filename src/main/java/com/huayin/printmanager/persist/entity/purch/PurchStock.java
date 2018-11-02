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
 * 采购管理 - 采购入库： 采购入库表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月8日, liudong
 * @version 	   2.0, 2018年2月23日上午11:39:24, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_stock")
public class PurchStock extends PurchBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 送货方式Id
	 */
	private Long deliveryClassId;

	/**
	 * 入库日期
	 */
	private Date storageTime;

	/**
	 * 明细表
	 */
	@Transient
	List<PurchStockDetail> detailList = new ArrayList<PurchStockDetail>();

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public Date getStorageTime()
	{
		return storageTime;
	}

	public void setStorageTime(Date storageTime)
	{
		this.storageTime = storageTime;
	}

	public List<PurchStockDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<PurchStockDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getDeliveryClassName()
	{
		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}
}
