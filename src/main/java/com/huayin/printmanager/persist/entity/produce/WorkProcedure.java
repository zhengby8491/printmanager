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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 生产工单：工单工序表
 * </pre>
 * @author       zhengby
 * @version      1.0, 2016年10月26日,zhaojt
 * @version 	   2.0, 2018年2月23日上午10:23:29, zhengby, 代码规范
 */
@Entity
@Table(name = "produce_work_procedure")
public class WorkProcedure extends BaseBillTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 工单ID
	 */
	private Long workId;

	/**
	 * 工单编号
	 */
	@Column(length = 50)
	private String workBillNo;

	/**
	 * 工单工序类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private WorkProcedureType workProcedureType;

	/**
	 * 父ID（部件ID或者成品ID)
	 */
	private Long parentId;

	/**
	 * 工序ID
	 */
	private Long procedureId;

	/**
	 * 工序CODE
	 */
	private String procedureCode;

	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 工序分类ID
	 */
	private Long procedureClassId;

	/**
	 * 工序类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private ProcedureType procedureType;

	/**
	 * 是否发外
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isOutSource;

	/**
	 * 是否发外（打印模板用这个字段）
	 */
	@Transient
	private String isOutSourceTemplate;

	/**
	 * 投入数
	 */
	private BigDecimal inputQty;

	/**
	 * 产出数
	 */
	private BigDecimal outputQty;

	/**
	 * 已发外数
	 */
	private BigDecimal outOfQty;

	/**
	 * 已到货数
	 */
	private BigDecimal arriveOfQty;

	/**
	 * 顺序索引
	 */
	private Integer sort;

	/**
	 * 工单成品对象
	 */
	@Transient
	private WorkPack workPack;

	/**
	 * 工单部件对象
	 */
	@Transient
	private WorkPart workPart;

	/**
	 * 工单对象
	 */
	@Transient
	private Work work;

	/**
	 * 工序条形码
	 */
	@Transient
	private String proedureBarCode;

	/**
	 * 生产数（为合计）
	 */
	@Transient
	private BigDecimal sumQty;

	/**
	 * 产品名称列表
	 */
	@Transient
	private List<WorkProduct> productList = Lists.newArrayList();

	public Long getWorkId()
	{
		return workId;
	}

	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public String getWorkBillNo()
	{
		return workBillNo;
	}

	public void setWorkBillNo(String workBillNo)
	{
		this.workBillNo = workBillNo;
	}

	public WorkProcedureType getWorkProcedureType()
	{
		return workProcedureType;
	}

	public void setWorkProcedureType(WorkProcedureType workProcedureType)
	{
		this.workProcedureType = workProcedureType;
	}

	public Long getProcedureId()
	{
		return procedureId;
	}

	public void setProcedureId(Long procedureId)
	{
		this.procedureId = procedureId;
	}

	public String getProcedureName()
	{
		return procedureName;
	}

	public void setProcedureName(String procedureName)
	{
		this.procedureName = procedureName;
	}

	public BoolValue getIsOutSource()
	{
		return isOutSource;
	}

	public void setIsOutSource(BoolValue isOutSource)
	{
		this.isOutSource = isOutSource;
	}

	public BigDecimal getInputQty()
	{
		return inputQty;
	}

	public void setInputQty(BigDecimal inputQty)
	{
		this.inputQty = inputQty;
	}

	public BigDecimal getOutputQty()
	{
		return outputQty;
	}

	public void setOutputQty(BigDecimal outputQty)
	{
		this.outputQty = outputQty;
	}

	public BigDecimal getOutOfQty()
	{
		return outOfQty;
	}

	public void setOutOfQty(BigDecimal outOfQty)
	{
		this.outOfQty = outOfQty;
	}

	public BigDecimal getArriveOfQty()
	{
		return arriveOfQty;
	}

	public void setArriveOfQty(BigDecimal arriveOfQty)
	{
		this.arriveOfQty = arriveOfQty;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

	public WorkPack getWorkPack()
	{
		return workPack;
	}

	public void setWorkPack(WorkPack workPack)
	{
		this.workPack = workPack;
	}

	public WorkPart getWorkPart()
	{
		return workPart;
	}

	public void setWorkPart(WorkPart workPart)
	{
		this.workPart = workPart;
	}

	public Work getWork()
	{
		return work;
	}

	public void setWork(Work work)
	{
		this.work = work;
	}

	public String getProcedureCode()
	{
		return procedureCode;
	}

	public void setProcedureCode(String procedureCode)
	{
		this.procedureCode = procedureCode;
	}

	public Long getProcedureClassId()
	{
		return procedureClassId;
	}

	public void setProcedureClassId(Long procedureClassId)
	{
		this.procedureClassId = procedureClassId;
	}

	public Long getParentId()
	{
		return parentId;
	}

	public void setParentId(Long parentId)
	{
		this.parentId = parentId;
	}

	public ProcedureType getProcedureType()
	{
		return procedureType;
	}

	public void setProcedureType(ProcedureType procedureType)
	{
		this.procedureType = procedureType;
	}

	public String getWorkProcedureTypeText()
	{
		if (workProcedureType != null)
		{

			return workProcedureType.getText();
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

	public String getIsOutSourceText()
	{
		if (isOutSource != null)
		{
			return isOutSource.getText();
		}
		return "-";
	}

	public String getProcedureClassName()
	{
		if (procedureClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PROCEDURECLASS.name(), procedureClassId, "name");
		}
		return "-";
	}

	public String getProedureBarCode()
	{
		return proedureBarCode;
	}

	public void setProedureBarCode(String proedureBarCode)
	{
		this.proedureBarCode = proedureBarCode;
	}

	public String getIsOutSourceTemplate()
	{
		return isOutSourceTemplate;
	}

	public void setIsOutSourceTemplate(String isOutSourceTemplate)
	{
		this.isOutSourceTemplate = isOutSourceTemplate;
	}

	public BigDecimal getSumQty()
	{
		return sumQty;
	}

	public void setSumQty(BigDecimal sumQty)
	{
		this.sumQty = sumQty;
	}

	public List<WorkProduct> getProductList()
	{
		return productList;
	}

	public void setProductList(List<WorkProduct> productList)
	{
		this.productList = productList;
	}

	public String getProductNames()
	{
		StringBuilder sb = new StringBuilder();
		if (this.productList != null && this.productList.size() > 0)
		{
			for (WorkProduct p : this.productList)
			{
				sb.append(p.getProductName()).append(",");
			}
		}
		else
		{
			sb.append("-");
		}
		return StringUtils.removeEnd(sb.toString(), ",");
	}
}
