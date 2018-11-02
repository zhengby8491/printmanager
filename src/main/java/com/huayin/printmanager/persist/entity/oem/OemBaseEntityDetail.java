/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 上午9:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.oem;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代加工管理  ： 代工单明细表信息基础类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日上午9:34:06, zhengby
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class OemBaseEntityDetail extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = -7123836243155091385L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;
	
	/**
	 * 源单类型字符串
	 */
	@Transient
	private String sourceBillTypeText;
	
	/**
	 * 源单ID
	 */
	@Column(length = 20)
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
	 * 产品ID（客户的产品）
	 */
	@Column(length = 50)
	private Long productId;

	/**
	 * 产品名称
	 */
	@Column(length = 50)
	private String productName;
	
	/**
	 * 部件名称
	 */
	@Column(length = 50)
	private String partName;
	
	/**
	 * 客户公司id
	 */
	@Column(length = 50)
	private String originCompanyId;
	
	/**
	 * 客户的发外工单的工序名称
	 */
	private String originProcedureName;

	/**
	 * 客户的发外工单的工序id
	 */
	private Long originProcedureId;

	/**
	 * 客户的发外工单单号
	 */
	private String originBillNo;
	
	/**
	 * 客户的发外工单单号Id
	 */
	private Long originBillId;
	
	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 工序id
	 */
	private Long procedureId;

	/**
	 * 工序分类id
	 */
	private Long procedureClassId;

	/**
	 * 产品规格
	 */
	private String style;

	/**
	 * 数量
	 */
	private BigDecimal qty = new BigDecimal(0);

	/**
	 * 单价(含税)
	 */
	@Column(columnDefinition = "decimal(18,4) default '0.0000'")
	private BigDecimal price = new BigDecimal(0.00);

	/**
	 * 单价(不含税）
	 */
	@Column(columnDefinition = "decimal(18,4) default '0.0000'")
	private BigDecimal noTaxPrice;

	/**
	 * 金额（含税）
	 */
	@Column(columnDefinition = "decimal(18,2) default '0.00'")
	private BigDecimal money;

	/**
	 * 金额(不含税）
	 */
	@Column(columnDefinition = "decimal(18,2) default '0.00'")
	private BigDecimal noTaxMoney;

	/**
	 * 税额
	 */
	@Column(columnDefinition = "decimal(18,2) default '0.00'")
	private BigDecimal tax;

	/**
	 * 税率ID
	 */
	private Long taxRateId;

	/**
	 * 税率值
	 */
	@Column(length = 20)
	private Integer percent;

	/**
	 * 加工要求
	 */
	@Column(length = 500)
	private String processRequire;

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

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getPartName()
	{
		return partName;
	}

	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getOriginCompanyId()
	{
		return originCompanyId;
	}

	public void setOriginCompanyId(String originCompanyId)
	{
		this.originCompanyId = originCompanyId;
	}

	public String getOriginProcedureName()
	{
		return originProcedureName;
	}

	public void setOriginProcedureName(String originProcedureName)
	{
		this.originProcedureName = originProcedureName;
	}

	public Long getOriginProcedureId()
	{
		return originProcedureId;
	}

	public void setOriginProcedureId(Long originProcedureId)
	{
		this.originProcedureId = originProcedureId;
	}

	public String getOriginBillNo()
	{
		return originBillNo;
	}

	public void setOriginBillNo(String originBillNo)
	{
		this.originBillNo = originBillNo;
	}

	public Long getOriginBillId()
	{
		return originBillId;
	}

	public void setOriginBillId(Long originBillId)
	{
		this.originBillId = originBillId;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
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

	public String getStyle()
	{
		return style;
	}

	public void setStyle(String style)
	{
		this.style = style;
	}

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

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

	public Long getTaxRateId()
	{
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId)
	{
		this.taxRateId = taxRateId;
	}

	public Integer getPercent()
	{
		return percent;
	}

	public void setPercent(Integer percent)
	{
		this.percent = percent;
	}

	public String getProcessRequire()
	{
		return processRequire;
	}

	public void setProcessRequire(String processRequire)
	{
		this.processRequire = processRequire;
	}

	public String getTaxRateName()
	{
		if (taxRateId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.TAXRATE.name(), taxRateId, "name");
		}
		return "-";
	}

	public String getSourceBillTypeText()
	{
		if (sourceBillType != null)
		{
			return sourceBillType.getText();
		}
		return "-";
	}

	public void setSourceBillTypeText(String sourceBillTypeText)
	{
		this.sourceBillTypeText = sourceBillTypeText;
	}
}
