/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 框架 - 单据表基类
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBillTableEntity extends BaseUserTableIdEntity
{
	private static final long serialVersionUID = -9064495946832615405L;

	/**
	 * 强制完工
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isForceComplete = BoolValue.NO;

	public BoolValue getIsForceComplete()
	{
		return isForceComplete;
	}

	public void setIsForceComplete(BoolValue isForceComplete)
	{
		this.isForceComplete = isForceComplete;
	}

}