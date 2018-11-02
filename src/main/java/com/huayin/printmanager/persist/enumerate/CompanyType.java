package com.huayin.printmanager.persist.enumerate;


/**
 * <pre>
 * 公司类型
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum CompanyType
{
	/**
	 * 普通工厂
	 */
	NORMAL("普通工厂");

	private String text;

	CompanyType(String text)
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
