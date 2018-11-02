/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月14日 下午6:50:55
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
 * 代工管理  - 代工退货单：明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月14日下午6:50:55, zhengby
 */
@Entity
@Table(name = "oem_return_detail")
public class OemReturnDetail extends OemBaseEntityDetailSub
{

	private static final long serialVersionUID = -2827380838500586221L;

	/**
	 * 已对帐数量
	 */
	private BigDecimal reconcilQty=new BigDecimal(0);
	
	/**
	 * 已对账金额
	 */
	private BigDecimal reconcilMoney=new BigDecimal(0);

	@Transient
	private OemReturn master;

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getReconcilMoney()
	{
		return reconcilMoney;
	}

	public void setReconcilMoney(BigDecimal reconcilMoney)
	{
		this.reconcilMoney = reconcilMoney;
	}

	public OemReturn getMaster()
	{
		return master;
	}

	public void setMaster(OemReturn master)
	{
		this.master = master;
	}
}
