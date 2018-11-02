/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 下午5:02:52
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferPrePrint;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;

/**
 * <pre>
 * 报价设置类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月17日
 */
public enum OfferSettingType
{
	MACHINE("机台设置", "fa-file-text-o", "machine", OfferMachine.class),

	PRE_PRINT("印前设置", "fa-file-text-o", "prePrint", OfferPrePrint.class),

	PROFIT("利润设置", "fa-file-text-o", "profit", OfferProfit.class),

	PAPER("纸张设置", "fa-file-text-o", "paper", OfferPaper.class),

	BFLUTE("坑纸设置", "fa-file-text-o", "bflute", OfferBflute.class),

	PROCEDURE("工序设置", "fa-file-text-o", "procedure", OfferProcedure.class),;

	/**
	 * 中文名称
	 */
	private String text;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * controller的映射
	 */
	private String mapping;

	/**
	 * 类名
	 */
	private Class<? extends BaseBasicTableEntity> cla;

	/**
	 * 
	 * 构造函数
	 * @param text
	 * @param icon
	 * @param mapping
	 */
	OfferSettingType(String text, String icon, String mapping, Class<? extends BaseBasicTableEntity> cla)
	{
		this.text = text;
		this.icon = icon;
		this.mapping = mapping;
		this.cla = cla;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getMapping()
	{
		return mapping;
	}

	public void setMapping(String mapping)
	{
		this.mapping = mapping;
	}

	public Class<? extends BaseTableIdEntity> getCla()
	{
		return cla;
	}

	public void setCla(Class<? extends BaseBasicTableEntity> cla)
	{
		this.cla = cla;
	}
}
