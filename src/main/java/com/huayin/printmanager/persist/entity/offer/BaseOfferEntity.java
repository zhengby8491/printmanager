/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.offer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 报价系统 - 订单主表基类
 * </pre>
 * @author       think
 * @since        1.0, 2018年2月7日
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseOfferEntity extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 是否已审核
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isCheck = BoolValue.NO;

	/**
	 * 强制完工
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isForceComplete = BoolValue.NO;

	/**
	 * 审核人
	 */
	@Column(length = 50)
	private String checkUserName;

	/**
	 * 审核时间
	 */
	private Date checkTime;

	public BoolValue getIsForceComplete()
	{
		return isForceComplete;
	}

	public void setIsForceComplete(BoolValue isForceComplete)
	{
		this.isForceComplete = isForceComplete;
	}

	public BoolValue getIsCheck()
	{
		return isCheck;
	}

	public void setIsCheck(BoolValue isCheck)
	{
		this.isCheck = isCheck;
	}

	public String getCheckUserName()
	{
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName)
	{
		this.checkUserName = checkUserName;
	}

	public Date getCheckTime()
	{
		return checkTime;
	}

	public void setCheckTime(Date checkTime)
	{
		this.checkTime = checkTime;
	}
}
