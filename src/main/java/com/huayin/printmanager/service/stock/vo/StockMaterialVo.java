package com.huayin.printmanager.service.stock.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 材料库存VO
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月24日
 */
public class StockMaterialVo
{
	private Long materialId;
	
	private Long warehouseId;
	
	private String warehouseName;
	
	private String style;
	
	private String qty;
	
	private BigDecimal workEmployQty;
	
	private BigDecimal purchQty;

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getWorkEmployQty()
	{
		return workEmployQty;
	}

	public void setWorkEmployQty(BigDecimal workEmployQty)
	{
		this.workEmployQty = workEmployQty;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

	public String getQty()
	{
		return qty;
	}

	public void setQty(String qty)
	{
		this.qty = qty;
	}

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName()
	{
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName)
	{
		this.warehouseName = warehouseName;
	}
	
}
