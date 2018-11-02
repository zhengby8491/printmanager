package com.huayin.printmanager.persist.entity.begin;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 材料期初子表
 * @ClassName: MaterialBeginDetail
 * @author zhong
 * @date 2016年5月20日 下午4:50:56
 */
@Entity
@Table(name = "basic_materialBegin_detail")
public class MaterialBeginDetail extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 材料id
	 */
	@Column(length = 50)
	private Long materialId;

	/**
	 * 材料代码
	 */
	@Column(length = 50)
	private String materialCode;

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
	 * 材料分类ID
	 */
	private Long materialClassId;

	/**
	 * 计价单位id
	 */
	private Long valuationUnitId;

	/**
	 * 计价单价
	 */
	private BigDecimal valuationPrice;

	/**
	 * 计价数量
	 */
	@Column(precision=19,scale=4)
	private BigDecimal valuationQty;

	/**
	 * 库存单位单价
	 */
	private BigDecimal price;

	/**
	 * 克重
	 */
	private Integer weight;

	/**
	 * 期初金额
	 */
	private BigDecimal money;

	/**
	 * 期初数量
	 */
	private BigDecimal qty;

	/**
	 * 库存单位
	 */
	@Column(length = 20)
	private Long stockUnitId;

	

	@Transient
	private MaterialBegin master;

	public String getMaterialCode()
	{
		return materialCode;
	}

	public void setMaterialCode(String materialCode)
	{
		this.materialCode = materialCode;
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

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
	{
		this.weight = weight;
	}

	public Long getMaterialId()
	{
		return materialId;
	}

	public void setMaterialId(Long materialId)
	{
		this.materialId = materialId;
	}

	public Long getMaterialClassId()
	{
		return materialClassId;
	}

	public void setMaterialClassId(Long materialClassId)
	{
		this.materialClassId = materialClassId;
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

	public BigDecimal getQty()
	{
		return qty;
	}

	public void setQty(BigDecimal qty)
	{
		this.qty = qty;
	}

	public BigDecimal getValuationPrice()
	{
		return valuationPrice;
	}

	public void setValuationPrice(BigDecimal valuationPrice)
	{
		this.valuationPrice = valuationPrice;
	}

	public BigDecimal getValuationQty()
	{
		return valuationQty;
	}

	public void setValuationQty(BigDecimal valuationQty)
	{
		this.valuationQty = valuationQty;
	}

	public MaterialBegin getMaster()
	{
		return master;
	}

	public void setMaster(MaterialBegin master)
	{
		this.master = master;
	}
	
	public String getMaterialClassName(){
		if (materialClassId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.MATERIALCLASS.name(), materialClassId,"name");
		}
		return "-";
	}
	
	public String getValuationUnitName(){
		if (valuationUnitId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), valuationUnitId,"name");
		}
		return "-";
	}
	
	public String getStockUnitName(){
		if (stockUnitId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), stockUnitId,"name");
		}
		return "-";
	}
	
}
