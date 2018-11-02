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
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 生产领料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialTakeService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:28:53, think
	 */
	public SearchResult<StockMaterialTake> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:29:01, think
	 */
	public SearchResult<StockMaterialTakeDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 工单转生产领料未清列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:29:08, think
	 */
	public SearchResult<WorkMaterial> findForTransmitTake(QueryParam queryParam);

	/**
	 * <pre>
	 * 工单转生产领料
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:29:16, think
	 */
	public List<StockMaterialTakeDetail> toTake(Long[] ids);
	
	/**
	 * <pre>
	 * 获取生产领料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:24, think
	 */
	public StockMaterialTake get(Long id);

	/**
	 * <pre>
	 * 获取生产领料明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:29, think
	 */
	public StockMaterialTakeDetail getDetail(Long id);

	/**
	 * <pre>
	 * 保存生产领料
	 * </pre>
	 * @param stockMaterialTake
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:35, think
	 */
	public ServiceResult<StockMaterialTake> save(StockMaterialTake stockMaterialTake);

	/**
	 * <pre>
	 * 修改生产领料
	 * </pre>
	 * @param stockMaterialTake
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:41, think
	 */
	public ServiceResult<StockMaterialTake> update(StockMaterialTake stockMaterialTake);

	/**
	 * <pre>
	 * 删除生产领料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:46, think
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 审核生产领料
	 * </pre>
	 * @param id
	 * @param forceCheck 强制审核
	 * @return
	 * @since 1.0, 2018年1月17日 上午9:39:21, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核生产领料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:53, think
	 */
	public Boolean checkBack(Long id);
	
	/**
	 * <pre>
	 * 强制完工
	 * </pre>
	 * @param ids
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:30:59, think
	 */
	public Boolean forceComplete(Long[] ids,BoolValue flag);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:31:06, think
	 */
	public StockMaterialTake lockHasChildren(Long id);
}
