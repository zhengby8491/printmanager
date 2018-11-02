/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午17:53:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale;

import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sale.SaleDeliver;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 销售管理 - 销售送货
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年7月1日
 * @version 	   2.0, 2018年2月22日下午5:52:31, zhengby, 代码规范
 */
public interface SaleDeliverService
{
	/**
	 * <pre>
	 * 根据id获取销售送货信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:54:11, zhengby
	 */
	public SaleDeliver get(Long id);

	/**
	 * <pre>
	 * 根据id获取销售送货明细信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:54:20, zhengby
	 */
	public SaleDeliverDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定销售送货实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:54:28, zhengby
	 */
	public SaleDeliver lock(Long id);

	/**
	 * <pre>
	 * 根据主表id获得销售送货明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:54:39, zhengby
	 */
	public List<SaleDeliverDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存销售送货
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:54:51, zhengby
	 */
	public ServiceResult<SaleDeliver> save(SaleDeliver order);

	/**
	 * <pre>
	 * 修改销售送货
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:55:01, zhengby
	 */
	public ServiceResult<SaleDeliver> update(SaleDeliver order);

	/**
	 * <pre>
	 * 审核
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年1月18日 上午10:00:20, think
	 */
	public List<StockProduct> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:55:18, zhengby
	 */
	public Boolean checkBack(Long id);

	/**
	 * <pre>
	 * 删除销售送货
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月22日 下午5:55:35, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @param dateMin 创建日期最小条件
	 * @param dateMax 创建日期最大条件
	 * @param userName 用户名 模糊查询
	 * @parama sortMap 排序条件
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:56:12, zhengby
	 */
	public SearchResult<SaleDeliver> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @param dateMin 创建日期最小条件
	 * @param dateMax 创建日期最大条件
	 * @param userName 用户名 模糊查询
	 * @parama sortMap 排序条件
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:56:36, zhengby
	 */
	public SearchResult<SaleDeliverDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 选择退货源列表
	 * </pre>
	 * @param queryParam
	 * @param dateMin 创建日期最小条件
	 * @param dateMax 创建日期最大条件
	 * @param userName 用户名 模糊查询
	 * @parama sortMap 排序条件
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:56:52, zhengby
	 */
	public SearchResult<SaleDeliverDetail> findDetailByConditions(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询返写对账总数量根据原单
	 * </pre>
	 * @param saleOrderDetail
	 * @since 1.0, 2018年2月22日 下午5:57:16, zhengby
	 */
	public void coutReturnReconcilQty(SaleOrderDetail saleOrderDetail);

	/**
	 * 
	 * <pre>
	 * 销售明细追踪检查送货单是否全部审核
	 * </pre>
	 * @param saleOrderBillNo 销售单号
   * @param productId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:57:30, zhengby
	 */
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId);

	// ==================== 新规范 - 代码重构 ====================
	
	/**
	 * <pre>
	 * 查询所有销售送货信息
	 * </pre>
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年1月22日 下午3:57:33, think
	 */
	SearchResult<SaleDeliverDetail> findAll(BoolValue isCheck);

	/**
	 * <pre>
	 * 查询所有已审核销售送货信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 下午3:56:37, think
	 */
	public SearchResult<SaleDeliverDetail> findAll();

}
