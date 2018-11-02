/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月16日 上午11:18:00
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 公共 - 快捷选择
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月16日
 */
public interface QuickService
{
	/**
	 * <pre>
	 * 查询供应商的代工平台信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 下午2:17:29, think
	 */
	public SearchResult<Supplier> findSupplierOemList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询客户的代工平台信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:44:24, think
	 */
	public SearchResult<Customer> findCustomerOemList(QueryParam queryParam);
}
