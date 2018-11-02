/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
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
import com.huayin.printmanager.persist.enumerate.ParamsType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProduceType;
import com.huayin.printmanager.persist.enumerate.ScheduleDataSource;
import com.huayin.printmanager.persist.enumerate.YieldReportingType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 工序信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_procedure")
public class Procedure extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

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
	 * 常用公式
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ParamsType paramsType;

	/**
	 * 工序代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 工序名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 生产加工方式
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProduceType produceType;

	/**
	 * 产量上报方式
	 */
	@Column(length = 100)
	@Enumerated(EnumType.STRING)
	private YieldReportingType yieldReportingType;

	/**
	 * 生产工序（默认“是”）
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isProduce = BoolValue.YES;

	/**
	 * 报价工序
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isQuotation = BoolValue.YES;

	/**
	 * 排程工序
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isSchedule = BoolValue.YES;

	/**
	 * 排程数据源
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private ScheduleDataSource scheduleDataSource;

	/**
	 * 报价公式Id
	 */
	private Long formulaId;

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
	 * 产品类型 1通用 2包装 3书刊
	 */
	private Integer productType;
	
	/**
	 * 报价分类名称
	 */
	@Transient
	private String className;

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

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

	public ProduceType getProduceType()
	{
		return produceType;
	}

	public void setProduceType(ProduceType produceType)
	{
		this.produceType = produceType;
	}

	public YieldReportingType getYieldReportingType()
	{
		return yieldReportingType;
	}

	public void setYieldReportingType(YieldReportingType yieldReportingType)
	{
		this.yieldReportingType = yieldReportingType;
	}

	public BoolValue getIsProduce()
	{
		return isProduce;
	}

	public void setIsProduce(BoolValue isProduce)
	{
		this.isProduce = isProduce;
	}

	public BoolValue getIsQuotation()
	{
		return isQuotation;
	}

	public void setIsQuotation(BoolValue isQuotation)
	{
		this.isQuotation = isQuotation;
	}

	public BoolValue getIsSchedule()
	{
		return isSchedule;
	}

	public void setIsSchedule(BoolValue isSchedule)
	{
		this.isSchedule = isSchedule;
	}

	public ScheduleDataSource getScheduleDataSource()
	{
		return scheduleDataSource;
	}

	public void setScheduleDataSource(ScheduleDataSource scheduleDataSource)
	{
		this.scheduleDataSource = scheduleDataSource;
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

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public Long getFormulaId()
	{
		return formulaId;
	}

	public void setFormulaId(Long formulaId)
	{
		this.formulaId = formulaId;
	}

	public String getProcedureClassName()
	{
		if (procedureClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PROCEDURECLASS.name(), procedureClassId, "name");
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

	public String getProduceTypeText()
	{
		if (produceType != null)
		{
			return produceType.getText();
		}
		return "-";
	}

	public String getYieldReportingTypeText()
	{
		if (yieldReportingType != null)
		{
			return yieldReportingType.getText();
		}
		return "-";
	}

	// scheduleDataSource
	public String getScheduleDataSourceText()
	{
		if (scheduleDataSource != null)
		{
			return scheduleDataSource.getText();
		}
		return "-";
	}

	public String getIsQuotationText()
	{
		if (isQuotation != null)
		{
			return isQuotation.getText();
		}
		return "-";
	}

	public String getIsScheduleText()
	{
		if (isSchedule != null)
		{
			return isSchedule.getText();
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

	public String getFormulaName()
	{
		// 返回常用公式
		if (paramsType != null)
		{
			return paramsType.getText() + "*单价";
		}
		// 返回自定义公式
		if (formulaId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.OFFERFORMULA.name(), formulaId, "name");
		}
		return null;
	}

	public ParamsType getParamsType()
	{
		return paramsType;
	}

	public void setParamsType(ParamsType paramsType)
	{
		this.paramsType = paramsType;
	}

	public Integer getProductType()
	{
		return productType;
	}

	public void setProductType(Integer productType)
	{
		this.productType = productType;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}
}
