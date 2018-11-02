/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.finance;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 财务管理 - 其他收款单明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_otherreceive_detail")
public class FinanceOtherReceiveDetail extends FinanceDetailBaseEntity
{
	private static final long serialVersionUID = 2781791832913839764L;

	/**
	 * 摘要
	 */
	private String summary;

	@Transient
	private FinanceOtherReceive master;

	public FinanceOtherReceive getMaster()
	{
		return master;
	}

	public void setMaster(FinanceOtherReceive master)
	{
		this.master = master;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}
}
