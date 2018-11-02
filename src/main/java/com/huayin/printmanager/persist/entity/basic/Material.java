/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.persist.enumerate.OfferMaterialType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_material")
public class Material extends BaseBasicTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 材料代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 材料名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 品牌
	 */
	@Column(length = 50)
	private String brand;

	/**
	 * 材料简称
	 */
	@Column(length = 20)
	private String shortName;

	/**
	 * 材料分类
	 */
	@Column(length = 50)
	private Long materialClassId;

	/**
	 * 材料类别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private MaterialType materialType;

	/**
	 * 库存单位
	 */
	@Column(length = 20)
	private Long stockUnitId;

	/**
	 * 计价单位
	 */
	@Column(length = 20)
	private Long valuationUnitId;

	/**
	 * 生产单位
	 */
	@Column(length = 20)
	private Long produceUnitId;

	/**
	 * 销售单位
	 */
	@Column(length = 20)
	private Long saleUnitId;

	/**
	 * 采购单价
	 */
	private BigDecimal purchPrice;

	/**
	 * 最近采购单价
	 */
	private BigDecimal lastPurchPrice;

	/**
	 * 销售单价
	 */
	private BigDecimal salePrice;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 保质期（天）
	 */
	private Integer shelfLife;

	/**
	 * 是否采购材料
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isPurch;

	/**
	 * 是否销售材料
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isSale;

	/**
	 * 是否生产材料
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isProduce;

	/**
	 * 算料公式
	 */
	@Column(length = 100)
	private String calculFormula;

	/**
	 * 是否有效
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isValid = BoolValue.YES;

	/**
	 * 最小库存量
	 */
	private BigDecimal minStockNum;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 材料分类实体
	 */
	@Transient
	private MaterialClass materialClass;

	/**
	 * 库存单位对象
	 */
	@Transient
	private Unit stockUnit;

	/**
	 * 销售单位对象
	 */
	@Transient
	private Unit saleUnit;

	/**
	 * 报价系统材料类型 1：基础材料  2：坑纸
	 */
	@Transient
	private OfferMaterialType offerMaterialType = null;
	
	/**
	 * 报价系统材料分类名称
	 */
	@Transient
	private String offerClassName = null;

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBrand()
	{
		return brand;
	}

	public void setBrand(String brand)
	{
		this.brand = brand;
	}

	public MaterialType getMaterialType()
	{
		return materialType;
	}

	public void setMaterialType(MaterialType materialType)
	{
		this.materialType = materialType;
	}

	public Long getStockUnitId()
	{
		return stockUnitId;
	}

	public void setStockUnitId(Long stockUnitId)
	{
		this.stockUnitId = stockUnitId;
	}

	public Long getValuationUnitId()
	{
		return valuationUnitId;
	}

	public void setValuationUnitId(Long valuationUnitId)
	{
		this.valuationUnitId = valuationUnitId;
	}

	public Long getProduceUnitId()
	{
		return produceUnitId;
	}

	public void setProduceUnitId(Long produceUnitId)
	{
		this.produceUnitId = produceUnitId;
	}

	public Long getSaleUnitId()
	{
		return saleUnitId;
	}

	public void setSaleUnitId(Long saleUnitId)
	{
		this.saleUnitId = saleUnitId;
	}

	public String getShortName()
	{
		return shortName;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
	}

	public BigDecimal getPurchPrice()
	{
		return purchPrice;
	}

	public void setPurchPrice(BigDecimal purchPrice)
	{
		this.purchPrice = purchPrice;
	}

	public BigDecimal getLastPurchPrice()
	{
		return lastPurchPrice;
	}

	public void setLastPurchPrice(BigDecimal lastPurchPrice)
	{
		this.lastPurchPrice = lastPurchPrice;
	}

	public BigDecimal getSalePrice()
	{
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice)
	{
		this.salePrice = salePrice;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Integer getShelfLife()
	{
		return shelfLife;
	}

	public void setShelfLife(Integer shelfLife)
	{
		this.shelfLife = shelfLife;
	}

	public BoolValue getIsPurch()
	{
		return isPurch;
	}

	public void setIsPurch(BoolValue isPurch)
	{
		this.isPurch = isPurch;
	}

	public BoolValue getIsSale()
	{
		return isSale;
	}

	public void setIsSale(BoolValue isSale)
	{
		this.isSale = isSale;
	}

	public BoolValue getIsProduce()
	{
		return isProduce;
	}

	public void setIsProduce(BoolValue isProduce)
	{
		this.isProduce = isProduce;
	}

	public String getCalculFormula()
	{
		return calculFormula;
	}

	public void setCalculFormula(String calculFormula)
	{
		this.calculFormula = calculFormula;
	}

	public BoolValue getIsValid()
	{
		return isValid;
	}

	public void setIsValid(BoolValue isValid)
	{
		this.isValid = isValid;
	}

	public BigDecimal getMinStockNum()
	{
		return minStockNum;
	}

	public void setMinStockNum(BigDecimal minStockNum)
	{
		this.minStockNum = minStockNum;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public MaterialClass getMaterialClass()
	{
		return materialClass;
	}

	public void setMaterialClass(MaterialClass materialClass)
	{
		this.materialClass = materialClass;
	}

	public Unit getStockUnit()
	{
		return stockUnit;
	}

	public void setStockUnit(Unit stockUnit)
	{
		this.stockUnit = stockUnit;
	}

	public Unit getSaleUnit()
	{
		return saleUnit;
	}

	public void setSaleUnit(Unit saleUnit)
	{
		this.saleUnit = saleUnit;
	}

	public String getMaterialClassName()
	{
		if (materialClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.MATERIALCLASS.name(), materialClassId, "name");
		}
		return "-";
	}

	public String getMaterialTypeText()
	{
		if (materialType != null)
		{
			return materialType.getText();
		}
		return "-";
	}

	public String getIsPurchText()
	{
		if (isPurch != null)
		{
			return isPurch.getText();
		}
		return "-";
	}

	public String getIsSaleText()
	{
		if (isSale != null)
		{
			return isSale.getText();
		}
		return "-";
	}

	public String getIsProduceText()
	{
		if (isProduce != null)
		{
			return isProduce.getText();
		}
		return "-";
	}

	public String getIsValidText()
	{
		if (isValid != null)
		{
			return isValid.getText();
		}
		return "-";
	}

	public String getStockUnitName()
	{
		if (stockUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), stockUnitId, "name");
		}
		return "-";
	}

	public String getValuationUnitName()
	{
		if (valuationUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), valuationUnitId, "name");
		}
		return "-";
	}

	public String getProduceUnitName()
	{
		if (produceUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), produceUnitId, "name");
		}
		return "-";
	}

	public String getSaleUnitIdName()
	{
		if (saleUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), saleUnitId, "name");
		}
		return "-";
	}

	public OfferMaterialType getOfferMaterialType()
	{
		return offerMaterialType;
	}

	public void setOfferMaterialType(OfferMaterialType offerMaterialType)
	{
		this.offerMaterialType = offerMaterialType;
	}

	public String getOfferClassName()
	{
		return offerClassName;
	}

	public void setOfferClassName(String offerClassName)
	{
		this.offerClassName = offerClassName;
	}
	
}
