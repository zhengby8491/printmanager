/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.purch;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.WorkMaterialType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 ： 采购模块明细表基础类
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月15日,zhaojt
 * @version 	   2.0, 2018年2月23日上午11:33:41, zhengby, 代码规范
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchDetailBaseEntity extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 源单ID(例如：如果施工单据类型-->取施工单材料表WorkMaterial表id)
	 */
	@Column(length = 50)
	private Long sourceId;

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单单据编号(例如：如果施工单据类型-->取施工单表Work表billNo)
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 工单材料类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private WorkMaterialType workMaterialType;

	/**
	 * 父ID（部件ID或者成品ID)
	 */
	private Long parentId;

	/**
	 * 源单数量(例如：如果施工单据类型-->取施工单材料表WorkMaterial表qty)
	 */
	private BigDecimal sourceQty;

	/**
	 * 材料id
	 */
	@Column(length = 50)
	private Long materialId;

	/**
	 * 材料分类
	 */
	@Column(length = 50)
	private Long materialClassId;

	/**
	 * 材料编号
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 材料名称
	 */
	@Column(length = 50)
	private String materialName;

	/**
	 * 材料规格
	 */
	@Column(length = 20)
	private String specifications;

	/**
	 * 采购单位
	 */
	private Long unitId;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 计价单位
	 */
	private Long valuationUnitId;

	/**
	 * 计价数量
	 */
	@Column(precision = 19, scale = 4)
	private BigDecimal valuationQty;

	/**
	 * 单价(含税)
	 */
	private BigDecimal price;

	/**
	 * 单价(不含税）
	 */
	private BigDecimal noTaxPrice;

	/**
	 * 计价单价(含税)
	 */
	private BigDecimal valuationPrice;

	/**
	 * 计价单价(不含税）
	 */
	private BigDecimal noTaxValuationPrice;

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
	 * 税率值
	 */
	@Column(length = 20)
	private Integer percent;

	/**
	 * 采购单位
	 */
	private String purchUnitName;

	/**
	 * 计价单位
	 */
	private String valuationUnitName;

	/**
	 * 税收
	 */
	private String trName;
	
	/**
	 * 标记来自外部创建的单据
	 */
	private Long extOrderId;
	
	private Long extOrderDetailId;
	
	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
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

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
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

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public BigDecimal getNoTaxValuationPrice()
	{
		return noTaxValuationPrice;
	}

	public void setNoTaxValuationPrice(BigDecimal noTaxValuationPrice)
	{
		this.noTaxValuationPrice = noTaxValuationPrice;
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

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getMaterialName()
	{
		return materialName;
	}

	public void setMaterialName(String materialName)
	{
		this.materialName = materialName;
	}

	public void setPurchUnitName(String purchUnitName)
	{
		this.purchUnitName = purchUnitName;
	}

	public String getPurchUnitName()
	{
		return purchUnitName;
	}

	public String getValuationUnitName()
	{
		return valuationUnitName;
	}

	public void setValuationUnitName(String valuationUnitName)
	{
		this.valuationUnitName = valuationUnitName;
	}

	public String getTrName()
	{
		return trName;
	}

	public void setTrName(String trName)
	{
		this.trName = trName;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public WorkMaterialType getWorkMaterialType()
	{
		return workMaterialType;
	}

	public void setWorkMaterialType(WorkMaterialType workMaterialType)
	{
		this.workMaterialType = workMaterialType;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public Long getExtOrderId()
	{
		return extOrderId;
	}

	public void setExtOrderId(Long extOrderId)
	{
		this.extOrderId = extOrderId;
	}

	public Long getExtOrderDetailId()
	{
		return extOrderDetailId;
	}

	public void setExtOrderDetailId(Long extOrderDetailId)
	{
		this.extOrderDetailId = extOrderDetailId;
	}

	// ----------------------------------------------------------------------------------------
	public String getSourceBillTypeText()
	{

		if (sourceBillType != null)
		{

			return sourceBillType.getText();
		}
		return "-";
	}

	public String getMaterialClassName()
	{
		if (materialClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.MATERIALCLASS.name(), materialClassId, "name");
		}
		return "-";
	}

	public String getUnitName()
	{

		if (unitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "name");
		}
		return "-";
	}

	/**
	 * <pre>
	 * 计价单位精度
	 * </pre>
	 * @return
	 */
	public Integer getValuationUnitAccuracy()
	{
		if (valuationUnitId != null)
		{
			return (Integer) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), valuationUnitId, "accuracy");
		}
		return 0;
	}

	/**
	 * <pre>
	 * 库存单位精度
	 * </pre>
	 * @return
	 */
	public Integer getStockUnitAccuracy()
	{
		if (unitId != null)
		{
			return (Integer) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "accuracy");
		}
		return 0;
	}
}
