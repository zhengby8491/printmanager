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
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外退货：发外退货表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, liudong
 * @version 	   2.0, 2018年2月23日下午4:08:07, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_return")
public class OutSourceReturn extends OutSourceBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 送货方式
	 */
	private Long deliveryClassId;

	/**
	 * 退货类型
	 */
	private ReturnGoodsType returnGoodsType;

	@Transient
	List<OutSourceReturnDetail> detailList = new ArrayList<OutSourceReturnDetail>();

	public ReturnGoodsType getReturnGoodsType()
	{
		return returnGoodsType;
	}

	public void setReturnGoodsType(ReturnGoodsType returnGoodsType)
	{
		this.returnGoodsType = returnGoodsType;
	}

	public List<OutSourceReturnDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<OutSourceReturnDetail> detailList)
	{
		this.detailList = detailList;
	}

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	// ------------------------------------------------------

	public String getDeliveryClassName()
	{

		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}

	public String getReturnGoodsTypeText()
	{

		if (returnGoodsType != null)
		{
			return returnGoodsType.getText();
		}
		return "-";
	}

}
