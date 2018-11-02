package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * 供应商付款单位表
 * @ClassName: SupplierPayer
 * @author zhong
 * @date 2016年5月20日 下午4:40:00
 */
@Entity
@Table(name = "basic_supplier_payer")
public class SupplierPayer extends BaseTableIdEntity
{

	private static final long serialVersionUID = 1L;
	/**
	 * 供应商Id
	 */
	private Long supplierId;

	/**
	 * 付款单位名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 是否默认
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isDefault;

	/**
	 * 删除标记，一般在编辑单据详情时用到
	 */
	@Transient
	private BoolValue delFlag=BoolValue.NO;

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

	public Long getSupplierId()
	{
		return supplierId;
	}

	public void setSupplierId(Long supplierId)
	{
		this.supplierId = supplierId;
	}
	
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
