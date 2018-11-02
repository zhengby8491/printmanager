/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月6日 上午9:45:28
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 外部接口 - 扩展信息
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月6日上午9:45:28, zhengby
 */
public class ExtendInfo
{
	/**
	 * 公司名称
	 */
	private String companyName;
	
	/**
	 * 营业执照号
	 */
	private String businessLicenceId;
	
	/**
	 * 法定代表人名称
	 */
	private String artificialPersonName;
	
	/**
	 * 税务人识别号
	 */
	private String taxManId;
	
	/**
	 * 公司详细地址
	 */
	private String companyDeclinedAddress;

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getBusinessLicenceId()
	{
		return businessLicenceId;
	}

	public void setBusinessLicenceId(String businessLicenceId)
	{
		this.businessLicenceId = businessLicenceId;
	}

	public String getArtificialPersonName()
	{
		return artificialPersonName;
	}

	public void setArtificialPersonName(String artificialPersonName)
	{
		this.artificialPersonName = artificialPersonName;
	}

	public String getTaxManId()
	{
		return taxManId;
	}

	public void setTaxManId(String taxManId)
	{
		this.taxManId = taxManId;
	}

	public String getCompanyDeclinedAddress()
	{
		return companyDeclinedAddress;
	}

	public void setCompanyDeclinedAddress(String companyDeclinedAddress)
	{
		this.companyDeclinedAddress = companyDeclinedAddress;
	}
	
}
