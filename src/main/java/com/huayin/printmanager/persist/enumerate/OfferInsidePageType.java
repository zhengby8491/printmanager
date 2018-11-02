/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2017年11月6日 下午2:15:19
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 自动报价 - 内页/插页P数类型
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月6日 下午2:15:19
 */
public enum OfferInsidePageType
{
	ZERO("0",0),
	TWO("2",2),
	FOUR("4",4),
	EIGHT("8",8),
	TWELVE("12",12),
	SIXTEEN("16",16),
	TWENTY("20",20),
	TWENTYFOUR("24",24),
	TEWNTYEIGHT("28",28),
	THIRTYTWO("32",32),
	THIRTYSIX("36",36),
	FOURTY("40",40),
	FOURTYFOUR("44",44),
	FOURTYEIGHT("48",48),
	FIFTYTWO("52",52),
	FIFTYSIX("56",56),
	SIXTY("60",60),
	SIXTYFOUR("64",64),
  SIXTYEIGHT("68",68),
  SEVENTYTWO("72",72),
  SEVENTYSIX("76",76),
  EIGHTY("80",80),
  EIGHTYFOUR("84",84),
  EIGHTYEIGHT("88",88),
  NINTYTWO("92",92),
  NINTYSIX("96",96),
	CUSTOM("自定义",-1);
	
	/**
	 * 文本描述
	 */
	private String text;
	
	/**
	 * 对应值
	 */
	private Integer value;
	
	
	OfferInsidePageType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
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
