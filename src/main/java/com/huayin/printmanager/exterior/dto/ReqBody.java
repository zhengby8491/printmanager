/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月28日 上午9:56:29
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 框架 - 印刷家请求RequestBody
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月28日上午9:56:29, zhengby
 */
public class ReqBody
{
	/**
	 * 法定代表人名称
	 */
	private String artificialPersonName;

	/**
	 * 营业执照号
	 */
	private String businessLicenceId;

	/**
	 * 公司详细地址
	 */
	private String companyDeclinedAddress;

	/**
	 * 公司名称
	 */
	private String companyName;

	/**
	 * 用户唯一标识
	 */
	private String dataId;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 用户所属平台  true:印刷家平台用户 false:商户平台用户
	 */
	private String isPHUser;

	/**
	 * 联系人
	 */
	private String linkMan;

	/**
	 * 联系人电话
	 */
	private String linkPhoneNum;

	private String mobile;

	/**
	 * 采购单编号
	 */
	private String purchaseOrderNm;

	/**
	 * 处理状态
	 */
	private String status;

	/**
	 * 税务人识别号
	 */
	private String taxManId;

	/**
	 * 商户网站唯一用户标识
	 */
	private String thirdPlatformUserId;

	/**
	 * 商户网站为用户生成的token
	 */
	private String token;

	/**
	 * 商户账户id
	 */
	private String uid;

	public String getArtificialPersonName()
	{
		return artificialPersonName;
	}

	public void setArtificialPersonName(String artificialPersonName)
	{
		this.artificialPersonName = artificialPersonName;
	}

	public String getBusinessLicenceId()
	{
		return businessLicenceId;
	}

	public void setBusinessLicenceId(String businessLicenceId)
	{
		this.businessLicenceId = businessLicenceId;
	}

	public String getCompanyDeclinedAddress()
	{
		return companyDeclinedAddress;
	}

	public void setCompanyDeclinedAddress(String companyDeclinedAddress)
	{
		this.companyDeclinedAddress = companyDeclinedAddress;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getDataId()
	{
		return dataId;
	}

	public void setDataId(String dataId)
	{
		this.dataId = dataId;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getIsPHUser()
	{
		return isPHUser;
	}

	public void setIsPHUser(String isPHUser)
	{
		this.isPHUser = isPHUser;
	}

	public String getLinkMan()
	{
		return linkMan;
	}

	public void setLinkMan(String linkMan)
	{
		this.linkMan = linkMan;
	}

	public String getLinkPhoneNum()
	{
		return linkPhoneNum;
	}

	public void setLinkPhoneNum(String linkPhoneNum)
	{
		this.linkPhoneNum = linkPhoneNum;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getPurchaseOrderNm()
	{
		return purchaseOrderNm;
	}

	public void setPurchaseOrderNm(String purchaseOrderNm)
	{
		this.purchaseOrderNm = purchaseOrderNm;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getTaxManId()
	{
		return taxManId;
	}

	public void setTaxManId(String taxManId)
	{
		this.taxManId = taxManId;
	}

	public String getThirdPlatformUserId()
	{
		return thirdPlatformUserId;
	}

	public void setThirdPlatformUserId(String thirdPlatformUserId)
	{
		this.thirdPlatformUserId = thirdPlatformUserId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}
}
