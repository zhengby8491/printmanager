/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月13日 下午6:19:32
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

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工送货单：主表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月13日下午6:19:32, zhengby
 */
@Entity
@Table(name = "oem_deliver")
public class OemDeliver extends OemBaseEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9194300398135830020L;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 送货日期
	 */
	private Date deliveryTime;
	
	/**
	 * 代工送货明细表
	 */
	@Transient
	private List<OemDeliverDetail> detailList;

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public String getDeliverClassName()
	{
		if (null != deliveryClassId)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}
	
	public List<OemDeliverDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OemDeliverDetail> detailList)
	{
		this.detailList = detailList;
	}
}
