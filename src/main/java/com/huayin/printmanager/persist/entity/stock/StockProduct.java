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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.entity.basic.Product;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "stock_product")
public class StockProduct extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 仓库ID
	 */
	private Long warehouseId;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 数量
	 */
	private Integer qty;

	/**
	 * 成本单价
	 */
	private BigDecimal price;

	/**
	 * 成本金额
	 */
	private BigDecimal money;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 是否是入库 的单据
	 */
	@Transient
	private BoolValue isIn;

	@Transient
	private Product product;

	@Transient
	private String json;

	public Long getWarehouseId()
	{
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId)
	{
		this.warehouseId = warehouseId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public BoolValue getIsIn()
	{
		return isIn;
	}

	public void setIsIn(BoolValue isIn)
	{
		this.isIn = isIn;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public Product getProduct()
	{
		return product;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}

	public String getJson()
	{
		return json;
	}

	public void setJson(String json)
	{
		this.json = json;
	}

	public String getWarehouseName()
	{
		if (warehouseId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.WAREHOUSE.name(), warehouseId, "name");
		}
		return "-";
	}

}
