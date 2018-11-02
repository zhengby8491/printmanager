/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年12月27日 下午3:45:09
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper;

import java.math.BigDecimal;
import java.util.Map;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.printmanager.service.ServiceFactory;

/**
 * <pre>
 * 框架 - 公共帮助类
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 */
public abstract class AbsHelper
{
	protected static ServiceFactory serviceFactory = ComponentContextLoader.getBean(ServiceFactory.class);
	
	/**
	 * <pre>
	 * 计算数量Map框架
	 * </pre>
	 * @author       think
	 * @since        1.0, 2018年1月22日
	 */
	public static class CountMap
	{
		private Long id;
		
		private BigDecimal count = new BigDecimal(0);
		
		public CountMap(Long id)
		{
			this.id = id;
		}

		public Long getId()
		{
			return this.id;
		}

		public void setId(Long id)
		{
			this.id = id;
		}

		public BigDecimal getCount()
		{
			return this.count;
		}

		public void setCount(BigDecimal count)
		{
			this.count = count;
		}
		
		public void countPlus(BigDecimal count)
		{
			this.count = this.count.add(count);
		}
	}
	
	/**
	 * <pre>
	 * 计算价格Map框架
	 * </pre>
	 * @param countMap
	 * @param sourceDetailId
	 * @param money
	 * @since 1.0, 2018年1月22日 下午4:42:15, think
	 */
	public static void processMoneyMap(Map<Long, BigDecimal> countMap, Long sourceDetailId, BigDecimal money)
	{
		// 源单id
		// 销售已对帐数量
		BigDecimal sum = countMap.get(sourceDetailId);
		if (null == sum)
		{
			sum = new BigDecimal(0);
		}
		sum = sum.add(money);
		countMap.put(sourceDetailId, sum);
	}
	
	/**
	 * <pre>
	 * 计算数量Map框架
	 * </pre>
	 * @param countMap
	 * @param sourceDetailId
	 * @param count
	 * @since 1.0, 2018年1月22日 上午10:21:37, think
	 */
	public static void processCountMap(Map<Long, BigDecimal> countMap, Long sourceDetailId, BigDecimal count)
	{
		// 源单id
		// 销售已对帐数量
		BigDecimal qty = countMap.get(sourceDetailId);
		if (null == qty)
		{
			qty = new BigDecimal(0);
		}
		qty = qty.add(count);
		countMap.put(sourceDetailId, qty);
	}
	
	/**
	 * <pre>
	 * 计算数量Map框架
	 * </pre>
	 * @param countMap
	 * @param sourceDetailId
	 * @param count
	 * @param id
	 * @since 1.0, 2018年1月22日 下午4:28:26, think
	 */
	public static void processCountMap(Map<Long, CountMap> countMap, Long sourceDetailId, BigDecimal count, Long id)
	{
		// 源单id
		// 销售已对帐数量
		CountMap map = countMap.get(sourceDetailId);
		if (null == map)
		{
			map = new CountMap(id);
		}
		map.countPlus(count);
		countMap.put(sourceDetailId, map);
	}
}
