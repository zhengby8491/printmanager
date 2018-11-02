package com.huayin.printmanager.persist.enumerate;

/**
 * 发外进度状态
 * @ClassName: ProgressStatusOutsource
 * @author zhong
 * @date 2016年5月24日 下午3:39:23
 */
public enum ProgressStatusOutsource
{
	NO_ARRIVING("未到货"),

	ALREAD_ARRIVED_YET_RECONCIL("已到货未对账"),

	ALREAD_RECONCIL_YET_PAYMENT("已对账未付款"),

	ALREAD_PAYMENT("已付款");

	private String text;

	ProgressStatusOutsource(String text)
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
