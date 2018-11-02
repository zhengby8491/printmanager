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

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外到货:发外到货表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, liudong
 * @version 	   2.0, 2018年2月23日下午3:57:06, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_arrive")
public class OutSourceArrive extends OutSourceBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 到货日期
	 */
	private Date deliveryTime;

	@Transient
	List<OutSourceArriveDetail> detailList = new ArrayList<OutSourceArriveDetail>();

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

	public List<OutSourceArriveDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OutSourceArriveDetail> detailList)
	{
		this.detailList = detailList;
	}

	// ------------------------------------------------------------------

	public String getDeliveryClassName()
	{
		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}

}
