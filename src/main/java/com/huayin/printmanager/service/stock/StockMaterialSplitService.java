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
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplitDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 材料分切
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialSplitService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:20, think
	 */
	public SearchResult<StockMaterialSplit> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:27, think
	 */
	public SearchResult<StockMaterialSplitDetail> findDetailByCondition(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 保存材料分切
	 * </pre>
	 * @param stockMaterialSplit
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:34, think
	 */
	public ServiceResult<StockMaterialSplit> save(StockMaterialSplit stockMaterialSplit);
	
	/**
	 * <pre>
	 * 修改材料分切
	 * </pre>
	 * @param stockMaterialSplit
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:43, think
	 */
	public ServiceResult<StockMaterialSplit> update(StockMaterialSplit stockMaterialSplit);
	
	/**
	 * <pre>
	 * 获取材料分切
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:49, think
	 */
	public StockMaterialSplit get(Long id);
	
	/**
	 * <pre>
	 * 删除材料分切
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:47:55, think
	 */
	public Integer delete(Long id);
	
	/**
	 * <pre>
	 * 审核材料分切
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:48:00, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);
	
	/**
	 * <pre>
	 * 反审核材料分切
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:48:05, think
	 */
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:48:13, think
	 */
	public StockMaterialSplit lockHasChildren(Long id);
	
}
