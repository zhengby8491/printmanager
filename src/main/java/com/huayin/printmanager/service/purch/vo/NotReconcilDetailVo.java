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

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 未对账明细VO（入库/退货）
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年7月7日, raintear
 * @version 	   2.0, 2018年2月26日上午9:51:34, zhengby, 代码规范
 */
public class NotReconcilDetailVo
{
	/**
	 * 详情ID
	 */
	private Long id;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String specifications;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 采购单位
	 */
	private String purchUnitName;

	/**
	 * 退货/入库数量
	 */
	private BigDecimal qty;

	/**
	 * 已对账数量
	 */
	private BigDecimal reconcilQty;

	/**
	 * 源单类型（取转入表的单据类型 命名为billType）
	 */
	@Enumerated(EnumType.STRING)
	private BillType billType;

	/**
	 * 源单号 （查询时直接带入   命名为billNo）
	 */
	private String billNo;

	/**
	 * 源单号(入库单 的源单单号 为采购订单号 )
	 */
	private String sourceBillNo;

	/**
	 * 采购订单号
	 */
	private String orderBillNo;

	/**
	 * 生产工单单号
	 */
	private String workBillNo;

	/**
	 * 生产工单id
	 */
	private Long workId;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 供应商名称
	 */
	private String supplierName;

	private Date createTime;

	/**
	 * 备注
	 */
	private String memo;
	

	/**
	 * 产品名称列表
	 */
	private List<WorkProduct> productList = Lists.newArrayList();

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
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

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getOrderBillNo()
	{
		return orderBillNo;
	}

	public void setOrderBillNo(String orderBillNo)
	{
		this.orderBillNo = orderBillNo;
	}

	public String getPurchUnitName()
	{
		return purchUnitName;
	}

	public void setPurchUnitName(String purchUnitName)
	{
		this.purchUnitName = purchUnitName;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getReconcilQty()
	{
		return reconcilQty;
	}

	public void setReconcilQty(BigDecimal reconcilQty)
	{
		this.reconcilQty = reconcilQty;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public String getBillTypeText()
	{
		if (billType != null)
		{

			return billType.getText();
		}
		return "-";
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
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

	public String getSupplierName()
	{
		return supplierName;
	}

	public void setSupplierName(String supplierName)
	{
		this.supplierName = supplierName;
	}

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
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
