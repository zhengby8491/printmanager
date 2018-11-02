/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <pre>
 * 微信 - 采购进度VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class PurchScheduleVo
{
	private Long id;

	/**
	 * 采购单号
	 */
	private String billNo;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String style;

	/**
	 * 单位
	 */
	private String unit;

	/**
	 * 采购数量
	 */
	private BigDecimal purchQty = new BigDecimal(0);

	/**
	 * 采购金额
	 */
	private BigDecimal purchMoney = new BigDecimal(0);

	/**
	 * 入库数量
	 */
	private BigDecimal stockQty = new BigDecimal(0);

	/**
	 * 对账数量
	 */
	private BigDecimal reconcilQty = new BigDecimal(0);

	/**
	 * 付款金额
	 */
	private BigDecimal paymentMoney = new BigDecimal(0);

	/**
	 * 进度状态
	 */
	private String scheduleState;

	/**
	 * 进度百分百
	 */
	private Integer schedulePercent;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public String getScheduleState()
	{
		return scheduleState;
	}

	public void setScheduleState(String scheduleState)
	{
		this.scheduleState = scheduleState;
	}

	public Integer getSchedulePercent()
	{
		return schedulePercent;
	}

	public void setSchedulePercent(Integer schedulePercent)
	{
		this.schedulePercent = schedulePercent;
	}

	public BigDecimal getPurchMoney()
	{
		return purchMoney;
	}

	public void setPurchMoney(BigDecimal purchMoney)
	{
		this.purchMoney = purchMoney;
	}

	public PurchScheduleVo(Long id, String billNo, String supplierName, String materialName, String style, String unit, BigDecimal purchQty, BigDecimal purchMoney, BigDecimal stockQty, BigDecimal reconcilQty, BigDecimal paymentMoney)
	{
		super();
		this.id = id;
		this.billNo = billNo;
		this.supplierName = supplierName;
		this.materialName = materialName;
		this.style = style;
		this.unit = unit;
		this.purchQty = purchQty;
		this.purchMoney = purchMoney;
		this.stockQty = stockQty;
		this.reconcilQty = reconcilQty;
		this.paymentMoney = paymentMoney;
	}

	public PurchScheduleVo()
	{
		super();
	}

}
