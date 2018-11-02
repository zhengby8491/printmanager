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
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 库存管理 - 生产退料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
public interface StockMaterialReturnService
{
	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:15:50, think
	 */
	public SearchResult<StockMaterialReturn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:00, think
	 */
	public SearchResult<StockMaterialReturnDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取生产退料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:09, think
	 */
	public StockMaterialReturn get(Long id);

	/**
	 * <pre>
	 * 获取生产退料明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:15, think
	 */
	public StockMaterialReturnDetail getDetail(Long id);

	/**
	 * <pre>
	 * 保存生产退料
	 * </pre>
	 * @param stockMaterialReturn
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:22, think
	 */
	public Long save(StockMaterialReturn stockMaterialReturn);

	/**
	 * <pre>
	 * 修改生产退料
	 * </pre>
	 * @param stockMaterialReturn
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:29, think
	 */
	public Long update(StockMaterialReturn stockMaterialReturn);

	/**
	 * <pre>
	 * 删除生产退料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:34, think
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 审核生产退料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:39, think
	 */
	public Boolean check(Long id);

	/**
	 * <pre>
	 * 反审核生产退料
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:45, think
	 */
	public List<StockMaterial> checkBack(Long id, BoolValue forceCheck);
	
	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:16:53, think
	 */
	public StockMaterialReturn lockHasChildren(Long id);
}
