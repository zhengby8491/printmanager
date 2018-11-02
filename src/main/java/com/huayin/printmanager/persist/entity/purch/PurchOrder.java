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
 * 采购管理 - 采购订单：采购订单表
 * </pre>
 * @author       zhengby
 *  @version 	   1.0, 2016年5月24日 下午3:24:44, zhong
 * @version 	   2.0, 2018年2月23日上午11:22:03, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_order")
public class PurchOrder extends PurchBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 交货日期
	 */
	private Date purchTime;

	/**
	 * 明细表
	 */
	@Transient
	List<PurchOrderDetail> detailList = new ArrayList<PurchOrderDetail>();

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public Date getPurchTime()
	{
		return purchTime;
	}

	public void setPurchTime(Date purchTime)
	{
		this.purchTime = purchTime;
	}

	public List<PurchOrderDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<PurchOrderDetail> detailList)
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
