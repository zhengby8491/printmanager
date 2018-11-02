/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月24日 上午9:26:54
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.lang;

import com.huayin.common.persist.query.SearchResult;

/**
 * <pre>
 * 框架 - 合并汇总数据实现
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月24日
 */
public interface MergeSum
{
	public <T> SearchResult<T> process(MergeSumAble<T> mergeSumAble, Class<T> type);
}
