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
import com.huayin.printmanager.persist.entity.stock.StockProductTransfer;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductTransferService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:57, think
	 */
	public SearchResult<StockProductTransfer> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:59, think
	 */
	public SearchResult<StockProductTransferDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存成品调拨单
	 * </pre>
	 * @param stockProductTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:33:12, think
	 */
	public ServiceResult<StockProductTransfer> save(StockProductTransfer stockProductTransfer);

	/**
	 * <pre>
	 * 修改成品调拨单
	 * </pre>
	 * @param stockProductTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:33:17, think
	 */
	public ServiceResult<StockProductTransfer> update(StockProductTransfer stockProductTransfer);

	/**
	 * <pre>
	 * 获取成品调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:33:21, think
	 */
	public StockProductTransfer get(Long id);

	/**
	 * <pre>
	 * 删除成品调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:33:26, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核成品调拨单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月18日 上午9:37:27, think
	 */
	public List<StockProduct> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核成品调拨单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月18日 上午9:47:15, think
	 */
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:33:37, think
	 */
	public StockProductTransfer lockHasChildren(Long id);
}
