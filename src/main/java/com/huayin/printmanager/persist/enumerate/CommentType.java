package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 留言类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年9月30日
 */
public enum CommentType
{
	PRODUCT("产品相关"), CONTENT("内容相关"), SERVICE("服务相关"), OTHER("其它");

	private String text;

	CommentType(String text)
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