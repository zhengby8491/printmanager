/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月3日 上午11:15:32
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.enums;

/**
 * <pre>
 * TODO 输入类型说明
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月3日上午11:15:32, zhengby
 */
public enum ActionType
{
	GET_USER_NOTICE("getUserNotice"),
	THIRD_CHECK_USER("thirdCheckUser"),
	CHECK_USER("checkUser"),
	USER_INFO("userInfo"),
	UPLOAD_USER("uploadUser"),
	PURCHASE_ORDER("purchaseOrder"),
	GET_PO_NOTICE("getPONotice"),
	NOTICE_PO_STATUS("noticePOStatus");
	
	private String text;
	
	ActionType(String text)
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
