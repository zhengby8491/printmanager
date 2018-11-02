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

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.InventoryType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料盘点单明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_inventory_detail")
public class StockMaterialInventoryDetail extends StockMaterialDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 盘点类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private InventoryType inventoryType;

	/**
	 * 材料库存ID
	 */
	private Long stockMaterialId;

	/**
	 * 材料库存数量
	 */
	private BigDecimal stockQty;

	/**
	 * 材料库存单位ID
	 */
	private Long stockUnitId;

	/**
	 * 盈亏数量
	 */
	private BigDecimal profitAndLossQty;

	/**
	 * 盈亏金额
	 */
	private BigDecimal profitAndLossMoney;

	/**
	 * 计价单位id
	 */
	private Long valuationUnitId;

	/**
	 * 计价数量
	 */
	@Column(precision = 19, scale = 4)
	private BigDecimal valuationQty;

	/**
	 * 计价单价
	 */
	private BigDecimal valuationPrice;

	/**
	 * 主表
	 */
	@Transient
	private StockMaterialInventory master;

	public InventoryType getInventoryType()
	{
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType)
	{
		this.inventoryType = inventoryType;
	}

	public Long getStockMaterialId()
	{
		return stockMaterialId;
	}

	public void setStockMaterialId(Long stockMaterialId)
	{
		this.stockMaterialId = stockMaterialId;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public BigDecimal getProfitAndLossMoney()
	{
		return profitAndLossMoney;
	}

	public void setProfitAndLossMoney(BigDecimal profitAndLossMoney)
	{
		this.profitAndLossMoney = profitAndLossMoney;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public BigDecimal getProfitAndLossQty()
	{
		return profitAndLossQty;
	}

	public void setProfitAndLossQty(BigDecimal profitAndLossQty)
	{
		this.profitAndLossQty = profitAndLossQty;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public StockMaterialInventory getMaster()
	{
		return master;
	}

	public void setMaster(StockMaterialInventory master)
	{
		this.master = master;
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

	public String getStockUnitName()
	{
		if (stockUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), stockUnitId, "name");
		}
		else
		{
			return "-";
		}

	}

	public String getValuationUnitName()
	{
		if (valuationUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), valuationUnitId, "name");
		}
		else
		{
			return "-";
		}
	}
}
