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

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;

/**
 * <pre>
 * 系统模块 - 版本公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_version_notice")
public class SystemVersionNotice extends BaseTableIdEntity
{
	private static final long serialVersionUID = 8865555374329340072L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	@Lob
	private String content;

	/**
	 * 是否发布
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue publish;

	/**
	 * 发布人
	 */
	private String userName;

	/**
	 * 发布时间
	 */
	private Date noticeTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public BoolValue getPublish()
	{
		return publish;
	}

	public void setPublish(BoolValue publish)
	{
		this.publish = publish;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Date getNoticeTime()
	{
		return noticeTime;
	}

	public void setNoticeTime(Date noticeTime)
	{
		this.noticeTime = noticeTime;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public String getPublishText()
	{
		if (publish != null)
		{

			return publish.getText();
		}
		return "-";
	}

}