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

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 销售管理 - 销售对账：销售对账明细表
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年5月24日 上午10:44:11
 * @version 	   2.0, 2018年2月22日下午6:27:01, zhengby, 代码规范
 */
@Entity
@Table(name="sale_reconcil_detail")
public class SaleReconcilDetail extends SaleDetailBaseEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 收款金额
	 */
	private BigDecimal receiveMoney=new BigDecimal(0);
	
	/**
	 * 未收款金额，临时返回属性，不要生成字段
	 */
	@Transient
	private BigDecimal receiveMoney2=new BigDecimal(0);

	/**
	 * 是否已完成收款
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isReceiveOver=BoolValue.NO;
	
	/**
	 * 客户订单单据编号
	 */
	@Column(length = 50)
	private String customerBillNo;
	
	/**
	 * 送/退货日期
	 */
	private Date deliveryTime;
	
	/**
	 * 销售对账单主表
	 */
	@Transient
	private SaleReconcil master;
	
	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public SaleReconcil getMaster()
	{
		return master;
	}

	public void setMaster(SaleReconcil master)
	{
		this.master = master;
	}

	public BoolValue getIsReceiveOver()
	{
		return isReceiveOver;
	}

	public void setIsReceiveOver(BoolValue isReceiveOver)
	{
		this.isReceiveOver = isReceiveOver;
	}

	public BigDecimal getReceiveMoney2()
	{
		return receiveMoney2;
	}

	public void setReceiveMoney2(BigDecimal receiveMoney2)
	{
		this.receiveMoney2 = receiveMoney2;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}
}
