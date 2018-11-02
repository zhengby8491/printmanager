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

import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 采购管理 - 采购退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:57:21, zhengby, 代码规范
 */
public interface PurchReturnService
{
	/**
	 * <pre>
	 * 根据id查询采购退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:57:37, zhengby
	 */
	public PurchRefund get(Long id);

	/**
	 * <pre>
	 * 根据id查询采购退货单明细表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:58:13, zhengby
	 */
	public PurchRefundDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 *  @since 1.0, 2018年2月23日 下午4:58:30, zhengby
	 */
	public PurchRefund lockHasChildren(Long id);
	
	/**
	 * <pre>
	 * 保存采购退货单
	 * </pre>
	 * @param purchRefund
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:58:28, zhengby
	 */
	public ServiceResult<PurchRefund> save(PurchRefund purchRefund);

	/**
	 * <pre>
	 * 更新采购退货单
	 * </pre>
	 * @param purchRefund
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:58:48, zhengby
	 */
	public ServiceResult<PurchRefund> update(PurchRefund purchRefund);

	/**
	 * <pre>
	 * 审核采购退货单
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月18日 上午11:17:20, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核采购退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:59:01, zhengby
	 */
	public Boolean checkBack(Long id);

	/**
	 * <pre>
	 * 删除采购退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:59:09, zhengby
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 多条件查询采购退货单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:59:26, zhengby
	 */
	public SearchResult<PurchRefund> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细表查询
	 * </pre>
	 * @return
	 */
	public SearchResult<PurchRefundDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据采购退货单号查找退货单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:36:36, zhengby
	 */
	PurchRefund get(String billNo);
}
