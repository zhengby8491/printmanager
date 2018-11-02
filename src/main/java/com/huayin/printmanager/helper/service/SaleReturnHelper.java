/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月22日 上午9:32:33
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
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;

/**
 * <pre>
 * 销售管理 - 销售退货
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月22日
 */
public class SaleReturnHelper extends AbsHelper
{

	/**
	 * <pre>
	 * 计算所有销售退货 - 已对账数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:22:35, think
	 */
	public static Map<Long, BigDecimal> countAllReconcilQty()
	{
		Map<Long, BigDecimal> map = Maps.newHashMap();
		SearchResult<SaleReturnDetail> all = serviceFactory.getSaleReturnService().findAll(ReturnGoodsType.EXCHANGE);
		for (SaleReturnDetail saleReturnDetail : all.getResult())
		{
			// 源单id
			Long sourceDetailId = saleReturnDetail.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			processCountMap(map, sourceDetailId, new BigDecimal(saleReturnDetail.getReconcilQty()));
		}
		return map;
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算所有销售退货 - 已对账数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:24:02, think
	 */
	public static BigDecimal countReconcilQty(Long sourceDetailId)
	{
		Map<Long, BigDecimal> map = countAllReconcilQty();
		return map.get(sourceDetailId);
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算所有销售退货 - 已对账数量
	 * </pre>
	 * @param map
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:25:53, think
	 */
	public static BigDecimal countReconcilQty(Map<Long, BigDecimal> map, Long sourceDetailId)
	{
		return map.get(sourceDetailId);
	}
}
