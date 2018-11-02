package com.huayin.printmanager.persist.enumerate;

/**
 * 付款方式类型
 * @ClassName: PaymentClassType
 * @author zhaojt
 * @date 2016年7月23日 下午4:46:55
 */
public enum PaymentClassType
{
	TOPAY("货到付款"),
	
	PAYTO("款到发货"),

	MONTHLY("月结");

	

	private String text;

	PaymentClassType(String text)
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
