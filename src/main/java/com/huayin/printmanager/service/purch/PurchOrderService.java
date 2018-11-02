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

import java.math.BigDecimal;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.purch.vo.WorkToPurchVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 采购管理 - 采购订单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日上午11:54:14, zhengby, 代码规范
 */
public interface PurchOrderService
{
	/**
	 * <pre>
	 * 根据单据id，获取采购订单主表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:55:35, zhengby
	 */
	public PurchOrder get(Long id);

	/**
	 * <pre>
	 * 根据单据编号，获取采购订单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月28日 下午7:28:40, zhengby
	 */
	public PurchOrder get(String billNo);

	/**
	 * <pre>
	 * 根据单据明细id，获取采购订单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:56:03, zhengby
	 */
	public PurchOrderDetail getDetail(Long id);
	
	// public PurchOrderDetail getDetailList(Long masterId); 根据单据id，获取明细列表

	/**
	 * <pre>
	 * 根据单据id，获取采购订单和明细
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年4月14日 上午10:31:56, think
	 */
	public PurchOrder getOrderDetail(String billNo);

	/**
	 * <pre>
	 * 根据工单ID获取采购明细
	 * </pre>
	 * @param workId
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:59:04, zhengby
	 */
	public List<PurchOrderDetail> getDetailByWork(Long workId);

	/**
	 * <pre>
	 * 物料需求计划明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:54:47, zhengby
	 */
	public List<WorkMaterial> getWorkToPurchByMaterial(QueryParam queryParam);

	/**
	 * <pre>
	 * 查在途采购量
	 * </pre>
	 * @param materialId
	 * @param style
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:58:56, zhengby
	 */
	public BigDecimal getPurchQty(Long materialId, String style);

	/**
	 * <pre>
	 * 物料需求计划列表查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:54:30, zhengby
	 */
	public SearchResult<WorkToPurchVo> transmitPurchOrderList(QueryParam queryParam);

	/**
	 * <pre>
	 * 物料需求计划转采购
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:55:14, zhengby
	 */
	public List<PurchOrderDetail> toPurch(Long[] ids);

	/**
	 * <pre>
	 * 库存预警转采购
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:55:24, zhengby
	 */
	public List<PurchOrderDetail> warnToPurch(Long[] ids);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:56:21, zhengby
	 */
	public PurchOrder lockHasChildren(Long id);

	/**
	 * <pre>
	 * 保存采购订单
	 * </pre>
	 * @param purchOrder
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:56:31, zhengby
	 */
	public PurchOrder save(PurchOrder purchOrder);

	/**
	 * <pre>
	 * 更新采购订单
	 * </pre>
	 * @param purchOrder
	 * @since 1.0, 2018年2月23日 上午11:56:46, zhengby
	 */
	public void update(PurchOrder purchOrder);

	/**
	 * <pre>
	 * 删除采购订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:56:59, zhengby
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 复制采购订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:57:11, zhengby
	 */
	public PurchOrder copy(Long id);

	/**
	 * <pre>
	 * 审核所有采购订单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:57:23, zhengby
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 强制完工采购订单
	 * </pre>
	 * @param ids
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:57:34, zhengby
	 */
	public Boolean forceComplete(Long[] ids, BoolValue flag);

	/**
	 * <pre>
	 * 根据条件 查询采购订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:57:54, zhengby
	 */
	public SearchResult<PurchOrder> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据多条件查询采购对账单详情
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:58:14, zhengby
	 */
	public SearchResult<PurchOrderDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 单价变更
	 * </pre>
	 * @param purchOrderDetail
	 * @since 1.0, 2018年4月12日 上午9:29:05, think
	 */
	public void changePrice(PurchOrderDetail purchOrderDetail) throws Exception;

}
