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
import com.huayin.printmanager.persist.entity.produce.WorkProduct;

/**
 * <pre>
 * 生产管理 - 常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月22日
 */
public class WorkHelper extends AbsHelper
{

	/**
	 * <pre>
	 * 计算所有工单产品已入库数量
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 上午9:47:42, think
	 */
	public static Map<Long, BigDecimal> countAllInStockQty()
	{
		Map<Long, BigDecimal> stockInQtyMap = Maps.newHashMap();
		SearchResult<WorkProduct> all = serviceFactory.getWorkService().findAllProduct();
		
		for (WorkProduct workProduct : all.getResult())
		{
			// 源单id
			Long sourceDetailId = workProduct.getSourceDetailId();
			if(null == sourceDetailId)
			{
				continue;
			}
			// 工单已入库数量
			processCountMap(stockInQtyMap, sourceDetailId, new BigDecimal(workProduct.getInStockQty()));
		}
		
		return stockInQtyMap;
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算工单产品已入库数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 上午9:48:38, think
	 */
	public static BigDecimal countInStockQty(Long sourceDetailId)
	{
		Map<Long, BigDecimal> inStockQtyMap = countAllInStockQty();
		return inStockQtyMap.get(sourceDetailId);
	}
	
	/**
	 * <pre>
	 * 根据sourceDetailId计算工单产品已入库数量
	 * </pre>
	 * @param inStockQtyMap
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:50:12, think
	 */
	public static BigDecimal countInStockQty(Map<Long, BigDecimal> inStockQtyMap, Long sourceDetailId)
	{
		BigDecimal qty = inStockQtyMap.get(sourceDetailId);
		
		return null == qty ? new BigDecimal(0) : qty;
	}
}
