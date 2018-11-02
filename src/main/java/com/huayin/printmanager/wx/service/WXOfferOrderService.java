/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 报价系统
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXOfferOrderService
{
	/**
	 * <pre>
	 * 报价单列表查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:41:53, think
	 */
	public SearchResult<OfferOrder> findByCondition(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 获取报价订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:42:00, think
	 */
	public OfferOrder get(Long id);
	
	/**
	 * <pre>
	 * 删除报价订单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:42:11, think
	 */
	public Boolean delete(Long id);
}
