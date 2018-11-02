/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * <pre>
 * 公共 - 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{

	private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	/**
	 * <pre>
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:07, think
	 */
	public static String getDate()
	{
		return getDate("yyyy-MM-dd");
	}

	/**
	 * <pre>
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * </pre>
	 * @param pattern
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:20, think
	 */
	public static String getDate(String pattern)
	{
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * <pre>
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * </pre>
	 * @param date
	 * @param pattern
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:29, think
	 */
	public static String formatDate(Date date, Object... pattern)
	{
		String formatDate = null;
		if (pattern != null && pattern.length > 0)
		{
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		}
		else
		{
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * <pre>
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 * </pre>
	 * @param date
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:37, think
	 */
	public static String formatDateTime(Date date)
	{
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * <pre>
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:43, think
	 */
	public static String getTime()
	{
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * <pre>
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:50, think
	 */
	public static String getDateTime()
	{
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * <pre>
	 * 得到当前年份字符串 格式（yyyy）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:38:57, think
	 */
	public static String getYear()
	{
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * <pre>
	 * 得到当前月份字符串 格式（MM）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:39:05, think
	 */
	public static String getMonth()
	{
		return formatDate(new Date(), "MM");
	}

	/**
	 * <pre>
	 * 得到当天字符串 格式（dd）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:39:12, think
	 */
	public static String getDay()
	{
		return formatDate(new Date(), "dd");
	}

	/**
	 * <pre>
	 * 得到当前星期字符串 格式（E）星期几
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:39:18, think
	 */
	public static String getWeek()
	{
		return formatDate(new Date(), "E");
	}

	/**
	 * <pre>
	 * 日期型字符串转化为日期 格式
	 *   "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" 
	 * </pre>
	 * @param str
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:39:25, think
	 */
	public static Date parseDate(Object str)
	{
		if (str == null)
		{
			return null;
		}
		try
		{
			return parseDate(str.toString(), parsePatterns);
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	/**
	 * <pre>
	 * 获取过去的天数
	 * </pre>
	 * @param date
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:39:55, think
	 */
	public static long pastDays(Date date)
	{
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * <pre>
	 * 获取过去的小时
	 * </pre>
	 * @param date
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:40:04, think
	 */
	public static long pastHour(Date date)
	{
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * <pre>
	 * 获取过去的分钟
	 * </pre>
	 * @param date
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:40:10, think
	 */
	public static long pastMinutes(Date date)
	{
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * <pre>
	 * 转换为时间（天,时:分:秒.毫秒）
	 * </pre>
	 * @param timeMillis
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:40:18, think
	 */
	public static String formatDateTime(long timeMillis)
	{
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		String msg = (day > 0 ? day + "天，" : "") + (hour > 0 ? hour + "时" : "") + (s > 0 ? s + "秒" : "") + sss + "毫秒";

		return msg;
	}

	/**
	 * <pre>
	 * 获取两个日期之间的天数
	 * </pre>
	 * @param before
	 * @param after
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:40:26, think
	 */
	public static double getDistanceOfTwoDate(Date before, Date after)
	{
		String dateMin = formatDate(before, "yyyy-MM-dd");
		String dateMax = formatDate(after, "yyyy-MM-dd");
		try
		{
			before = parseDate(dateMin, "yyyy-MM-dd");
			after = parseDate(dateMax, "yyyy-MM-dd");
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * <pre>
	 * 获取指定月份的天数  
	 * </pre>
	 * @param year
	 * @param month
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:42:38, think
	 */
	public static int getDaysByYearMonth(int year, int month)
	{
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param sourceTime
	 * @param curTime
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:42:35, think
	 */
	public static boolean isInTime(String sourceTime, String curTime)
	{
		if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":"))
		{
			throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
		}
		if (curTime == null || !curTime.contains(":"))
		{
			throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
		}
		String[] args = sourceTime.split("-");
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try
		{
			long now = sdf.parse(curTime).getTime();
			long start = sdf.parse(args[0]).getTime();
			long end = sdf.parse(args[1]).getTime();
			if (args[1].equals("00:00"))
			{
				args[1] = "24:00";
			}
			if (end < start)
			{
				if (now >= end && now < start)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else
			{
				if (now >= start && now < end)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
		}

	}
}
