/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月8日 下午2:24:12
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sale;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sale.SaleDeliverDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReturnDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 销售管理 - 销售未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月8日下午2:24:12, zhengby
 */
public interface SaleTransmitService
{
	/**
	 * <pre>
	 * 工单未送货
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月8日 下午2:34:08, zhengby
	 */
	public SearchResult<SaleOrderDetail> findForTransmitDeliverByWork(QueryParam queryParam);

	/**
	 * <pre>
	 * 工单未送货
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月8日 下午2:35:45, zhengby
	 */
	public SearchResult<SaleOrderDetail> findForTransmitDeliver(QueryParam queryParam);

	/**
	 * <pre>
	 * 送货单转对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月8日 下午2:50:18, zhengby
	 */
	public SearchResult<SaleDeliverDetail> findDeliverForTransmitReconcil(QueryParam queryParam);

	/**
	 * <pre>
	 * 退货单转对账
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月8日 下午2:51:15, zhengby
	 */
	public SearchResult<SaleReturnDetail> findReturnForTransmitReconcil(QueryParam queryParam);
	
}
