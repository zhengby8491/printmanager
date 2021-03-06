/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
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
 * 基础设置 - 职位设置
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 * @since        2.0, 2017年12月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_position")
public class Position extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 职位代码
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 职位名称
	 */
	@Column(length = 50)
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
