/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月14日 下午6:50:40
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
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工退货单：主表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月14日下午6:50:40, zhengby
 */
@Entity
@Table(name = "oem_return")
public class OemReturn extends OemBaseEntity
{
	private static final long serialVersionUID = -5640478893709401427L;

	/**
	 * 退货类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ReturnGoodsType returnType;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 物流信息
	 */
	private String logisticsInfo;

	/**
	 * 退货详情列表
	 */
	@Transient
	private List<OemReturnDetail> detailList = new ArrayList<OemReturnDetail>();

	public ReturnGoodsType getReturnType()
	{
		return returnType;
	}

	public void setReturnType(ReturnGoodsType returnType)
	{
		this.returnType = returnType;
	}

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public String getLogisticsInfo()
	{
		return logisticsInfo;
	}

	public void setLogisticsInfo(String logisticsInfo)
	{
		this.logisticsInfo = logisticsInfo;
	}

	public List<OemReturnDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OemReturnDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getReturnTypeText()
	{
		if (returnType != null)
		{
			return returnType.getText();
		}
		return "-";
	}
	public String getDeliveryClassName()
	{
		if(deliveryClassId!=null){
			return (String)UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId,"name");
		}
		return "-";
	}
}
