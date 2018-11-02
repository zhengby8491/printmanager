/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月28日 上午10:23:39
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 框架  - 印刷家请求RequestHead
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月28日上午10:23:39, zhengby
 */
public class ReqHeader
{
	/**
	 * 请求名称
	 */
	private String action;
	
	/**
	 * 商户号
	 */
	private String merchant;
	
	/**
	 * 商户秘钥
	 */
	private String tokenKey;
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 请求时间戳
	 */
	private String requestTime;
	
	/**
	 * 序列号
	 */
	private String seriNumber;
	
	/**
	 * 加密方式
	 */
	private String signStyle;
	
	/**
	 * 版本号
	 */
	private String version;

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getMerchant()
	{
		return merchant;
	}

	public void setMerchant(String merchant)
	{
		this.merchant = merchant;
	}

	public String getTokenKey()
	{
		return tokenKey;
	}

	public void setTokenKey(String tokenKey)
	{
		this.tokenKey = tokenKey;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}

	public String getRequestTime()
	{
		return requestTime;
	}

	public void setRequestTime(String requestTime)
	{
		this.requestTime = requestTime;
	}

	public String getSeriNumber()
	{
		return seriNumber;
	}

	public void setSeriNumber(String seriNumber)
	{
		this.seriNumber = seriNumber;
	}

	public String getSignStyle()
	{
		return signStyle;
	}

	public void setSignStyle(String signStyle)
	{
		this.signStyle = signStyle;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	} 
	
}
