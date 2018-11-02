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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.huayin.common.persist.entity.AbstractEntity;
import com.huayin.printmanager.persist.CacheType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CompanyState;
import com.huayin.printmanager.persist.enumerate.CompanyType;
import com.huayin.printmanager.persist.enumerate.CurrencyType;
import com.huayin.printmanager.persist.enumerate.InitStep;

/**
 * <pre>
 * 系统模块 - 公司管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = CacheType.SECOND_CACHE_DEFAULT)
public class Company extends AbstractEntity
{
	private static final long serialVersionUID = -7400565006999701846L;

	/**
	 * 主键，可以自定义，再此默认取列表最大ID值加1
	 */
	@Id
	private String id;

	/**
	 * 注册用户ID
	 */
	private Long registerUserId;

	/**
	 * 到期时间
	 */
	private Date expireTime;

	/**
	 * 币别
	 */
	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	private CurrencyType standardCurrency;

	/**
	 * 公司名称
	 */
	@Column(length = 50)
	private String name;

	/**
	 * 传真
	 */
	@Column(length = 20)
	private String fax;

	/**
	 * 联系人
	 */
	@Column(length = 30)
	private String linkName;

	/**
	 * 电话
	 */
	@Column(length = 20)
	private String tel;

	/**
	 * 邮箱
	 */
	@Column(length = 50)
	private String email;

	/**
	 * 微信公众号
	 */
	@Column(length = 50)
	private String weixin;

	/**
	 * 网址
	 */
	private String website;

	/**
	 * 简介
	 */
	private String introduction;

	/**
	 * 所在省份
	 */
	@Column(length = 20)
	private String province;

	/**
	 * 所在城市
	 */
	@Column(length = 20)
	private String city;

	/**
	 * 所在区县
	 */
	@Column(length = 20)
	private String county;

	/**
	 * 具体地址
	 */
	private String address;

	/**
	 * 状态(未开通,正常,暂停,关闭)
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private CompanyState state;

	/**
	 * 类型(普通)
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private CompanyType type;

	/**
	 * 是否正式用户（相对测试）
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isFormal;

	/**
	 * 初始化进度
	 */
	@Column(length = 20)
	@Enumerated(javax.persistence.EnumType.STRING)
	private InitStep initStep;

	/**
	 * 角色数据限制（级别高于全局角色数据（系统参数表）控制）
	 */
	private Integer roleCountMax;

	/**
	 * 代理商ID
	 */
	private Long agentQuotientId;

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
	 * 合同公司
	 */
	@Column(length = 50)
	private String contractCompanyName;

	/**
	 * 合同日期
	 */
	private Date contractTime;

	/**
	 * 系统版本（工单版本：1；标准版本：2）
	 */
	private Integer systemVersion;

	/**
	 * 代工平台（勾选则可以在发外订单种选择代工得供应商）
	 */
	@Column(columnDefinition = "varchar(20) default 'NO'")
	@Enumerated(EnumType.STRING)
	private BoolValue isOem = BoolValue.NO;

	@Transient
	private List<Long> menuIdList = new ArrayList<Long>();

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public Date getExpireTime()
	{
		return expireTime;
	}

	public void setExpireTime(Date expireTime)
	{
		this.expireTime = expireTime;
	}

	public String getLinkName()
	{
		return linkName;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public CompanyType getType()
	{
		return type;
	}

	public CompanyState getState()
	{
		return state;
	}

	public void setState(CompanyState state)
	{
		this.state = state;
	}

	public void setType(CompanyType type)
	{
		this.type = type;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
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

	public CurrencyType getStandardCurrency()
	{
		return standardCurrency;
	}

	public void setStandardCurrency(CurrencyType standardCurrency)
	{
		this.standardCurrency = standardCurrency;
	}

	public InitStep getInitStep()
	{
		return initStep;
	}

	public void setInitStep(InitStep initStep)
	{
		this.initStep = initStep;
	}

	public String getCounty()
	{
		return county;
	}

	public void setCounty(String county)
	{
		this.county = county;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public String getWeixin()
	{
		return weixin;
	}

	public void setWeixin(String weixin)
	{
		this.weixin = weixin;
	}

	public BoolValue getIsFormal()
	{
		return isFormal;
	}

	public void setIsFormal(BoolValue isFormal)
	{
		this.isFormal = isFormal;
	}

	public String getIntroduction()
	{
		return introduction;
	}

	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}

	public List<Long> getMenuIdList()
	{
		return menuIdList;
	}

	public void setMenuIdList(List<Long> menuIdList)
	{
		this.menuIdList = menuIdList;
	}

	public String getContractCompanyName()
	{
		return contractCompanyName;
	}

	public void setContractCompanyName(String contractCompanyName)
	{
		this.contractCompanyName = contractCompanyName;
	}

	public Date getContractTime()
	{
		return contractTime;
	}

	public void setContractTime(Date contractTime)
	{
		this.contractTime = contractTime;
	}

	public Long getAgentQuotientId()
	{
		return agentQuotientId;
	}

	public void setAgentQuotientId(Long agentQuotientId)
	{
		this.agentQuotientId = agentQuotientId;
	}

	public Long getRegisterUserId()
	{
		return registerUserId;
	}

	public void setRegisterUserId(Long registerUserId)
	{
		this.registerUserId = registerUserId;
	}

	public Integer getRoleCountMax()
	{
		return roleCountMax;
	}

	public void setRoleCountMax(Integer roleCountMax)
	{
		this.roleCountMax = roleCountMax;
	}

	public Integer getSystemVersion()
	{
		return systemVersion;
	}

	public void setSystemVersion(Integer systemVersion)
	{
		this.systemVersion = systemVersion;
	}

	public BoolValue getIsOem()
	{
		return isOem;
	}

	public void setIsOem(BoolValue isOem)
	{
		this.isOem = isOem;
	}

	public String getStateText()
	{
		if (state != null)
		{
			return state.getText();
		}
		return "-";
	}

	public String getInitStepText()
	{
		if (initStep != null)
		{
			return initStep.getText();
		}
		return "-";
	}

	public String getIsFormalText()
	{
		if (isFormal != null)
		{
			return isFormal.getText();
		}
		return "-";
	}

	public String getTypeText()
	{
		if (type != null)
		{
			return type.getText();
		}
		return "-";
	}

	public String getStandardCurrencyText()
	{
		if (standardCurrency != null)
		{
			return standardCurrency.getText();
		}
		return "-";
	}
}