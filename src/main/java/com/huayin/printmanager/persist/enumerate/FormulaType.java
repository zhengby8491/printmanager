package com.huayin.printmanager.persist.enumerate;

/**
 * 
 * <pre>
 * 公式类型
 * </pre>
 * @author raintear
 * @version 1.0, 2016年12月20日
 */
public enum FormulaType
{
	 
	AREA("平方"),
	 
	CUSTOM("自定义"),
	 
	LADDER("阶梯"),
	 
	NORMAL("常用");

	private String text;


	FormulaType(String text)
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