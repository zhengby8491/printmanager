/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.huayin.printmanager.wx.entity.Image;
import com.huayin.printmanager.wx.entity.ImageMessage;
import com.huayin.printmanager.wx.entity.Music;
import com.huayin.printmanager.wx.entity.MusicMessage;
import com.huayin.printmanager.wx.entity.News;
import com.huayin.printmanager.wx.entity.NewsMessage;
import com.huayin.printmanager.wx.entity.TextMessage;
import com.thoughtworks.xstream.XStream;

/**
 * <pre>
 * 框架 - 消息工具
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class MessageUtil
{
	public static final String MESSAGE_TEXT = "text";

	public static final String MESSAGE_NEWS = "news";

	public static final String MESSAGE_IMAGE = "image";

	public static final String MESSAGE_VOICE = "voice";

	public static final String MESSAGE_MUSIC = "music";

	public static final String MESSAGE_VIDEO = "video";

	public static final String MESSAGE_LINK = "link";

	public static final String MESSAGE_LOCATION = "location";

	public static final String MESSAGE_EVNET = "event";

	public static final String MESSAGE_SUBSCRIBE = "subscribe";

	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";

	public static final String MESSAGE_CLICK = "CLICK";

	public static final String MESSAGE_VIEW = "VIEW";

	public static final String MESSAGE_SCANCODE = "scancode_push";

	/**
	 * xml转为map集合
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlTomap(HttpServletRequest request) throws IOException, DocumentException
	{
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();

		List<Element> list = root.elements();

		for (Element e : list)
		{
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}

	/**
	 * 将文本消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToxml(TextMessage t)
	{
		XStream xStream = new XStream();
		xStream.alias("xml", t.getClass());
		return xStream.toXML(t);
	}

	/**
	 * 生成返回消息内容
	 */
	public static String initText(String toUserName, String fromUserName, String content)
	{
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToxml(text);
	}

	/**
	 * 关注后提示的消息
	 */
	public static String menuText()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注这个辣鸡测试号");
		sb.append("请按照菜单提示进行操作：\n\n");
		sb.append("1、骂你是逗B\n");
		sb.append("2、慕课网介绍\n");
		sb.append("3、图片图片\n");
		sb.append("4、音乐音乐\n");
		sb.append("5、翻译英文\n\n");
		sb.append("回复？调出此菜单。");
		return sb.toString();
	}

	public static String firstMenu()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("你是逗B");
		return sb.toString();
	}

	public static String threeMenu()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("词组翻译使用指南\n\n");
		sb.append("使用示例：\n");
		sb.append("翻译足球\n");
		sb.append("翻译中国足球\n");
		sb.append("回复？显示主菜单。");
		return sb.toString();
	}

	/**
	 * 将文本消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String newsMessageToxml(NewsMessage n)
	{
		XStream xStream = new XStream();
		xStream.alias("xml", n.getClass());
		xStream.alias("item", new News().getClass());
		return xStream.toXML(n);
	}

	/**
	 * 将图像消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage)
	{
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}

	/**
	 * 将音乐消息对象转为xml
	 * @param textMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage)
	{
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 创建图文消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName, String fromUserName)
	{
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();

		News news = new News();
		news.setTitle("啊啊啊啊");
		news.setDescription("辣鸡辣鸡辣鸡辣鸡");
		news.setPicUrl("http://14aa867652.imwork.net.ngrok.cc/WeixinKF/image/imooc.jpg");
		news.setUrl("http://www.imooc.com");

		newsList.add(news);

		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());

		message = newsMessageToxml(newsMessage);
		return message;
	}

	/**
	 * 创建图片消息
	 */
	public static String initImageMessage(String toUserName, String formUserName)
	{
		String message = null;
		Image Image = new Image();
		Image.setMediaId("8zITL7S7uBqp6RuaDLlEGnpv0hg5KFrfv21M8DlX9_70FugxzIBW3rSelONsU4Ow");
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(formUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(Image);
		message = imageMessageToXml(imageMessage);
		// System.out.println(message);
		return message;
	}

	/**
	 * 创建声音消息
	 */
	public static String initMusicMessage(String toUserName, String formUserName)
	{
		String message = null;
		Music Music = new Music();
		Music.setThumbMediaId("WakASrKsuYL8O5VBv5bT5pE6WPFOGWmzpugiAhKpXyncOKR9Q3H9Wci-PvNkRe30");
		Music.setDescription("陈奕迅 - I Do");
		Music.setTitle("陈奕迅 - I Do");
		Music.setMusicUrl("http://14aa867652.imwork.net.ngrok.cc/WeixinKF/resource/bbb.mp3");
		Music.setHQMusicUrl("http://14aa867652.imwork.net.ngrok.cc/WeixinKF/resource/bbb.mp3");

		MusicMessage MusicMessage = new MusicMessage();
		MusicMessage.setFromUserName(toUserName);
		MusicMessage.setToUserName(formUserName);
		MusicMessage.setMsgType(MESSAGE_MUSIC);
		MusicMessage.setCreateTime(new Date().getTime());
		MusicMessage.setMusic(Music);
		message = musicMessageToXml(MusicMessage);
		// System.out.println(message);
		return message;
	}
}
