/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月30日 上午10:27:30
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.OfferProcedureFormulaType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProcedureUnit;
import com.huayin.printmanager.persist.enumerate.SwitchStatus;

/**
 * <pre>
 * 报价模块 - 工序设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月30日
 */
@Entity
@Table(name = "offer_procedure")
public class OfferProcedure extends BaseBasicTableEntity
{
	private static final long serialVersionUID = -2860065179753149781L;

	/**
	 * 机台类型
	 */
	@Enumerated(EnumType.STRING)
	private OfferType offerType;

	/**
	 * 工序类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProcedureType procedureType;

	/**
	 * 工序分类（覆膜、过油）
	 */
	private String procedureClass;

	/**
	 * 工序名称
	 */
	private String name;

	/**
	 * 单价/元
	 */
	private BigDecimal price;

	/**
	 * 单位
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProcedureUnit procedureUnit;

	/**
	 * 每张最低单价/元
	 */
	private BigDecimal lowestPrice;

	/**
	 * 起步价/元
	 */
	private BigDecimal startPrice;

	/**
	 * 公式类型（普通公式、自定义公式）
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private OfferProcedureFormulaType offerProcedureFormulaType;

	/**
	 * 普通公式字符串（只用于给用户展示，不做计算）
	 */
	@Column(length = 500)
	private String formulaText;

	/**
	 * 普通公式字符串
	 */
	@Column(length = 500)
	private String formula;

	/**
	 * 普通公式参数
	 */
	@Column(length = 300)
	private String formulaParam;

	/**
	 * 自定义公式字符串（只用于给用户展示，不做计算）
	 */
	@Column(length = 500)
	private String customFormulaText;

	/**
	 * 自定义公式字符串
	 */
	@Column(length = 500)
	private String customFormula;

	/**
	 * 自定义公式参数
	 */
	@Column(length = 3000)
	private String customFormulaParam;

	/**
	 * 状态
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private SwitchStatus switchStatus;

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
	 * 是否全删
	 */
	@Transient
	private Boolean isDeleteAll = false;
	
	public OfferType getOfferType()
	{
		return offerType;
	}
	
	public String getOfferTypeText()
	{
		if(null != this.offerType)
		{
			return this.offerType.getText();
		}
		return "";
	}

	public void setOfferType(OfferType offerType)
	{
		this.offerType = offerType;
	}

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public String getProcedureClass()
	{
		return procedureClass;
	}

	public void setProcedureClass(String procedureClass)
	{
		this.procedureClass = procedureClass;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public ProcedureUnit getProcedureUnit()
	{
		return procedureUnit;
	}

	public void setProcedureUnit(ProcedureUnit procedureUnit)
	{
		this.procedureUnit = procedureUnit;
	}

	public BigDecimal getLowestPrice()
	{
		return lowestPrice;
	}

	public void setLowestPrice(BigDecimal lowestPrice)
	{
		this.lowestPrice = lowestPrice;
	}

	public BigDecimal getStartPrice()
	{
		return startPrice;
	}

	public void setStartPrice(BigDecimal startPrice)
	{
		this.startPrice = startPrice;
	}

	public OfferProcedureFormulaType getOfferProcedureFormulaType()
	{
		return offerProcedureFormulaType;
	}

	public void setOfferProcedureFormulaType(OfferProcedureFormulaType offerProcedureFormulaType)
	{
		this.offerProcedureFormulaType = offerProcedureFormulaType;
	}

	public String getFormulaText()
	{
		return formulaText;
	}

	public void setFormulaText(String formulaText)
	{
		this.formulaText = formulaText;
	}

	public String getFormula()
	{
		return formula;
	}

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

	public String getFormulaParam()
	{
		return formulaParam;
	}

	public void setFormulaParam(String formulaParam)
	{
		this.formulaParam = formulaParam;
	}

	public String getCustomFormulaText()
	{
		return customFormulaText;
	}

	public void setCustomFormulaText(String customFormulaText)
	{
		this.customFormulaText = customFormulaText;
	}

	public String getCustomFormula()
	{
		return customFormula;
	}

	public void setCustomFormula(String customFormula)
	{
		this.customFormula = customFormula;
	}

	public String getCustomFormulaParam()
	{
		return customFormulaParam;
	}

	public void setCustomFormulaParam(String customFormulaParam)
	{
		this.customFormulaParam = customFormulaParam;
	}

	public SwitchStatus getSwitchStatus()
	{
		return switchStatus;
	}

	public void setSwitchStatus(SwitchStatus switchStatus)
	{
		this.switchStatus = switchStatus;
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

	/**
	 * 
	 * <pre>
	 * 工序类型
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月30日 下午4:06:48, think
	 */
	public String getProcedureTypeText()
	{
		if (procedureType != null)
		{
			return procedureType.getText();
		}
		return "-";
	}

	/**
	 * 
	 * <pre>
	 * 单位
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月30日 下午4:07:16, think
	 */
	public String getProcedureUnitText()
	{
		if (procedureUnit != null)
		{
			return procedureUnit.getText();
		}
		return "-";
	}

	/**
	 * 
	 * <pre>
	 * 公式类型
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月30日 下午4:07:45, think
	 */
	public String getOfferProcedureFormulaTypeText()
	{
		if (offerProcedureFormulaType != null)
		{
			return offerProcedureFormulaType.getText();
		}
		return "-";
	}

	/**
	 * 
	 * <pre>
	 * 状态
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月30日 下午4:08:17, think
	 */
	public String getSwitchStatusText()
	{
		if (switchStatus != null)
		{
			return switchStatus.getText();
		}
		return "-";
	}

	public Boolean getIsDeleteAll()
	{
		return isDeleteAll;
	}

	public void setIsDeleteAll(Boolean isDeleteAll)
	{
		this.isDeleteAll = isDeleteAll;
	}
	
}
