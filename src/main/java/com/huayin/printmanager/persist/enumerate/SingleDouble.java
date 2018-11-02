package com.huayin.printmanager.persist.enumerate;

public enum SingleDouble
{
	SINGLE("单面",1),

	DOUBLE("双面",2);

	private String text;
	
	private Integer value;

	SingleDouble(String text,Integer value)
	{
		this.text = text;
		this.value=value;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Integer getValue()
	{
		return value;
	}

	public void setValue(Integer value)
	{
		this.value = value;
	}
	
}
