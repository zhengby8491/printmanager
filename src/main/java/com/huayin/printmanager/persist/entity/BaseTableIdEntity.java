/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.common.persist.entity.AbstractTableIdEntity;

/**
 * <pre>
 * 框架 - 基础信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月26日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseTableIdEntity extends AbstractTableIdEntity
{
	private static final long serialVersionUID = -9064495946832615405L;

	/**
	 * 所属公司编号
	 */
	@Column(length = 20)
	private String companyId;

	public String getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(String companyId)
	{
		this.companyId = companyId;
	}
}