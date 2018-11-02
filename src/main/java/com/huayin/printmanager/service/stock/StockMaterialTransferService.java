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
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransfer;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料库存调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialTransferService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:02, think
	 */
	public SearchResult<StockMaterialTransfer> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:09, think
	 */
	public SearchResult<StockMaterialTransferDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存材料库存调拨单
	 * </pre>
	 * @param stockMaterialTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:17, think
	 */
	public ServiceResult<StockMaterialTransfer> save(StockMaterialTransfer stockMaterialTransfer);

	/**
	 * <pre>
	 * 修改材料库存调拨单
	 * </pre>
	 * @param stockMaterialTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:23, think
	 */
	public ServiceResult<StockMaterialTransfer> update(StockMaterialTransfer stockMaterialTransfer);

	/**
	 * <pre>
	 * 获取材料库存调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:29, think
	 */
	public StockMaterialTransfer get(Long id);

	/**
	 * <pre>
	 * 删除材料库存调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:34, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料库存调拨单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月17日 下午2:27:36, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核材料库存调拨单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月17日 下午3:42:48, think
	 */
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:39:46, think
	 */
	public StockMaterialTransfer lockHasChildren(Long id);
}
