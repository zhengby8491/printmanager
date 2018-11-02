/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午9:39:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.finance;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.AdjustType;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务调整  - 财务调整单主表
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午9:39:00, zhengby
 */
@Entity
@Table(name = "finance_adjust")
public class FinanceAdjust extends BaseBillMasterTableEntity
{
	private static final long serialVersionUID = 7022120680796964595L;
	
	/**
	 * 调整人Id
	 */
	private Long employeeId;
	
	/**
	 * 调整时间
	 */
	private Date adjustTime;	
	
	/**
	 * 调整类型
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private AdjustType adjustType;
	
	/**
	 * 子表
	 */
	@Transient
	private List<FinanceAdjustDetail> detailList;


	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public Date getAdjustTime()
	{
		return adjustTime;
	}

	public void setAdjustTime(Date adjustTime)
	{
		this.adjustTime = adjustTime;
	}

	public AdjustType getAdjustType()
	{
		return adjustType;
	}

	public void setAdjustType(AdjustType adjustType)
	{
		this.adjustType = adjustType;
	}

	public List<FinanceAdjustDetail> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<FinanceAdjustDetail> detailList)
	{
		this.detailList = detailList;
	}
	
	public String getAdjustTypeText()
	{
		if (null != adjustType)
		{
			return adjustType.getText();
		}
		return "-";
	}
	
	public String getAdjustTypeTarget()
	{
		if (null != adjustType)
		{
			return adjustType.getTarget();
		}
		return "-";
	}
	
	public String getEmployeeName()
	{
		if (employeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), employeeId, "name");
		}
		return "-";
	}
}
