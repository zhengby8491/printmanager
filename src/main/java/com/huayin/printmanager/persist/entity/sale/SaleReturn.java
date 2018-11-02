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
 * 销售管理 - 销售退货：销售退货表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 上午9:55:56	
 * @version 	   2.0, 2018年2月22日下午6:45:39, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_return")
public class SaleReturn extends SaleBaseEntity
{

	private static final long serialVersionUID = 1L;

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
	private List<SaleReturnDetail> detailList=new ArrayList<SaleReturnDetail>();

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
	
	public List<SaleReturnDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<SaleReturnDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getReturnTypeText()
	{
		if(returnType!=null)
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
