/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午18:24:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 销售管理 - 销售对账
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年7月1日
 * @version 	   2.0, 2018年2月22日下午6:15:45, zhengby, 代码规范
 */
public interface SaleReconcilService
{
	/**
	 * <pre>
	 * 根据id获得销售对账信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:16:35, zhengby
	 */
	public SaleReconcil get(Long id);

	/**
	 * <pre>
	 * 根据id获得销售对账明细信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:16:47, zhengby
	 */
	public SaleReconcilDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定销售对账实体，直到事务结束
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:16:59, zhengby
	 */
	public SaleReconcil lock(Long id);

	/**
	 * <pre>
	 * 根据主表id获得销售对账的列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:17:10, zhengby
	 */
	public List<SaleReconcilDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存销售对账
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:21:10, zhengby
	 */
	public SaleReconcil save(SaleReconcil order);

	/**
	 * <pre>
	 * 修改销售对账
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:21:23, zhengby
	 */
	public SaleReconcil update(SaleReconcil order);

	/**
	 * <pre>
	 * 删除销售对账
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月22日 下午6:21:32, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 主表多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:21:42, zhengby
	 */
	public SearchResult<SaleReconcil> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:21:52, zhengby
	 */
	public SearchResult<SaleReconcilDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 强制完工对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids 主表ID数组
	 * @param arriveIds 到货明细ID数组
	 * @param returnIds 退货明细ID数组
	 * @param completeFlag 完工标记
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:22:02, zhengby
	 */
	public boolean forceComplete(TableType tableType, @RequestParam("ids[]") Long[] ids, @RequestParam("deliverIds[]") Long[] deliverIds, @RequestParam("returnIds[]") Long[] returnIds, BoolValue completeFlag);

	/**
	 * <pre>
	 * 查询对账单的返写总金额根据原单
	 * </pre>
	 * @param saleDeliverDetail
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:22:43, zhengby
	 */
	public BigDecimal countReturnReceiveMoney(SaleDeliverDetail saleDeliverDetail);

	/**
	 * <pre>
	 * 销售明细追踪检查对账单是否全部审核
	 * </pre>
	 * @param billNo
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:22:56, zhengby
	 */
	public Boolean hasCheckAll(String billNo, Long productId);

	// ==================== 新规范 - 代码重构 ====================

	/**
	 * <pre>
	 * 查询所有销售对账信息
	 * </pre>
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年1月22日 下午4:37:25, think
	 */
	public SearchResult<SaleReconcilDetail> findAll(BoolValue isCheck);

	/**
	 * <pre>
	 * 查询所有已审核销售对账信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 下午4:38:12, think
	 */
	public SearchResult<SaleReconcilDetail> findAll();
}
