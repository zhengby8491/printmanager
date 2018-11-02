package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 销售工序类型
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
public enum SaleProcedureType
{

	PRODUCT("成品工序"),

	PART("部件工序");

	private String text;

	SaleProcedureType(String text)
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
