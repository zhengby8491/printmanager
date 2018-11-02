package com.huayin.printmanager.persist.enumerate;

/**
 * 付款类型
 * @ClassName: PaymentType
 * @author zhaojt
 * @date 2016年7月23日 下午4:46:55
 */
public enum PaymentType
{
	PAYMENT("付款"),

	ADVANCE("预付款");

	private String text;

	PaymentType(String text)
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
