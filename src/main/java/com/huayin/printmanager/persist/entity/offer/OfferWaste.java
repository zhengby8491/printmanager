/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 下午1:28:48
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.OfferType;

/**
 * <pre>
 * 报价模块 - 损耗设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月16日
 */
@Entity
@Table(name = "offer_waste")
public class OfferWaste extends BaseTableIdEntity
{

	private static final long serialVersionUID = -6830810825121757543L;

	/**
	 * 报价类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private OfferType offerType;

	/**
	 * 单色印刷 - 起印张数/版
	 */
	private Integer spStartSheetZQ = 150;

	/**
	 * 单色印刷 - 每千印/版
	 */
	private Integer spThousandSheetZQ = 150;

	/**
	 * 双色印刷 - 起印张数/版
	 */
	private Integer dpStartSheetZQ = 150;

	/**
	 * 双色印刷 - 每千印/版
	 */
	private Integer dpThousandSheetZQ = 150;

	/**
	 * 印后加工
	 */
	private Integer workAfter = 150;

	/**
	 * 排序
	 */
	private Integer sort = 0;

	public OfferType getOfferType()
	{
		return offerType;
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public Integer getSpStartSheetZQ()
	{
		return spStartSheetZQ;
	}

	public void setSpStartSheetZQ(Integer spStartSheetZQ)
	{
		this.spStartSheetZQ = spStartSheetZQ;
	}

	public Integer getSpThousandSheetZQ()
	{
		return spThousandSheetZQ;
	}

	public void setSpThousandSheetZQ(Integer spThousandSheetZQ)
	{
		this.spThousandSheetZQ = spThousandSheetZQ;
	}

	public Integer getDpStartSheetZQ()
	{
		return dpStartSheetZQ;
	}

	public void setDpStartSheetZQ(Integer dpStartSheetZQ)
	{
		this.dpStartSheetZQ = dpStartSheetZQ;
	}

	public Integer getDpThousandSheetZQ()
	{
		return dpThousandSheetZQ;
	}

	public void setDpThousandSheetZQ(Integer dpThousandSheetZQ)
	{
		this.dpThousandSheetZQ = dpThousandSheetZQ;
	}

	public Integer getWorkAfter()
	{
		return workAfter;
	}

	public void setWorkAfter(Integer workAfter)
	{
		this.workAfter = workAfter;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}
}
