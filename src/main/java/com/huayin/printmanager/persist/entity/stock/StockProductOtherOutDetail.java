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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 库存管理 - 成品其它出库细表
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_otherout_detail")
public class StockProductOtherOutDetail extends StockProductDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品库存ID
	 */
	private Long stockProductId;

	/**
	 * 库存数量
	 */
	private BigDecimal stockProductQty;

	/**
	 * 主表
	 */
	@Transient
	private StockProductOtherOut master;

	public Long getStockProductId()
	{
		return stockProductId;
	}

	public void setStockProductId(Long stockProductId)
	{
		this.stockProductId = stockProductId;
	}

	public BigDecimal getStockProductQty()
	{
		return stockProductQty;
	}

	public void setStockProductQty(BigDecimal stockProductQty)
	{
		this.stockProductQty = stockProductQty;
	}

	public StockProductOtherOut getMaster()
	{
		return master;
	}

	public void setMaster(StockProductOtherOut master)
	{
		this.master = master;
	}
}
