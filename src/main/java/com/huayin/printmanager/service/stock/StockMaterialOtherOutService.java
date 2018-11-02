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

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料其他出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialOtherOutService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:09:40, think
	 */
	public SearchResult<StockMaterialOtherOut> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:09:48, think
	 */
	public SearchResult<StockMaterialOtherOutDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存材料其他出库
	 * </pre>
	 * @param stockMaterialOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:09:55, think
	 */
	public ServiceResult<StockMaterialOtherOut> save(StockMaterialOtherOut stockMaterialOtherOut);

	/**
	 * <pre>
	 * 修改材料其他出库
	 * </pre>
	 * @param stockMaterialOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:05, think
	 */
	public ServiceResult<StockMaterialOtherOut> update(StockMaterialOtherOut stockMaterialOtherOut);

	/**
	 * <pre>
	 * 获取材料其他出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:12, think
	 */
	public StockMaterialOtherOut get(Long id);

	/**
	 * <pre>
	 * 获取材料其他出库明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:18, think
	 */
	public StockMaterialOtherOutDetail getDetail(Long id);

	/**
	 * <pre>
	 * 删除材料其他出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:26, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料其他出库
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月17日 下午1:47:16, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核材料其他出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:36, think
	 */
	public Integer checkBack(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:10:43, think
	 */
	public StockMaterialOtherOut lockHasChildren(Long id);

}
