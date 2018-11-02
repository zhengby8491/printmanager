/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

/**
 * <pre>
 * 框架 - 基础信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBasicTableEntity extends BaseTableIdEntity
{
	private static final long serialVersionUID = -9064495946832615405L;

	/**
	 * 排序
	 */
	private Integer sort;

	public Integer getSort()
	{
		return sort;
	}

	public void setSort(Integer sort)
	{
		this.sort = sort;
	}
}