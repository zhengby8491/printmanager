package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 合作商状态
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum SmsPartnerState
{
	/**
	 * 关闭
	 */
	CLOSED("关闭"),
	/**
	 * 打开
	 */
	OPEN("打开");

	private String text;

	SmsPartnerState(String text)
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