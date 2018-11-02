/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.stock;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventory;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料盘点单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialInventoryService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:22, think
	 */
	public SearchResult<StockMaterialInventory> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:28, think
	 */
	public SearchResult<StockMaterialInventoryDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存材料盘点单
	 * </pre>
	 * @param stockMaterialInventory
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:37, think
	 */
	public Long save(StockMaterialInventory stockMaterialInventory);

	/**
	 * <pre>
	 * 修改材料盘点单
	 * </pre>
	 * @param stockMaterialInventory
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:48, think
	 */
	public Long update(StockMaterialInventory stockMaterialInventory);

	/**
	 * <pre>
	 * 获取材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:54, think
	 */
	public StockMaterialInventory get(Long id);

	/**
	 * <pre>
	 * 删除材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:11:59, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:12:05, think
	 */
	public Integer check(Long id);

	/**
	 * <pre>
	 * 反审核材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:12:09, think
	 */
	public Integer checkBack(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:12:15, think
	 */
	public StockMaterialInventory lockHasChildren(Long id);
}
