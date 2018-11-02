/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月23日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.FormulaType;

/**
 * <pre>
 * 报价模块 - 机台设置 - 自定义报价公式
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月23日
 */
@Entity
@Table(name = "offer_machine_formula")
public class OfferFormula extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 报价-机台-主表ID
	 */
	private Long masterId;
	
	/**
	 * 报价-机台-主表
	 */
	private OfferMachine master;
	
	/**
	 * 公式类型
	 */
	@Enumerated(EnumType.STRING)
	private FormulaType formulaType;
	
	/**
	 * 是否需要手动输入面积
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isCustom=BoolValue.NO;
	
	/**
	 * 编号
	 */
	private String code;
	
	/**
	 * 公式字符串
	 */
	@Column(length=3000)
	private String formula;
	
	/**
	 * 公式参数
	 */
	@Column(length=3000)
	private String formulaParam;
	
	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public OfferMachine getMaster()
	{
		return master;
	}

	public void setMaster(OfferMachine master)
	{
		this.master = master;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getFormula()
	{
		return formula;
	}

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

	public FormulaType getFormulaType()
	{
		return formulaType;
	}

	public void setFormulaType(FormulaType formulaType)
	{
		this.formulaType = formulaType;
	}

	public BoolValue getIsCustom()
	{
		return isCustom;
	}

	public void setIsCustom(BoolValue isCustom)
	{
		this.isCustom = isCustom;
	}

	public String getFormulaParam()
	{
		return formulaParam;
	}

	public void setFormulaParam(String formulaParam)
	{
		this.formulaParam = formulaParam;
	}
}
