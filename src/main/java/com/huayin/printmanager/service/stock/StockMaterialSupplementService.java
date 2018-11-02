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
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 生产补料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialSupplementService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:35, think
	 */
	public SearchResult<StockMaterialSupplement> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:41, think
	 */
	public SearchResult<StockMaterialSupplementDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 保存生产补料
	 * </pre>
	 * @param stockMaterialSupplement
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:52, think
	 */
	public ServiceResult<StockMaterialSupplement> save(StockMaterialSupplement stockMaterialSupplement);
	
	/**
	 * <pre>
	 * 修改生产补料
	 * </pre>
	 * @param stockMaterialSupplement
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:57, think
	 */
	public ServiceResult<StockMaterialSupplement> update(StockMaterialSupplement stockMaterialSupplement);
	
	/**
	 * <pre>
	 * 获取生产补料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:13:02, think
	 */
	public StockMaterialSupplement get(Long id);
	
	/**
	 * <pre>
	 * 获取生产补料明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:13:07, think
	 */
	public StockMaterialSupplementDetail getDetail(Long id);
	
	/**
	 * <pre>
	 * 删除生产补料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:13:14, think
	 */
	public Integer delete(Long id);
	
	/**
	 * <pre>
	 * 审核生产补料
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月17日 上午11:39:08, think
	 */
	public List<StockMaterial> check(Long id, BoolValue forceCheck);
	
	/**
	 * <pre>
	 * 反审核生产补料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:13:22, think
	 */
	public Integer checkBack(Long id);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:13:27, think
	 */
	public StockMaterialSupplement lockHasChildren(Long id);
}
