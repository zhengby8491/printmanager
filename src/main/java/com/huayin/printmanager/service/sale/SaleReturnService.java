/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午18:51:13
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sale.SaleReturn;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 销售管理 - 销售退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日
 * @version 	   2.0, 2018年2月22日下午6:51:39, zhengby, 代码规范
 */
public interface SaleReturnService
{
	/**
	 * 
	 * <pre>
	 * 根据id获取销售退货信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:52:21, zhengby
	 */
	public SaleReturn get(Long id);

	/**
	 * <pre>
	 * 根据id获取销售退货明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:52:42, zhengby
	 */
	public SaleReturnDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定销售退货实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:52:51, zhengby
	 */
	public SaleReturn lock(Long id);

	/**
	 * <pre>
	 * 根据主表id获取销售退货明细列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:53:00, zhengby
	 */
	public List<SaleReturnDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存销售退货
	 * </pre>
	 * @param order
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午6:53:10, zhengby
	 */
	public void save(SaleReturn order);

	/**
	 * <pre>
	 * 修改销售退货
	 * </pre>
	 * @param order
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午6:53:19, zhengby
	 */
	public void update(SaleReturn order);

	/**
	 * <pre>
	 * 审核
	 * </pre>
	 * @param id
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午6:53:29, zhengby
	 */
	public void check(Long id);

	/**
	 * <pre>
	 * 反审核
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:53:38, zhengby
	 */
	public List<StockProduct> checkBack(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 删除销售退货
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月22日 下午6:53:45, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 主表多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:53:53, zhengby
	 */
	public SearchResult<SaleReturn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:54:01, zhengby
	 */
	public SearchResult<SaleReturnDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 退货对账数量
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午6:54:26, zhengby
	 */
	public Integer coutReturnReconcilQty(Long id);

	// ==================== 新规范 - 代码重构 ====================
	
	/**
	 * <pre>
	 * 查询所有销售送货明细
	 * </pre>
	 * @param isCheck
	 * @param type
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:04:29, think
	 */
	public SearchResult<SaleReturnDetail> findAll(BoolValue isCheck, ReturnGoodsType type);

	/**
	 * <pre>
	 * 查询所有已审核的销售送货明细
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:05:34, think
	 */
	public SearchResult<SaleReturnDetail> findAll(ReturnGoodsType type);

}
