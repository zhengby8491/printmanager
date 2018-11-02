/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月16日 上午10:31:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 代工平台 - 源数据来源类型（暂用于代工平台）
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月16日
 */
public enum ZeroOriginType
{
	COMPANY("公司"), SUPPLIER("供应商"), CUSTOMER("客户");

	private String text;

	ZeroOriginType(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
}
