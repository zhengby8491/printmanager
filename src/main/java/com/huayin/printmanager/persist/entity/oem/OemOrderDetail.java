/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月6日 下午4:52:05
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
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 代工管理  - 代工订单 :明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月6日下午4:52:05, zhengby
 */
@Entity
@Table(name = "oem_order_detail")
public class OemOrderDetail extends OemBaseEntityDetail
{
	private static final long serialVersionUID = 7256238981801550463L;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 已对账数量,临时返回属性，不需要生成字段
	 */
	@Transient
	private BigDecimal reconcilQty;

	/**
	 * 已收款金额,临时返回属性，不需要生成字段
	 */
	@Transient
	private BigDecimal receiveMoney;

	/**
	 * 生产数量,临时返回属性，不需要生成字段
	 */
	private Integer produceNum;

	/**
	 * 已送货数量
	 */
	private BigDecimal deliverQty = new BigDecimal(0);

	/**
	 * 已送货审核状态
	 */
	@Transient
	private Boolean deliverCheck;

	/**
	 * 已对账审核状态
	 */
	@Transient
	private Boolean reconcilCheck;

	/**
	 * 已收款审核状态
	 */
	@Transient
	private Boolean receiveCheck;

	/**
	 * 已送货金额
	 */
	@Column(columnDefinition = "decimal(18,2) default '0.00'")
	private BigDecimal deliverMoney = new BigDecimal(0);

	/**
	 * 主表
	 */
	@Transient
	private OemOrder master;

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
	}

	public Integer getProduceNum()
	{
		return produceNum;
	}

	public void setProduceNum(Integer produceNum)
	{
		this.produceNum = produceNum;
	}

	public BigDecimal getDeliverQty()
	{
		return deliverQty;
	}

	public void setDeliverQty(BigDecimal deliverQty)
	{
		this.deliverQty = deliverQty;
	}

	public Boolean getDeliverCheck()
	{
		return deliverCheck;
	}

	public void setDeliverCheck(Boolean deliverCheck)
	{
		this.deliverCheck = deliverCheck;
	}

	public Boolean getReconcilCheck()
	{
		return reconcilCheck;
	}

	public void setReconcilCheck(Boolean reconcilCheck)
	{
		this.reconcilCheck = reconcilCheck;
	}

	public Boolean getReceiveCheck()
	{
		return receiveCheck;
	}

	public void setReceiveCheck(Boolean receiveCheck)
	{
		this.receiveCheck = receiveCheck;
	}

	public BigDecimal getDeliverMoney()
	{
		return deliverMoney;
	}

	public void setDeliverMoney(BigDecimal deliverMoney)
	{
		this.deliverMoney = deliverMoney;
	}

	public OemOrder getMaster()
	{
		return master;
	}

	public void setMaster(OemOrder master)
	{
		this.master = master;
	}

}
