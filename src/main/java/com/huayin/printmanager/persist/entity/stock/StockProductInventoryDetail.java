/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.InventoryType;

/**
 * <pre>
 * 库存管理 - 成品盘点明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_inventory_detail")
public class StockProductInventoryDetail extends StockProductDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 成品库存ID
	 */
	private Long stockProductId;

	/**
	 * 盘点类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private InventoryType inventoryType;

	/**
	 * 库存数量
	 */
	private String stockQty;

	/**
	 * 盈亏数量
	 */
	private Integer profitAndLossQty;

	/**
	 * 盈亏金额
	 */
	private BigDecimal profitAndLossMoney;

	/**
	 * 主表
	 */
	@Transient
	private StockProductInventory master;

	public InventoryType getInventoryType()
	{
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType)
	{
		this.inventoryType = inventoryType;
	}

	public String getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(String stockQty)
	{
		this.stockQty = stockQty;
	}

	public Integer getProfitAndLossQty()
	{
		return profitAndLossQty;
	}

	public void setProfitAndLossQty(Integer profitAndLossQty)
	{
		this.profitAndLossQty = profitAndLossQty;
	}

	public BigDecimal getProfitAndLossMoney()
	{
		return profitAndLossMoney;
	}

	public void setProfitAndLossMoney(BigDecimal profitAndLossMoney)
	{
		this.profitAndLossMoney = profitAndLossMoney;
	}

	public StockProductInventory getMaster()
	{
		return master;
	}

	public void setMaster(StockProductInventory master)
	{
		this.master = master;
	}

	public Long getStockProductId()
	{
		return stockProductId;
	}

	public void setStockProductId(Long stockProductId)
	{
		this.stockProductId = stockProductId;
	}

	public String getInventoryTypeText()
	{
		if (inventoryType != null)
		{
			return inventoryType.getText();
		}
		else
		{
			return "-";
		}
	}

}
