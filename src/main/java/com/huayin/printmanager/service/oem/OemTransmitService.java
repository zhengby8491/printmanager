/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:50:27
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 *  代工管理  - 代工未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:50:27, zhengby
 */
public interface OemTransmitService
{

	/**
	 * <pre>
	 * 查询未送货清单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月13日 下午5:16:11, zhengby
	 */
	public SearchResult<OemOrderDetail> findOrderForTransmitDeliver(QueryParam queryParam);


	/**
	 * <pre>
	 * 查询送货未对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:42:10, zhengby
	 */
	public SearchResult<OemDeliverDetail> findDeliverForTransmitReconcil(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询退货未对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:42:47, zhengby
	 */
	public SearchResult<OemReturnDetail> findReturnForTransmitReconcil(QueryParam queryParam);
}
