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

import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 支付
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXPayService
{
	/**
	 * <pre>
	 * 查询所有产品模块和产品菜单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:46:22, think
	 */
	Map<String, Object> findProductAndMenu();

	/**
	 * <pre>
	 * 根据ID获取购买产品信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:46:28, think
	 */
	Buy getByProductId(Long id);

	/**
	 * <pre>
	 * 查询所有订单信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:46:34, think
	 */
	SearchResult<BuyRecord> findAllOrder(QueryParam queryParam);
	
}
