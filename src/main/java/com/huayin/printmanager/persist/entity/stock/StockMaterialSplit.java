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
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.SplitType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料分切
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_split")
public class StockMaterialSplit extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 分切类型
	 */
	@Enumerated(EnumType.STRING)
	private SplitType splitType;

	/**
	 * 分切日期
	 */
	private Date splitTime;

	/**
	 * 材料库存ID
	 */
	private Long stockMaterialId;

	/**
	 * 材料ID
	 */
	private Long materialId;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String specifications;

	/**
	 * 材料库存单位ID
	 */
	private Long stockUnitId;

	/**
	 * 计价单位id
	 */
	private Long valuationUnitId;

	/**
	 * 库存数量
	 */
	private BigDecimal qty;

	/**
	 * 计价数量
	 */
	@Column(precision = 19, scale = 4)
	private BigDecimal valuationQty;

	private BigDecimal price;

	private BigDecimal valuationPrice;

	private BigDecimal money;

	/**
	 * 子表实体
	 */
	@Transient
	private List<StockMaterialSplitDetail> detailList;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public List<StockMaterialSplitDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<StockMaterialSplitDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}

	public SplitType getSplitType()
	{
		return splitType;
	}

	public void setSplitType(SplitType splitType)
	{
		this.splitType = splitType;
	}

	public Date getSplitTime()
	{
		return splitTime;
	}

	public void setSplitTime(Date splitTime)
	{
		this.splitTime = splitTime;
	}

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
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

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

	public Long getStockMaterialId()
	{
		return stockMaterialId;
	}

	public void setStockMaterialId(Long stockMaterialId)
	{
		this.stockMaterialId = stockMaterialId;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
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

	public Material getMaterial()
	{
		if (materialId != null)
		{
			return (Material) UserUtils.getBasicInfo(BasicType.MATERIAL.name(), materialId);
		}
		else
		{
			return null;
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

	public String getSplitTypeText()
	{
		if (splitType != null)
		{
			return splitType.getText();
		}
		return "-";
	}
}
