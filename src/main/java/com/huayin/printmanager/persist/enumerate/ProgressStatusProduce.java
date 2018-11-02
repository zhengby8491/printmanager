package com.huayin.printmanager.persist.enumerate;

/**
 * 生产进度状态(待定，等颜金平确认)
 * @ClassName: ProgressStatusProduce
 * @author zhong
 * @date 2016年5月24日 下午3:39:23
 */
public enum ProgressStatusProduce
{

	N_xxx("xxx");

	private String text;

	ProgressStatusProduce(String text)
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
