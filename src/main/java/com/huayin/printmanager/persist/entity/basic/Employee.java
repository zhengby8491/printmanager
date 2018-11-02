/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.persist.entity.basic;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.EmployeeState;
import com.huayin.printmanager.persist.enumerate.SexType;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 员工信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:11:49
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Entity
@Table(name = "basic_employee")
public class Employee extends BaseBasicTableEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 工号
	 */
	@Column(length = 50)
	private String code;

	/**
	 * 员工名称
	 */
	@Column(length = 20)
	private String name;

	/**
	 * 员工手机
	 */
	@Column(length = 20)
	private String mobile;

	/**
	 * 性别
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private SexType sexType;

	/**
	 * 邮箱
	 */
	@Column(length = 40)
	private String email;

	/**
	 * 职位id
	 */
	@Column(length = 20)
	private Long positionId;

	/**
	 * 部门id
	 */
	@Column(length = 20)
	private Long departmentId;

	/**
	 * 状态
	 */
	@Column(name="state",length = 20)
	@Enumerated(EnumType.STRING)
	private EmployeeState state;

	/**
	 * 入职时间
	 */
	private Date entryTime;

	/**
	 * 离职时间
	 */
	private Date departureTime;

	/**
	 * 创建人
	 */
	@Column(length = 50)
	private String createName;

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

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public SexType getSexType()
	{
		return sexType;
	}

	public void setSexType(SexType sexType)
	{
		this.sexType = sexType;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Long getPositionId()
	{
		return positionId;
	}

	public void setPositionId(Long positionId)
	{
		this.positionId = positionId;
	}

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public void setDepartmentId(Long departmentId)
	{
		this.departmentId = departmentId;
	}

	public Date getEntryTime()
	{
		return entryTime;
	}

	public void setEntryTime(Date entryTime)
	{
		this.entryTime = entryTime;
	}

	public Date getDepartureTime()
	{
		return departureTime;
	}

	public void setDepartureTime(Date departureTime)
	{
		this.departureTime = departureTime;
	}

	public EmployeeState getState()
	{
		return state;
	}

	public void setState(EmployeeState state)
	{
		this.state = state;
	}

	public String getCreateName()
	{
		return createName;
	}

	public void setCreateName(String createName)
	{
		this.createName = createName;
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

	public String getSexTypeText(){
		if (sexType!=null)
		{
			return sexType.getText();
		}
		return "-";
	}
	
	public String getStateText(){
		if (state!=null)
		{
			return state.getText();
		}
		return "-";
	}
	
	public String getPositionName()
	{
		if(positionId!=null){
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.POSITION.name(), positionId,"name");
		}
		return "-";
	}
	
	public String getDepartmentName()
	{
		if(departmentId!=null){
			return (String) UserUtils.getBasicInfoFiledValue(BasicType.DEPARTMENT.name(), departmentId,"name");
		}
		return "-";
	}
}
