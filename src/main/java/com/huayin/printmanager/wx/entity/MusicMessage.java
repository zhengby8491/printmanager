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
 * 微信框架 - 声音消息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class MusicMessage extends BaseMessage
{
	private Music Music;

	public Music getMusic()
	{
		return Music;
	}

	public void setMusic(Music music)
	{
		Music = music;
	}
}
