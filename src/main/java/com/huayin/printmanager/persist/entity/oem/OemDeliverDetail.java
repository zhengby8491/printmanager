/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月13日 下午6:19:11
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 代工管理  - 代工送货单：明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月13日下午6:19:11, zhengby
 */
@Entity
@Table(name = "oem_deliver_detail")
public class OemDeliverDetail extends OemBaseEntityDetailSub
{
	private static final long serialVersionUID = -8983324891821063365L;

	/**
	 * 退货数量
	 */
	private BigDecimal returnQty = new BigDecimal(0);

	/**
	 * 退货金额
	 */
	private BigDecimal returnMoney = new BigDecimal(0);

	/**
	 * 对帐数量
	 */
	private BigDecimal reconcilQty = new BigDecimal(0);


	/**
	 * 对账金额
	 */
	private BigDecimal reconcilMoney = new BigDecimal(0);

	/**
	 * 送货单主表
	 */
	@Transient
	private OemDeliver master;

	public BigDecimal getReturnQty()
	{
		return returnQty;
	}

	public void setReturnQty(BigDecimal returnQty)
	{
		this.returnQty = returnQty;
	}

	public BigDecimal getReturnMoney()
	{
		return returnMoney;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public void setReturnMoney(BigDecimal returnMoney)
	{
		this.returnMoney = returnMoney;
	}

	public BigDecimal getReconcilMoney()
	{
		return reconcilMoney;
	}

	public void setReconcilMoney(BigDecimal reconcilMoney)
	{
		this.reconcilMoney = reconcilMoney;
	}

	public OemDeliver getMaster()
	{
		return master;
	}

	public void setMaster(OemDeliver master)
	{
		this.master = master;
	}

}
