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

import org.springframework.web.bind.annotation.RequestParam;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.purch.vo.NotReconcilDetailVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 采购管理 - 采购对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午2:04:26, zhengby, 代码规范
 */
public interface PurchReconcilService
{

	/**
	 * <pre>
	 * 保存采购对账单
	 * </pre>
	 * @param purchReconcil
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:04:54, zhengby
	 */
	public PurchReconcil save(PurchReconcil purchReconcil);

	/**
	 * <pre>
	 * 更新采购对账单
	 * </pre>
	 * @param purchReconcil
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:05:17, zhengby
	 */
	public PurchReconcil update(PurchReconcil purchReconcil);

	/**
	 * <pre>
	 * 查找未对账明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:05:30, zhengby
	 */
	public List<NotReconcilDetailVo> transmitPurchReconcilList(QueryParam queryParam);

	/**
	 * <pre>
	 * 未清转对账
	 * </pre>
	 * @param checkbox
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:05:40, zhengby
	 */
	public PurchReconcil findByCheckbox(String[] checkbox);

	/**
	 * <pre>
	 * 根据id查询采购对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:05:55, zhengby
	 */
	public PurchReconcil get(Long id);

	/**
	 * <pre>
	 * 根据id查询采购对账单明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:06:23, zhengby
	 */
	public PurchReconcilDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:06:50, zhengby
	 */
	public PurchReconcil lockHasChildren(Long id);
	
	/**
	 * <pre>
	 * 删除采购对账单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:07:00, zhengby
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 根据条件查询采购对账单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:07:20, zhengby
	 */
	public SearchResult<PurchReconcil> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 强制完工采购对账单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @param stockIds
	 * @param refundIds
	 * @param completeFlag
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:07:52, zhengby
	 */
	public boolean forceComplete(TableType tableType, @RequestParam("ids[]") Long[] ids,
			@RequestParam("stockIds[]") Long[] stockIds, @RequestParam("refundIds[]") Long[] refundIds,
			BoolValue completeFlag);

	/**
	 * <pre>
	 * 明细表查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:10:20, zhengby
	 */
	public SearchResult<PurchReconcilDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询库存量大于对账量的记录
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:26:18, zhengby
	 */
	List<PurchStockDetail> findStockQtyReconcil(List<Long> ids);

	/**
	 * <pre>
	 * 查询退货量大于已对账量的记录
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:26:42, zhengby
	 */
	List<PurchRefundDetail> findRefundQtyReconcil(List<Long> ids);
}
