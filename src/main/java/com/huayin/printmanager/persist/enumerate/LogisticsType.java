package com.huayin.printmanager.persist.enumerate;
/**
 * 物流类型
 * @author houmaolong
 *
 */
public enum LogisticsType
{
	APPOINTMENT("预约自提","result=0;"),

	CITYDELIVERY("市内送货","result=12;if(0.8*材料重量*1000>12){result=0.8*材料重量*1000}"),

	EXPRESS("物流快递","result=20;if(0.8*材料重量*1000>20){result=0.8*材料重量*1000}");

	private String text;
	
	
	private String formula;

	LogisticsType(String text,String formula)
	{
		this.text = text;
		this.formula = formula;
	}

	public String getFormula()
	{
		return formula;
	}

	public void setFormula(String formula)
	{
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
	
}
