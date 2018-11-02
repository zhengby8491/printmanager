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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 成品入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockProductInService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:09, think
	 */
	public SearchResult<StockProductIn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:15, think
	 */
	public SearchResult<StockProductInDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存成品入库
	 * </pre>
	 * @param stockProductIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:25, think
	 */
	public Long save(StockProductIn stockProductIn);

	/**
	 * <pre>
	 * 修改成品入库
	 * </pre>
	 * @param StockProductIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:29, think
	 */
	public Long update(StockProductIn StockProductIn);

	/**
	 * <pre>
	 * 获取成品入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:37, think
	 */
	public StockProductIn get(Long id);

	/**
	 * <pre>
	 * 获取成品入库明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:43, think
	 */
	public StockProductInDetail getDetail(Long id);

	/**
	 * <pre>
	 * 删除成品入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:51, think
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 审核成品入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:07:56, think
	 */
	public Boolean check(Long id);

	/**
	 * <pre>
	 * 反审核成品入库
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:08:02, think
	 */
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 工单转入库
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:08:12, think
	 */
	public List<StockProductInDetail> transmit(Long[] ids);

	/**
	 * <pre>
	 * 未清
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:08:25, think
	 */
	public SearchResult<WorkProduct> findForTransmitProductIn(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:08:32, think
	 */
	public StockProductIn lockHasChildren(Long id);
	
	/**
	 * <pre>
	 * 销售明细追踪检查入库单是否全部审核
	 * </pre>
	 * @param saleOrderBillNo
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:08:41, think
	 */
	public Boolean hasCheckAll(String saleOrderBillNo,Long productId);
}
