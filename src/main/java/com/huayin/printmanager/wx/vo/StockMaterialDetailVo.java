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

/**
 * <pre>
 * 微信 - 材料库存VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class StockMaterialDetailVo
{
	private String materialName;
	
	private String style;
	
	private Integer weight;
	
	private String unitName;
	
	private BigDecimal qty=new BigDecimal(0);
	
	private BigDecimal price=new BigDecimal(0);
	
	private BigDecimal money=new BigDecimal(0);

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

	public StockMaterialDetailVo(String materialName, String style, Integer weight, String unitName, BigDecimal qty,
			BigDecimal price, BigDecimal money)
	{
		super();
		this.materialName = materialName;
		this.style = style;
		this.weight = weight;
		this.unitName = unitName;
		this.qty = qty;
		this.price = price;
		this.money = money;
	}

	public StockMaterialDetailVo()
	{
		super();
	}
}
