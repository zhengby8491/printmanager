/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.outsource;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 发外管理 - 发外对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:43:45, zhengby, 代码规范
 */
public interface OutSourceReconcilService
{

	/**
	 * <pre>
	 * 获取发外对账单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:44:10, zhengby
	 */
	public OutSourceReconcil get(Long id);

	/**
	 * <pre>
	 * 获取发外对账单明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:44:32, zhengby
	 */
	public OutSourceReconcilDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:44:44, zhengby
	 */
	public OutSourceReconcil lock(Long id);

	/**
	 * <pre>
	 * 获取发外对账明细表列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:45:05, zhengby
	 */
	public List<OutSourceReconcilDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存发外对账单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:45:27, zhengby
	 */
	public OutSourceReconcil save(OutSourceReconcil order);

	/**
	 * <pre>
	 * 更新发外对账单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:45:37, zhengby
	 */
	public OutSourceReconcil update(OutSourceReconcil order);

	/**
	 * <pre>
	 * 删除发外对账单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 下午4:45:45, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 主表多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:45:55, zhengby
	 */
	public SearchResult<OutSourceReconcil> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 *  明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:46:13, zhengby
	 */
	public SearchResult<OutSourceReconcilDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 强制完工发外对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids 主表ID数组
	 * @param arriveIds 到货明细ID数组
	 * @param returnIds 退货明细ID数组
	 * @param completeFlag 完工标记
	 * @return
	 */
	public boolean forceComplete(TableType tableType, @RequestParam("ids[]") Long[] ids, @RequestParam("arriveIds[]") Long[] arriveIds, @RequestParam("returnIds[]") Long[] returnIds, BoolValue completeFlag);

	/**
	 * <pre>
	 * 查询返写付款金额 说明:发外到货明细ID即对账源明细ID
	 * </pre>
	 * @param outSourceArriveDetail
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:46:48, zhengby
	 */
	public BigDecimal countReturnPaymentMoney(OutSourceArriveDetail outSourceArriveDetail);
}
