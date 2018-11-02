/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.ProgressStatusSale;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售订单：销售订单表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月22日下午5:27:53, zhengby, 代码规范
 */
@Entity
@Table(name = "sale_order")
public class SaleOrder extends SaleBaseEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 送货方式
	 */
	@Column(length = 50)
	private Long deliveryClassId;

	/**
	 * 客户订单单据编号
	 */
	@Column(length = 50)
	private String customerBillNo;

	/**
	 * 进度状态
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProgressStatusSale progressStatus;

	/**
	 * 销售订单详情列表
	 */
	@Transient
	private List<SaleOrderDetail> detailList = new ArrayList<SaleOrderDetail>();

	/**
	 * 客户信息
	 */
	@Transient
	private Customer customer = null;
	
	/**
	 * 保存时，更新报价订单
	 */
	@Transient
	private List<Long> offerOrderIdList = null;

	public Long getDeliveryClassId()
	{
		return deliveryClassId;
	}

	public void setDeliveryClassId(Long deliveryClassId)
	{
		this.deliveryClassId = deliveryClassId;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public ProgressStatusSale getProgressStatus()
	{
		return progressStatus;
	}

	public void setProgressStatus(ProgressStatusSale progressStatus)
	{
		this.progressStatus = progressStatus;
	}

	public List<SaleOrderDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<SaleOrderDetail> detailList)
	{
		this.detailList = detailList;
	}

	public String getDeliveryClassName()
	{
		if (deliveryClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DELIVERYCLASS.name(), deliveryClassId, "name");
		}
		return "-";
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	public List<Long> getOfferOrderIdList()
	{
		return offerOrderIdList;
	}

	public void setOfferOrderIdList(List<Long> offerOrderIdList)
	{
		this.offerOrderIdList = offerOrderIdList;
	}
}
