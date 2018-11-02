/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.SaleMaterialType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售订单： 销售订单明细材料表
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
@Entity
@Table(name = "sale_order_material")
public class SaleOrderMaterial extends BaseBillTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 销售明细ID
	 */
	private Long saleDetailId;

	/**
	 * 销售材料类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private SaleMaterialType saleMaterialType;

	/**
	 * 父ID（部件ID或者成品ID)
	 */
	private Long parentId;

	/**
	 * 材料ID
	 */
	private Long materialId;

	/**
	 * 材料编号
	 */
	private String materialCode;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String style;

	/**
	 * 克 重
	 */
	private Integer weight;

	/**
	 * 库存单位
	 */
	@Column(length = 20)
	private Long stockUnitId;

	/**
	 * 计价单位
	 */
	@Column(length = 20)
	private Long valuationUnitId;

	/**
	 * 材料开数
	 */
	private Integer splitQty;

	/**
	 * 材料用量
	 */
	private BigDecimal qty;

	/**
	 * 已采购数量
	 */
	private BigDecimal purchQty;

	/**
	 * 已领料数量
	 */
	private BigDecimal takeQty;

	/**
	 * 是否无需领料
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isNotTake;

	/**
	 * 是否无需采购
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isNotPurch;

	/**
	 * 是否客户来纸
	 */
	@Column(length = 10)
	@Enumerated(EnumType.STRING)
	private BoolValue isCustPaper;

	/**
	 * 库存数量
	 */
	@Transient
	private BigDecimal stockQty;

	/**
	 * 销售产品对象
	 */
	@Transient
	private SaleOrderPack saleOrderPack;

	/**
	 * 销售部件对象
	 */
	@Transient
	private SaleOrderPart saleOrderPart;

	/**
	 * 销售明细对象
	 */
	@Transient
	private SaleOrderDetail saleOrderDetail;

	public Long getSaleDetailId()
	{
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId)
	{
		this.saleDetailId = saleDetailId;
	}

	public SaleMaterialType getSaleMaterialType()
	{
		return saleMaterialType;
	}

	public void setSaleMaterialType(SaleMaterialType saleMaterialType)
	{
		this.saleMaterialType = saleMaterialType;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public SaleOrderPack getSaleOrderPack()
	{
		return saleOrderPack;
	}

	public void setSaleOrderPack(SaleOrderPack saleOrderPack)
	{
		this.saleOrderPack = saleOrderPack;
	}

	public SaleOrderPart getSaleOrderPart()
	{
		return saleOrderPart;
	}

	public void setSaleOrderPart(SaleOrderPart saleOrderPart)
	{
		this.saleOrderPart = saleOrderPart;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Integer getSplitQty()
	{
		return splitQty;
	}

	public void setSplitQty(Integer splitQty)
	{
		this.splitQty = splitQty;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
	}

	public BigDecimal getTakeQty()
	{
		return takeQty;
	}

	public void setTakeQty(BigDecimal takeQty)
	{
		this.takeQty = takeQty;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public String getMaterialCode()
	{
		return materialCode;
	}

	public void setMaterialCode(String materialCode)
	{
		this.materialCode = materialCode;
	}

	public BoolValue getIsNotTake()
	{
		return isNotTake;
	}

	public void setIsNotTake(BoolValue isNotTake)
	{
		this.isNotTake = isNotTake;
	}

	public BoolValue getIsNotPurch()
	{
		return isNotPurch;
	}

	public void setIsNotPurch(BoolValue isNotPurch)
	{
		this.isNotPurch = isNotPurch;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BoolValue getIsCustPaper()
	{
		return isCustPaper;
	}

	public void setIsCustPaper(BoolValue isCustPaper)
	{
		this.isCustPaper = isCustPaper;
	}

	public SaleOrderDetail getSaleOrderDetail()
	{
		return saleOrderDetail;
	}

	public void setSaleOrderDetail(SaleOrderDetail saleOrderDetail)
	{
		this.saleOrderDetail = saleOrderDetail;
	}

	// -------------------------------------------------------------------
	public String getStockUnitName()
	{

		if (stockUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), stockUnitId, "name");
		}
		return "-";
	}

	public String getIsNotTakeText()
	{
		if (isNotTake != null)
		{

			return isNotTake.getText();
		}
		return "-";
	}

	public String getIsNotPurchText()
	{
		if (isNotPurch != null)
		{

			return isNotPurch.getText();
		}
		return "-";
	}

}
