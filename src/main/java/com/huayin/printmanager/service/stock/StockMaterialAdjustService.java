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
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjust;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料库存调整单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialAdjustService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:03, think
	 */
	public SearchResult<StockMaterialAdjust> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:12, think
	 */
	public SearchResult<StockMaterialAdjustDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存材料库存调整单
	 * </pre>
	 * @param stockMaterialAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:20, think
	 */
	public Long save(StockMaterialAdjust stockMaterialAdjust);

	/**
	 * <pre>
	 * 修改材料库存调整单
	 * </pre>
	 * @param stockMaterialAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:30, think
	 */
	public Long update(StockMaterialAdjust stockMaterialAdjust);

	/**
	 * <pre>
	 * 获取材料库存调整单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:36, think
	 */
	public StockMaterialAdjust get(Long id);

	/**
	 * <pre>
	 * 删除材料库存调整单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:44, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料库存调整单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:51, think
	 */
	public Integer check(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:18:58, think
	 */
	public StockMaterialAdjust lockHasChildren(Long id);

}
