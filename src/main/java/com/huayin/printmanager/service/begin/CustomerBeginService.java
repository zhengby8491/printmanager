/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.begin.CustomerBegin;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 客户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
public interface CustomerBeginService
{
	/**
	 * <pre>
	 * 根据id获取客户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:08, think
	 */
	public CustomerBegin get(Long id);

	/**
	 * <pre>
	 * 根据id获取客户期初明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:16, think
	 */
	public List<CustomerBeginDetail> getDetail(Long id);

	/**
	 * <pre>
	 * 添加客户期初
	 * </pre>
	 * @param customerBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:25, think
	 */
	public Long save(CustomerBegin customerBegin);

	/**
	 * <pre>
	 * 修改客户期初
	 * </pre>
	 * @param customerBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:35, think
	 */
	public Long update(CustomerBegin customerBegin);

	/**
	 * <pre>
	 * 删除客户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:40, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核客户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:47, think
	 */
	public Boolean audit(Long id);

	/**
	 * <pre>
	 * 反审核客户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:47:25, zhengby
	 */
	public Boolean auditCancel(Long id);
	
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:46:55, think
	 */
	public SearchResult<CustomerBegin> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:47:03, think
	 */
	public CustomerBegin lockHasChildren(Long id);

	/**
	 * <pre>
	 * 根据客户id查询客户期初明细
	 * </pre>
	 * @param customerId
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:47:09, think
	 */
	public CustomerBeginDetail findCustomerBeginDetail(Long customerId);

	/**
	 * <pre>
	 * 查出全部客户的期初
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月26日 上午10:26:31, zhengby
	 */
	public List<CustomerBeginDetail> findAll(QueryParam queryParam);

}
