package com.huayin.printmanager.persist.enumerate;

/**
 * /** <pre>
 * 短信发送终端类型
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum SmsSendType
{
	WUOTAO("沃淘科技"), YUNPIAN("云片网络");
	private String text;

	SmsSendType(String text)
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