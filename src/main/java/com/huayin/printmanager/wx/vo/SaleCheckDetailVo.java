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
 * 微信 - 销售明细VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SaleCheckDetailVo
{
	private Long id;

	/**
	 * 单号
	 */
	private String billNo;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

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
	 * 销售数量
	 */
	private Integer qty;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 金额
	 */
	private BigDecimal money;

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

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public Integer getQty()
	{
		return qty;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public SaleCheckDetailVo(Long id, Date deliveryTime, String productName, String style, String unitName, Integer qty, BigDecimal price, BigDecimal money)
	{
		super();
		this.id = id;
		this.deliveryTime = deliveryTime;
		this.productName = productName;
		this.style = style;
		this.unitName = unitName;
		this.qty = qty;
		this.price = price;
		this.money = money;
	}

	public SaleCheckDetailVo()
	{
		super();
	}
}
