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
 * 微信 - 未入库采购VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class PurchNotStockVo
{
	private Long id;

	/**
	 * 交货天数
	 */
	private Double deliveryDay;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 交货剩余天数百分比
	 */
	private BigDecimal deliveryPercent;

	/**
	 * 采购单号
	 */
	private String billNo;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 规格
	 */
	private String specifications;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 采购数量
	 */
	private BigDecimal purchQty;

	/**
	 * 入库数量
	 */
	private BigDecimal stockQty;

	/**
	 * 创建日期
	 */
	private Date createTime;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getDeliveryDay()
	{
		return deliveryDay;
	}

	public void setDeliveryDay(Double deliveryDay)
	{
		this.deliveryDay = deliveryDay;
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

	public String getBillNo()
	{
		return billNo;
	}

	public BigDecimal getDeliveryPercent()
	{
		return deliveryPercent;
	}

	public void setDeliveryPercent(BigDecimal deliveryPercent)
	{
		this.deliveryPercent = deliveryPercent;
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

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
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

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

}
