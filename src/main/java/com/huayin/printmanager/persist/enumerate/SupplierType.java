/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月29日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.enumerate;

/**
 * <pre>
 * 基础枚举 - 供应商类型
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月29日
 * @since        2.0, 2017年12月29日 下午17:07:00, think, 规范和国际化
 */
public enum SupplierType
{
	/**
	 * 材料商(关联ID:无)
	 */
	MATERIAL("材料商"),
	/**
	 * 加工商
	 */
	PROCESS("加工商"),
	/**
	 * 综合供应商
	 */
	MATERIAL_AND_PROCESS("综合供应商");

	private String text;

	SupplierType(String text)
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
