/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月5日 上午11:21:59
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.dto;

/**
 * <pre>
 * 验证签名返回体
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月5日上午11:21:59, zhengby
 */
public class ValidateReturnDto
{
	/**
	 * 验证是否通过
	 */
	private boolean isPass;
	
	/**
	 * 请求内容
	 */
	private RequestDto requestDto;

	public boolean isPass()
	{
		return isPass;
	}

	public void setPass(boolean isPass)
	{
		this.isPass = isPass;
	}

	public RequestDto getRequestDto()
	{
		return requestDto;
	}

	public void setRequestDto(RequestDto requestDto)
	{
		this.requestDto = requestDto;
	}
}
