package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 性别
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum SexType
{
	MAN("男"),

	WOMAN("女"),

	UNKOWN("保密");

	private String text;

	SexType(String text)
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
