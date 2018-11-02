package com.huayin.printmanager.persist.enumerate;

/**
 * 采购进度状态
 * @ClassName: ProgressStatusPurch
 * @author zhong
 * @date 2016年5月24日 下午3:39:23
 */
public enum ProgressStatusPurch
{

	NO_STORAGE("未入库"),

	ALREAD_STORAGE_YET_RECONCIL("已入库未对账"),

	ALREAD_RECONCIL_YET_PAYMENT("已对账未付款"),

	ALREAD_PAYMENT("已付款");

	private String text;

	ProgressStatusPurch(String text)
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
