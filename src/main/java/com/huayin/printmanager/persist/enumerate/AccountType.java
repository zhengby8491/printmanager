package com.huayin.printmanager.persist.enumerate;

/**
 * 账号类型
 * @ClassName: AccountType
 * @author zhong
 * @date 2016年5月23日 下午4:59:35
 */
public enum AccountType
{

	COMMON("公"),

	PRIVATE("私");

	private String text;

	AccountType(String text)
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
