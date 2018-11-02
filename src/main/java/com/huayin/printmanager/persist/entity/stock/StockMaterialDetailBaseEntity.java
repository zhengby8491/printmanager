/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.math.BigDecimal;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存明细表基类
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class StockMaterialDetailBaseEntity extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 材料ID
	 */
	private Long materialId;

	/**
	 * 材料代码
	 */
	private String code;

	/**
	 * 材料名称
	 */
	private String materialName;

	/**
	 * 材料规格
	 */
	private String specifications;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 材料分类Id
	 */
	private Long materialClassId;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 成本单价
	 */
	private BigDecimal price;

	/**
	 * 成本金额
	 */
	private BigDecimal money;

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
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

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
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

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public String getMaterialClassName()
	{
		if (materialClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.MATERIALCLASS.name(), materialClassId, "name");
		}
		return "-";
	}
}
