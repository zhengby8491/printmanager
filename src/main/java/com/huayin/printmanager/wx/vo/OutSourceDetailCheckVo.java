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
 * 微信 - 发外加工单审核明细VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class OutSourceDetailCheckVo
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
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 规格
	 */
	private String style;

	/**
	 * 加工数量
	 */
	private Integer qty;

	/**
	 * 加工单价
	 */
	private BigDecimal price;

	/**
	 * 金额(不含税）
	 */
	private BigDecimal money;

	/**
	 * 单位
	 */
	private String unitName;

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
		if ("".equals(productName))
		{
			productName = null;
		}
		this.productName = productName;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		if ("".equals(procedureName))
		{
			procedureName = null;
		}
		this.procedureName = procedureName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getQty()
	{
		return qty;
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

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

}
