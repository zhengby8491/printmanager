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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.service.purch.vo.PurchScheduleVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;

/**
 * <pre>
 * 采购管理 - 采购订单汇总
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午3:26:47, zhengby, 代码规范
 */
public interface PurchSumService
{
	/**
	 * <pre>
	 * 按供应商名称查询采购汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:27:36, zhengby
	 */
	public SearchResult<SumVo> findBySupplierName(QueryParam queryParam);

	/**
	 * <pre>
	 * 按供应商分类查询采购汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:27:47, zhengby
	 */
	public SearchResult<SumVo> findBySupplierClass(QueryParam queryParam);

	/**
	 * <pre>
	 * 按材料名称查询采购汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:28:57, zhengby
	 */
	public SearchResult<SumVo> findByMaterialName(QueryParam queryParam);

	/**
	 * <pre>
	 * 按材料分类查询采购汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:28:16, zhengby
	 */
	public SearchResult<SumVo> findByMaterialClass(QueryParam queryParam);

	/**
	 * <pre>
	 * 按采购员查询采购汇总
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:28:27, zhengby
	 */
	public SearchResult<SumVo> findByEmployeeName(QueryParam queryParam);

	/**
	 * <pre>
	 * 采购进度追踪表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:28:35, zhengby
	 */
	public SearchResult<PurchScheduleVo> findPurchSchedule(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取采购订单年份列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:28:44, zhengby
	 */
	public List<PurchOrder> getYearsFromPurchOrder();

}
