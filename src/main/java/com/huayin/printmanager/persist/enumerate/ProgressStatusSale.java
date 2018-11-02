package com.huayin.printmanager.persist.enumerate;

/**
 * 销售进度状态
 * @ClassName: ProgressStatusSale
 * @author zhong
 * @date 2016年5月24日 下午3:39:23
 */
public enum ProgressStatusSale
{

	NO_PRODUCE("未生产"),

	ALREAD_PRODUCE_NO_STORAGE("已生产未入库"),

	ALREAD_STORAGE_NO_DELIVERY("已入库未送货"),

	ALREAD_DELIVERY_NO_RECONCIL("已送货未对账"),

	ALREAD_RECONCIL_NO_PAYMENT("已对账未收款"),

	ALREAD_PAYMENT("已收款");

	private String text;

	ProgressStatusSale(String text)
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
