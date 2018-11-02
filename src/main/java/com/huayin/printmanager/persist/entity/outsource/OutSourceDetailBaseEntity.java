/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.outsource;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;

/**
 * <pre>
 * 发外管理 ：发外管理明细表基础信息类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月7日, zhaojt
 * @version 	   2.0, 2018年2月23日下午4:00:38, zhengby, 代码规范
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class OutSourceDetailBaseEntity extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 4160451619391354908L;

	/**
	 * 源单类型
	 */
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 源单ID
	 */
	@Column(length = 50)
	private Long sourceId;

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单单据编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 源单数量
	 */
	private BigDecimal sourceQty;

	/**
	 * 生产工单号
	 */
	@Column(length = 50)
	private String workBillNo;

	/**
	 * 生产工单id
	 */
	private Long workId;

	/**
	 * 发外类型
	 */
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private OutSourceType type;

	/**
	 * 部件名称
	 */
	@Column(length = 50)
	private String partName;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 规格
	 */
	private String style;

	/**
	 * 工序ID
	 */
	private Long procedureId;

	/**
	 * 工序CODE
	 */
	private String procedureCode;

	/**
	 * 工序分类ID
	 */
	private Long procedureClassId;

	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 工序类型
	 */
	@Enumerated(EnumType.STRING)
	private ProcedureType procedureType;

	/**
	 * 生产数量（工单产品表的生产数量）
	 */
	private Integer produceNum;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 单价（税前单价）
	 */
	private BigDecimal price;

	/**
	 * 单价（税后单价）
	 */
	private BigDecimal noTaxPrice;

	/**
	 * 金额（含税）
	 */
	private BigDecimal money;

	/**
	 * 金额(不含税）
	 */
	private BigDecimal noTaxMoney;

	/**
	 * 税额
	 */
	private BigDecimal tax;

	/**
	 * 税率ID
	 */
	private Long taxRateId;

	/**
	 * 税率名称
	 */
	private String taxRateName;

	/**
	 * 税率值
	 */
	private Integer taxRatePercent;

	/**
	 * 工单类型（主要针对轮转工单）
	 */
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	/**
	 * 工单工序类型
	 */
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private WorkProcedureType workProcedureType;

	/**
	 * 工序要求
	 */
	@Column(length = 150)
	private String processRequire;

	/**
	 * 代工平台id
	 */
	@Column(length = 20)
	private String originCompanyId;

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public BigDecimal getNoTaxPrice()
	{
		return noTaxPrice;
	}

	public void setNoTaxPrice(BigDecimal noTaxPrice)
	{
		this.noTaxPrice = noTaxPrice;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public String getProcessRequire()
	{
		return processRequire;
	}

	public void setProcessRequire(String processRequire)
	{
		this.processRequire = processRequire;
	}

	public WorkProcedureType getWorkProcedureType()
	{
		return workProcedureType;
	}

	public void setWorkProcedureType(WorkProcedureType workProcedureType)
	{
		this.workProcedureType = workProcedureType;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public BigDecimal getNoTaxMoney()
	{
		return noTaxMoney;
	}

	public void setNoTaxMoney(BigDecimal noTaxMoney)
	{
		this.noTaxMoney = noTaxMoney;
	}

	public BigDecimal getTax()
	{
		return tax;
	}

	public void setTax(BigDecimal tax)
	{
		this.tax = tax;
	}

	public String getTaxRateName()
	{
		return taxRateName;
	}

	public void setTaxRateName(String taxRateName)
	{
		this.taxRateName = taxRateName;
	}

	public Integer getTaxRatePercent()
	{
		return taxRatePercent;
	}

	public void setTaxRatePercent(Integer taxRatePercent)
	{
		this.taxRatePercent = taxRatePercent;
	}

	public OutSourceType getType()
	{
		return type;
	}

	public void setType(OutSourceType type)
	{
		this.type = type;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public Integer getProduceNum()
	{
		return produceNum;
	}

	public void setProduceNum(Integer produceNum)
	{
		this.produceNum = produceNum;
	}

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
	}

	public BillType getSourceBillType()
	{
		return sourceBillType;
	}

	public void setSourceBillType(BillType sourceBillType)
	{
		this.sourceBillType = sourceBillType;
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

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public BigDecimal getSourceQty()
	{
		return sourceQty;
	}

	public void setSourceQty(BigDecimal sourceQty)
	{
		this.sourceQty = sourceQty;
	}

	public Long getProcedureId()
	{
		return procedureId;
	}

	public void setProcedureId(Long procedureId)
	{
		this.procedureId = procedureId;
	}

	public String getProcedureCode()
	{
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode)
	{
		this.procedureCode = procedureCode;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	// -----------------------------------------------------
	public String getSourceBillTypeText()
	{

		if (sourceBillType != null)
		{

			return sourceBillType.getText();
		}
		return "-";
	}

	public String getProcedureTypeText()
	{

		if (procedureType != null)
		{
			return procedureType.getText();
		}
		return "-";
	}

	public String getTypeText()
	{
		if (type != null)
		{

			return type.getText();
		}
		return "-";
	}

	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

}
