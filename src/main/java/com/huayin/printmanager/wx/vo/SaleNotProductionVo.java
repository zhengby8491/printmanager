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

import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 微信 - 未生产订单VO（销售订单）
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SaleNotProductionVo
{
	private Long id;

	/**
	 * 单据编号
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

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 交货剩余天数
	 */
	private Double deliveryDay;

	/**
	 * 交货剩余天数百分比
	 */
	private BigDecimal deliveryPercent;

	/**
	 * 创建日期
	 */
	private Date createTime;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 销售数量
	 */
	private Integer saleQty;

	/**
	 * 生产数量
	 */
	private Integer produceedQty;

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

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
		this.setDeliveryDay(DateUtils.getDistanceOfTwoDate(new Date(), deliveryTime));
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

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public Double getDeliveryDay()
	{
		return deliveryDay;
	}

	public void setDeliveryDay(Double deliveryDay)
	{
		this.deliveryDay = deliveryDay;
	}

	public BigDecimal getDeliveryPercent()
	{
		return deliveryPercent;
	}

	public void setDeliveryPercent(BigDecimal deliveryPercent)
	{
		this.deliveryPercent = deliveryPercent;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

}
