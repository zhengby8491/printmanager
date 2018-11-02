/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月7日 下午1:43:08
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemOrder;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;

/**
 * <pre>
 * 代工管理  - 代工单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月7日下午1:43:08, zhengby
 */
public interface OemOrderService  
{

	/**
	 * <pre>
	 * 查询代工单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月9日 下午5:21:28, zhengby
	 */
	public OemOrder get(Long id);

	/**
	 * <pre>
	 * 查询明细表数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月9日 下午5:29:22, zhengby
	 */
	public List<OemOrderDetail> getDetailList(Long id);
	
	/**
	 * <pre>
	 * 根据源单公司id和源单工序id查询订单明细 
	 * </pre>
	 * @param originCompanyId
	 * @param originProcedureId
	 * @return
	 * @since 1.0, 2018年4月2日 下午3:59:03, think
	 */
	public OemOrderDetail getDetail(String originCompanyId, Long originProcedureId);

	/**
	 * <pre>
	 * 保存
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月9日 下午5:38:40, zhengby
	 */
	public void save(OemOrder order);

	/**
	 * <pre>
	 * 修改
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年3月9日 下午5:45:37, zhengby
	 */
	public void update(OemOrder order);

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年3月9日 下午6:29:53, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 查询列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月12日 下午7:44:20, zhengby
	 */
	public SearchResult<OemOrder> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 锁定数据,直到事务结束
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 上午11:29:03, zhengby
	 */
	public OemOrder lockHasChildren(Long id);

	/**
	 * <pre>
	 * 查询明细单据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 下午2:33:15, zhengby
	 */
	public OemOrderDetail getDetail(Long id);

	/**
	 * <pre>
	 * 查询明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月13日 下午3:34:30, zhengby
	 */
	public SearchResult<OemOrderDetail> findDetailBycondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取单个代工单明细（含主表）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 下午6:38:29, zhengby
	 */
	public OemOrderDetail getDetailHasMaster(Long id);

	/**
	 * <pre>
	 * 获取代工单主表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月13日 下午6:39:00, zhengby
	 */
	public OemOrder getMaster(Long id);

	/**
	 * <pre>
	 * 查询代工单进度表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:13:28, zhengby
	 */
	public SearchResult<OemOrderDetail> findFlowByCondition(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 获取代工订单创建日期列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月26日 下午7:09:42, zhengby
	 */
	public List<OemOrder> getYearsFromOemOrder();
	
	/**
	 * <pre>
	 * 代工订单汇总(按客户)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午6:49:00, zhengby
	 */
	public SearchResult<SumVo> sumOemOrderByCustomer(QueryParam queryParam, String type);

	/**
	 * <pre>
	 * 代工订单汇总(按工序)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月26日 下午7:35:53, zhengby
	 */
	public SearchResult<SumVo> sumOemOrderByProcedure(QueryParam queryParam, String type);

	/**
	 * <pre>
	 * 代工订单汇总(按销售员)
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月26日 下午7:38:47, zhengby
	 */
	public SearchResult<SumVo> sumOemOrderBySeller(QueryParam queryParam);

}
