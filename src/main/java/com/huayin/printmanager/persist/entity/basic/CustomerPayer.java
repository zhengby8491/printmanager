package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 付款单位表
 * @ClassName: CustomerPayer
 * @author zhong
 * @date 2016年5月19日 下午12:19:27
 */
@Entity
@Table(name = "basic_customer_payer")
public class CustomerPayer extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 客户id
	 */
	private Long customerId;
	
	/**
	 * 单位名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 是否默认
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isDefault;

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public BoolValue getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(BoolValue isDefault)
	{
		this.isDefault = isDefault;
	}

	public String getCustomerName(){
		if (customerId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.CUSTOMER.name(), customerId,"name");
		}
		return "-";
	}
}
