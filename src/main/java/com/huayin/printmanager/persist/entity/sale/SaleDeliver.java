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

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售送货：销售送货表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 上午9:25:17, zhong
 * @version 	   2.0, 2018年2月22日下午6:01:51, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_deliver")
public class SaleDeliver extends SaleBaseEntity
{

	private static final long serialVersionUID = 1L;
	

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 送货日期
	 */
	private Date deliveryTime;
	
	/**
	 * 物流信息
	 */
	private String logisticsInfo;
	
	/**
	 * 送货详情列表
	 */
	@Transient
	private List<SaleDeliverDetail> detailList=new ArrayList<SaleDeliverDetail>();
	
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

	public String getLogisticsInfo()
	{
		return logisticsInfo;
	}

	public void setLogisticsInfo(String logisticsInfo)
	{
		this.logisticsInfo = logisticsInfo;
	}

	public List<SaleDeliverDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<SaleDeliverDetail> detailList)
	{
		this.detailList = detailList;
	}
	
	public String getDeliveryClassName()
	{
		if(deliveryClassId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId,"name");
		}
		return "-";
	}

}
