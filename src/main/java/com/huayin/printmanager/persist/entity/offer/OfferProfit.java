/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 下午1:33:43
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.OfferProfitType;
import com.huayin.printmanager.persist.enumerate.OfferType;

/**
 * <pre>
 * 报价模块 - 利润设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日
 */
@Entity
@Table(name = "offer_profit")
public class OfferProfit extends BaseBasicTableEntity
{
	private static final long serialVersionUID = -2911685199621082688L;
	
	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;
	
	/**
	 * 利润类型（按数量、按金额）
	 */
	@Enumerated(EnumType.STRING)
	private OfferProfitType offerProfitType;

	/**
	 * （数量|金额）范围起始
	 */
	private Integer rangeStart;
	
	/**
	 * （数量|金额）范围结束
	 */
	private Integer rangeEnd;
	
	/**
	 * 利润百分比
	 */
	private Integer percent;
	
	/**
	 * 固定金额
	 */
	private Integer money;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public OfferProfitType getOfferProfitType()
	{
		return offerProfitType;
	}

	public void setOfferProfitType(OfferProfitType offerProfitType)
	{
		this.offerProfitType = offerProfitType;
	}

	public Integer getRangeStart()
	{
		return rangeStart;
	}

	public void setRangeStart(Integer rangeStart)
	{
		this.rangeStart = rangeStart;
	}

	public Integer getRangeEnd()
	{
		return rangeEnd;
	}

	public void setRangeEnd(Integer rangeEnd)
	{
		this.rangeEnd = rangeEnd;
	}

	public Integer getPercent()
	{
		return percent;
	}

	public void setPercent(Integer percent)
	{
		this.percent = percent;
	}

	public Integer getMoney()
	{
		return money;
	}

	public void setMoney(Integer money)
	{
		this.money = money;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
}
