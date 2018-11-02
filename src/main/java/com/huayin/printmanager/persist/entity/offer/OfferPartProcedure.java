/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月10日 上午9:34:50
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.OfferProcedureFormulaType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProcedureUnit;

/**
 * <pre>
 * 自动报价 - 部件的后道工序
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月10日 上午9:34:50
 */
@Entity
@Table(name = "offer_order_part_procedure")
public class OfferPartProcedure extends BaseTableIdEntity implements Cloneable
{
	private static final long serialVersionUID = 6272861891693364785L;
	
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
	 * 工序设置的id
	 */
	private Long procedureId;

	/**
	 * 工序名称
	 */
	private String procedureName;

	/**
	 * 部件的id
	 */
	private Long partId;

	/**
	 * 报价单Id（装订工序）
	 */
	private Long orderId;

	/**
	 * 指定㎡-长
	 */
	private Integer length;

	/**
	 * 指定㎡-宽
	 */
	private Integer width;

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
	 * 最低单价/元
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
	
	public OfferType getOfferType()
	{
		return offerType;
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

	public Long getPartId()
	{
		return partId;
	}

	public void setPartId(Long partId)
	{
		this.partId = partId;
	}

	public Long getOrderId()
	{
		return orderId;
	}

	public void setOrderId(Long orderId)
	{
		this.orderId = orderId;
	}

	public Integer getLength()
	{
		return length;
	}

	public void setLength(Integer length)
	{
		this.length = length;
	}

	public Integer getWidth()
	{
		return width;
	}

	public void setWidth(Integer width)
	{
		this.width = width;
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

}
