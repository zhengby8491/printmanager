/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月20日 下午3:34:41
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 机台公式类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月20日
 */
public enum OfferMachineType
{

	START_PRINT("开机费+印工计价"),
	CUSTOM("自定义公式计价"),;
	
	private String text;
	
	OfferMachineType(String text)
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
