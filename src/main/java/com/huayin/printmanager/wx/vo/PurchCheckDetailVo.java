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
 * 微信 - 采购明细VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class PurchCheckDetailVo
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
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 规格
	 */
	private String specifications;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 计价单位
	 */
	private String valuationUnitName;

	/**
	 * 库存单位
	 */
	private String unitName;

	/**
	 * 采购数量
	 */
	private BigDecimal qty;

	/**
	 * 计价数量
	 */
	private BigDecimal valuationQty;

	/**
	 * 采购单价
	 */
	private BigDecimal valuationPrice;

	/**
	 * 采购金额
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

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public PurchCheckDetailVo(Long id, Date deliveryTime, String materialName, String specifications, Integer weight, String unitName, String valuationUnitName, BigDecimal qty, BigDecimal valuationQty, BigDecimal valuationPrice, BigDecimal money)
	{
		super();
		this.id = id;
		this.deliveryTime = deliveryTime;
		this.materialName = materialName;
		this.specifications = specifications;
		this.weight = weight;
		this.unitName = unitName;
		this.valuationUnitName = valuationUnitName;
		this.qty = qty;
		this.valuationQty = valuationQty;
		this.valuationPrice = valuationPrice;
		this.money = money;
	}

	public PurchCheckDetailVo()
	{
		super();
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getValuationUnitName()
	{
		return valuationUnitName;
	}

	public void setValuationUnitName(String valuationUnitName)
	{
		this.valuationUnitName = valuationUnitName;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

}
