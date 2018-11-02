package com.huayin.printmanager.persist.enumerate;

/**
 * /** 发送类型
 */
public enum SmsSendState
{
	/**
	 * 立即发送
	 */
	IMMEDIATELY("立即发送", 1),
	/**
	 * 定时发送
	 */
	SCHEDULED("定时发送", 2);

	private String text;

	private int value;

	private SmsSendState(String text, int value)
	{
		this.text = text;
		this.value = value;
	}

	public String getText()
	{
		return text;
	}

	public int getValue()
	{
		return value;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

}