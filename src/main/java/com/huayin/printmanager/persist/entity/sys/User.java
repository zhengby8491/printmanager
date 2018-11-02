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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.common.persist.entity.AuditedEntity;
import com.huayin.printmanager.persist.entity.BaseUserTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.UserSource;

/**
 * <pre>
 * 系统模块 - 用户管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_user")
public class User extends BaseUserTableIdEntity implements AuditedEntity
{
	private static final long serialVersionUID = -6082641296878214602L;

	/**
	 * 账号
	 */
	@Column(length = 30, unique = true)
	private String userName;

	/**
	 * 登录密码
	 */
	@Column(length = 255)
	private String password;

	/**
	 * 手机
	 */
	@Column(length = 20, unique = true)
	private String mobile;

	/**
	 * 电话
	 */
	@Column(length = 20)
	private String phone;

	/**
	 * 邮箱
	 */
	@Column(length = 30)
	private String email;

	/**
	 * 员工ID
	 */
	private Long employeeId;

	/**
	 * 真实姓名
	 */
	@Column(length = 20)
	private String realName;

	/**
	 * 用户状态
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private State state;

	/**
	 * 来源
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private UserSource resource;

	/**
	 * 最后登陆用户ip
	 */
	private String lastLoginIp;

	/**
	 * 最后登陆时间
	 */
	private Date lastLoginTime;

	/**
	 * 登陆成功次数
	 */
	private Integer loginCount;

	/**
	 * 错误登陆次数
	 */
	private Integer loginErrCount;

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

	/**
	 * 版本信息通知1已通知 0未通知
	 */
	private int versionNotify = 0;
	
	/**
	 * 印刷家商户token，印管家的默认为null
	 */
	private String token;
	
	/**
	 * 绑定印刷家标记
	 */
	@Enumerated(EnumType.STRING)
	private BoolValue isSign = BoolValue.NO;
	
	/**
	 * 印刷家用户的唯一标识
	 */
	private String uid;

	@Transient
	private Company company;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getLastLoginIp()
	{
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp)
	{
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getLoginCount()
	{
		return loginCount;
	}

	public void setLoginCount(Integer loginCount)
	{
		this.loginCount = loginCount;
	}

	public Integer getLoginErrCount()
	{
		return loginErrCount;
	}

	public void setLoginErrCount(Integer loginErrCount)
	{
		this.loginErrCount = loginErrCount;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
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

	public Company getCompany()
	{
		return company;
	}

	public void setCompany(Company company)
	{
		this.company = company;
	}

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public UserSource getResource()
	{
		return resource;
	}

	public void setResource(UserSource resource)
	{
		this.resource = resource;
	}

	public int getVersionNotify()
	{
		return versionNotify;
	}

	public void setVersionNotify(int versionNotify)
	{
		this.versionNotify = versionNotify;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public BoolValue getIsSign()
	{
		return isSign;
	}

	public void setIsSign(BoolValue isSign)
	{
		this.isSign = isSign;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

}