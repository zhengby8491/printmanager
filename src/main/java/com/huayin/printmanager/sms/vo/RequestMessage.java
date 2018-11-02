/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.sms.vo;

import java.io.Serializable;

/**
 * <pre>
 * 短信平台 - 发送短信
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class RequestMessage extends Message implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4328232478062296803L;
	
	private String mobile;

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	
}
