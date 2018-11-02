package com.huayin.printmanager.persist.enumerate;

/**
 * 微信汇总查询方式
 * @author liudong
 */
public enum WXSumQueryType
{

	DAY("当天"),

	MONTH("当月"),
	
	SHOULD("应收/付"),
	
	EXPIRE("到期收/付");

	private String text;

	WXSumQueryType(String text)
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
