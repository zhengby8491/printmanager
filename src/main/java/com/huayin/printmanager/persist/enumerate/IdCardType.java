package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 证件类型枚举
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2010-7-19
 */
public enum IdCardType
{

	/**
	 * 身份证
	 */
	IDCARD("身份证"),
	/**
	 * 护照
	 */
	PASSPORT("护照"),
	/**
	 * 军官证
	 */
	SOLDIERCARD("军官证"),
	/**
	 * 士兵证
	 */
	XSOLDIERCARD("士兵证"),
	/**
	 * 港澳台胞证
	 */
	HK2MACAU2TAICARD("港澳台胞证"),
	/**
	 * 其它证件
	 */
	OTHERCARD("其它证");
	private String text;

	IdCardType(String text)
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
