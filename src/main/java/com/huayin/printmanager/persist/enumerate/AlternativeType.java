package com.huayin.printmanager.persist.enumerate;
/**
 * 备选方案
 * @author houmaolong
 *
 */
public enum AlternativeType
{
	NORMALPRO("正常生产","报价费用*0"),

	EXPEDITEDPRO("加急生产","报价费用*0.1");

	private String text;
	
	private String formula;

	AlternativeType(String text,String formula)
	{
		this.text = text;
		this.formula = formula;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getFormula()
	{
		return formula;
	}

	public void setFormula(String formula)
	{
		this.formula = formula;
	}

	
}
