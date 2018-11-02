/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月6日 下午4:50:54
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusSale;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工订单：主表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月6日下午4:50:54, zhengby
 */
@Entity
@Table(name = "oem_order")
public class OemOrder extends OemBaseEntity
{
	private static final long serialVersionUID = 2759969912630609845L;

	/**
	 * 送货方式
	 */
	@Column(length = 50)
	private Long deliveryClassId;
	
	/**
	 * 进度状态
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProgressStatusSale progressStatus;
	/**
	 * 代工详情列表
	 */
	@Transient
	List<OemOrderDetail> detailList = new ArrayList<OemOrderDetail>();

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public ProgressStatusSale getProgressStatus()
	{
		return progressStatus;
	}

	public void setProgressStatus(ProgressStatusSale progressStatus)
	{
		this.progressStatus = progressStatus;
	}

	public String getDeliverClassName()
	{
		if (null != deliveryClassId)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}

	public List<OemOrderDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OemOrderDetail> detailList)
	{
		this.detailList = detailList;
	}

}
