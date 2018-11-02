/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年7月12日 下午5:54:22
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.exterior.service;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.exterior.dto.RequestDto;
import com.huayin.printmanager.exterior.dto.ResponseDto;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrder;
import com.huayin.printmanager.exterior.vo.ExteriorPurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 外部接口  - 平台采购单数据交互
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年7月12日下午5:54:22, zhengby
 */
public interface ExteriorPurchService
{
	/**
	* <pre>
	* 获取外部采购单
	* </pre>
	* @param id
	* @return
	* @since 1.0, 2018年7月18日 下午3:29:34, zhengby
	*/
	public ExteriorPurchOrder get(Long id);

	/**
	* <pre>
	* 获取一条外部采购单明细
	* </pre>
	* @param id
	* @return
	* @since 1.0, 2018年7月18日 下午3:29:56, zhengby
	*/
	public ExteriorPurchOrderDetail getDetail(Long id);

	/**
	* <pre>
	* 获取一条外部采购单明细（含主表）
	* </pre>
	* @param id
	* @return
	* @since 1.0, 2018年7月18日 下午3:33:28, zhengby
	*/
	public ExteriorPurchOrderDetail getDetailHasMater(Long id);

	/**
	* <pre>
	* 获取外部采购单的明细表
	* </pre>
	* @param masterId
	* @return
	* @since 1.0, 2018年7月18日 下午3:32:05, zhengby
	*/
	public List<ExteriorPurchOrderDetail> getDetailList(Long masterId);

	/**
	* <pre>
	* 获取全部外部采购单明细（不分公司）
	* </pre>
	* @param masterId
	* @return
	* @since 1.0, 2018年7月18日 下午3:30:24, zhengby
	*/
	public List<ExteriorPurchOrderDetail> getAllDetailList(Long masterId);

	/**
	* <pre>
	* 获取外部采购单（含明细）
	* </pre>
	* @param id
	* @return
	* @since 1.0, 2018年7月18日 下午3:30:57, zhengby
	*/
	public ExteriorPurchOrder getOrderHasDetail(Long id);
	
	/**
	 * <pre>
	 * 根据印刷家的采购单编号查询订单
	 * </pre>
	 * @param purchOrderNm
	 * @return
	 * @since 1.0, 2018年7月19日 下午5:12:00, zhengby
	 */
	public ExteriorPurchOrder findOrderByConditions(String purchOrderNm);
	
	/**
	* <pre>
	* 接收印刷家通知采购单
	* </pre>
	* @param requestDto
	* @return
	* @since 1.0, 2018年7月18日 下午3:27:59, zhengby
	*/
	public ResponseDto acceptPONotice(RequestDto requestDto);

	/**
	* <pre>
	* 创建采购订单
	* </pre>
	* @param puchaseOrderNm
	 * @return 
	* @since 1.0, 2018年7月18日 下午3:28:23, zhengby
	*/
	public void purchaseOrder(String puchaseOrderNm);

	/**
	* <pre>
	* 外部采购单转成本系统的采购订单
	* </pre>
	* @param ids
	* @return
	* @since 1.0, 2018年7月18日 下午3:31:23, zhengby
	*/
	public PurchOrder createPurchOrder(List<Long> ids);

	/**
	* <pre>
	* 通知印刷家外部采购单的入库状态(定时任务)
	* </pre>
	* @since 1.0, 2018年7月18日 下午3:34:01, zhengby
	*/
	public void noticePOStatus();

	/**
	* <pre>
	* 查看外部采购单明细列表
	* </pre>
	* @param queryParam
	* @return
	* @since 1.0, 2018年7月18日 下午3:29:04, zhengby
	*/
	public SearchResult<ExteriorPurchOrder> exteriorPurchList(QueryParam queryParam);

}
