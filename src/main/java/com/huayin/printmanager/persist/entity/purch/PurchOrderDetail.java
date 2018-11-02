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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 采购管理 - 采购订单：采购订单明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 下午2:33:38, zhong
 * @version 	   2.0, 2018年2月23日上午11:20:43, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_order_detail")
public class PurchOrderDetail extends PurchDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 已入库数量
	 */
	private BigDecimal storageQty = new BigDecimal(0);

	/**
	 * 入库日期
	 */
	private Date storageTime;

	/**
	 * 采购订单主表
	 */
	@Transient
	private PurchOrder master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();
	

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public BigDecimal getStorageQty()
	{
		return storageQty;
	}

	public void setStorageQty(BigDecimal storageQty)
	{
		this.storageQty = storageQty;
	}

	public Date getStorageTime()
	{
		return storageTime;
	}

	public void setStorageTime(Date storageTime)
	{
		this.storageTime = storageTime;
	}

	public PurchOrder getMaster()
	{
		return master;
	}

	public void setMaster(PurchOrder master)
	{
		this.master = master;
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		else
		{
			sb.append("-");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
