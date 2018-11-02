/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;

/**
 * <pre>
 * 系统模块 - 员工业务数据分享
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_data_share")
public class DataShare extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 被分享数据的员工ID
	 */
	@Column(nullable = false)
	private Long sharedUserId;

	/**
	 * 可以查看业务数据的员工
	 */
	@Column(nullable = false)
	private Long employeeId;

	public Long getSharedUserId()
	{
		return sharedUserId;
	}

	public void setSharedUserId(Long sharedUserId)
	{
		this.sharedUserId = sharedUserId;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}
}
