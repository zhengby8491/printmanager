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
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产补料明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_material_supplement_detail")
public class StockMaterialSupplementDetail extends StockMaterialDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType billType;

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
	 * 源单数量
	 */
	private Integer sourceQty;

	/**
	 * 材料库存ID
	 */
	private Long stockMaterialId;

	/**
	 * 材料库存数量
	 */
	private Integer stockQty;

	/**
	 * 材料库存单位ID
	 */
	private Long stockUnitId;

	/**
	 * 生产工单号
	 */
	@Column(length = 50)
	private String workBillNo;

	/**
	 * 计价单位ID
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
	private StockMaterialSupplement master;

	public Integer getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(Integer sourceQty)
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

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
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

	public StockMaterialSupplement getMaster()
	{
		return master;
	}

	public void setMaster(StockMaterialSupplement master)
	{
		this.master = master;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
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

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public String getBillTypeText()
	{
		if (billType != null)
		{
			return billType.getText();
		}
		return "-";
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
