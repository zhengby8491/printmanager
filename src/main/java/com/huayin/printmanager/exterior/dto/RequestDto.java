/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月2日 上午9:37:32
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 请求
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月2日上午9:37:32, zhengby
 */
public class RequestDto
{
	/**
	 * 请求头
	 */
	private ReqHeader reqHead;
	
	/**
	 * 请求体
	 */
	private ReqBody reqBody;

	public ReqHeader getReqHead()
	{
		return reqHead;
	}

	public void setReqHead(ReqHeader reqHead)
	{
		this.reqHead = reqHead;
	}

	public ReqBody getReqBody()
	{
		return reqBody;
	}

	public void setReqBody(ReqBody reqBody)
	{
		this.reqBody = reqBody;
	}

}
