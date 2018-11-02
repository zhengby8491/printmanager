/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月13日 上午11:50:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * 支付类型
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月13日上午11:50:00, zhengby
 */
public enum PaymentType
{
	ZFB("支付宝",0),
	JD("京东支付", 1),
	XY("小印支付", 2),
	OUTLINE("线下支付",3),
	ZFB_BANK("支付宝银行", 4),
	WX("微信支付", 5),
	WX_PC("微信PC端",6),
	JI_FEN("积分支付",7),
	ZHUAN_ZHANG("转账支付", 8),
	TU_MU("徙木金融印刷家白条", 9),
	ZHANG_QI("账期支付", 10),
	ZFB_SJ("支付宝手机端",100),
	JD_SJ("京东支付手机端",101),
	;
	
	private String text;
	private Integer value;
	
	PaymentType(String text, Integer value)
	{
		this.text = text;
		this.value = value;
	}
	
	public static PaymentType setType(Integer value)
	{
		for (PaymentType p : PaymentType.values())
		{
			if (p.getValue() == value)
			{
				return p;
			}
		}
		return null;
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
