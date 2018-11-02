/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月19日 下午5:19:26
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.util.Map;

import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;

/**
 * <pre>
 *  代工管理 - 代工送货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月19日下午5:19:26, zhengby
 */
public class OemDeliverHelper extends AbsHelper
{
	/**
	 * <pre>
	 * 计算所有销售送货单 - 已对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:31:34, zhengby
	 */
	public static Map<Long, CountMap> countAllReconcilQty()
	{
		Map<Long, CountMap> map = Maps.newHashMap();
		SearchResult<OemDeliverDetail> all = serviceFactory.getOemDeliverService().findAll();
		
		for (OemDeliverDetail detail : all.getResult())
		{
			// 源单id
			Long sourceDetailId = detail.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			// 送货单对帐数量
			processCountMap(map, sourceDetailId, detail.getReconcilQty(), detail.getId());
		}
		
		return map;
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算销售送货单 - 已对账数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:31:17, zhengby
	 */
	public static CountMap countReconcilQty(Long sourceDetailId)
	{
		Map<Long, CountMap> all = countAllReconcilQty();
		return all.get(sourceDetailId);
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算销售送货单 - 已对账数量
	 * </pre>
	 * @param all
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:29:58, zhengby
	 */
	public static CountMap countReconcilQty(Map<Long, CountMap> all, Long sourceDetailId)
	{
		return all.get(sourceDetailId);
	}
}
