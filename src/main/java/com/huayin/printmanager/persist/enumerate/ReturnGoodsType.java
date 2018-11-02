package com.huayin.printmanager.persist.enumerate;

/**
 * 退货方式
 * @ClassName: ReturnType
 * @author zhong
 * @date 2016年5月24日 上午9:59:18
 */
public enum ReturnGoodsType
{

	EXCHANGE("换货"),

	RETURN("退货");

	private String text;

	ReturnGoodsType(String text)
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
