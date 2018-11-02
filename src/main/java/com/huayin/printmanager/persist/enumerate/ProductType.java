package com.huayin.printmanager.persist.enumerate;

/**
 * 产品类型
 * @ClassName: ProductType
 * @author zhong
 * @date 2016年5月23日 下午3:33:45
 */
public enum ProductType
{

	/**
	 * 包装印刷
	 */
	PACKE("包装印刷"),
	/**
	 * 书刊印刷
	 */
	BOOK("书刊印刷"),
	/**
	 * 轮转印刷
	 */
	ROTARY("轮转印刷");

	private String text;

	ProductType(String text)
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
