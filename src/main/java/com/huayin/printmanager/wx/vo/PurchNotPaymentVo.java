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
 * 微信 - 未付款采购
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class PurchNotPaymentVo
{
	private Long id;

	/**
	 * 付款天数
	 */
	private Double paymentDay;

	/**
	 * 结算日期
	 */
	private Date reconcilTime;

	/**
	 * 结算剩余天数百分比
	 */
	private BigDecimal reconcilPercent;

	/**
	 * 创建日期
	 */
	private Date createTime;

	/**
	 * 采购单号
	 */
	private String billNo;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String specifications;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 采购数量
	 */
	private BigDecimal qty;

	/**
	 * 采购金额
	 */
	private BigDecimal money;

	/**
	 * 付款金额
	 */
	private BigDecimal paymentMoney;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public BigDecimal getReconcilPercent()
	{
		return reconcilPercent;
	}

	public void setReconcilPercent(BigDecimal reconcilPercent)
	{
		this.reconcilPercent = reconcilPercent;
	}

	public Double getPaymentDay()
	{
		return paymentDay;
	}

	public void setPaymentDay(Double paymentDay)
	{
		this.paymentDay = paymentDay;
	}

	public Date getReconcilTime()
	{
		return reconcilTime;
	}

	public void setReconcilTime(Date reconcilTime)
	{
		this.reconcilTime = reconcilTime;
		this.setPaymentDay(DateUtils.getDistanceOfTwoDate(new Date(), reconcilTime));
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
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
