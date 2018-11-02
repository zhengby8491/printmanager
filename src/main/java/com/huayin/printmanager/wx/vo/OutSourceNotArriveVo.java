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
import java.util.List;

import com.huayin.printmanager.utils.DateUtils;

/**
 * <pre>
 * 微信 - 未到货发外
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class OutSourceNotArriveVo
{
	private Long id;

	/**
	 * 交货天数
	 */
	private Double deliveryDay;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 交货剩余天数百分比
	 */
	private BigDecimal deliveryPercent;

	/**
	 * 生产单号
	 */
	private String workBillNo;

	/**
	 * 成品名称
	 */
	private String productName;

	/**
	 * 发外单号
	 */
	private String billNo;

	/**
	 * 加工商名称
	 */
	private String supplierName;

	/**
	 * 工序对应的成品名称集合
	 */
	private List<String> name;

	/**
	 * 规格
	 */
	private String style;

	/**
	 * 单位
	 */
	private String unitName;

	/**
	 * 加工数量
	 */
	private Integer qty;

	/**
	 * 到货数量
	 */
	private Integer arriveQty;

	/**
	 * 创建日期
	 */
	private Date createTime;

	/**
	 * 工序ID
	 */
	private Long procedureId;

	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 发外类型
	 */
	private String type;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Double getDeliveryDay()
	{
		return deliveryDay;
	}

	public void setDeliveryDay(Double deliveryDay)
	{
		this.deliveryDay = deliveryDay;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
		this.setDeliveryDay(DateUtils.getDistanceOfTwoDate(new Date(), deliveryTime));
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
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

	public List<String> getName()
	{
		return name;
	}

	public void setName(List<String> name)
	{
		this.name = name;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public BigDecimal getDeliveryPercent()
	{
		return deliveryPercent;
	}

	public void setDeliveryPercent(BigDecimal deliveryPercent)
	{
		this.deliveryPercent = deliveryPercent;
	}

	public Integer getArriveQty()
	{
		return arriveQty;
	}

	public void setArriveQty(Integer arriveQty)
	{
		this.arriveQty = arriveQty;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Long getProcedureId()
	{
		return procedureId;
	}

	public void setProcedureId(Long procedureId)
	{
		this.procedureId = procedureId;
	}

	public String getUnitName()
	{
		return unitName;
	}

	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

}
