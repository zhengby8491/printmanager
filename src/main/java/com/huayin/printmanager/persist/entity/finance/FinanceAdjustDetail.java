/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午9:55:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.finance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 财务管理 - 财务调整单明细表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午9:55:00, zhengby
 */
@Entity
@Table(name = "finance_adjust_detail")
public class FinanceAdjustDetail extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = -5908184411239101559L;

	/**
	 * 客户/供应商/账户id
	 */
	@Column(length = 50)
	private Long businessId;

	/**
	 * 客户/供应商/账户名称
	 */
	@Column(length = 80)
	private String businessName;

	/**
	 * 客户/供应商/账户编号
	 */
	private String businessCode;

	/**
	 * 调整金额
	 */
	private BigDecimal adjustMoney;

	/**
	 * 调整事由
	 */
	@Column(length = 1000)
	private String reason;

	/**
	 * 已收/付款
	 */
	@Column(columnDefinition = "decimal(18,2) default '0.00'")
	private BigDecimal receiveOrPayMoney = new BigDecimal(0);

	/**
	 * 完成收/付款
	 */
	@Column(length = 8)
	@Enumerated(EnumType.STRING)
	private BoolValue isReceiveOrPayOver;

	/**
	 * 主表
	 */
	@Transient
	private FinanceAdjust master;

	public Long getBusinessId()
	{
		return businessId;
	}

	public void setBusinessId(Long businessId)
	{
		this.businessId = businessId;
	}

	public String getBusinessName()
	{
		return businessName;
	}

	public void setBusinessName(String businessName)
	{
		this.businessName = businessName;
	}

	public String getBusinessCode()
	{
		return businessCode;
	}

	public void setBusinessCode(String businessCode)
	{
		this.businessCode = businessCode;
	}

	public BigDecimal getAdjustMoney()
	{
		return adjustMoney;
	}

	public void setAdjustMoney(BigDecimal adjustMoney)
	{
		this.adjustMoney = adjustMoney;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public BigDecimal getReceiveOrPayMoney()
	{
		return receiveOrPayMoney;
	}

	public void setReceiveOrPayMoney(BigDecimal receiveOrPayMoney)
	{
		this.receiveOrPayMoney = receiveOrPayMoney;
	}

	public BoolValue getIsReceiveOrPayOver()
	{
		return isReceiveOrPayOver;
	}

	public void setIsReceiveOrPayOver(BoolValue isReceiveOrPayOver)
	{
		this.isReceiveOrPayOver = isReceiveOrPayOver;
	}

	public FinanceAdjust getMaster()
	{
		return master;
	}

	public void setMaster(FinanceAdjust master)
	{
		this.master = master;
	}
}
