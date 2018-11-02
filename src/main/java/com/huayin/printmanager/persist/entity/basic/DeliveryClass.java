/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
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
 * 基础设置 - 送货方式
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_delivery_class")
public class DeliveryClass extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 送货费用公式
	 */
	@Column(length = 100)
	private String formula;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
