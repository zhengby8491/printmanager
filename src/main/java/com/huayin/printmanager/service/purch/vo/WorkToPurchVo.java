/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.purch.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 物料需求计划VO
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月23日, raintear
 * @version 	   2.0, 2018年2月26日上午9:53:39, zhengby, 代码规范
 */
public class WorkToPurchVo
{
	/**
	 * 材料id
	 */
	private Long materialId;

	/**
	 * 材料分类名称
	 */
	private String materialClassName;

	/**
	 * 材料编码
	 */
	private String code;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String style;

	/**
	 * 材料克重
	 */
	private String weight;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 库存单位id
	 */
	private Long stockUnitId;

	/**
	 * 计价单位id
	 */
	private Long valuationUnitId;

	/**
	 * 库存数量
	 */
	private BigDecimal stockQty;

	/**
	 * 工单占用量
	 */
	private BigDecimal workQty;

	/**
	 * 在途采购量
	 */
	private BigDecimal purchQty;

	/**
	 * 需采购数量
	 */
	private BigDecimal qty;

	public String getMaterialClassName()
	{
		return materialClassName;
	}

	public void setMaterialClassName(String materialClassName)
	{
		this.materialClassName = materialClassName;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

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

	public String getWeight()
	{
		return weight;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
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

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public BigDecimal getWorkQty()
	{
		return workQty;
	}

	public void setWorkQty(BigDecimal workQty)
	{
		this.workQty = workQty;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

}
