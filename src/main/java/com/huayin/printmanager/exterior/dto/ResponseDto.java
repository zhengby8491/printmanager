/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月2日 上午9:38:13
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 响应
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月2日上午9:38:13, zhengby
 */
public class ResponseDto
{
	/**
	 * 响应头
	 */
	private RspHeader responseHeader;

	/**
	 * 响应体
	 */
	private RspBody rspBody;

	public RspHeader getResponseHeader()
	{
		return responseHeader;
	}

	public void setResponseHeader(RspHeader responseHeader)
	{
		this.responseHeader = responseHeader;
	}

	public RspBody getRspBody()
	{
		return rspBody;
	}

	public void setRspBody(RspBody rspBody)
	{
		this.rspBody = rspBody;
	}

}
