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
 * 成品期初子表
 * @ClassName: ProductBeginDetail
 * @author zhong
 * @date 2016年5月20日 下午5:20:56
 */
@Entity
@Table(name = "basic_productBegin_detail")
public class ProductBeginDetail extends BaseBillDetailTableEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 标题
	 */
	@Column(length = 50)
	private String title;

	/**
	 * 产品Id
	 */
	private Long productId;

	/**
	 * 产品代码
	 */
	@Column(length = 50)
	private String productCode;
	
	/**
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;
	
	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 产品规格
	 */
	private String specifications;

	/**
	 * 单价
	 */
	private BigDecimal price;

	/**
	 * 单位
	 */
	@Column(length = 20)
	private Long unit;

	/**
	 * 库存数量
	 */
	private Integer stockQty;

	/**
	 * 期初数量
	 */
	private Integer qty;

	/**
	 * 期初单价
	 */
	private BigDecimal periodPrice;

	/**
	 * 金额
	 */
	private BigDecimal money;

	/**
	 * 排序
	 */
	private Integer sort;

	@Transient
	private ProductBegin master;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public BigDecimal getPrice()
	{
		return price;
	}

	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}

	public Long getUnit()
	{
		return unit;
	}

	public void setUnit(Long unit)
	{
		this.unit = unit;
	}

	public Integer getStockQty()
	{
		return stockQty;
	}

	public void setStockQty(Integer stockQty)
	{
		this.stockQty = stockQty;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public BigDecimal getPeriodPrice()
	{
		return periodPrice;
	}

	public void setPeriodPrice(BigDecimal periodPrice)
	{
		this.periodPrice = periodPrice;
	}

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

	public ProductBegin getMaster()
	{
		return master;
	}

	public void setMaster(ProductBegin master)
	{
		this.master = master;
	}

	//unit
	public String getUnitName(){
		if (unit!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unit,"name");
		}
		return "-";
	}
}
