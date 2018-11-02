package com.huayin.printmanager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.utils.JedisUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Jedis Cache 工具类
 * 
 * @author zhaojt
 * @version 2014-6-29
 */
public class JedisUtils_Test {

	private static Logger logger = LoggerFactory.getLogger(JedisUtils_Test.class);
	private static JedisPool jedisPool = null;

	public static Set<String> keys(String pattern) {
		Set<String> keys = new HashSet<String>();
		Jedis jedis = null;
		try {
			jedis = getResource();
			keys = jedis.keys(pattern);
		} catch (Exception e) {
			logger.warn("keys {}", pattern, e);
		} finally {
			jedis.close();
		}
		return keys;
	}

	public static int hsize(String cacheName) {
		int size = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			size = jedis.hlen(JedisUtils.getBytesKey(cacheName)).intValue();
		} catch (Exception e) {
			logger.error("size {}", cacheName, e);
		} finally {
			jedis.close();
		}
		return size;
	}

	public static Set<Object> hkeys(String cacheName) {
		Set<Object> keys = Sets.newHashSet();
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Set<byte[]> set = jedis.hkeys(JedisUtils.getBytesKey(cacheName));
			for (byte[] key : set) {
				keys.add((Object) key);
			}
			logger.debug("keys {} {} ", cacheName, keys);
			return keys;
		} catch (Exception e) {
			logger.error("keys {}", cacheName, e);
		} finally {
			jedis.close();
		}
		return keys;
	}

	public static Collection<Object> hvals(String cacheName) {
		Collection<Object> vals = Collections.emptyList();
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Collection<byte[]> col = jedis.hvals(JedisUtils
					.getBytesKey(cacheName));
			for (byte[] val : col) {
				vals.add((Object) val);
			}
			logger.debug("values {} {} ", cacheName, vals);
		} catch (Exception e) {
			logger.error("values {}", cacheName, e);
		} finally {
			jedis.close();
		}
		return vals;
	}

	/**
	 * 获取HASH缓存字段
	 * 
	 * @param key
	 *            字段名
	 * @return 字段值
	 */
	public static Object hget(String cacheName, Object key) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			value = (Object) toObject(jedis.hget(getBytesKey(cacheName),
					getBytesKey(key)));
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 设置HASH缓存字段
	 * 
	 * @param key
	 *            字段名
	 * @param value
	 *            字段值
	 */
	public static void hset(String cacheName, Object key, Object value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.hset(getBytesKey(cacheName), getBytesKey(key), toBytes(value));
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 删除HASH缓存字段
	 * 
	 * @param key
	 *            字段名
	 */
	public static void hdel(String cacheName, Object key) {

		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.hdel(getBytesKey(cacheName), getBytesKey(key));
		} catch (Exception e) {
			logger.debug("hdel {} {}", cacheName, key, e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 删除整个HASH缓存
	 */
	public static void hdel(String cacheName) {

		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.hdel(getBytesKey(cacheName));
		} catch (Exception e) {
			logger.debug("hdel {} {}", cacheName, e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value)
						&& !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static Object getObject(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		Object value = request.getAttribute(key);
		if (value != null) {
			return value;
		}
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));
				request.removeAttribute(key);
				request.setAttribute(key, value);
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(getBytesKey(key), toBytes(value));
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 设置List缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.rpush(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setList {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 设置List缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value,
			int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listAdd {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			logger.debug("listObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listObjectAdd {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getSet {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		Set<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedis.smembers(getBytesKey(key));
				for (byte[] bs : set) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectSet {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 设置Set缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.sadd(key, (String[]) value.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSet {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 设置Set缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value,
			int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectSet {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long setSetAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.sadd(key, value);
			logger.debug("setSetAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetAdd {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long setSetObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) set.toArray());
			logger.debug("setSetObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetObjectAdd {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 获取Map缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		@SuppressWarnings("unchecked")
		Map<String, String> value = (Map<String, String>) request
				.getAttribute(key);
		if (value != null) {
			return value;
		}
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hgetAll(key);
				request.removeAttribute(key);
				request.setAttribute(key, value);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getMap {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

	/**
	 * 获取Map缓存
	 * 
	 * @param key
	 *            键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (Map<String, Object>) request
				.getAttribute(key);
		if (value != null) {
			return value;
		}
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
					value.put(StringUtils.toString(e.getKey()),
							toObject(e.getValue()));
				}
				request.removeAttribute(key);
				request.setAttribute(key, value);
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return value;
	}

	/**
	 * 设置Map缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value,
			int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.hmset(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setMap {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 设置Map缓存
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param cacheSeconds
	 *            超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value,
			int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向Map缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hmset(key, value);
			logger.debug("mapPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapPut {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 向Map缓存中添加值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static String mapObjectPut(String key, Map<String, Object> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
			logger.debug("mapObjectPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapObjectPut {} = {}", key, value, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 移除Map缓存中的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(key, mapKey);
			logger.debug("mapRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 移除Map缓存中的值
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static long mapObjectRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 判断Map缓存中的Key是否存在
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(key, mapKey);
			logger.debug("mapExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapExists {}  {}", key, mapKey, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 判断Map缓存中的Key是否存在
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return
	 */
	public static boolean mapObjectExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectExists {}  {}", key, mapKey, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				result = jedis.del(key);
				logger.debug("del {}", key);
			} else {
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				result = jedis.del(getBytesKey(key));
				logger.debug("delObject {}", key);
			} else {
				logger.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("delObject {}", key, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			logger.warn("exists {}", key, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key
	 *            键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(getBytesKey(key));
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			logger.warn("existsObject {}", key, e);
		} finally {
			jedis.close();
		}
		return result;
	}

	public static void expire(String key, int cacheSeconds) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.expire(key, cacheSeconds);
			logger.debug("expire {} {}", key, cacheSeconds);
		} catch (Exception e) {
			logger.warn("expire {} {}", key, cacheSeconds, e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {

			JedisPoolConfig jedisConfig=new JedisPoolConfig();
			jedisPool = new redis.clients.jedis.JedisPool(jedisConfig,"192.168.1.168",6379,1000,"hysystem");
			jedis = jedisPool.getResource();
			// logger.debug("getResource.", jedis);
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			jedis.close();
			throw e;
		}
		return jedis;
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * 
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}

	public static void main(String[] args)
	{
		User u=new User();
		u.setId(1l);
		u.setUserName("a");
		setObject("a",u ,60);
		delObject("a");
	}
}
