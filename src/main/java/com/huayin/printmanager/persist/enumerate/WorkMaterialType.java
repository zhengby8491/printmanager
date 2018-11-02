package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 工单材料类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum WorkMaterialType
{

	PRODUCT("成品材料"),

	PART("部件材料");

	private String text;

	WorkMaterialType(String text)
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
