/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BillType;

/**
 * <pre>
 * 库存管理 - 成品入库明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_in_detail")
public class StockProductInDetail extends StockProductDetailBaseEntity
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
	 * 源单数量
	 */
	private Integer sourceQty;

	/**
	 * 源单单据编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 已入库数量
	 */
	private Integer alreadyInQty;

	/**
	 * 销售订单ID
	 */
	private Long saleOrderId;

	/**
	 * 销售订单单据编号
	 */
	private String saleOrderBillNo;

	/**
	 * 主表
	 */
	@Transient
	private StockProductIn master;

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

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public Integer getAlreadyInQty()
	{
		return alreadyInQty;
	}

	public void setAlreadyInQty(Integer alreadyInQty)
	{
		this.alreadyInQty = alreadyInQty;
	}

	public Long getSaleOrderId()
	{
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId)
	{
		this.saleOrderId = saleOrderId;
	}

	public String getSaleOrderBillNo()
	{
		return saleOrderBillNo;
	}

	public void setSaleOrderBillNo(String saleOrderBillNo)
	{
		this.saleOrderBillNo = saleOrderBillNo;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public StockProductIn getMaster()
	{
		return master;
	}

	public void setMaster(StockProductIn master)
	{
		this.master = master;
	}

	public Integer getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(Integer sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public String getSourceBillTypeText()
	{
		if (sourceBillType != null)
		{
			return sourceBillType.getText();
		}
		return "-";
	}
}
