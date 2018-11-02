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

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import com.huayin.printmanager.persist.entity.BaseBillDetailTableEntity;
import com.huayin.printmanager.persist.enumerate.BillType;

/**
 * <pre>
 * 财务管理 -  财务明细
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class FinanceDetailBaseEntity extends BaseBillDetailTableEntity
{
	private static final long serialVersionUID = 4160451619391354908L;

	/**
	 * 源单ID
	 */
	@Column(length = 50)
	private Long sourceId;

	/**
	 * 源单明细ID
	 */
	@Column(length = 50)
	private Long sourceDetailId;

	/**
	 * 源单类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType sourceBillType;

	/**
	 * 源单编号
	 */
	@Column(length = 50)
	private String sourceBillNo;

	/**
	 * 源单金额
	 */
	private BigDecimal sourceMoney;

	/**
	 * 源单余额(付款前的未付款金额)
	 */
	private BigDecimal sourceBalanceMoney;

	/**
	 * 本次付款金额
	 */
	private BigDecimal money;

	public Long getSourceId()
	{
		return sourceId;
	}

	public void setSourceId(Long sourceId)
	{
		this.sourceId = sourceId;
	}

	public Long getSourceDetailId()
	{
		return sourceDetailId;
	}

	public void setSourceDetailId(Long sourceDetailId)
	{
		this.sourceDetailId = sourceDetailId;
	}

	public BillType getSourceBillType()
	{
		return sourceBillType;
	}

	public void setSourceBillType(BillType sourceBillType)
	{
		this.sourceBillType = sourceBillType;
	}

	public String getSourceBillNo()
	{
		return sourceBillNo;
	}

	public void setSourceBillNo(String sourceBillNo)
	{
		this.sourceBillNo = sourceBillNo;
	}

	public BigDecimal getSourceMoney()
	{
		return sourceMoney;
	}

	public void setSourceMoney(BigDecimal sourceMoney)
	{
		this.sourceMoney = sourceMoney;
	}

	public BigDecimal getSourceBalanceMoney()
	{
		return sourceBalanceMoney;
	}

	public void setSourceBalanceMoney(BigDecimal sourceBalanceMoney)
	{
		this.sourceBalanceMoney = sourceBalanceMoney;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	// ------------------------------------------------------------

	public String getSourceBillTypeText()
	{
		if (sourceBillType != null)
		{
			return sourceBillType.getText();
		}
		return "-";
	}
}
