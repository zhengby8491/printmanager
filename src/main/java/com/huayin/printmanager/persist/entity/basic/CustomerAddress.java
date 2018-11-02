package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 客户地址表
 * @ClassName: CustomerAddress
 * @author zhong
 * @date 2016年5月19日 上午11:59:50
 */
@Entity
@Table(name = "basic_customer_address")
public class CustomerAddress extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;

	/**
	 * 客户id
	 */
	private Long customerId;

	/**
	 * 客户地址
	 */
	private String address;

	/**
	 * 联系人姓名
	 */
	@Column(length = 20)
	private String userName;

	/**
	 * 联系人手机
	 */
	@Column(length = 20)
	private String mobile;
	
	/**
	 * 联系人电话
	 */
	@Column(length = 20)
	private String phone;

	/**
	 * 传真
	 */
	@Column(length = 20)
	private String fax;

	/**
	 * qq
	 */
	@Column(length = 20)
	private String qq;
	/**
	 * 邮箱
	 */
	@Column(length = 40)
	private String email;
	/**
	 * 是否默认
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isDefault;
	public Long getCustomerId()
	{
		return customerId;
	}
	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getMobile()
	{
		return mobile;
	}
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getFax()
	{
		return fax;
	}
	public void setFax(String fax)
	{
		this.fax = fax;
	}
	public String getQq()
	{
		return qq;
	}
	public void setQq(String qq)
	{
		this.qq = qq;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
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
