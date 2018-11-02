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
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 产品期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
public interface ProductBeginService
{

	/**
	 * <pre>
	 * 根据id获取产品期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:14, think
	 */
	public ProductBegin get(Long id);

	/**
	 * <pre>
	 * 根据id获取产品期初明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:23, think
	 */
	public List<ProductBeginDetail> getDetail(Long id);

	/**
	 * <pre>
	 * 添加产品期初
	 * </pre>
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:38, think
	 */
	public Long save(ProductBegin productBegin);

	/**
	 * <pre>
	 * 修改产品期初
	 * </pre>
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:44, think
	 */
	public Long update(ProductBegin productBegin);

	/**
	 * <pre>
	 * 删除产品期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:51, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核产品期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:14:57, think
	 */
	public Integer audit(Long id);
	
	/**
	 * <pre>
	 * 反审核产品期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:57:03, zhengby
	 */
	public Integer auditCancel(Long id);
	
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:15:05, think
	 */
	public SearchResult<ProductBegin> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:15:11, think
	 */
	public ProductBegin lockHasChildren(Long id);
}
