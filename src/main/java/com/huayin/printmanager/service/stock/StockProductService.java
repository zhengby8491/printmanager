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
import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductLog;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductService
{

	/**
	 * <pre>
	 * 多条件查询
	 * </pre>
	 * @param productName
	 * @param warehouseId
	 * @param productClassId
	 * @param isEmptyWare
	 * @param pageIndex
	 * @param pageSize
	 * @param code
	 * @param specifications
	 * @param customerMaterialCode
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:56:41, think
	 */
	public SearchResult<StockProduct> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核出入库操作
	 * isIn : yes表示审核入库单,no 表示审核出库单
	 * </pre>
	 * @param stockProduct
	 * @param isIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:57:16, think
	 */
	public Long stock(StockProduct stockProduct, InOutType type);

	/**
	 * <pre>
	 * 反审出库库存操作
	 * isIn : yes表示反审核入库单，no表示反审核出库单
	 * </pre>
	 * @param stockProduct
	 * @param isIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:57:21, think
	 */
	public Long backStock(StockProduct stockProduct, InOutType type);

	/**
	 * <pre>
	 * 调整成品库存
	 * </pre>
	 * @param stockProduct
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:57:28, think
	 */
	public Long adjust(StockProduct stockProduct);

	/**
	 * <pre>
	 * 成品库存单价变更
	 * </pre>
	 * @param stockProduct
	 * @return
	 * @since 1.0, 2018年4月27日 下午4:24:25, think
	 */
	public Long changePrice(StockProduct stockProduct);
	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param productClassId
	 * @param productName
	 * @param warehouseId
	 * @param pageIndex
	 * @param pageSize
	 * @param isEmptyWare
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:58:19, think
	 */
	public SearchResult<StockProduct> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询成品库存出入库记录明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:58:28, think
	 */
	public SearchResult<StockProductLog> findStockProductLogDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询成品库存出入库记录汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:58:42, think
	 */
	public SearchResult<StockProductLog> findStockProductLogSumList(QueryParam queryParam);

	/**
	 * <pre>
	 * 批量查询成品库存
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年4月14日 下午3:46:23, zhengby
	 */
	public Map<Long, Integer> findStockQty(List<Long> list);

	/**
	 * <pre>
	 * 查询某个成品库存数量
	 * </pre>
	 * @param productId
	 * @param warehouseId
	 * @return
	 * @since 1.0, 2018年5月21日 上午9:54:15, zhengby
	 */
	public Integer getStockQty(Long productId, Long warehouseId);

}
