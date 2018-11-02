package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 
 * <pre>
 * 产品客户关联表
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年8月24日
 */
@Entity
@Table(name="basic_product_customer")
public class Product_Customer extends BaseTableIdEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Long productId;

	@Column(nullable = false)
	private Long customerId;

	public Long getProductId()
	{
		return productId;
	}

	public void setProductId(Long productId)
	{
		this.productId = productId;
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getProductName(){
		if (productId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.PRODUCT.name(), productId,"name");
		}
		return "-";
	}
	
	public String getCustomerName(){
		if (customerId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.CUSTOMER.name(), customerId,"name");
		}
		return "-";
	}
}
