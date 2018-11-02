/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年1月16日 下午2:21:03
 * Copyright: 	Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.huayin.printmanager.persist.enumerate.BillType;

/**
 * <pre>
 * 未清生产工单明细转工单vo
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年1月16日下午2:21:03
 */
public class WorkCreateVo
{
	/**
	 * 产品id
	 */
	private Long id;
	/**
	 * 产品名称
	 */
	private String name;
	
	/**
	 * 产品规格
	 */
	private String specifications;
	
	/**
	 * 单位id
	 */
	private Long unitId;
	
	private Integer sourceQty;
	
	private Integer saleProduceQty;
	
	private Integer spareProduceQty;
	
	/**
	 * 源单id
	 */
	private Long sourceId;
	
	private Long sourceDetailId;
	
	private BillType sourceBillType;
	
	private String sourceBillNo;
	
	/**
	 * 客户单号
	 */
	private String customerBillNo;
	
	private Long customerId;
	
	private String customerCode;
	
	private String customerName;
	
	private String customerMaterialCode;
	
	private Date deliveryTime;
	
	private String customerRequire;
	
	private String memo;
	
	private BigDecimal salePrice;
	
	private String imgUrl;
	
	private BigDecimal money;
	
	private String code;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public Long getUnitId()
	{
		return unitId;
	}

	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public Integer getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(Integer sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public Integer getSaleProduceQty()
	{
		return saleProduceQty;
	}

	public void setSaleProduceQty(Integer saleProduceQty)
	{
		this.saleProduceQty = saleProduceQty;
	}

	public Integer getSpareProduceQty()
	{
		return spareProduceQty;
	}

	public void setSpareProduceQty(Integer spareProduceQty)
	{
		this.spareProduceQty = spareProduceQty;
	}

	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public BillType getSourceBillType()
	{
		return sourceBillType;
	}

	public void setSourceBillType(BillType sourceBillType)
	{
		this.sourceBillType = sourceBillType;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerCode()
	{
		return customerCode;
	}

	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public String getCustomerRequire()
	{
		return customerRequire;
	}

	public void setCustomerRequire(String customerRequire)
	{
		this.customerRequire = customerRequire;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public BigDecimal getSalePrice()
	{
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice)
	{
		this.salePrice = salePrice;
	}

	public String getImgUrl()
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}
	
}
