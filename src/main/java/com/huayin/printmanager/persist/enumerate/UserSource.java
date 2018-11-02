package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 用户来源
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年6月20日
 */
public enum UserSource
{

	WEB("WEB注册"), MOBILE("手机注册"), INNER("内部添加"),
	// 印刷家注册标记
	YSJ("印刷家注册"),;

	private String text;

	UserSource(String text)
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
