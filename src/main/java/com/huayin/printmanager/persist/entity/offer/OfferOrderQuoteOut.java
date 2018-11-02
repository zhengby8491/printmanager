/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月30日 下午3:41:37
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
 * 自动报价 - 对外报价单阶梯数据
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月30日 下午3:41:37
 */
@Entity
@Table(name = "offer_order_quote_out")
public class OfferOrderQuoteOut extends BaseTableIdEntity 
{
	private static final long serialVersionUID = -5596088466390112795L;
	
	/**
	 * 主表Id
	 */
	private Long masterId;
	
	/**
	 * 对外报价单列表 - 印刷纸张
	 */
	private String printName;
	
	/**
	 * 对外报价单列表 - 颜色
	 */
	private String printColor;
	
	/**
	 * 对外报价单列表 - 加工工序
	 */
	private String printProcedure;
	
	/**
	 * 对外报价单列表 - 数量
	 */
	private Integer amount;

	/**
	 * 对外报价单列表 - 单价
	 */
	@Column(columnDefinition = "double(10,4) default '0.0000'")
	private double price;

	/**
	 * 对外报价单列表 - 金额
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double fee;

	/**
	 * 对外报价单列表 - 含税单价
	 */
	@Column(columnDefinition = "double(10,4) default '0.0000'")
	private double taxPrice;

	/**
	 * 对外报价单列表 - 含税金额
	 */
	@Column(columnDefinition = "double(10,2) default '0.00'")
	private double taxFee;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public String getPrintName()
	{
		return printName;
	}

	public void setPrintName(String printName)
	{
		this.printName = printName;
	}

	public String getPrintColor()
	{
		return printColor;
	}

	public void setPrintColor(String printColor)
	{
		this.printColor = printColor;
	}

	public String getPrintProcedure()
	{
		return printProcedure;
	}

	public void setPrintProcedure(String printProcedure)
	{
		this.printProcedure = printProcedure;
	}

	public Integer getAmount()
	{
		return amount;
	}

	public void setAmount(Integer amount)
	{
		this.amount = amount;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public double getFee()
	{
		return fee;
	}

	public void setFee(double fee)
	{
		this.fee = fee;
	}

	public double getTaxPrice()
	{
		return taxPrice;
	}

	public void setTaxPrice(double taxPrice)
	{
		this.taxPrice = taxPrice;
	}

	public double getTaxFee()
	{
		return taxFee;
	}

	public void setTaxFee(double taxFee)
	{
		this.taxFee = taxFee;
	}

}
