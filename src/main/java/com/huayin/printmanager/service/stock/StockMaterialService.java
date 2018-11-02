/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.stock;

import java.math.BigDecimal;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialLog;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.InOutType;
import com.huayin.printmanager.persist.enumerate.MaterialType;
import com.huayin.printmanager.service.stock.vo.StockMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料库存
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialService
{

	/**
	 * <pre>
	 * 多条件查询
	 * </pre>
	 * @param materialName
	 * @param materialType
	 * @param warehouseId
	 * @param materialClassId
	 * @param isEmptyWare
	 * @param pageIndex
	 * @param pageSize
	 * @param code
	 * @param specifications
	 * @param weight
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:27:53, think
	 */
	public SearchResult<StockMaterial> findByCondition(String materialName, MaterialType materialType, Long warehouseId, Long materialClassId, BoolValue isEmptyWare, Integer pageIndex, Integer pageSize, String code, String specifications, Integer weight);

	/**
	 * <pre>
	 * 审核出入库操作
	 * </pre>
	 * @param stockMaterial
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:04, think
	 */
	public Long stock(StockMaterial stockMaterial, InOutType type);

	/**
	 * <pre>
	 * 出库反审库存操作
	 * </pre>
	 * @param stockMaterial
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:11, think
	 */
	public Long backStock(StockMaterial stockMaterial, InOutType type);

	/**
	 * <pre>
	 * 直接调整库存操作
	 * </pre>
	 * @param stockMaterial
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:22, think
	 */
	public Long adjust(StockMaterial stockMaterial);

	/**
	 * <pre>
	 * 库存计价单价变更
	 * </pre>
	 * @param stockMaterial
	 * @return
	 * @since 1.0, 2018年4月26日 下午5:36:56, think
	 */
	public Long changeValuationPrice(StockMaterial stockMaterial);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param materialClassId
	 * @param materialName
	 * @param warehouseId
	 * @param pageIndex
	 * @param pageSize
	 * @param isEmptyWare
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:29, think
	 */
	public SearchResult<StockMaterial> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查材料库存数量
	 * </pre>
	 * @param materialId
	 * @param style
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:37, think
	 */
	public BigDecimal getQty(Long materialId, String style);

	/**
	 * <pre>
	 * 最低库存预警
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:44, think
	 */
	public SearchResult<StockMaterial> stockWarn(QueryParam queryParam);

	/**
	 * <pre>
	 * 查材料库存出入库记录明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:28:51, think
	 */
	public SearchResult<StockMaterialLog> findStockMaterialLogDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 查材料库存出入库记录汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:29:04, think
	 */
	public SearchResult<StockMaterialLog> findStockMaterialLogSumList(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据材料ID 查此材料库存列表
	 * </pre>
	 * @param materialId
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:29:09, think
	 */
	public List<StockMaterialVo> findByMaterialId(Long materialId);

}
