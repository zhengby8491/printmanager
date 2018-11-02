/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年11月1日 上午10:13:58
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
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
import com.huayin.printmanager.persist.enumerate.OfferType;

/**
 * <pre>
 * 报价模块 - 坑纸设置
 * </pre>
 * @author THINK
 * @version 1.0, 2017年11月1日
 */
@Entity
@Table(name = "offer_bflute")
public class OfferBflute extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 203258909430724258L;

	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;
	
	/**
	 * 坑形
	 */
	@Column(length = 50)
	private String pit;
	
	/**
	 * 纸质
	 */
	@Column(length = 50)
	private String paperQuality;
	
	/**
	 * 单价
	 */
	@Column(length = 11)
	private BigDecimal price;
	
	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}



	public String getPaperQuality()
	{
		return paperQuality;
	}

	public void setPaperQuality(String paperQuality)
	{
		this.paperQuality = paperQuality;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getPit()
	{
		return pit;
	}

	public void setPit(String pit)
	{
		this.pit = pit;
	}
}
