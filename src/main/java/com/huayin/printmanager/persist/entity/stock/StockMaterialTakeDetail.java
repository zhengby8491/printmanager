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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产领料明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_take_detail")
public class StockMaterialTakeDetail extends StockMaterialDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 源单ID
	 */
	private Long sourceId;

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单单据编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 部件名称
	 */
	private String partName;

	/**
	 * 产品名称（多个产品名称）
	 */
	private String productName;
	
	/**
	 * 产品id（多个产品id）
	 */
	private String productId;
	
	/**
	 * 源单数量
	 */
	private BigDecimal sourceQty;

	/**
	 * 材料库存ID
	 */
	private Long stockMaterialId;
	
	/**
	 * 材料库存数量
	 */
	private Integer stockQty;

	/**
	 * 库存单位ID
	 */
	private Long stockUnitId;

	/**
	 * 库存单位名
	 */
	private String stockUnitName;

	/**
	 * 计价单位ID
	 */
	private Long valuationUnitId;

	/**
	 * 计价单位名
	 */
	private String valuationUnitName;

	/**
	 * 计价数量
	 */
	@Column(precision = 19, scale = 4)
	private BigDecimal valuationQty;

	/**
	 * 计价数量
	 */
	private BigDecimal valuationPrice;

	/**
	 * 主表
	 */
	@Transient
	private StockMaterialTake master;

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductId()
	{
		return productId;
	}

	public BigDecimal getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(BigDecimal sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public Long getStockMaterialId()
	{
		return stockMaterialId;
	}

	public void setStockMaterialId(Long stockMaterialId)
	{
		this.stockMaterialId = stockMaterialId;
	}

	public Integer getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(Integer stockQty)
	{
		this.stockQty = stockQty;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public BillType getSourceBillType()
	{
		return sourceBillType;
	}

	public void setSourceBillType(BillType sourceBillType)
	{
		this.sourceBillType = sourceBillType;
	}

	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public StockMaterialTake getMaster()
	{
		return master;
	}

	public void setMaster(StockMaterialTake master)
	{
		this.master = master;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

	public String getStockUnitName()
	{
		return stockUnitName;
	}

	public void setStockUnitName(String stockUnitName)
	{
		this.stockUnitName = stockUnitName;
	}

	public String getValuationUnitName()
	{
		return valuationUnitName;
	}

	public void setValuationUnitName(String valuationUnitName)
	{
		this.valuationUnitName = valuationUnitName;
	}

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public String getSourceBillTypeText()
	{
		if (sourceBillType != null)
		{
			return sourceBillType.getText();
		}
		return "-";
	}

	/**
	 * <pre>
	 * 计价单位精度
	 * </pre>
	 * @return
	 */
	public Integer getValuationUnitAccuracy()
	{
		if (valuationUnitId != null)
		{
			return (Integer) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), valuationUnitId, "accuracy");
		}
		return 0;
	}

}
