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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品其它入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductOtherInService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:34, think
	 */
	public SearchResult<StockProductOtherIn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:40, think
	 */
	public SearchResult<StockProductOtherInDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存成品其它入库
	 * </pre>
	 * @param stockProductOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:50, think
	 */
	public Long save(StockProductOtherIn stockProductOtherIn);

	/**
	 * <pre>
	 * 修改成品其它入库
	 * </pre>
	 * @param stockProductOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:55, think
	 */
	public Long update(StockProductOtherIn stockProductOtherIn);

	/**
	 * <pre>
	 * 获取成品其它入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:59, think
	 */
	public StockProductOtherIn get(Long id);

	/**
	 * <pre>
	 * 删除成品其它入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:01:06, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核成品其它入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:01:10, think
	 */
	public Integer check(Long id);

	/**
	 * <pre>
	 * 反审核成品其它入库
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:01:21, think
	 */
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:01:28, think
	 */
	public StockProductOtherIn lockHasChildren(Long id);
}
