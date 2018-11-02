/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年02月22日 下午17:53:23
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sale;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.SaleProcedureType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 销售管理 - 销售订单：销售工序表
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
@Entity
@Table(name = "sale_order_procedure")
public class SaleOrderProcedure extends BaseBillTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 销售明细ID
	 */
	private Long saleDetailId;

	/**
	 * 销售工序类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private SaleProcedureType saleProcedureType;

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
	private SaleOrderPack saleOrderPack;

	/**
	 * 工单部件对象
	 */
	@Transient
	private SaleOrderPart saleOrderPart;

	/**
	 * 销售明细对象
	 */
	@Transient
	private SaleOrderDetail saleOrderDetail;

	/**
	 * 工序条形码
	 */
	@Transient
	private String proedureBarCode;

	public Long getSaleDetailId()
	{
		return saleDetailId;
	}

	public void setSaleDetailId(Long saleDetailId)
	{
		this.saleDetailId = saleDetailId;
	}

	public SaleProcedureType getSaleProcedureType()
	{
		return saleProcedureType;
	}

	public void setSaleProcedureType(SaleProcedureType saleProcedureType)
	{
		this.saleProcedureType = saleProcedureType;
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

	public SaleOrderPack getSaleOrderPack()
	{
		return saleOrderPack;
	}

	public void setSaleOrderPack(SaleOrderPack saleOrderPack)
	{
		this.saleOrderPack = saleOrderPack;
	}

	public SaleOrderPart getSaleOrderPart()
	{
		return saleOrderPart;
	}

	public void setSaleOrderPart(SaleOrderPart saleOrderPart)
	{
		this.saleOrderPart = saleOrderPart;
	}

	public SaleOrderDetail getSaleOrderDetail()
	{
		return saleOrderDetail;
	}

	public void setSaleOrderDetail(SaleOrderDetail saleOrderDetail)
	{
		this.saleOrderDetail = saleOrderDetail;
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
		if (saleProcedureType != null)
		{

			return saleProcedureType.getText();
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

}
