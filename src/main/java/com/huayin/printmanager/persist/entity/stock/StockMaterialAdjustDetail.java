/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调整单明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_adjust_detail")
public class StockMaterialAdjustDetail extends StockMaterialDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 材料库存ID
	 */
	private Long stockMaterialId;

	/**
	 * 材料库存单位ID
	 */
	private Long stockUnitId;

	/**
	 * 调整金额
	 */
	private BigDecimal adjustMoney;

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
	 * 主表实体
	 */
	@Transient
	private StockMaterialAdjust master;

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

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

	public BigDecimal getAdjustMoney()
	{
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney)
	{
		this.adjustMoney = adjustMoney;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public StockMaterialAdjust getMaster()
	{
		return master;
	}

	public void setMaster(StockMaterialAdjust master)
	{
		this.master = master;
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
