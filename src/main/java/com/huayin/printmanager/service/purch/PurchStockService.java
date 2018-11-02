/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.purch;

import java.util.Date;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 采购管理 - 采购入库
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午2:43:10, zhengby, 代码规范
 */
public interface PurchStockService
{

	/**
	 * <pre>
	 * 查找采购入库单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:43:31, zhengby
	 */
	public PurchStock get(Long id);

	/**
	 * <pre>
	 * 查找采购入库单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:43:54, zhengby
	 */
	public PurchStockDetail getDetail(Long id);
	
	/**
	 * <pre>
	 * 获取明细（包含Master）
	 * </pre>
	 * @param id
	 * @return
	 *  @since 1.0, 2018年2月23日 下午2:44:12, zhengby
	 */
	PurchStockDetail getDetailMaster(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:44:24, zhengby
	 */
	public PurchStock lockHasChildren(Long id);
	
	/**
	 * <pre>
	 * 保存采购入库单
	 * </pre>
	 * @param purchStock
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:44:36, zhengby
	 */
	public PurchStock save(PurchStock purchStock);

	/**
	 * <pre>
	 * 更新采购入库单
	 * </pre>
	 * @param purchStock
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:45:01, zhengby
	 */
	public PurchStock update(PurchStock purchStock);

	/**
	 * <pre>
	 * 删除采购入库单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:45:18, zhengby
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 复制采购入库单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:45:34, zhengby
	 */
	public PurchStock copy(Long id);

	/**
	 * <pre>
	 * 采购订单转采购入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:45:47, zhengby
	 */
	public PurchStock toStock(Long id);

	/**
	 * <pre>
	 * 查未入库采购订单明细
	 * </pre>
	 * @param isForceComplete
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:46:07, zhengby
	 */
	public SearchResult<PurchOrderDetail> transmitPurchStockList(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据明细表id集合查询明细表数据并关联主表
	 * </pre>
	 * @param listId
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:46:21, zhengby
	 */
	public PurchStock findListByDetailIds(Long[] listId);

	public SearchResult<PurchStock> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 审核采购入库单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:46:31, zhengby
	 */
	public Boolean check(Long id);

	/**
	 * <pre>
	 * 反审核采购入库单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:46:46, zhengby
	 */
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 查退货单此材料的退货数量
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:47:04, zhengby
	 */
	public Double findRefundQty(Long id);

	/**
	 * <pre>
	 * 根据查询条件查找采购入库单明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:49:45, zhengby
	 */
	public SearchResult<PurchStockDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 退货来源
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param supplierName
	 * @param billNo
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	public SearchResult<PurchStockDetail> findRefundSource(Date dateMin, Date dateMax, Long supplierId, String billNo,
			int pageSize, int pageNumber,BoolValue auditFlag);

	/**
	 * <pre>
	 * 根据入库单号查询入库单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:48:52, zhengby
	 */
	PurchStock get(String billNo);

}
