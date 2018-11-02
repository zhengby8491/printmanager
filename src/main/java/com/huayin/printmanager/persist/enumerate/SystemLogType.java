package com.huayin.printmanager.persist.enumerate;

/**
 * /** <pre>
 * 系统日志类型
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-1-19
 */
public enum SystemLogType
{
	REGISTER("注册"), 
	LOGIN("登录"),
	LOGINOUT("退出系统"),
	FINDPWD("找回密码"),
	RESETPWD("重置密码"),
	BUSINESS("业务数据"),
	BASIC("基础数据"),
	DEFAULT("");
	private String text;

	SystemLogType(String text)
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