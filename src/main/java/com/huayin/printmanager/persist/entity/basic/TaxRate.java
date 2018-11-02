/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;

/**
 * <pre>
 * 基础设置 - 税率信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_taxRate")
public class TaxRate extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 税率名称(税收)
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 税率值（%）
	 */
	@Column(length = 20)
	private Integer percent;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getPercent()
	{
		return percent;
	}

	public void setPercent(Integer percent)
	{
		this.percent = percent;
	}
}
