/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.persist.entity.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.SystemLogType;

/**
 * <pre>
 * 系统模块 - 系统日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_systemlog")
public class SystemLog extends BaseTableIdEntity
{
	private static final long serialVersionUID = -7359669634271106954L;

	/**
	 * 用户Id
	 */
	private Long userId;

	/**
	 * 系统日志类型
	 */
	@Column(length = 30)
	@Enumerated(javax.persistence.EnumType.STRING)
	private SystemLogType type;

	/**
	 * 模块名称
	 */
	@Column(length = 50)
	private String module;

	/**
	 * 操作结果
	 */
	@Column(length = 50)
	private String operationResult;

	/**
	 * 员工姓名
	 */
	@Column(length = 50)
	private String employeeName;

	/**
	 * 执行日期
	 */
	private Date execTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * IP
	 */
	private String operatorIp;

	/**
	 * 设备
	 */
	private String deviceType;

	/**
	 * 浏览器
	 */
	private String browser;

	/**
	 * 系统
	 */
	private String userAgent;

	@Transient
	private User user;

	@Transient
	private Company company;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public SystemLogType getType()
	{
		return type;
	}
	
//	public String getTypeText()
//	{
//		return ResourceBundleMessageSource.getMessageEnum(type, type.getText());
//	}

	public void setType(SystemLogType type)
	{
		this.type = type;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public Date getExecTime()
	{
		return execTime;
	}

	public void setExecTime(Date execTime)
	{
		this.execTime = execTime;
	}

	public String getModule()
	{
		return module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public String getOperationResult()
	{
		return operationResult;
	}

	public void setOperationResult(String operationResult)
	{
		this.operationResult = operationResult;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Company getCompany()
	{
		return company;
	}

	public void setCompany(Company company)
	{
		this.company = company;
	}

	public String getOperatorIp()
	{
		return operatorIp;
	}

	public void setOperatorIp(String operatorIp)
	{
		this.operatorIp = operatorIp;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getBrowser()
	{
		return browser;
	}

	public void setBrowser(String browser)
	{
		this.browser = browser;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getEmployeeName()
	{
		return employeeName;
	}

	public void setEmployeeName(String employeeName)
	{
		this.employeeName = employeeName;
	}

}