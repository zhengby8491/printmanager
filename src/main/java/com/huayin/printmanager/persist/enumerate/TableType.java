package com.huayin.printmanager.persist.enumerate;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;

/**
 * <pre>
 * 表类型
 * </pre>
 * @author zhaojt
 * @version 1.0, 2016年7月20日
 */
public enum TableType
{
	/** 主表 **/
	MASTER("主表", BaseBillMasterTableEntity.class),
	/** 明细表 **/
	DETAIL("子表", BaseBillDetailTableEntity.class);

	/**
	 * 描述
	 */
	private String text;

	/**
	 * 表名
	 */
	private Class<? extends BaseBillTableEntity> cla;

	TableType(String text, Class<? extends BaseBillTableEntity> cla)
	{
		this.text = text;
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

	public Class<? extends BaseBillTableEntity> getCla()
	{
		return cla;
	}

	public void setCla(Class<? extends BaseBillTableEntity> cla)
	{
		this.cla = cla;
	}

}
