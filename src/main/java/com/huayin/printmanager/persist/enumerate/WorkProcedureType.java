package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 工单工序类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum WorkProcedureType
{

	PRODUCT("成品工序"),

	PART("部件工序");

	private String text;

	WorkProcedureType(String text)
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
