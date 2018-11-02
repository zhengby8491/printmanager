/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.vo;

import java.math.BigDecimal;

/**
 * <pre>
 * 生产工单vo
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年10月26日,zhaojt
 * @version 	   2.0, 2018年2月26日上午11:04:07, zhengby, 代码规范
 */
public class WorkDeskVo
{
	/**
	 * 销售总量
	 */
	private Integer saleTotalQty;

	/**
	 * 销售总额
	 */
	private BigDecimal saleTotalMoney;

	/**
	 * 采购总量
	 */
	private BigDecimal purchTotalQty;

	/**
	 * 采购总额
	 */
	private BigDecimal purchTotalMoney;

	/**
	 * 客户欠款
	 */
	private BigDecimal customerDebt;

	/**
	 * 供应商欠款
	 */
	private BigDecimal supplierDebt;

	/**
	 * 成品库存总量
	 */
	private Integer productStockQty;

	/**
	 * 成品库存金额
	 */
	private BigDecimal productStockMoney;

	/**
	 * 材料库存总量
	 */
	private BigDecimal materialStockQty;

	/**
	 * 材料库存金额
	 */
	private BigDecimal materialStockMoney;

	/**
	 * 低于材料最低库存
	 */
	private Integer materialMinStock;

	/**
	 * 3日内未到货发外
	 */
	private Integer notArriveOutSource;

	/**
	 * 3日未入库采购
	 */
	private Integer notStockPurch;

	/**
	 * 3日未送货订单
	 */
	private Integer notDeliveSale;

	/**
	 * 3日未付款账单
	 */
	private Integer notPayment;

	/**
	 * 3日内未收款账单
	 */
	private Integer notReceiveOrder;

	/**
	 * 销售订单待审核
	 */
	private Integer notCheckSale;

	/**
	 * 采购订单待审核
	 */
	private Integer notCheckPurch;

	/**
	 * 生产工单待审核
	 */
	private Integer notCheckWork;

	/**
	 * 待审核付款单
	 */
	private Integer notCheckPayment;

	/**
	 * 待审核付款核销单
	 */
	private Integer notCheckWriteoffPayment;

	/**
	 * 待审核发外加工单
	 */
	private Integer notCheckOutSource;

	/**
	 * 待审核收款单
	 */
	private Integer notCheckReceive;

	/**
	 * 待审核收款核销单
	 */
	private Integer notCheckWriteoffReceive;

	/**
	 * 未生产订单 数量
	 */
	private Integer workSaleSumQty;

	/**
	 * 未送货订单数量
	 */
	private Integer deliverSumQty;

	/**
	 * 待采购入库数量
	 */
	private Integer purchSumQty;

	/**
	 * 未到货发外数量
	 */
	private Integer arriveSumQty;

	/**
	 * 未收款销售数量
	 */
	private Integer receiveSumQty;

	/**
	 * 未付款采购数量
	 */
	private Integer paymentPurchSumQty;

	/**
	 * 未付款发外数量
	 */
	private Integer paymentOutSourceSumQty;

	/**
	 * 待采购物料数量
	 */
	private Integer workPurchQty;

	/**
	 * 待领料物料数量
	 */
	private Integer workTakeQty;

	/**
	 * 待采购对账数量
	 */
	private Integer purchReconcilQty;

	/**
	 * 待销售对账数量
	 */
	private Integer saleReconcilQty;

	/**
	 * 待加工对账数量
	 */
	private Integer processReconcilQty;

	/**
	 * 待入库工单
	 */
	private Integer workStockQty;

	public Integer getSaleTotalQty()
	{
		return saleTotalQty;
	}

	public void setSaleTotalQty(Integer saleTotalQty)
	{
		this.saleTotalQty = saleTotalQty;
	}

	public BigDecimal getSaleTotalMoney()
	{
		return saleTotalMoney;
	}

	public void setSaleTotalMoney(BigDecimal saleTotalMoney)
	{
		this.saleTotalMoney = saleTotalMoney;
	}

	public BigDecimal getPurchTotalQty()
	{
		return purchTotalQty;
	}

	public void setPurchTotalQty(BigDecimal purchTotalQty)
	{
		this.purchTotalQty = purchTotalQty;
	}

	public BigDecimal getPurchTotalMoney()
	{
		return purchTotalMoney;
	}

	public void setPurchTotalMoney(BigDecimal purchTotalMoney)
	{
		this.purchTotalMoney = purchTotalMoney;
	}

	public BigDecimal getCustomerDebt()
	{
		return customerDebt;
	}

	public void setCustomerDebt(BigDecimal customerDebt)
	{
		this.customerDebt = customerDebt;
	}

	public BigDecimal getSupplierDebt()
	{
		return supplierDebt;
	}

	public void setSupplierDebt(BigDecimal supplierDebt)
	{
		this.supplierDebt = supplierDebt;
	}

	public Integer getProductStockQty()
	{
		return productStockQty;
	}

	public void setProductStockQty(Integer productStockQty)
	{
		this.productStockQty = productStockQty;
	}

	public BigDecimal getProductStockMoney()
	{
		return productStockMoney;
	}

	public void setProductStockMoney(BigDecimal productStockMoney)
	{
		this.productStockMoney = productStockMoney;
	}

	public BigDecimal getMaterialStockQty()
	{
		return materialStockQty;
	}

	public void setMaterialStockQty(BigDecimal materialStockQty)
	{
		this.materialStockQty = materialStockQty;
	}

	public BigDecimal getMaterialStockMoney()
	{
		return materialStockMoney;
	}

	public void setMaterialStockMoney(BigDecimal materialStockMoney)
	{
		this.materialStockMoney = materialStockMoney;
	}

	public Integer getMaterialMinStock()
	{
		return materialMinStock;
	}

	public void setMaterialMinStock(Integer materialMinStock)
	{
		this.materialMinStock = materialMinStock;
	}

	public Integer getNotArriveOutSource()
	{
		return notArriveOutSource;
	}

	public void setNotArriveOutSource(Integer notArriveOutSource)
	{
		this.notArriveOutSource = notArriveOutSource;
	}

	public Integer getNotStockPurch()
	{
		return notStockPurch;
	}

	public void setNotStockPurch(Integer notStockPurch)
	{
		this.notStockPurch = notStockPurch;
	}

	public Integer getNotDeliveSale()
	{
		return notDeliveSale;
	}

	public void setNotDeliveSale(Integer notDeliveSale)
	{
		this.notDeliveSale = notDeliveSale;
	}

	public Integer getNotPayment()
	{
		return notPayment;
	}

	public void setNotPayment(Integer notPayment)
	{
		this.notPayment = notPayment;
	}

	public Integer getNotReceiveOrder()
	{
		return notReceiveOrder;
	}

	public void setNotReceiveOrder(Integer notReceiveOrder)
	{
		this.notReceiveOrder = notReceiveOrder;
	}

	public Integer getNotCheckSale()
	{
		return notCheckSale;
	}

	public void setNotCheckSale(Integer notCheckSale)
	{
		this.notCheckSale = notCheckSale;
	}

	public Integer getNotCheckPurch()
	{
		return notCheckPurch;
	}

	public void setNotCheckPurch(Integer notCheckPurch)
	{
		this.notCheckPurch = notCheckPurch;
	}

	public Integer getNotCheckWork()
	{
		return notCheckWork;
	}

	public void setNotCheckWork(Integer notCheckWork)
	{
		this.notCheckWork = notCheckWork;
	}

	public Integer getNotCheckPayment()
	{
		return notCheckPayment;
	}

	public void setNotCheckPayment(Integer notCheckPayment)
	{
		this.notCheckPayment = notCheckPayment;
	}

	public Integer getNotCheckOutSource()
	{
		return notCheckOutSource;
	}

	public void setNotCheckOutSource(Integer notCheckOutSource)
	{
		this.notCheckOutSource = notCheckOutSource;
	}

	public Integer getNotCheckReceive()
	{
		return notCheckReceive;
	}

	public void setNotCheckReceive(Integer notCheckReceive)
	{
		this.notCheckReceive = notCheckReceive;
	}

	public Integer getWorkSaleSumQty()
	{
		return workSaleSumQty;
	}

	public void setWorkSaleSumQty(Integer workSaleSumQty)
	{
		this.workSaleSumQty = workSaleSumQty;
	}

	public Integer getDeliverSumQty()
	{
		return deliverSumQty;
	}

	public void setDeliverSumQty(Integer deliverSumQty)
	{
		this.deliverSumQty = deliverSumQty;
	}

	public Integer getPurchSumQty()
	{
		return purchSumQty;
	}

	public void setPurchSumQty(Integer purchSumQty)
	{
		this.purchSumQty = purchSumQty;
	}

	public Integer getArriveSumQty()
	{
		return arriveSumQty;
	}

	public void setArriveSumQty(Integer arriveSumQty)
	{
		this.arriveSumQty = arriveSumQty;
	}

	public Integer getReceiveSumQty()
	{
		return receiveSumQty;
	}

	public void setReceiveSumQty(Integer receiveSumQty)
	{
		this.receiveSumQty = receiveSumQty;
	}

	public Integer getPaymentPurchSumQty()
	{
		return paymentPurchSumQty;
	}

	public void setPaymentPurchSumQty(Integer paymentPurchSumQty)
	{
		this.paymentPurchSumQty = paymentPurchSumQty;
	}

	public Integer getPaymentOutSourceSumQty()
	{
		return paymentOutSourceSumQty;
	}

	public void setPaymentOutSourceSumQty(Integer paymentOutSourceSumQty)
	{
		this.paymentOutSourceSumQty = paymentOutSourceSumQty;
	}

	public Integer getNotCheckWriteoffPayment()
	{
		return notCheckWriteoffPayment;
	}

	public void setNotCheckWriteoffPayment(Integer notCheckWriteoffPayment)
	{
		this.notCheckWriteoffPayment = notCheckWriteoffPayment;
	}

	public Integer getNotCheckWriteoffReceive()
	{
		return notCheckWriteoffReceive;
	}

	public void setNotCheckWriteoffReceive(Integer notCheckWriteoffReceive)
	{
		this.notCheckWriteoffReceive = notCheckWriteoffReceive;
	}

	public Integer getWorkPurchQty()
	{
		return workPurchQty;
	}

	public void setWorkPurchQty(Integer workPurchQty)
	{
		this.workPurchQty = workPurchQty;
	}

	public Integer getWorkTakeQty()
	{
		return workTakeQty;
	}

	public void setWorkTakeQty(Integer workTakeQty)
	{
		this.workTakeQty = workTakeQty;
	}

	public Integer getPurchReconcilQty()
	{
		return purchReconcilQty;
	}

	public void setPurchReconcilQty(Integer purchReconcilQty)
	{
		this.purchReconcilQty = purchReconcilQty;
	}

	public Integer getSaleReconcilQty()
	{
		return saleReconcilQty;
	}

	public void setSaleReconcilQty(Integer saleReconcilQty)
	{
		this.saleReconcilQty = saleReconcilQty;
	}

	public Integer getProcessReconcilQty()
	{
		return processReconcilQty;
	}

	public void setProcessReconcilQty(Integer processReconcilQty)
	{
		this.processReconcilQty = processReconcilQty;
	}

	public Integer getWorkStockQty()
	{
		return workStockQty;
	}

	public void setWorkStockQty(Integer workStockQty)
	{
		this.workStockQty = workStockQty;
	}

}
