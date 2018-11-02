/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月28日 上午10:34:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

import java.util.List;

/**
 * <pre>
 * 响应body
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月28日上午10:34:02, zhengby
 */
public class RspBody
{
	/**
	 * 校验状态
	 */
	private String state;
	
	/**
	 * 商户id
	 */
	private String uid;
	
	/**
	 * token
	 */
	private String token;
	
	/**
	 * 是否印刷家商户
	 */
	private String isPHUser;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 联系人
	 */
	private String linkMan;
	
	/**
	 * 联系人电话
	 */
	private String linkPhoneNum;
	
	/**
	 * 校验状态
	 */
	private String verifyState;
	
	/**
	 * 授权url
	 */
	private String forwardURL;
	
	/**
	 * 扩展信息
	 */
	private ExtendInfo extendInfo;
	
	//采购订单
	/**
	 * 采购单编号
	 */
	private String purchaseOrderNm; 
	/**
	 * 联系方式
	 */
	private String linkPhone;
	/**
	 * 供应商
	 */
	private String sellerName;
	
	/**
	 * 供应商地址
	 */
	private String sellerAddress;
	
	/**
	 * 订单金额
	 */
	private String orderPrice;
	
	/**
	 * 运费
	 */
	private String orderFreight;
	               
	/**
	 * 支付类型
	 */
	private String paymentType;
	
	/**
	 * 配送类型
	 */
	private String shipmentType;
	
	/**
	 * 发票类型
	 */
	private String invoiceType;
	
	/**
	 * 物流运输
	 */
	private String deliveryMethod;
	
	/**
	 * 收货地址
	 */
	private String shippingAddress;
	
	/**
	 * 印刷家平台订单号
	 */
	private String orderNm;
	
	/**
	 * 订单状态
	 */
	private String orderStatus;
	
	/**
	 * 订单备注
	 */
	private String orderRemark;
	
	/**
	 * 采购单明细
	 */
	private List<RspPurchItems> items;
	
	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getIsPHUser()
	{
		return isPHUser;
	}

	public void setIsPHUser(String isPHUser)
	{
		this.isPHUser = isPHUser;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getLinkMan()
	{
		return linkMan;
	}

	public void setLinkMan(String linkMan)
	{
		this.linkMan = linkMan;
	}

	public String getLinkPhoneNum()
	{
		return linkPhoneNum;
	}

	public void setLinkPhoneNum(String linkPhoneNum)
	{
		this.linkPhoneNum = linkPhoneNum;
	}

	public String getVerifyState()
	{
		return verifyState;
	}

	public void setVerifyState(String verifyState)
	{
		this.verifyState = verifyState;
	}

	public String getForwardURL()
	{
		return forwardURL;
	}

	public void setForwardURL(String forwardURL)
	{
		this.forwardURL = forwardURL;
	}

	public ExtendInfo getExtendInfo()
	{
		return extendInfo;
	}

	public void setExtendInfo(ExtendInfo extendInfo)
	{
		this.extendInfo = extendInfo;
	}

	public String getPurchaseOrderNm()
	{
		return purchaseOrderNm;
	}

	public void setPurchaseOrderNm(String purchaseOrderNm)
	{
		this.purchaseOrderNm = purchaseOrderNm;
	}

	public String getLinkPhone()
	{
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone)
	{
		this.linkPhone = linkPhone;
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

	public String getOrderPrice()
	{
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice)
	{
		this.orderPrice = orderPrice;
	}

	public String getOrderFreight()
	{
		return orderFreight;
	}

	public void setOrderFreight(String orderFreight)
	{
		this.orderFreight = orderFreight;
	}

	public String getPaymentType()
	{
		return paymentType;
	}

	public void setPaymentType(String paymentType)
	{
		this.paymentType = paymentType;
	}

	public String getShipmentType()
	{
		return shipmentType;
	}

	public void setShipmentType(String shipmentType)
	{
		this.shipmentType = shipmentType;
	}

	public String getInvoiceType()
	{
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType)
	{
		this.invoiceType = invoiceType;
	}

	public String getDeliveryMethod()
	{
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod)
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

	public String getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus)
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

	public List<RspPurchItems> getItems()
	{
		return items;
	}

	public void setItems(List<RspPurchItems> items)
	{
		this.items = items;
	}
	
}
