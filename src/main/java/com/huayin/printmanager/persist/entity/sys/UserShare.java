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

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 系统模块 - 用户快捷登录功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_usershare")
public class UserShare extends BaseTableIdEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * 共享类型
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(length = 20)
	private UserShareType userType;

	/**
	 * 系统用户ID
	 */
	private Long userId;

	/**
	 * 第三方唯一标识
	 */
	private String identifier;

	/**
	 * 其它配置信息
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "map_key")
	@Column(name = "map_value")
	private Map<String, String> extConfigs;

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public UserShareType getUserType()
	{
		return userType;
	}

	public void setUserType(UserShareType userType)
	{
		this.userType = userType;
	}

	public Map<String, String> getExtConfigs()
	{
		return extConfigs;
	}

	public void setExtConfigs(Map<String, String> extConfigs)
	{
		this.extConfigs = extConfigs;
	}

}
