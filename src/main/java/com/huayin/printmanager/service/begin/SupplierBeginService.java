/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.begin.SupplierBegin;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 供应商期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
public interface SupplierBeginService
{
	/**
	 * <pre>
	 * 根据id获取供应商期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:30:52, think
	 */
	public SupplierBegin get(Long id);

	/**
	 * <pre>
	 * 根据id获取供应商期初明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:31:18, think
	 */
	public List<SupplierBeginDetail> getDetail(Long id);

	/**
	 * <pre>
	 * 添加供应商期初
	 * </pre>
	 * @param supplierBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:31:29, think
	 */
	public Long save(SupplierBegin supplierBegin);

	/**
	 * <pre>
	 * 更新供应商期初
	 * </pre>
	 * @param supplierBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:31:36, think
	 */
	public Long update(SupplierBegin supplierBegin);

	/**
	 * <pre>
	 * 删除供应商期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:31:44, think
	 */
	public boolean delete(Long id);

	/**
	 * <pre>
	 * 审核供应商期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:31:51, think
	 */
	public boolean audit(Long id);


	/**
	 * <pre>
	 * 反审核供应商期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:59:42, zhengby
	 */
	public boolean auditCancel(Long id);
	
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:32:06, think
	 */
	public SearchResult<SupplierBegin> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 */
	public SupplierBegin lockHasChildren(Long id);

	/**
	 * <pre>
	 * 根据供应商id查询供应商期初明细
	 * </pre>
	 * @param supplierId
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:32:13, think
	 */
	public SupplierBeginDetail findSupplierBeginDetail(Long supplierId);

	/**
	 * <pre>
	 * 查询所有供应商期初
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:33:01, think
	 */
	public List<SupplierBeginDetail> findAll(QueryParam queryParam);

}
