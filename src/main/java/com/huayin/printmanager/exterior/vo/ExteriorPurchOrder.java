/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 上午10:15:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.exterior.enums.DeliveryMethod;
import com.huayin.printmanager.exterior.enums.InvoiceType;
import com.huayin.printmanager.exterior.enums.OrderStatusType;
import com.huayin.printmanager.exterior.enums.PaymentType;
import com.huayin.printmanager.exterior.enums.ShipmentType;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;

/**
 * <pre>
 * 印刷家的采购订单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日上午10:15:02, zhengby
 */
@Entity
@Table(name = "exterior_purch_order")
public class ExteriorPurchOrder extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = -2392528284253296419L;

	/**
	 * 采购单编号
	 */
	private String purchaseOrderNm;
	
	/**
	 * 用户唯一标识
	 */
	private String uid;
	
	/**
	 * 本系统的用户id
	 */
	private Long localUserId;
	
	/**
	 * 供应商
	 */
	private String sellerName;
	
	/**
	 * 供应商地址
	 */
	private String sellerAddress;
	
	/**
	 * 联系人
	 */
	private String linkMan;
	
	/**
	 * 联系电话
	 */
	private String linkPhone;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderPrice;
	
	/**
	 * 运费
	 */
	private BigDecimal orderFreight;
										 
	/**
	 * 支付类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	
	/**
	 * 配送类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ShipmentType shipmentType;
	
	/**
	 * 发票类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private InvoiceType invoiceType;
	
	/**
	 * 物流运输
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private DeliveryMethod deliveryMethod;
	
	/**
	 * shippingAddress
	 */
	private String shippingAddress;
	
	/**
	 * 印刷家平台订单号
	 */
	private String orderNm;
	
	/**
	 * 订单状态
	 */
	private OrderStatusType orderStatus;
	
	/**
	 * 订单备注
	 */
	private String orderRemark;
	
	/**
	 * 最后操作时间
	 */
	private Date lastOperatTime;
	
	/**
	 * 下游采购订单主表id（用于标记来自印刷家的订单）
	 */
	private Long purchOrderId;
	/**
	 * 采购单明细
	 */
	@Transient
	private List<ExteriorPurchOrderDetail> items = new ArrayList<ExteriorPurchOrderDetail>();

	public String getPurchaseOrderNm()
	{
		return purchaseOrderNm;
	}

	public void setPurchaseOrderNm(String purchaseOrderNm)
	{
		this.purchaseOrderNm = purchaseOrderNm;
	}

	public Long getLocalUserId()
	{
		return localUserId;
	}

	public void setLocalUserId(Long localUserId)
	{
		this.localUserId = localUserId;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getSellerName()
	{
		return sellerName;
	}

	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}

	public String getSellerAddress()
	{
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress)
	{
		this.sellerAddress = sellerAddress;
	}

	public String getLinkMan()
	{
		return linkMan;
	}

	public void setLinkMan(String linkMan)
	{
		this.linkMan = linkMan;
	}

	public String getLinkPhone()
	{
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone)
	{
		this.linkPhone = linkPhone;
	}

	public BigDecimal getOrderPrice()
	{
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice)
	{
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderFreight()
	{
		return orderFreight;
	}

	public void setOrderFreight(BigDecimal orderFreight)
	{
		this.orderFreight = orderFreight;
	}

	public PaymentType getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType)
	{
		this.paymentType = paymentType;
	}

	public ShipmentType getShipmentType()
	{
		return shipmentType;
	}

	public void setShipmentType(ShipmentType shipmentType)
	{
		this.shipmentType = shipmentType;
	}

	public InvoiceType getInvoiceType()
	{
		return invoiceType;
	}

	public void setInvoiceType(InvoiceType invoiceType)
	{
		this.invoiceType = invoiceType;
	}

	public DeliveryMethod getDeliveryMethod()
	{
		return deliveryMethod;
	}

	public void setDeliveryMethod(DeliveryMethod deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}

	public String getShippingAddress()
	{
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress)
	{
		this.shippingAddress = shippingAddress;
	}

	public String getOrderNm()
	{
		return orderNm;
	}

	public void setOrderNm(String orderNm)
	{
		this.orderNm = orderNm;
	}

	public OrderStatusType getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(OrderStatusType orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public String getOrderRemark()
	{
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark)
	{
		this.orderRemark = orderRemark;
	}

	public List<ExteriorPurchOrderDetail> getItems()
	{
		return items;
	}

	public void setItems(List<ExteriorPurchOrderDetail> items)
	{
		this.items = items;
	}

	public Date getLastOperatTime()
	{
		return lastOperatTime;
	}

	public void setLastOperatTime(Date lastOperatTime)
	{
		this.lastOperatTime = lastOperatTime;
	}

	public Long getPurchOrderId()
	{
		return purchOrderId;
	}

	public void setPurchOrderId(Long purchOrderId)
	{
		this.purchOrderId = purchOrderId;
	}

}
