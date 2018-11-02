package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 公司信息初始化步骤
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年9月3日
 */
public enum InitStep
{
	INIT_COMPANY("公司信息"), INIT_BASIC("基础资料"), OVER("完成");

	private String text;

	InitStep(String text)
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
