package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * TODO 广告位置
 * </pre>
 * @author houmaolong
 * @version 1.0, 2017年2月6日
 */
public enum AgentType
{
	AGENT("代理"),
	 
	ADVERTISEMENT("广告"),
	 
	COMPREHESIVE("综合");

	private String text;

	AgentType(String text)
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