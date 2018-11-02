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
 * 库存管理 - 成品调整明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product_adjust_detail")
public class StockProductAdjustDetail extends StockProductDetailBaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 主表
	 */
	@Transient
	private StockProductAdjust master;

	/**
	 * 产品库存ID
	 */
	private Long stockProductlId;

	public StockProductAdjust getMaster()
	{
		return master;
	}

	public void setMaster(StockProductAdjust master)
	{
		this.master = master;
	}

	public Long getStockProductlId()
	{
		return stockProductlId;
	}

	public void setStockProductlId(Long stockProductlId)
	{
		this.stockProductlId = stockProductlId;
	}

}
