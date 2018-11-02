/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.entity;

/**
 * <pre>
 * 微信框架 - 图文消息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class News
{
	private String Title;

	private String Description;

	private String PicUrl;

	private String Url;

	public String getTitle()
	{
		return Title;
	}

	public void setTitle(String title)
	{
		Title = title;
	}

	public String getDescription()
	{
		return Description;
	}

	public void setDescription(String description)
	{
		Description = description;
	}

	public String getPicUrl()
	{
		return PicUrl;
	}

	public void setPicUrl(String picUrl)
	{
		PicUrl = picUrl;
	}

	public String getUrl()
	{
		return Url;
	}

	public void setUrl(String url)
	{
		Url = url;
	}
}
