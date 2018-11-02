/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月28日 上午10:33:39
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 响应header
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月28日上午10:33:39, zhengby
 */
public class RspHeader
{
	private String rspCode;
	
	private String rspMsg;

	public String getRspCode()
	{
		return rspCode;
	}

	public void setRspCode(String rspCode)
	{
		this.rspCode = rspCode;
	}

	public String getRspMsg()
	{
		return rspMsg;
	}

	public void setRspMsg(String rspMsg)
	{
		this.rspMsg = rspMsg;
	}
	
}
