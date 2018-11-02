/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月24日 上午8:56:57
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.lang;

import com.huayin.common.persist.query.SearchResult;

/**
 * <pre>
 * 框架 - 合并汇总数据接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月24日
 */
public interface MergeSumAble<T>
{
	/**
	 * <pre>
	 * 获取所有数据结果
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月24日 上午8:57:34, think
	 */
	public abstract SearchResult<T> getResult(Class<T> type);
}
