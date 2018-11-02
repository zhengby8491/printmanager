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

/**
 * <pre>
 * 微信 - 预警首页VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class HomePageWarnVo
{
	/**
	 * 未生产订单
	 */
	private Integer workSaleSumQty;

	/**
	 * 未送货订单
	 */
	private Integer saleDeliverSumQty;

	/**
	 * 未入库采购
	 */
	private Integer purchStockSumQty;

	/**
	 * 未到货发外
	 */
	private Integer arriveSumQty;

	/**
	 * 未收款销售
	 */
	private Integer receiveSumQty;

	/**
	 * 未付款采购
	 */
	private Integer paymentPurchSumQty;

	/**
	 * 未付款发外
	 */
	private Integer paymentOutSourceSumQty;

	/**
	 * 未生产订单
	 */
	public Integer getWorkSaleSumQty()
	{
		return workSaleSumQty;
	}

	public void setWorkSaleSumQty(Integer workSaleSumQty)
	{
		this.workSaleSumQty = workSaleSumQty;
	}

	/**
	 * 未送货订单
	 */
	public Integer getSaleDeliverSumQty()
	{
		return saleDeliverSumQty;
	}

	public void setSaleDeliverSumQty(Integer saleDeliverSumQty)
	{
		this.saleDeliverSumQty = saleDeliverSumQty;
	}

	/**
	 * 未入库采购
	 */
	public Integer getPurchStockSumQty()
	{
		return purchStockSumQty;
	}

	public void setPurchStockSumQty(Integer purchStockSumQty)
	{
		this.purchStockSumQty = purchStockSumQty;
	}

	/**
	 * 未到货发外
	 */
	public Integer getArriveSumQty()
	{
		return arriveSumQty;
	}

	public void setArriveSumQty(Integer arriveSumQty)
	{
		this.arriveSumQty = arriveSumQty;
	}

	/**
	 * 未收款销售
	 */
	public Integer getReceiveSumQty()
	{
		return receiveSumQty;
	}

	public void setReceiveSumQty(Integer receiveSumQty)
	{
		this.receiveSumQty = receiveSumQty;
	}

	/**
	 * 未付款采购
	 */
	public Integer getPaymentPurchSumQty()
	{
		return paymentPurchSumQty;
	}

	public void setPaymentPurchSumQty(Integer paymentPurchSumQty)
	{
		this.paymentPurchSumQty = paymentPurchSumQty;
	}

	/**
	 * 未付款发外
	 */
	public Integer getPaymentOutSourceSumQty()
	{
		return paymentOutSourceSumQty;
	}

	public void setPaymentOutSourceSumQty(Integer paymentOutSourceSumQty)
	{
		this.paymentOutSourceSumQty = paymentOutSourceSumQty;
	}

	public HomePageWarnVo(Integer workSaleSumQty, Integer saleDeliverSumQty, Integer purchStockSumQty, Integer arriveSumQty, Integer receiveSumQty, Integer paymentPurchSumQty, Integer paymentOutSourceSumQty)
	{
		super();
		this.workSaleSumQty = workSaleSumQty;
		this.saleDeliverSumQty = saleDeliverSumQty;
		this.purchStockSumQty = purchStockSumQty;
		this.arriveSumQty = arriveSumQty;
		this.receiveSumQty = receiveSumQty;
		this.paymentPurchSumQty = paymentPurchSumQty;
		this.paymentOutSourceSumQty = paymentOutSourceSumQty;
	}

	public HomePageWarnVo()
	{
		super();
	}
}
