/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.stock;

import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品其它出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductOtherOutService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:32, think
	 */
	public SearchResult<StockProductOtherOut> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:34, think
	 */
	public SearchResult<StockProductOtherOutDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存成品其它出库
	 * </pre>
	 * @param stockProductOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:47, think
	 */
	public ServiceResult<StockProductOtherOut> save(StockProductOtherOut stockProductOtherOut);

	/**
	 * <pre>
	 * 修改成品其它出库
	 * </pre>
	 * @param stockProductOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:53, think
	 */
	public ServiceResult<StockProductOtherOut> update(StockProductOtherOut stockProductOtherOut);

	/**
	 * <pre>
	 * 获取成品其它出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:58, think
	 */
	public StockProductOtherOut get(Long id);

	/**
	 * <pre>
	 * 删除成品其它出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:21:04, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核成品其它出库
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月18日 上午9:04:57, think
	 */
	public List<StockProduct> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核成品其它出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:21:10, think
	 */
	public Integer checkBack(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:21:16, think
	 */
	public StockProductOtherOut lockHasChildren(Long id);
}
