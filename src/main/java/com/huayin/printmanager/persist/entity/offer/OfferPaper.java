/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年10月30日 上午11:46:47
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
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OfferType;

/**
 * <pre>
 * 报价模块 - 纸张材料
 * </pre>
 * @author THINK
 * @version 1.0, 2017年10月30日
 */
@Entity
@Table(name = "offer_paper")
public class OfferPaper extends BaseBasicTableEntity
{
	private static final long serialVersionUID = -6391150075102787523L;

	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;
	
	/**
	 * 材料名称
	 */
	private String name;
	
	/**
	 * 克重（g）
	 */
	private Integer weight;
	
	/**
	 * 吨价
	 */
	@Column(precision=11, scale=2)
	private BigDecimal tonPrice;
	
	/**
	 * 是否自翻版
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isPageTurn;

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
	
	
	
	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getTonPrice()
	{
		return tonPrice;
	}

	public void setTonPrice(BigDecimal tonPrice)
	{
		this.tonPrice = tonPrice;
	}

	public BoolValue getIsPageTurn()
	{
		return isPageTurn;
	}

	public void setIsPageTurn(BoolValue isPageTurn)
	{
		this.isPageTurn = isPageTurn;
	}
	public OfferType getOfferType()
	{
		return offerType;
	}
	
	public String getOfferTypeText()
	{
		if(null != this.offerType)
		{
			return this.offerType.getText();
		}
		return "";
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
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
}
