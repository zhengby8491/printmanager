package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 销售材料类型
 * </pre>
 * @author think
 * @version 1.0, 2017年9月18日
 */
public enum SaleMaterialType
{

	PRODUCT("成品材料"),

	PART("部件材料");

	private String text;

	SaleMaterialType(String text)
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
