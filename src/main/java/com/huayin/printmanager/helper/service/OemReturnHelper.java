/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月19日 下午5:37:34
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
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;

/**
 * <pre>
 * 代工管理 - 代工退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月19日下午5:37:34, zhengby
 */
public class OemReturnHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 计算所有代工退货 - 已对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:38:16, zhengby
	 */
	public static Map<Long, BigDecimal> countAllReconcilQty()
	{
		Map<Long, BigDecimal> map = Maps.newHashMap();
		SearchResult<OemReturnDetail> all = serviceFactory.getOemReturnService().findAll(ReturnGoodsType.EXCHANGE);
		for (OemReturnDetail oemReturnDetail : all.getResult())
		{
			// 源单id
			Long sourceDetailId = oemReturnDetail.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			processCountMap(map, sourceDetailId, oemReturnDetail.getReconcilQty());
		}
		return map;
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算所有代工退货 - 已对账数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:38:24, zhengby
	 */
	public static BigDecimal countReconcilQty(Long sourceDetailId)
	{
		Map<Long, BigDecimal> map = countAllReconcilQty();
		return map.get(sourceDetailId);
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算所有代工退货 - 已对账数量
	 * </pre>
	 * @param map
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:38:33, zhengby
	 */
	public static BigDecimal countReconcilQty(Map<Long, BigDecimal> map, Long sourceDetailId)
	{
		return map.get(sourceDetailId);
	}
}
