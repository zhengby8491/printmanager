/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.stock;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品库存明细基类
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class StockProductDetailBaseEntity extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品ID
	 */
	private Long productId;

	/**
	 * 产品代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 成品名称
	 */
	@Column(length = 50)
	private String productName;

	/**
	 * 产品图片
	 */
	@Transient
	private String imgUrl;

	/**
	 * 产品分类id
	 */
	@Column(length = 50)
	private Long productClassId;

	/**
	 * 规格
	 */
	private String specifications;

	/**
	 * 销售单位
	 */
	@Column(length = 20)
	private Long unitId;

	/**
	 * 客户ID
	 */
	@Column(length = 20)
	private Long customerId;

	/**
	 * 客户代码
	 */
	@Column(length = 50)
	private String customerCode;

	/**
	 * 客户名称
	 */
	@Column(length = 50)
	private String customerName;

	/**
	 * 客户料号
	 */
	@Column(length = 50)
	private String customerMaterialCode;

	/**
	 * 数量
	 */
	private Integer qty;

	/**
	 * 成本单价
	 */
	private BigDecimal price;

	/**
	 * 成本金额
	 */
	private BigDecimal money;

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getProductName()
	{
		return productName;
	}

	public void setProductName(String productName)
	{
		this.productName = productName;
	}

	public Long getProductClassId()
	{
		return productClassId;
	}

	public void setProductClassId(Long productClassId)
	{
		this.productClassId = productClassId;
	}

	public String getSpecifications()
	{
		return specifications;
	}

	public void setSpecifications(String specifications)
	{
		this.specifications = specifications;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerCode()
	{
		return customerCode;
	}

	public void setCustomerCode(String customerCode)
	{
		this.customerCode = customerCode;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
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

	public Long getUnitId()
	{
		return unitId;
	}

	public void setUnitId(Long unitId)
	{
		this.unitId = unitId;
	}

	public Integer getQty()
	{
		return qty;
	}

	public void setQty(Integer qty)
	{
		this.qty = qty;
	}

	public String getCustomerMaterialCode()
	{
		return customerMaterialCode;
	}

	public void setCustomerMaterialCode(String customerMaterialCode)
	{
		this.customerMaterialCode = customerMaterialCode;
	}

	public String getProductClassName()
	{
		if (productClassId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PRODUCTCLASS.name(), productClassId, "name");
		}
		return "-";
	}

	public String getUnitName()
	{
		if (unitId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.UNIT.name(), unitId, "name");
		}
		else
		{
			return "-";
		}
	}

	public String getImgUrl()
	{
		return imgUrl;
	}

	public void setImgUrl(String imgUrl)
	{
		this.imgUrl = imgUrl;
	}
}
