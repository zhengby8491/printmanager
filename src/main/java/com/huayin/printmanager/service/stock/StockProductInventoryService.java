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
import com.huayin.printmanager.persist.entity.stock.StockProductInventory;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品盘点
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductInventoryService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:27, think
	 */
	public SearchResult<StockProductInventory> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:33, think
	 */
	public SearchResult<StockProductInventoryDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存成品盘点
	 * </pre>
	 * @param stockProductInventory
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:38, think
	 */
	public Long save(StockProductInventory stockProductInventory);

	/**
	 * <pre>
	 * 修改成品盘点
	 * </pre>
	 * @param stockProductInventory
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:46, think
	 */
	public Long update(StockProductInventory stockProductInventory);

	/**
	 * <pre>
	 * 获取成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:52, think
	 */
	public StockProductInventory get(Long id);

	/**
	 * <pre>
	 * 删除成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:49:57, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:50:02, think
	 */
	public Integer check(Long id);

	/**
	 * <pre>
	 * 反审核成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:50:06, think
	 */
	public Integer checkBack(Long id);

	/**
	 * <pre>
	 * 未审核出入库
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:50:13, think
	 */
	public List<NotCheckStockVo> findNotCheckStockProduct();

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:50:26, think
	 */
	public StockProductInventory lockHasChildren(Long id);
}
