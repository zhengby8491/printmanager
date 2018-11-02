/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 基础枚举 - 常用计算公式
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public enum ParamsType
{
	YZS("印张数",BoolValue.NO),
	YBZS("印版张数",BoolValue.NO),
	YBFS("印版付数",BoolValue.NO),
  SYY("(色数+专色*2)*印版付数*印版张数",BoolValue.NO), 
	YY("印张面积*印张数",BoolValue.NO),
	YYSD("印张面积*印张数*印刷面数",BoolValue.NO),
	ZY("指定面积*印张数",BoolValue.YES),
	ZYSD("指定面积*印张数*印刷面数",BoolValue.YES),
	YYS("印张面积*印张数*(色数+专色*2)",BoolValue.NO),
	TC("总贴数*产品数",BoolValue.NO),
	TY("总贴数*印张数",BoolValue.NO),
	PP("P数*产品数",BoolValue.NO),
	TPP("总P数*产品数",BoolValue.NO),
	PY("P数*印张数",BoolValue.NO),
	TPY("总P数*印张数",BoolValue.NO);
	private String text;
	private BoolValue isCustom;
	ParamsType(String text, BoolValue isCustom)
	{
		this.text = text;
		this.isCustom = isCustom;
	}
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	public BoolValue getIsCustom()
	{
		return isCustom;
	}
	public void setIsCustom(BoolValue isCustom)
	{
		this.isCustom = isCustom;
	}
}
