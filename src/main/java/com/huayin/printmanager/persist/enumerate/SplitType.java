package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 分切类型
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月9日
 */
public enum SplitType
{
	
	CURLYTOFACE("卷-张"),

	CURLYTOCURLY("卷-卷"),

	FACETOFACE("张-张");

	private String text;


	SplitType(String text)
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