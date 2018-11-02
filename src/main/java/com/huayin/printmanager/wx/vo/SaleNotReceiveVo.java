/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 微信 - 未收款销售VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class SaleNotReceiveVo
{
	private Long id;

	/**
	 * 收款天数
	 */
	private Double receiveDay;

	/**
	 * 结算日期
	 */
	private Date reconcilTime;

	/**
	 * 结算剩余天数百分比
	 */
	private BigDecimal reconcilPercent;

	/**
	 * 销售单号
	 */
	private String saleBillNo;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 销售数量
	 */
	private Integer saleQty;

	/**
	 * 销售金额
	 */
	private BigDecimal saleMoney;

	/**
	 * 收款金额
	 */
	private BigDecimal receiveMoney;

	/**
	 * 创建日期
	 */
	private Date createTime;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getReceiveDay()
	{
		return receiveDay;
	}

	public void setReceiveDay(Double receiveDay)
	{
		this.receiveDay = receiveDay;
	}

	public Date getReconcilTime()
	{
		return reconcilTime;
	}

	public void setReconcilTime(Date reconcilTime)
	{
		this.reconcilTime = reconcilTime;
		this.setReceiveDay(DateUtils.getDistanceOfTwoDate(new Date(), reconcilTime));
	}

	public String getSaleBillNo()
	{
		return saleBillNo;
	}

	public void setSaleBillNo(String saleBillNo)
	{
		this.saleBillNo = saleBillNo;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public Integer getSaleQty()
	{
		return saleQty;
	}

	public void setSaleQty(Integer saleQty)
	{
		this.saleQty = saleQty;
	}

	public BigDecimal getSaleMoney()
	{
		return saleMoney;
	}

	public void setSaleMoney(BigDecimal saleMoney)
	{
		this.saleMoney = saleMoney;
	}

	public BigDecimal getReceiveMoney()
	{
		return receiveMoney;
	}

	public BigDecimal getReconcilPercent()
	{
		return reconcilPercent;
	}

	public void setReconcilPercent(BigDecimal reconcilPercent)
	{
		this.reconcilPercent = reconcilPercent;
	}

	public void setReceiveMoney(BigDecimal receiveMoney)
	{
		this.receiveMoney = receiveMoney;
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
