/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.ProductType;

/**
 * <pre>
 * 基础设置 - 产品分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_product_class")
public class ProductClass extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 产品分类代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 产品分类名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 产品类别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private ProductType productType;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public ProductType getProductType()
	{
		return productType;
	}

	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}

	public String getProductTypeText()
	{
		if (productType != null)
		{
			return productType.getText();
		}
		return "-";
	}
}
