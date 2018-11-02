/**
 * <pre>
 * Author:		   liu dong
 * Create:	 	   2017年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 盒型计算公式
 * </pre>
 * @author       liu dong
 * @since        1.0, 2017年11月6日
 */
public enum BoxFormula
{
	PKXH("平口箱盒","length*2+width*2+35","high+(width*0.5+10)+high+(0.5*width+10)"),
	
	KDH("扣底盒","width/2+30+high+width+30","length+width/2+30"),
	
	ZDKDH("自动扣底盒","width/2+30+high+width+30","length+width*2+30"),

	SSH("收缩盒","high*3+width*2+20","width*2+(width*0.5+10)+(width*0.5+10)+length"),
	
	SXDGGH("双插带挂钩盒","length*2+width*2+25","high+width*2+30"),
	
	SCDAQKH("双插带安全扣盒","length*2+width*2+25","high+width*2+30"),
	
	SXH("双插盒","length*2+width*2+25","high+width*2+30"),
	
	TSH("提手盒","length*2+width*2+30","high+30+0.5*width+30"),
	
	KDDGGH("扣底带挂钩盒","length*2+width*2+25","high+width+25+0.5*width+25");
	
	private String text;
	
	private String lenFormula;
	
	private String widFormula;

	BoxFormula(String text,String lenFormula,String widFormula)
	{
		this.text = text;
		this.lenFormula=lenFormula;
		this.widFormula=widFormula;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getLenFormula()
	{
		return lenFormula;
	}

	public void setLenFormula(String lenFormula)
	{
		this.lenFormula = lenFormula;
	}

	public String getWidFormula()
	{
		return widFormula;
	}

	public void setWidFormula(String widFormula)
	{
		this.widFormula = widFormula;
	}
	
}
