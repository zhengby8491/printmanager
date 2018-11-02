/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 单据主表基类
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseBillMasterTableEntity extends BaseBillTableEntity
{
	private static final long serialVersionUID = -9064495946832615405L;

	/**
	 * 单据类型
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private BillType billType;

	/**
	 * 单据编号
	 */
	@Column(length = 50)
	private String billNo;

	/**
	 * 是否作废
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isCancel = BoolValue.NO;

	/**
	 * 是否已审核
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private BoolValue isCheck = BoolValue.NO;
	
	/**
	 * 强制审核
	 */
	@Transient
	private BoolValue forceCheck = BoolValue.NO;

	/**
	 * 审核人
	 */
	@Column(length = 50)
	private String checkUserName;

	/**
	 * 审核时间
	 */
	private Date checkTime;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

	/**
	 * 创建单据员工ID
	 */
	@Column(length = 50)
	private Long createEmployeeId;

	/**
	 * 修改人
	 */
	@Column(length = 50)
	private String updateName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 打印次数
	 */
	private Integer printCount;

	@Transient
	private String billTypeText;

	public BoolValue getIsCancel()
	{
		return isCancel;
	}

	public void setIsCancel(BoolValue isCancel)
	{
		this.isCancel = isCancel;
	}

	public BoolValue getIsCheck()
	{
		return isCheck;
	}

	public void setIsCheck(BoolValue isCheck)
	{
		this.isCheck = isCheck;
	}

	public BoolValue getForceCheck()
	{
		return forceCheck;
	}

	public void setForceCheck(BoolValue forceCheck)
	{
		this.forceCheck = forceCheck;
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

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
	}

	public Long getCreateEmployeeId()
	{
		return createEmployeeId;
	}

	public String getCreateEmployeeName()
	{
		if (createEmployeeId != null)
		{
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.EMPLOYEE.name(), createEmployeeId, "name");
		}
		return null;
	}

	public void setCreateEmployeeId(Long createEmployeeId)
	{
		this.createEmployeeId = createEmployeeId;
	}

	public String getUpdateName()
	{
		return updateName;
	}

	public void setUpdateName(String updateName)
	{
		this.updateName = updateName;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public Integer getPrintCount()
	{
		return printCount;
	}

	public void setPrintCount(Integer printCount)
	{
		this.printCount = printCount;
	}

	public BillType getBillType()
	{
		return billType;
	}

	public void setBillType(BillType billType)
	{
		this.billType = billType;
	}

	public String getBillNo()
	{
		return billNo;
	}

	public void setBillNo(String billNo)
	{
		this.billNo = billNo;
	}

	public void setBillTypeText(String billTypeText)
	{
		this.billTypeText = billTypeText;
	}

	public String getBillTypeText()
	{
		if (billType != null)
		{
			return billType.getText();
		}
		return "-";
	}
}