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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 采购管理 - 采购对账 ： 采购对账明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年5月24日 下午3:23:38, zhong
 * @version 	   2.0, 2018年2月23日上午11:36:33, zhengby, 代码规范
 */
@Entity
@Table(name = "purch_reconcil_detail")
public class PurchReconcilDetail extends PurchDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 付款金额
	 */
	private BigDecimal paymentMoney = new BigDecimal(0);

	/**
	 * 是否已完成付款
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isPaymentOver;

	/**
	 * 采购单号
	 */
	private String orderBillNo;

	/**
	 * 生产工单单号
	 */
	private String workBillNo;

	/**
	 * 生产工单id
	 */
	private Long workId;

	/**
	 * 出/入库日期
	 */
	private Date deliveryTime;

	@Transient
	private PurchReconcil master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

	public BoolValue getIsPaymentOver()
	{
		return isPaymentOver;
	}

	public void setIsPaymentOver(BoolValue isPaymentOver)
	{
		this.isPaymentOver = isPaymentOver;
	}

	public String getOrderBillNo()
	{
		return orderBillNo;
	}

	public void setOrderBillNo(String orderBillNo)
	{
		this.orderBillNo = orderBillNo;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public PurchReconcil getMaster()
	{
		return master;
	}

	public void setMaster(PurchReconcil master)
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
