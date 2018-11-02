/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 上午9:18:05
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 外部接口  - 采购订单详情
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日上午9:18:05, zhengby
 */
public class RspPurchItems
{
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
	private String buyNum;
	
	/**
	 * 商品价格
	 */
	private String itemPrice;
	
	/**
	 * 优惠价格
	 */
	private String discountPrice;
	
	/**
	 * 商品总金额
	 */
	private String itemTotalPrice;
	
	/**
	 * 换算率
	 */
	private String conversionRate;
	
	/**
	 * 交货日期
	 */
	private String deliveryDate;

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

	public String getBuyNum()
	{
		return buyNum;
	}

	public void setBuyNum(String buyNum)
	{
		this.buyNum = buyNum;
	}

	public String getItemPrice()
	{
		return itemPrice;
	}

	public void setItemPrice(String itemPrice)
	{
		this.itemPrice = itemPrice;
	}

	public String getDiscountPrice()
	{
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice)
	{
		this.discountPrice = discountPrice;
	}

	public String getItemTotalPrice()
	{
		return itemTotalPrice;
	}

	public void setItemTotalPrice(String itemTotalPrice)
	{
		this.itemTotalPrice = itemTotalPrice;
	}

	public String getConversionRate()
	{
		return conversionRate;
	}

	public void setConversionRate(String conversionRate)
	{
		this.conversionRate = conversionRate;
	}

	public String getDeliveryDate()
	{
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}
	
}
