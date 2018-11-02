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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 预收核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "finance_writeoffreceive")
public class FinanceWriteoffReceive extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 *客户Id
	 */
	private Long customerId;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 *核销人
	 */
	private Long employeeId;

	/**
	 * 冲销金额
	 */
	private BigDecimal money;

	/**
	 * 折扣金额
	 */
	private BigDecimal discount;

	/**
	 * 冲销日期
	 */
	private Date billTime;

	@Transient
	List<FinanceWriteoffReceiveDetail> detailList = new ArrayList<FinanceWriteoffReceiveDetail>();

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public BigDecimal getMoney()
	{
		return money;
	}

	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}

	public List<FinanceWriteoffReceiveDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<FinanceWriteoffReceiveDetail> detailList)
	{
		this.detailList = detailList;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public Date getBillTime()
	{
		return billTime;
	}

	public void setBillTime(Date billTime)
	{
		this.billTime = billTime;
	}

	// -------------------------------------------------------
	public String getEmployeeName()
	{

		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}

}
