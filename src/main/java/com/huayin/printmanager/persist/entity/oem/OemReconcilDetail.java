/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月16日 上午9:10:05
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

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
 * 代工管理  - 代工对账单：明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月16日上午9:10:05, zhengby
 */
@Entity
@Table(name = "oem_reconcil_detail")
public class OemReconcilDetail extends OemBaseEntityDetailSub
{
	private static final long serialVersionUID = -8160547211796464173L;

	/**
	 * 收款金额
	 */
	private BigDecimal receiveMoney=new BigDecimal(0);
	
	/**
	 * 是否已完成收款
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isReceiveOver=BoolValue.NO;
	
	/**
	 * 送/退货日期
	 */
	private Date deliveryTime;
	
	@Transient
	private OemReconcil master;

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public BoolValue getIsReceiveOver()
	{
		return isReceiveOver;
	}

	public void setIsReceiveOver(BoolValue isReceiveOver)
	{
		this.isReceiveOver = isReceiveOver;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public OemReconcil getMaster()
	{
		return master;
	}

	public void setMaster(OemReconcil master)
	{
		this.master = master;
	}
}
