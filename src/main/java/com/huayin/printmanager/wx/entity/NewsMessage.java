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

import java.util.List;

/**
 * <pre>
 * 微信框架 - 图文消息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class NewsMessage extends BaseMessage
{
	private int ArticleCount;

	private List<News> Articles;

	public int getArticleCount()
	{
		return ArticleCount;
	}

	public void setArticleCount(int articleCount)
	{
		ArticleCount = articleCount;
	}

	public List<News> getArticles()
	{
		return Articles;
	}

	public void setArticles(List<News> articles)
	{
		Articles = articles;
	}
}
