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
import java.util.List;

/**
 * <pre>
 * 微信 - 发外进度VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class OutSourceScheduleVo
{
	private Long id;

	private String workBillNo;

	/**
	 * 部件名称
	 */
	private String partName;

	/**
	 * 工序名称 
	 */
	private String procedureName;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 发外单号
	 */
	private String billNo;

	/**
	 * 加工商名称
	 */
	private String supplierName;

	/**
	 * 成品名称集合
	 */
	private List<String> name;

	/**
	 * 规格
	 */
	private String style;

	/**
	 * 加工数量
	 */
	private BigDecimal qty;

	/**
	 * 加工金额
	 */
	private BigDecimal money;

	/**
	 * 到货数量
	 */
	private BigDecimal arriveQty;

	/**
	 * 对账数量
	 */
	private BigDecimal reconcilQty;

	/**
	 * 付款金额
	 */
	private BigDecimal paymentMoney;

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

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
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

	public List<String> getName()
	{
		return name;
	}

	public void setName(List<String> name)
	{
		this.name = name;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getArriveQty()
	{
		return arriveQty;
	}

	public void setArriveQty(BigDecimal arriveQty)
	{
		this.arriveQty = arriveQty;
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

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public OutSourceScheduleVo(Long id, String workBillNo, String partName, String unitName, Date deliveryTime, String billNo, String supplierName, List<String> name, String style, BigDecimal qty, BigDecimal money, BigDecimal arriveQty, BigDecimal reconcilQty, BigDecimal paymentMoney)
	{
		super();
		this.id = id;
		this.workBillNo = workBillNo;
		this.partName = partName;
		this.unitName = unitName;
		this.deliveryTime = deliveryTime;
		this.billNo = billNo;
		this.supplierName = supplierName;
		this.name = name;
		this.style = style;
		this.qty = qty;
		this.money = money;
		this.arriveQty = arriveQty;
		this.reconcilQty = reconcilQty;
		this.paymentMoney = paymentMoney;
	}

	public OutSourceScheduleVo()
	{
		super();
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

}
