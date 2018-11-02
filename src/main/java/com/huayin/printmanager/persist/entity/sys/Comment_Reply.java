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
 * 系统模块 - 留言板回复
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Entity
@Table(name = "sys_comment_reply")
public class Comment_Reply extends BaseTableIdEntity
{
	private static final long serialVersionUID = -5485657632550109904L;

	/**
	 * 提问表Id
	 */
	private Long masterId;

	/**
	 * 回复内容
	 */
	@Lob
	private String reply;

	/**
	 * 回复人
	 */
	private String replyUserName;

	/**
	 * 是否管理员回复
	 */
	@Enumerated(javax.persistence.EnumType.STRING)
	private BoolValue isManagerReplay = BoolValue.NO;

	/**
	 * 回复时间
	 */
	private Date updateTime;

	public Long getMasterId()
	{
		return masterId;
	}

	public void setMasterId(Long masterId)
	{
		this.masterId = masterId;
	}

	public String getReply()
	{
		return reply;
	}

	public void setReply(String reply)
	{
		this.reply = reply;
	}

	public String getReplyUserName()
	{
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName)
	{
		this.replyUserName = replyUserName;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public BoolValue getIsManagerReplay()
	{
		return isManagerReplay;
	}

	public void setIsManagerReplay(BoolValue isManagerReplay)
	{
		this.isManagerReplay = isManagerReplay;
	}
}
