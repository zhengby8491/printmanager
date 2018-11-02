/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年9月27日 上午11:48:51
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 入库出库类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年9月27日上午11:48:51, zhengby
 */
public enum InOutType
{
	OUT("出库"), IN("入库"),;

	private String text;

	private InOutType(String text)
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
