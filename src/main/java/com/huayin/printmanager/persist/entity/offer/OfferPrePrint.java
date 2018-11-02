/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年10月19日 上午11:35:58
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * @since	    1.0 
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;

/**
 * <pre>
 * 报价模块 - 印前费用
 * </pre>
 * @author THINK
 * @since 1.0, 2017年10月19日
 */
@Entity
@Table(name = "offer_preprint")
public class OfferPrePrint extends BaseBasicTableEntity
{

	private static final long serialVersionUID = 625547559202167388L;

	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;

	/**
	 * 送货时间从...
	 */
	private Date deliveryTimeMin;

	/**
	 * 送货时间至...
	 */
	private Date deliveryTimeMax;

	/**
	 * 是否选择包装费
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue packingChk = BoolValue.NO;

	/**
	 * 是否选择运费
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue freightChk = BoolValue.NO;

	/**
	 * 包装费
	 */
	private BigDecimal packing;

	/**
	 * 包装费/个
	 */
	private Integer packingPer;

	/**
	 * 运费
	 */
	private BigDecimal freight;

	public Date getDeliveryTimeMin()
	{
		return deliveryTimeMin;
	}

	public void setDeliveryTimeMin(Date deliveryTimeMin)
	{
		this.deliveryTimeMin = deliveryTimeMin;
	}

	public Date getDeliveryTimeMax()
	{
		return deliveryTimeMax;
	}

	public void setDeliveryTimeMax(Date deliveryTimeMax)
	{
		this.deliveryTimeMax = deliveryTimeMax;
	}

	public BigDecimal getPacking()
	{
		return packing;
	}

	public void setPacking(BigDecimal packing)
	{
		this.packing = packing;
	}

	public BigDecimal getFreight()
	{
		return freight;
	}

	public void setFreight(BigDecimal freight)
	{
		this.freight = freight;
	}

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public BoolValue getPackingChk()
	{
		return packingChk;
	}

	public void setPackingChk(BoolValue packingChk)
	{
		this.packingChk = packingChk;
	}

	public BoolValue getFreightChk()
	{
		return freightChk;
	}

	public void setFreightChk(BoolValue freightChk)
	{
		this.freightChk = freightChk;
	}

	public Integer getPackingPer()
	{
		return packingPer;
	}

	public void setPackingPer(Integer packingPer)
	{
		this.packingPer = packingPer;
	}
}
