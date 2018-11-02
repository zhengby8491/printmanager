/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
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
 * 基础设置  - 部门信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
@Entity
@Table(name = "basic_department")
public class Department extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 部门名称
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
