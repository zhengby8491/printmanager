/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 购买信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface BuyService
{
	/**
	 * <pre>
	 * 根据产品id获取购买的产品信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:42:28, think
	 */
	public Buy get(Long id);

	/**
	 * <pre>
	 * 根据id查询下单记录
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:44:37, think
	 */
	public BuyRecord getOrder(Long id);

	/**
	 * <pre>
	 * 根据条件查询购买的产品信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:42:51, think
	 */
	public SearchResult<Buy> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据订单编号查询下单记录
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:43:15, think
	 */
	public BuyRecord findOrderByBillNo(String billNo);

	/**
	 * <pre>
	 * 查询所有下单记录
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:43:22, think
	 */
	public SearchResult<BuyRecord> findAllOrder(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询购买过的产品
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:43:33, think
	 */
	public BuyRecord findByOrderState();

	/**
	 * <pre>
	 * 查询产品模块
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:45:36, think
	 */
	public List<Buy> findProductList();

	/**
	 * <pre>
	 * 新增购买信息
	 * </pre>
	 * @param product
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:43:40, think
	 */
	public Buy save(Buy product);

	/**
	 * <pre>
	 * 保存订单
	 * </pre>
	 * @param purchaseRecord
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:46:16, think
	 */
	public BuyRecord savaOrer(BuyRecord purchaseRecord);

	/**
	 * <pre>
	 * 手动添加/更新订单
	 * </pre>
	 * @param purchaseRecord
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:46:24, think
	 */
	BuyRecord savaOrerManual(BuyRecord purchaseRecord);

	/**
	 * <pre>
	 * 修改购买信息
	 * </pre>
	 * @param product
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:44:52, think
	 */
	public Buy update(Buy product);

	/**
	 * <pre>
	 * 修改销售产品对应的权限
	 * </pre>
	 * @param productId
	 * @param menuIdList
	 * @since 1.0, 2017年10月25日 下午4:45:29, think
	 */
	public void updateProductMemu(Long productId, List<Long> menuIdList);

	/**
	 * <pre>
	 * 修改订单状态
	 * </pre>
	 * @param out_trade_no
	 * @param trade_no
	 * @param paymentMethod
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午4:46:35, think
	 */
	public void updateOrderInfo(String out_trade_no, String trade_no, int paymentMethod) throws OperatorException;

	/**
	 * <pre>
	 * 修改订单状态
	 * </pre>
	 * @param out_trade_no
	 * @param trade_no
	 * @param paymentMethod
	 * @param attach
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午4:46:41, think
	 */
	public void updateOrderInfo(String out_trade_no, String trade_no, int paymentMethod, String attach) throws OperatorException;

	/**
	 * <pre>
	 * 修改订单
	 * </pre>
	 * @param orderId
	 * @param productId
	 * @throws OperatorException
	 * @since 1.0, 2017年10月25日 下午4:46:47, think
	 */
	public void updateOrder(Long orderId, Long productId) throws OperatorException;

	/**
	 * <pre>
	 * 删除购买信息
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午4:46:55, think
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 取消订单
	 * </pre>
	 * @param id
	 * @since 1.0, 2017年10月25日 下午4:47:34, think
	 */
	public void cancelOrder(Long id);

	/**
	 * <pre>
	 * 检查订单
	 * </pre>
	 * @param orderState
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:47:40, think
	 */
	public boolean check(int orderState);
}
