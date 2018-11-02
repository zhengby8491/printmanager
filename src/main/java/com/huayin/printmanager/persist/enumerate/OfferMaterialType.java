/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月1日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 报价系统 - 材料类型
 * </pre>
 * @author       think
 * @version      1.0, 2017年3月1日 下午4:23:07
 */
public enum OfferMaterialType
{
	MATERIAL("材料"), BFLUTE("坑纸"),;
	private String text;

	OfferMaterialType(String text)
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
