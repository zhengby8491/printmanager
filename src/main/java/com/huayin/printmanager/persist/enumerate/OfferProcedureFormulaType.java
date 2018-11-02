/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月30日 上午11:00:58
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 报价模块 - 工序设置 - 公式类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月30日
 */
public enum OfferProcedureFormulaType
{
	NORMAL("普通公式"), 
	CUSTOM("自定义公式"),;

	private String text;

	OfferProcedureFormulaType(String text)
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
