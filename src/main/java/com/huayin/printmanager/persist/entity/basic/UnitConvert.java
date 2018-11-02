/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 单位换算
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_unit_convert")
public class UnitConvert extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 单位换算名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 源单位ID
	 */
	@Column(length = 20)
	private Long sourceUnitId;

	/**
	 * 换算单位ID
	 */
	@Column(length = 20)
	private Long conversionUnitId;

	/**
	 * 换算方式(换算公式，换算系数)
	 */
	@Column(length = 100)
	private String formula;

	/**
	 * 换算内容
	 */
	@Column(length = 100)
	private String conversionContent;

	/**
	 * 是否原始的数据
	 */
	private String isOrigin;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Long getSourceUnitId()
	{
		return sourceUnitId;
	}

	public void setSourceUnitId(Long sourceUnitId)
	{
		this.sourceUnitId = sourceUnitId;
	}

	public Long getConversionUnitId()
	{
		return conversionUnitId;
	}

	public void setConversionUnitId(Long conversionUnitId)
	{
		this.conversionUnitId = conversionUnitId;
	}

	public String getFormula()
	{
		return formula;
	}

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

	public String getConversionContent()
	{
		return conversionContent;
	}

	public void setConversionContent(String conversionContent)
	{
		this.conversionContent = conversionContent;
	}

	public String getSourceUnitName()
	{
		if (sourceUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), sourceUnitId, "name");
		}
		return "-";
	}

	public String getConversionUnitName()
	{
		if (conversionUnitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), conversionUnitId, "name");
		}
		return "-";
	}

	public String getIsOrigin()
	{
		return isOrigin;
	}

	public void setIsOrigin(String isOrigin)
	{
		this.isOrigin = isOrigin;
	}

}
