package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 印刷方式
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年8月15日
 */
public enum PrintType
{
	SINGLE("单面"),
	DOUBLE("正反"),
	MYSELF("自翻"),
	WOSELF("天地翻"),
	ROSELF("对滚"),
	BLANK("无");
	
	private String text;

	PrintType(String text)
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
