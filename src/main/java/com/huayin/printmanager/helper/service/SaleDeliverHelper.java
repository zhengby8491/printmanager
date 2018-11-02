/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月22日 下午3:44:13
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.helper.AbsHelper;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;

/**
 * <pre>
 * 销售管理 - 销售送货
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月22日
 */
public class SaleDeliverHelper extends AbsHelper
{

	/**
	 * <pre>
	 * 计算所有销售送货单 - 已对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 下午4:05:14, think
	 */
	public static Map<Long, CountMap> countAllReconcilQty()
	{
		Map<Long, CountMap> map = Maps.newHashMap();
		SearchResult<SaleDeliverDetail> all = serviceFactory.getSaleDeliverService().findAll();
		
		for (SaleDeliverDetail detail : all.getResult())
		{
			// 源单id
			Long sourceDetailId = detail.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			// 送货单对帐数量
			processCountMap(map, sourceDetailId, new BigDecimal(detail.getReconcilQty()), detail.getId());
		}
		
		return map;
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算销售送货单 - 已对账数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 下午4:06:18, think
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
	 * @since 1.0, 2018年1月22日 下午4:06:55, think
	 */
	public static CountMap countReconcilQty(Map<Long, CountMap> all, Long sourceDetailId)
	{
		return all.get(sourceDetailId);
	}
}
