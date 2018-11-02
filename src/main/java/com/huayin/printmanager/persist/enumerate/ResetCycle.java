package com.huayin.printmanager.persist.enumerate;

import java.util.Date;

import com.huayin.common.util.DateTimeUtil;
/**
 * <pre>
 * 复位周期
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-9-7
 */
public enum ResetCycle
{
	/**
	 * 年
	 */
	YEAR("年"),
	/**
	 * 月
	 */
	MONTH("月"),
	/**
	 * 日
	 */
	DAY("日"),
	/**
	 * 不复位
	 */
	NERVER("不复位");

	private String text;

	ResetCycle(String text)
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

	public String getCurrentDateNode()
	{
		if (this == ResetCycle.YEAR)
		{
			return DateTimeUtil.formatToStr(new Date(), "yyyy");
		}
		else if (this == ResetCycle.MONTH)
		{
			return DateTimeUtil.formatToStr(new Date(), "yyyy-MM");
		}
		else if (this == ResetCycle.DAY)
		{
			return DateTimeUtil.formatToStr(new Date(), "yyyy-MM-dd");
		}
		else
		{
			return "0";
		}
	}
}
