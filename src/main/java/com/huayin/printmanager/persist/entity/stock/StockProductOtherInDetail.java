/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 库存管理 - 成品其它入库明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_otherin_detail")
public class StockProductOtherInDetail extends StockProductDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品库存ID
	 */
	private String stockProductId;

	/**
	 * 库存数量
	 */
	private String stockProductQty;

	/**
	 * 主表
	 */
	@Transient
	private StockProductOtherIn master;

	public String getStockProductId()
	{
		return stockProductId;
	}

	public void setStockProductId(String stockProductId)
	{
		this.stockProductId = stockProductId;
	}

	public String getStockProductQty()
	{
		return stockProductQty;
	}

	public void setStockProductQty(String stockProductQty)
	{
		this.stockProductQty = stockProductQty;
	}

	public StockProductOtherIn getMaster()
	{
		return master;
	}

	public void setMaster(StockProductOtherIn master)
	{
		this.master = master;
	}

}
