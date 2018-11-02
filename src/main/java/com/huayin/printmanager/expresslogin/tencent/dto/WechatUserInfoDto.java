/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.expresslogin.tencent.dto;

/**
 * 
 * <pre>
 * 框架  - 微信用户登录
 * </pre>
 * @author mys
 * @version 1.0, 2017年2月27日
 */
public class WechatUserInfoDto
{
	private String openid;

	private String nickname;

	private String sex;

	private String provice;

	private String city;

	private String country;

	private String headimgurl;

	private String[] privilege;

	private String unionid;

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getProvice()
	{
		return provice;
	}

	public void setProvice(String provice)
	{
		this.provice = provice;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getHeadimgurl()
	{
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl)
	{
		this.headimgurl = headimgurl;
	}

	public String[] getPrivilege()
	{
		return privilege;
	}

	public void setPrivilege(String[] privilege)
	{
		this.privilege = privilege;
	}

	public String getUnionid()
	{
		return unionid;
	}

	public void setUnionid(String unionid)
	{
		this.unionid = unionid;
	}
}
