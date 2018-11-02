package com.huayin.printmanager.persist.enumerate;

/**
 * 员工状态
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author zhong
 * @version 1.0, 2016年9月5日
 */
public enum EmployeeState
{
	NORMAL("正常"),

	HOLIDAY("休假"),
	
	LEAVEJOB("离职");

	private String text;

	EmployeeState(String text)
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
