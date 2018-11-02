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
 * 发外管理 - 发外对账:发外对账明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日 下午17:15:38, zhaojitao
 * @version 	   2.0, 2018年2月23日下午4:07:13, zhengby, 代码规范
 */
@Entity
@Table(name = "outsource_reconcil_detail")
public class OutSourceReconcilDetail extends OutSourceDetailBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 已付款金额
	 */
	private BigDecimal paymentMoney = new BigDecimal(0);

	/**
	 * 是否已完成付款
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isPaymentOver = BoolValue.NO;

	/**
	 * 加工单号
	 */
	private String outSourceBillNo;

	/**
	 * 送/退货日期
	 */
	private Date deliveryTime;

	/**
	 * 主表对象
	 */
	@Transient
	private OutSourceReconcil master;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

	/**
	 * 产品名称字符串
	 */
	@Transient
	private String productNames;

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

	public String getOutSourceBillNo()
	{
		return outSourceBillNo;
	}

	public void setOutSourceBillNo(String outSourceBillNo)
	{
		this.outSourceBillNo = outSourceBillNo;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public OutSourceReconcil getMaster()
	{
		return master;
	}

	public void setMaster(OutSourceReconcil master)
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

	public void setProductNames(String productNames)
	{
		this.productNames = productNames;
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
