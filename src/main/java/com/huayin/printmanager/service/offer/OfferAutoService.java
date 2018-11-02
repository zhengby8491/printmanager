/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年11月3日 上午9:41:02
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.offer;

import com.huayin.printmanager.persist.entity.offer.OfferOrder;

/**
 * <pre>
 * 报价系统 - 自动报价
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月3日 上午9:41:02
 */
public interface OfferAutoService
{
	// ==================== 自动报价 - 公共 ======================
	
	/**
	 * 
	 * <pre>
	 * 保存
	 * </pre>
	 * @param offerOrder
	 * @return
	 * @since 1.0, 2017年11月7日 下午4:36:10, zhengby
	 */
	public OfferOrder save(OfferOrder offerOrder);

	/**
	 * <pre>
	 * 计算报价
	 * </pre>
	 * @param offerOrder
	 * @return
	 * @since 1.0, 2017年11月6日 上午10:58:09, think
	 */
	public OfferOrder quote(OfferOrder offerOrder);
}
