/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.produce;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProductType;

/**
 * <pre>
 * 生产管理 - 生产任务表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年5月5日, minxl
 * @version 	   2.0, 2018年2月23日上午10:29:26, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_report_task")
public class WorkReportTask extends BaseBillTableEntity
{

	private static final long serialVersionUID = 4159833166945140802L;

	/**
	 * 是否显示(默认显示)
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isShow = BoolValue.YES;

	/**
	 * 部件排序
	 */
	private Integer partSort;

	/**
	 * 是否部件（默认“是”）
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isPart = BoolValue.YES;

	/**
	 * 是否排成（默认“是”）
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isSchedule = BoolValue.YES;

	/**
	 * 是否发外（默认“否”）
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isOutSource = BoolValue.NO;

	/**
	 * 工序排序
	 */
	private int sort;

	/**
	 * 工序表ID
	 */
	private Long procedureRefId;

	/**
	 * 工单类型
	 */
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	/**
	 * 生产数量
	 */
	private Integer produceQty;

	/**
	 * 应产数
	 */
	private BigDecimal yieldQty;

	/**
	 * 已上报数量
	 */
	private BigDecimal reportQty = new BigDecimal(0);

	/**
	 * 未上报数量
	 */
	private BigDecimal unreportQty;

	/**
	 * 交货日期
	 */
	private Date deliveryTime;

	/**
	 * 生产日期
	 */
	private Date createTime;

	/**
	 * 上报时间
	 * */
	private Date updateTime;

	/**
	 * 销售单号  可以多个
	 */
	@Column(length = 200)
	private String sourceBillNo;

	/**
	 * 工单编号
	 */
	@Column(length = 50)
	private String billNo;

	/**
	 * 客户名称
	 */
	@Column(length = 200)
	private String customerName;
	
	/**
	 * 客户id(由于一个customerName可以存在多个客户名)
	 */
	@Column(length = 300)
	private String customerId;

	/**
	 * 客户料号
	 */
	private String customerMaterialCode;

	/**
	 * 成品名称 产品名称
	 */
	@Column(length = 200)
	private String productName;

	/**
	 * 成品id(由于一个customerName可以存在多个客户名)
	 */
	@Column(length = 300)
	private String productId;
	
	/**
	 * 产品规格
	 */
	@Column(length = 200)
	private String specifications;

	/**
	 * 工序id
	 */
	private Long procedureId;

	/**
	 * 工序分类ID
	 */
	private Long procedureClassId;

	/**
	 * 工序类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProcedureType procedureType;

	/**
	 * 工序代码
	 */
	@Column(length = 50)
	private String procedureCode;

	/**
	 * 工序名称
	 */
	@Column(length = 50)
	private String procedureName;

	/**
	 * 部件id
	 */
	@Column
	private Long partId;

	/**
	 * 部件名称
	 */
	@Column(length = 50)
	private String partName;

	/**
	 * 上机规格
	 */
	private String style;

	/**
	 * 机台名称
	 */
	@Column(length = 50)
	private String machineName;

	/**
	 * 客户订单单据编号
	 */
	@Column(length = 50)
	private String customerBillNo;

	@Transient
	private Date startTime = new Date(System.currentTimeMillis());

	@Transient
	private Date endTime = new Date(System.currentTimeMillis());

	/**
	 * 合格数量
	 */
	@Transient
	private BigDecimal qualifiedQty = new BigDecimal(0);

	/**
	 *不合格数量
	 */
	@Transient
	private BigDecimal unqualified = new BigDecimal(0);

	public Long getProcedureRefId()
	{
		return procedureRefId;
	}

	public void setProcedureRefId(Long procedureRefId)
	{
		this.procedureRefId = procedureRefId;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public Integer getProduceQty()
	{
		return produceQty;
	}

	public void setProduceQty(Integer produceQty)
	{
		this.produceQty = produceQty;
	}

	public BigDecimal getYieldQty()
	{
		return yieldQty;
	}

	public void setYieldQty(BigDecimal yieldQty)
	{
		this.yieldQty = yieldQty;
	}

	public BigDecimal getReportQty()
	{
		return reportQty;
	}

	public void setReportQty(BigDecimal reportQty)
	{
		this.reportQty = reportQty;
	}

	public BigDecimal getUnreportQty()
	{
		return unreportQty;
	}

	public void setUnreportQty(BigDecimal unreportQty)
	{
		this.unreportQty = unreportQty;
	}

	public Date getDeliveryTime()
	{
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime)
	{
		this.deliveryTime = deliveryTime;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	
	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	public Long getPartId()
	{
		return partId;
	}

	public void setPartId(Long partId)
	{
		this.partId = partId;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public String getMachineName()
	{
		return machineName;
	}

	public void setMachineName(String machineName)
	{
		this.machineName = machineName;
	}

	public BoolValue getIsPart()
	{
		return isPart;
	}

	public void setIsPart(BoolValue isPart)
	{
		this.isPart = isPart;
	}

	public BoolValue getIsSchedule()
	{
		return isSchedule;
	}

	public void setIsSchedule(BoolValue isSchedule)
	{
		this.isSchedule = isSchedule;
	}

	public BoolValue getIsOutSource()
	{
		return isOutSource;
	}

	public void setIsOutSource(BoolValue isOutSource)
	{
		this.isOutSource = isOutSource;
	}

	public int getSort()
	{
		return sort;
	}

	public void setSort(int sort)
	{
		this.sort = sort;
	}

	public Long getProcedureId()
	{
		return procedureId;
	}

	public void setProcedureId(Long procedureId)
	{
		this.procedureId = procedureId;
	}

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public String getProcedureCode()
	{
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode)
	{
		this.procedureCode = procedureCode;
	}

	public String getCustomerBillNo()
	{
		return customerBillNo;
	}

	public void setCustomerBillNo(String customerBillNo)
	{
		this.customerBillNo = customerBillNo;
	}

	public Integer getPartSort()
	{
		return partSort;
	}

	public void setPartSort(Integer partSort)
	{
		this.partSort = partSort;
	}

	public BoolValue getIsShow()
	{
		return isShow;
	}

	public void setIsShow(BoolValue isShow)
	{
		this.isShow = isShow;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime == null ? new Date() : endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public BigDecimal getQualifiedQty()
	{
		return qualifiedQty;
	}

	public void setQualifiedQty(BigDecimal qualifiedQty)
	{
		this.qualifiedQty = qualifiedQty;
	}

	public BigDecimal getUnqualified()
	{
		return unqualified;
	}

	public void setUnqualified(BigDecimal unqualified)
	{
		this.unqualified = unqualified;
	}
}
