/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月16日 上午10:04:45
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.offer;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferOrderQuoteInner;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * 
 * <pre>
 * 报价服务层
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月16日
 */
public interface OfferOrderService
{
	/**
	 * <pre>
	 * 报价单列表查询
	 * </pre>
	 * @return
	 */
	public SearchResult<OfferOrder> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据id获取报价订单信息，以及引用信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月9日 下午1:44:52, think
	 */
	public OfferOrder get(Long id);

	/**
	 * <pre>
	 * 根据offerNo获取报价订单信息，以及引用信息
	 * </pre>
	 * @param offerNo
	 * @return
	 * @since 1.0, 2018年2月28日 下午4:29:54, think
	 */
	public OfferOrder get(String offerNo);

	/**
	 * <pre>
	 * 获取报价订单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月9日 下午1:44:38, think
	 */
	public OfferOrder getOrder(Long id);
	
	/**
	 * <pre>
	 * 查询所有对内报价
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月1日 下午5:10:23, think
	 */
	public List<OfferOrderQuoteInner> findAllQuoteInner();

	/**
	 * <pre>
	 * 删除报价订单信息，以及引用信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月9日 下午1:45:11, think
	 */
	public Boolean delete(Long id);

	/**
	 * <pre>
	 * 更新报价订单信息
	 * </pre>
	 * @param offerOrder
	 * @return
	 * @since 1.0, 2018年2月9日 下午1:46:34, think
	 */
	public OfferOrder updateOrder(OfferOrder offerOrder);

	/**
	 * <pre>
	 * 审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月7日 上午11:14:59, think
	 */
	public Boolean check(Long id);

	/**
	 * <pre>
	 * 反审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月10日 上午9:20:28, think
	 */
	public Boolean checkBack(Long id);

	/**
	 * <pre>
	 * 根据销售id查询报价信息
	 * </pre>
	 * @param saleId
	 * @return
	 * @since 1.0, 2018年2月9日 下午2:45:05, think
	 */
	public List<OfferOrder> findBySaleId(Long saleId);

	/**
	 * <pre>
	 * 强制完工
	 * </pre>
	 * @param ids
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月28日 上午11:58:36, think
	 */
	public boolean forceComplete(List<Long> ids, BoolValue flag);


}
