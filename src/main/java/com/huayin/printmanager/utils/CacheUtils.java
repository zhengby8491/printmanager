/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.util.HashMap;
import java.util.Map;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * <pre>
 * 公共 - Cache工具类
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class CacheUtils
{
	private static CacheManager cacheManager = ((CacheManager) ComponentContextLoader.getBean("ehCacheManagerFactory"));

	public static String cacheType = SystemConfigUtil.getConfig(SysConstants.CACHE_TYPE);

	private static final String PUBLIC_CACHE_SYS = "PUBLIC_CACHE_SYS";

	private static final Integer cacheMinute = Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.CACHE_MINUTE));

	/**
	 * <pre>
	 * 获取SYS_CACHE缓存
	 * </pre>
	 * @param key
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:18:13, think
	 */
	public static Object get(String key)
	{
		return get(PUBLIC_CACHE_SYS, key);
	}

	/**
	 * <pre>
	 * 写入SYS_CACHE缓存
	 * </pre>
	 * @param key
	 * @param value
	 * @since 1.0, 2017年10月26日 上午9:18:20, think
	 */
	public static void put(String key, Object value)
	{
		put(PUBLIC_CACHE_SYS, key, value);
	}

	/**
	 * <pre>
	 * 从SYS_CACHE缓存中移除
	 * </pre>
	 * @param key
	 * @since 1.0, 2017年10月26日 上午9:18:27, think
	 */
	public static void remove(String key)
	{
		remove(PUBLIC_CACHE_SYS, key);
	}

	/**
	 * <pre>
	 * 获取缓存
	 * </pre>
	 * @param cacheName
	 * @param key
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:18:33, think
	 */
	public static Object get(String cacheName, String key)
	{

		if (cacheType.equals(SysConstants.CACHE_TYPE_EHCACHE))
		{// chcache框架
			Element element = getCache(cacheName).get(key);
			return element == null ? null : element.getObjectValue();
		}
		else if (cacheType.equals(SysConstants.CACHE_TYPE_REDIS))
		{// redis框架
			if (JedisUtils.mapObjectExists(cacheName, key))
			{
				return JedisUtils.getObjectMap(cacheName).get(key);
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * 写入缓存
	 * </pre>
	 * @param cacheName
	 * @param key
	 * @param value
	 * @since 1.0, 2017年10月26日 上午9:18:41, think
	 */
	public static void put(String cacheName, String key, Object value)
	{
		if (cacheType.equals(SysConstants.CACHE_TYPE_EHCACHE))
		{// chcache框架
			Element element = new Element(key, value);
			getCache(cacheName).put(element);
			getCache(cacheName).flush();
		}
		else if (cacheType.equals(SysConstants.CACHE_TYPE_REDIS))
		{// redis框架：目前效率低下
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(key, value);
			if (!JedisUtils.exists(cacheName))
			{
				JedisUtils.setObjectMap(cacheName, map, cacheMinute * 60);
			}
			else
			{
				JedisUtils.mapObjectPut(cacheName, map);
			}
		}
	}

	/**
	 * <pre>
	 * 从缓存中移除
	 * </pre>
	 * @param cacheName
	 * @param key
	 * @since 1.0, 2017年10月26日 上午9:18:49, think
	 */
	public static void remove(String cacheName, String key)
	{
		if (cacheType.equals(SysConstants.CACHE_TYPE_EHCACHE))
		{// chcache框架
			getCache(cacheName).remove(key);
			getCache(cacheName).flush();
		}
		else if (cacheType.equals(SysConstants.CACHE_TYPE_REDIS))
		{
			JedisUtils.mapObjectRemove(cacheName, key);
		}
	}

	/**
	 * <pre>
	 * 从缓存中移除缓存域
	 * </pre>
	 * @param cacheName
	 * @since 1.0, 2017年10月26日 上午9:18:55, think
	 */
	public static void removeCacheName(String cacheName)
	{
		if (cacheType.equals(SysConstants.CACHE_TYPE_EHCACHE))
		{// chcache框架
			removeCache(cacheName);
		}
		else if (cacheType.equals(SysConstants.CACHE_TYPE_REDIS))
		{
			JedisUtils.delObject(cacheName);
		}

	}

	/**
	 * <pre>
	 * 获得一个Cache，没有则创建一个。
	 * </pre>
	 * @param cacheName
	 * @return
	 * @since 1.0, 2017年10月26日 上午9:19:05, think
	 */
	public static Cache getCache(String cacheName)
	{
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null)
		{
			cacheManager.removeCache(cacheName);
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(true);
		}
		return cache;
	}

	/**
	 * <pre>
	 * 删除一个Cache域。
	 * </pre>
	 * @param cacheName
	 * @since 1.0, 2017年10月26日 上午9:19:12, think
	 */
	private static void removeCache(String cacheName)
	{
		cacheManager.removeCache(cacheName);
	}
}
