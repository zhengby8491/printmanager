package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * TODO 广告位置
 * </pre>
 * @author houmaolong
 * @version 1.0, 2017年2月6日
 */
public enum AdvertisementType
{
	LOGIN("登录界面"),
	 
	WORK("工作台");

	private String text;

	AdvertisementType(String text)
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