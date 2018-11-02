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
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.huayin.printmanager.persist.entity.BaseTableIdEntity;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.CommentType;

/**
 * <pre>
 * 系统模块 - 留言板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_comment")
public class Comment extends BaseTableIdEntity
{
	private static final long serialVersionUID = -5166155105773639041L;

	/**
	 * 标题
	 */
	@Column(length = 50, nullable = false)
	private String title;

	/**
	 * 留言类型
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private CommentType type;

	/**
	 * 内容
	 */
	@Lob
	private String content;

	/**
	 * 留言人
	 */
	private String userName;

	/**
	 * 联系方式
	 */
	private String contact;

	/**
	 * 表情标记
	 */
	@Column(length = 20)
	private String expression;

	/**
	 * 留言人IP
	 */
	@Column(length = 50)
	private String ip;

	/**
	 * 审核状态
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue auditState = BoolValue.NO;

	/**
	 * 是否公开
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isPublic = BoolValue.NO;

	/**
	 * 留言时间
	 */
	private Date createTime;

	/**
	 * 回复状态
	 */
	@Column(length = 10)
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue replyState = BoolValue.NO;

	/**
	 * 最后回复时间
	 */
	private Date updateTime;

	@Transient
	private List<Comment_Reply> detailList;

	public BoolValue getAuditState()
	{
		return auditState;
	}

	public void setAuditState(BoolValue auditState)
	{
		this.auditState = auditState;
	}

	public CommentType getType()
	{
		return type;
	}

	public void setType(CommentType type)
	{
		this.type = type;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getContact()
	{
		return contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public String getExpression()
	{
		return expression;
	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public BoolValue getIsPublic()
	{
		return isPublic;
	}

	public void setIsPublic(BoolValue isPublic)
	{
		this.isPublic = isPublic;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public BoolValue getReplyState()
	{
		return replyState;
	}

	public void setReplyState(BoolValue replyState)
	{
		this.replyState = replyState;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public List<Comment_Reply> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<Comment_Reply> detailList)
	{
		this.detailList = detailList;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}
}
