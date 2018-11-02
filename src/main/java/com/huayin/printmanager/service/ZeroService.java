/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月16日 下午2:28:59
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service;

import java.util.List;
import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 框架 - 公共业务常用功能
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月16日
 */
public interface ZeroService
{
	/**
	 * <pre>
	 * 查询供应商信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 下午2:16:09, think
	 */
	public SearchResult<Supplier> findSupplierList(QueryParam queryParam);

	/**
	 * <pre>
	 * 从公司查询供应商信息（用于代工平台）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 下午2:26:37, think
	 */
	public SearchResult<Supplier> findSupplierListFromCompany(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 查询客户信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午11:04:23, think
	 */
	public SearchResult<Customer> findCustomerList(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 从公司查询客户信息（用于代工平台）
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午11:04:32, think
	 */
	public SearchResult<Customer> findCustomerListFromCompany(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据originCompanyId查询客户信息
	 * </pre>
	 * @param originCompanyId
	 * @return
	 * @since 1.0, 2018年3月20日 下午2:12:04, think
	 */
	public Customer findCustomerByOriginCompanyId(String originCompanyId);

	/**
	 * <pre>
	 * 代工订单是否已引用
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月21日 下午1:41:16, think
	 */
	public boolean isOemOrderUse(QueryParam queryParam);

	/**
	 * <pre>
	 * 跨公司查询成品名称
	 * </pre>
	 * @param targetCompanyId
	 * @return
	 * @since 1.0, 2018年5月9日 上午10:12:42, zhengby
	 */
	public Map<String, Map<Long, List<WorkProduct>>> findAllProductForMap(List<String> targetCompanyId);

}
