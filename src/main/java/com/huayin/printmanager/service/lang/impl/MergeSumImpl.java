/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月24日 上午8:58:37
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.lang.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Reflections;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.lang.MergeSum;
import com.huayin.printmanager.service.lang.MergeSumAble;

/**
 * <pre>
 * 框架 - 合并汇总数据实现
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月24日
 */
@Service
public class MergeSumImpl extends BaseServiceImpl implements MergeSum
{
	public <T> SearchResult<T> process(MergeSumAble<T> mergeSumAble, Class<T> type)
	{
		// 1. 获取结果集
		SearchResult<T> result = mergeSumAble.getResult(type);
		
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal totalNoTaxMony = new BigDecimal(0);
		try
		{
			// 2. 创建一个心得汇总结果
			T newObject = type.newInstance();
			
			for (T t : result.getResult())
			{
				BigDecimal _totalMoney = (BigDecimal) Reflections.invokeGetter(t, "totalMoney");
				BigDecimal _totalTax = (BigDecimal) Reflections.invokeGetter(t, "totalTax");
				BigDecimal _totalNoTaxMony = (BigDecimal) Reflections.invokeGetter(t, "noTaxTotalMoney");
				totalMoney = totalMoney.add(_totalMoney);
				totalTax = totalTax.add(_totalTax);
				totalNoTaxMony = totalNoTaxMony.add(_totalNoTaxMony);
			}
			// 设置newObject得结果
			Reflections.invokeSetter(newObject, "totalMoney", totalMoney);
			Reflections.invokeSetter(newObject, "totalTax", totalTax);
			Reflections.invokeSetter(newObject, "noTaxTotalMoney", totalNoTaxMony);
			
			// 3. 存放汇总结构
			result.getResult().add(newObject);
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
