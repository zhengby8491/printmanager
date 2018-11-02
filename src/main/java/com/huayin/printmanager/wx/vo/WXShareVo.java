/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.vo;

import java.io.Serializable;

/**
 * <pre>
 * 微信 - user关系VO
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WXShareVo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String openid;

	private Long userId;

	private String companyId;

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(String companyId)
	{
		this.companyId = companyId;
	}

	public WXShareVo(String openid, Long userId, String companyId)
	{
		super();
		this.openid = openid;
		this.userId = userId;
		this.companyId = companyId;
	}

	public WXShareVo()
	{
		super();
	}

}
