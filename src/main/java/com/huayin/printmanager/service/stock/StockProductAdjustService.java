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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjust;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品调整
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductAdjustService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:51:46, think
	 */
	public SearchResult<StockProductAdjust> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:51:51, think
	 */
	public SearchResult<StockProductAdjustDetail> findDetailByCondition(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 保存成品调整
	 * </pre>
	 * @param stockProductAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:02, think
	 */
	public Long save(StockProductAdjust stockProductAdjust);
	
	/**
	 * <pre>
	 * 修改成品调整
	 * </pre>
	 * @param stockProductAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:08, think
	 */
	public Long update(StockProductAdjust stockProductAdjust);
	
	/**
	 * <pre>
	 * 获取成品调整
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:13, think
	 */
	public StockProductAdjust get(Long id);
	
	/**
	 * <pre>
	 * 删除成品调整
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:18, think
	 */
	public Integer delete(Long id);
	
	/**
	 * <pre>
	 * 审核成品调整
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:23, think
	 */
	public Integer check(Long id);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:52:28, think
	 */
	public StockProductAdjust lockHasChildren(Long id);
}
