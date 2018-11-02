/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月19日 下午5:32:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;

/**
 * <pre>
 * 代工管理 - 代工对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月19日下午5:32:06, zhengby
 */
public class OemReconcilHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 计算所有销售对账 - 已收款金额
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:32:46, zhengby
	 */
	public static Map<Long, BigDecimal> countAllReceiveMoney()
	{
		Map<Long, BigDecimal> map = Maps.newHashMap();
		SearchResult<OemReconcilDetail> all = serviceFactory.getOemReconcilService().findAll();
		for (OemReconcilDetail detail : all.getResult())
		{
			// 源单id
			Long sourceDetailId = detail.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			processMoneyMap(map, sourceDetailId, detail.getReceiveMoney());
		}
		return map;
	}

	/**
	 * <pre>
	 * 根据sourceDetailId计算所有销售对账 - 已收款金额
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:32:56, zhengby
	 */
	public static BigDecimal countReconcilQty(Long sourceDetailId)
	{
		Map<Long, BigDecimal> map = countAllReceiveMoney();
		return map.get(sourceDetailId);
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算所有销售对账 - 已收款金额
	 * </pre>
	 * @param map
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:33:05, zhengby
	 */
	public static BigDecimal countReconcilQty(Map<Long, BigDecimal> map, Long sourceDetailId)
	{
		return map.get(sourceDetailId);
	}
}
