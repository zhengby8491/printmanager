package com.huayin.printmanager.persist.enumerate;

/**
 * 交易类型
 * @ClassName: AccountTransType
 * @author zhaojitao
 * @date 2016年9月9日
 */
public enum AccountTransType
{
	BEGIN("期初"), PAYMENT("付款"),PAYMENT_CANCEL("付款作废"), RECEIVE("收款"), RECEIVE_CANCEL("收款作废"),
	OTHER_PAYMENT("其它付款"),OTHER_PAYMENT_CANCEL("其它付款作废"), OTHER_RECEIVE("其它收款"), OTHER_RECEIVE_CANCEL("其它收款作废"),
	FINANCE_ADJUST("财务调整");

	private String text;

	AccountTransType(String text)
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
