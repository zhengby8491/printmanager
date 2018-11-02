/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月26日 上午9:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.purch.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 采购进度追踪
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年10月8日, raintear
 * @version 	   2.0, 2018年2月26日上午9:53:00, zhengby, 代码规范
 */
public class PurchScheduleVo
{
	private Long id;

	private Date createTime;

	private String workNo;

	private String billNo;

	/**
	 * 采购订单ID
	 */
	private Long purchOrderId;
	
	private Long workId;
	
	private String supplierName;

	private String employeeName;

	private String materialName;

	private String style;

	private Integer weight;

	private String unit;

	private Date deliveryTime;

	private BigDecimal purchQty = new BigDecimal("0");

	private BigDecimal money = new BigDecimal("0");

	private BigDecimal stockQty = new BigDecimal("0");

	private BigDecimal refundQty = new BigDecimal("0");

	private BigDecimal reconcilQty = new BigDecimal("0");

	private BigDecimal reconcilMoney = new BigDecimal("0");

	private BigDecimal paymentMoney = new BigDecimal("0");

	private String workBillNo;
	
	/**
	 * 产品名称列表
	 */
	private List<WorkProduct> productList = Lists.newArrayList();

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public Long getPurchOrderId()
	{
		return purchOrderId;
	}

	public void setPurchOrderId(Long purchOrderId)
	{
		this.purchOrderId = purchOrderId;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getUnit()
	{
		return unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public BigDecimal getPurchQty()
	{
		return purchQty;
	}

	public void setPurchQty(BigDecimal purchQty)
	{
		this.purchQty = purchQty;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(BigDecimal stockQty)
	{
		this.stockQty = stockQty;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public BigDecimal getReconcilMoney()
	{
		return reconcilMoney;
	}

	public void setReconcilMoney(BigDecimal reconcilMoney)
	{
		this.reconcilMoney = reconcilMoney;
	}

	public BigDecimal getPaymentMoney()
	{
		return paymentMoney;
	}

	public void setPaymentMoney(BigDecimal paymentMoney)
	{
		this.paymentMoney = paymentMoney;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public BigDecimal getRefundQty()
	{
		return refundQty;
	}

	public void setRefundQty(BigDecimal refundQty)
	{
		this.refundQty = refundQty;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getWorkNo()
	{
		return workNo;
	}

	public void setWorkNo(String workNo)
	{
		this.workNo = workNo;
	}
	
	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		else
		{
			sb.append("-");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
