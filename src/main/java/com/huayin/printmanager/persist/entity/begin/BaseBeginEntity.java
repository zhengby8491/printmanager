/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.begin;

import java.util.Date;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;

/**
 * <pre>
 * 基础设置 - 期初主表基类
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBeginEntity extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 期初日期
	 */
	private Date beginTime;

	public Date getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}
}
