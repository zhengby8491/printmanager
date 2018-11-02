package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 通用状态
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum State
{
	NORMAL("正常"),

	CLOSED("停用");

	private String text;

	State(String text)
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
