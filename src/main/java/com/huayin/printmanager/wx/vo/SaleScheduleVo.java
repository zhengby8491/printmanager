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
 * 微信 - 销售进度追踪VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SaleScheduleVo
{
	private Long id;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 销售单号
	 */
	private String billNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 成品名称
	 */
	private String productName;

	private String style;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 销售数量
	 */
	private Integer saleQty;

	/**
	 * 销售订单金额
	 */
	private BigDecimal saleMoney = new BigDecimal(0);

	/**
	 * 生产数量
	 */
	private Integer produceedQty;

	/**
	 * 送货数量
	 */
	private Integer deliverQty;

	/**
	 * 对账数量
	 */
	private Integer reconcilQty;

	/**
	 * 收款金额
	 */
	private BigDecimal receiveMoney;

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

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
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

	public Integer getSaleQty()
	{
		return saleQty;
	}

	public void setSaleQty(Integer saleQty)
	{
		this.saleQty = saleQty;
	}

	public Integer getProduceedQty()
	{
		return produceedQty;
	}

	public void setProduceedQty(Integer produceedQty)
	{
		this.produceedQty = produceedQty;
	}

	public Integer getDeliverQty()
	{
		return deliverQty;
	}

	public void setDeliverQty(Integer deliverQty)
	{
		this.deliverQty = deliverQty;
	}

	public Integer getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(Integer reconcilQty)
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

	public BigDecimal getSaleMoney()
	{
		return saleMoney;
	}

	public void setSaleMoney(BigDecimal saleMoney)
	{
		this.saleMoney = saleMoney;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public SaleScheduleVo(Long id, Date deliveryTime, String billNo, String customerName, String productName, String style, String unitName, Integer saleQty, BigDecimal saleMoney, Integer produceedQty, Integer deliverQty, Integer reconcilQty, BigDecimal receiveMoney)
	{
		super();
		this.id = id;
		this.deliveryTime = deliveryTime;
		this.billNo = billNo;
		this.customerName = customerName;
		this.productName = productName;
		this.style = style;
		this.unitName = unitName;
		this.saleQty = saleQty;
		this.saleMoney = saleMoney;
		this.produceedQty = produceedQty;
		this.deliverQty = deliverQty;
		this.reconcilQty = reconcilQty;
		this.receiveMoney = receiveMoney;
	}

	public SaleScheduleVo()
	{
		super();
	}
}
