package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 财务交易模式（加钱/减钱）
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年8月4日
 */
public enum FinanceTradeMode
{
	/**
	 * 增加
	 */
	ADD("增加"),
	/**
	 * 减少
	 */
	SUBTRACT("减少");

	private String text;

	FinanceTradeMode(String text)
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