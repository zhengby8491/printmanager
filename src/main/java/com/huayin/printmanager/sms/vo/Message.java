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
public class Message implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4328232478062296803L;

	/**
	 * 账户ID
	 */
	private String accountId;
	/**
	 * 摘要: MD5(accountId+secretkey+content)
	 */
	private String digest;
	
	private String content;

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}

	public String getDigest()
	{
		return digest;
	}

	public void setDigest(String digest)
	{
		this.digest = digest;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
	
	
}
