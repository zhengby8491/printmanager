package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 发送状态
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum SmsLogState
{
	FAILURE("发送失败"),
	/**
	 * 已发送
	 */
	SENDED("已发送"),
	/**
	 * 正在发送
	 */
	SENDING("正在发送"),

	/**
	 * 等待发送
	 */
	WAITING("等待发送");

	private String text;

	SmsLogState(String text)
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