/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月30日 下午3:48:42
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 自动报价 - 内部核价单阶梯数据
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月30日 下午3:50:37
 */
@Entity
@Table(name = "offer_order_quote_inner")
public class OfferOrderQuoteInner extends BaseTableIdEntity
{
	private static final long serialVersionUID = -2286049253636566127L;
	
	/**
	 * 主表Id
	 */
	private Long masterId;
	/**
	 * 阶梯数量
	 */
	private Integer amount;
	
	/**
	 * 纸张费
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double paperFee = 0.0;
	
	/**
	 * 印刷费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double printFee;
	
	/**
	 * 工序费
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double procedureFee;
	
	/**
	 * 其他费用
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double ohterFee;

	/**
	 * 物流费(运费)
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double freightFee;
	
	/**
	 * 成本金额
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double costMoney;
	
	/**
	 * 利润
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double profitFee;
	
	/**
	 * 未税金额
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double untaxedFee;
	
	/**
	 * 未税单价
	 */
	@Column(columnDefinition = "double(10,4) default '0.0000'")
	private Double untaxedPrice;
	
	/**
	 * 含税金额
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private Double taxFee;

	/**
	 * 含税单价
	 */
	@Column(columnDefinition = "double(10,4) default '0.0000'")
	private Double taxPrice;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public Double getPaperFee()
	{
		return paperFee;
	}

	public void setPaperFee(Double paperFee)
	{
		this.paperFee = paperFee;
	}

	public Double getPrintFee()
	{
		return printFee;
	}

	public void setPrintFee(Double printFee)
	{
		this.printFee = printFee;
	}

	public Double getProcedureFee()
	{
		return procedureFee;
	}

	public void setProcedureFee(Double procedureFee)
	{
		this.procedureFee = procedureFee;
	}

	public Double getOhterFee()
	{
		return ohterFee;
	}

	public void setOhterFee(Double ohterFee)
	{
		this.ohterFee = ohterFee;
	}

	public Double getFreightFee()
	{
		return freightFee;
	}

	public void setFreightFee(Double freightFee)
	{
		this.freightFee = freightFee;
	}

	public Double getCostMoney()
	{
		return costMoney;
	}

	public void setCostMoney(Double costMoney)
	{
		this.costMoney = costMoney;
	}

	public Double getProfitFee()
	{
		return profitFee;
	}

	public void setProfitFee(Double profitFee)
	{
		this.profitFee = profitFee;
	}

	public Double getUntaxedFee()
	{
		return untaxedFee;
	}

	public void setUntaxedFee(Double untaxedFee)
	{
		this.untaxedFee = untaxedFee;
	}

	public Double getUntaxedPrice()
	{
		return untaxedPrice;
	}

	public void setUntaxedPrice(Double untaxedPrice)
	{
		this.untaxedPrice = untaxedPrice;
	}

	public Double getTaxFee()
	{
		return taxFee;
	}

	public void setTaxFee(Double taxFee)
	{
		this.taxFee = taxFee;
	}

	public Double getTaxPrice()
	{
		return taxPrice;
	}

	public void setTaxPrice(Double taxPrice)
	{
		this.taxPrice = taxPrice;
	}
	
	
}
