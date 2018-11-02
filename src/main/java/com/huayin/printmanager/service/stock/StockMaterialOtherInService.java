/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.stock;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料其他入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialOtherInService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:28:37, think
	 */
	public SearchResult<StockMaterialOtherIn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:28:44, think
	 */
	public SearchResult<StockMaterialOtherInDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存材料其他入库
	 * </pre>
	 * @param stockMaterialOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:28:50, think
	 */
	public Long save(StockMaterialOtherIn stockMaterialOtherIn);

	/**
	 * <pre>
	 * 修改材料其他入库
	 * </pre>
	 * @param stockMaterialOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:28:58, think
	 */
	public Long update(StockMaterialOtherIn stockMaterialOtherIn);

	/**
	 * <pre>
	 * 获取材料其他入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:29:03, think
	 */
	public StockMaterialOtherIn get(Long id);

	/**
	 * <pre>
	 * 删除材料其他入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:29:08, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料其他入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:29:13, think
	 */
	public Integer check(Long id);

	/**
	 * <pre>
	 * 反审核材料其他入库
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:29:19, think
	 */
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:29:25, think
	 */
	public StockMaterialOtherIn lockHasChildren(Long id);
}
