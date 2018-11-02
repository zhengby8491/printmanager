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

import java.util.Date;

/**
 * <pre>
 * 微信 - 生产工单进度Vo
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class ProduceScheduleVo
{
	private Long id;

	/**
	 * 进度状态
	 */
	private String scheduleState;

	/**
	 * 销售单号
	 */
	private String saleBillNo;

	/**
	 * 生产单号
	 */
	private String workBillNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 产品规格
	 */
	private String style;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 生产数量
	 */
	private Integer workQty;

	/**
	 * 入库数量
	 */
	private Integer inStockQty;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

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

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
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

	public String getSaleBillNo()
	{
		return saleBillNo;
	}

	public void setSaleBillNo(String saleBillNo)
	{
		this.saleBillNo = saleBillNo;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
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

	public Integer getWorkQty()
	{
		return workQty;
	}

	public void setWorkQty(Integer workQty)
	{
		this.workQty = workQty;
	}

	public Integer getInStockQty()
	{
		return inStockQty;
	}

	public void setInStockQty(Integer inStockQty)
	{
		this.inStockQty = inStockQty;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public ProduceScheduleVo(Long id, String scheduleState, Date deliveryTime, String saleBillNo, String workBillNo, String customerName, String productName, String style, String unitName, Integer workQty, Integer inStockQty)
	{
		super();
		this.id = id;
		this.scheduleState = scheduleState;
		this.deliveryTime = deliveryTime;
		this.saleBillNo = saleBillNo;
		this.workBillNo = workBillNo;
		this.customerName = customerName;
		this.productName = productName;
		this.style = style;
		this.unitName = unitName;
		this.workQty = workQty;
		this.inStockQty = inStockQty;
	}

	public ProduceScheduleVo()
	{
		super();
	}

}
