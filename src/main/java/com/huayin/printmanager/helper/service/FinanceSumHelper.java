/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月13日 下午3:54:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.helper.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;

/**
 * <pre>
 * 财务管理 - 汇总查询
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月13日
 */
public class FinanceSumHelper
{
	/**
	 * <pre>
	 * 手动分页
	 * </pre>
	 * @param all
	 * @param pageNumber
	 * @param pageSize
	 * @since 1.0, 2018年1月13日 下午3:38:56, think
	 */
	public static <T> void pageManual(SearchResult<T> all, int pageNumber, int pageSize)
	{
		List<T> result = all.getResult();
		int startSize = (pageNumber - 1) * pageSize;
		int endSize = pageNumber * pageSize;
		if(endSize > result.size())
		{
			endSize = result.size();
		}
		//System.out.println(startSize + "," + endSize + "," + result.size());
		List<T> subList = result.subList(startSize, endSize);
		List<T> resultNew = Lists.newArrayList();
		for(T vo : subList)
		{
			resultNew.add(vo);
		}
		all.setResult(resultNew);
	}
}
