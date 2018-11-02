/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 上午11:37:29
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillTableEntity;

/**
 * <pre>
 * 印刷家的采购订单明细
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日上午11:37:29, zhengby
 */
@Entity
@Table(name = "exterior_purch_order_detail")
public class ExteriorPurchOrderDetail extends BaseBillTableEntity
{
	private static final long serialVersionUID = -7712131907424806537L;

	private Long masterId;
	
	/**
	 * 物料编码
	 */
	private String materialCode;
	
	/**
	 * 商品名称
	 */
	private String itemName;
	
	/**
	 * 品牌名称
	 */
	private String brandName;
	
	/**
	 * 商品所属类目
	 */
	private String category;
	
	/**
	 * sku信息
	 */
	private String skuInfo;
	
	/**
	 * 采购单位
	 */
	private String buyUnit;
	
	/**
	 * 采购数量
	 */
	@Column(columnDefinition = "decimal(18,3) default '0'")
	private BigDecimal buyNum;
	
	/**
	 * 商品价格
	 */
	private BigDecimal itemPrice;
	
	/**
	 * 优惠价格
	 */
	private BigDecimal discountPrice;
	
	/**
	 * 商品总金额
	 */
	private BigDecimal itemTotalPrice;
	
	/**
	 * 换算率
	 */
	private Double conversionRate;
	
	/**
	 * 交货日期
	 */
	private Date deliveryDate;
	
	/**
	 * 下游采购订单主表id（用于标记来自印刷家的订单）
	 */
	private Long purchOrderId;
	
	/**
	 * 下游采购订单明细表id（用于标记来自印刷家的订单）
	 */
	private Long purchOrderDetailId;
	
	/**
	 * 已入库数量
	 */
	@Column(columnDefinition = "decimal(18,2) default '0'")
	private BigDecimal storageQty = new BigDecimal(0);
	
	@Transient
	private ExteriorPurchOrder master;
	
	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public String getMaterialCode()
	{
		return materialCode;
	}

	public void setMaterialCode(String materialCode)
	{
		this.materialCode = materialCode;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getSkuInfo()
	{
		return skuInfo;
	}

	public void setSkuInfo(String skuInfo)
	{
		this.skuInfo = skuInfo;
	}

	public String getBuyUnit()
	{
		return buyUnit;
	}

	public void setBuyUnit(String buyUnit)
	{
		this.buyUnit = buyUnit;
	}

	public BigDecimal getBuyNum()
	{
		return buyNum;
	}

	public void setBuyNum(BigDecimal buyNum)
	{
		this.buyNum = buyNum;
	}

	public BigDecimal getItemPrice()
	{
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice)
	{
		this.itemPrice = itemPrice;
	}

	public BigDecimal getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public BigDecimal getItemTotalPrice()
	{
		return itemTotalPrice;
	}

	public void setItemTotalPrice(BigDecimal itemTotalPrice)
	{
		this.itemTotalPrice = itemTotalPrice;
	}

	public Double getConversionRate()
	{
		return conversionRate;
	}

	public void setConversionRate(Double conversionRate)
	{
		this.conversionRate = conversionRate;
	}

	public Date getDeliveryDate()
	{
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}

	public ExteriorPurchOrder getMaster()
	{
		return master;
	}

	public void setMaster(ExteriorPurchOrder master)
	{
		this.master = master;
	}

	public Long getPurchOrderId()
	{
		return purchOrderId;
	}

	public void setPurchOrderId(Long purchOrderId)
	{
		this.purchOrderId = purchOrderId;
	}

	public Long getPurchOrderDetailId()
	{
		return purchOrderDetailId;
	}

	public void setPurchOrderDetailId(Long purchOrderDetailId)
	{
		this.purchOrderDetailId = purchOrderDetailId;
	}

	public BigDecimal getStorageQty()
	{
		return storageQty;
	}

	public void setStorageQty(BigDecimal storageQty)
	{
		this.storageQty = storageQty;
	}
	
}
