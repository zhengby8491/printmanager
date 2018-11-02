package com.huayin.printmanager.persist.enumerate;
/**
 * <pre>
 * 公司状态
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum CompanyState
{
	/**
	 * 关闭
	 */
	CLOSED("关闭"),
	/**
	 * 未开通
	 */
	NOTOPEN("未开通"),
	/**
	 * 正常
	 */
	ONSALING("正常"),
	/**
	 * 暂停
	 */
	PAUSE("暂停");

	private String text;

	CompanyState(String text)
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