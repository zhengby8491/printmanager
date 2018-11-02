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
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;

/**
 * <pre>
 * 销售管理 - 销售对账
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月22日
 */
public class SaleReconcilHelper extends AbsHelper
{

	/**
	 * <pre>
	 * 计算所有销售对账 - 已收款金额
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:22:35, think
	 */
	public static Map<Long, BigDecimal> countAllReceiveMoney()
	{
		Map<Long, BigDecimal> map = Maps.newHashMap();
		SearchResult<SaleReconcilDetail> all = serviceFactory.getSaleReconcilService().findAll();
		for (SaleReconcilDetail detail : all.getResult())
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
	 * @since 1.0, 2018年1月22日 上午10:24:02, think
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
	 * @since 1.0, 2018年1月22日 上午10:25:53, think
	 */
	public static BigDecimal countReconcilQty(Map<Long, BigDecimal> map, Long sourceDetailId)
	{
		return map.get(sourceDetailId);
	}
}
