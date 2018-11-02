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
 * 供应商地址
 * @ClassName: SupplierAddress
 * @author zhong
 * @date 2016年5月19日 下午3:14:00
 */
@Entity
@Table(name = "basic_supplier_address")
public class SupplierAddress extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;
	/**
	 * 供应商Id
	 */
	private Long supplierId;
	/**
	 * 供应商地址
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
	@Enumerated(EnumType.STRING)
	private BoolValue isDefault;

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public BoolValue getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault(BoolValue isDefault)
	{
		this.isDefault = isDefault;
	}

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
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
	
	//
	public String getSupplierName(){
		if (supplierId!=null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.SUPPLIER.name(), supplierId,"name");
		}
		return "-";
	}
	
	public String getIsDefaultText(){
		if(isDefault!=null){
			return isDefault.getText();
		}
		return "-";
	}

}
