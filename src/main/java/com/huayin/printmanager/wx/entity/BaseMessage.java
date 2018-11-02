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
 * 微信框架 - 基础消息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class BaseMessage
{
	/**
	 * 接收方微信号
	 */
	private String ToUserName;

	/**
	 * 发送方微信号
	 */
	private String FromUserName;

	/**
	 * 创建时间
	 */
	private long CreateTime;

	/**
	 * 消息类型
	 */
	private String MsgType;

	public String getToUserName()
	{
		return ToUserName;
	}

	public void setToUserName(String toUserName)
	{
		ToUserName = toUserName;
	}

	public String getFromUserName()
	{
		return FromUserName;
	}

	public void setFromUserName(String fromUserName)
	{
		FromUserName = fromUserName;
	}

	public long getCreateTime()
	{
		return CreateTime;
	}

	public void setCreateTime(long createTime)
	{
		CreateTime = createTime;
	}

	public String getMsgType()
	{
		return MsgType;
	}

	public void setMsgType(String msgType)
	{
		MsgType = msgType;
	}

}
