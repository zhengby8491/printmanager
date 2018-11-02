package com.huayin.printmanager.persist.enumerate;

/**
 * 订单类型
 * @ClassName: OrderType
 * @author zhong
 * @date 2016年5月23日 下午6:58:45
 */
public enum OrderType
{
	NORMAL("正常订单"),

	SENDSTOCK("发库存订单");

	private String text;

	OrderType(String text)
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
