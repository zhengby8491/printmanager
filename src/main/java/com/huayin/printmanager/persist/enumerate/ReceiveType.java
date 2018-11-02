package com.huayin.printmanager.persist.enumerate;

/**
 * 收款类型
 * @ClassName: ReceiveType
 * @author zhaojt
 * @date 2016年7月23日 下午4:46:55
 */
public enum ReceiveType
{
	RECEIVE("收款"),

	ADVANCE("预收款");

	private String text;

	ReceiveType(String text)
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
