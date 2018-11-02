/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.outsource;

import java.util.Date;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArrive;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 发外管理 - 发外到货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:30:43, zhengby, 代码规范
 */
public interface OutSourceArriveService
{
	/**
	 * <pre>
	 * 获取发外到货单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:30:47, zhengby
	 */
	public OutSourceArrive get(Long id);

	/**
	 * <pre>
	 * 获取发外到货明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:32:05, zhengby
	 */
	public OutSourceArriveDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:32:24, zhengby
	 */
	public OutSourceArrive lock(Long id);

	/**
	 * <pre>
	 * 获取发外到货明细表列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:32:57, zhengby
	 */
	public List<OutSourceArriveDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存发外到货单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:33:26, zhengby
	 */
	public OutSourceArrive save(OutSourceArrive order);

	/**
	 * <pre>
	 * 更新发外到货单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:33:42, zhengby
	 */
	public OutSourceArrive update(OutSourceArrive order);

	/**
	 * <pre>
	 * 审核发外到货单
	 * </pre>
	 * @param id
	 * @param flag
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:33:56, zhengby
	 */
	public boolean check(Long id, BoolValue flag);

	/**
	 * <pre>
	 * 查找销售订单下的产品库存
	 * </pre>
	 * @param i
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:34:13, zhengby
	 */
	public List<StockProduct> checkBack(Long i);

	/**
	 * <pre>
	 * 删除发外到货单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 下午4:34:25, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param dateMin 创建日期最小条件
	 * @param dateMax 创建日期最大条件
	 * @param userName 用户名 模糊查询
	 * @parama sortMap 排序条件
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:34:51, zhengby
	 */
	public SearchResult<OutSourceArrive> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param dateMin 创建日期最小条件
	 * @param dateMax 创建日期最大条件
	 * @param userName 用户名 模糊查询
	 * @parama sortMap 排序条件
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:34:51, zhengby
	 */
	public SearchResult<OutSourceArriveDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:34:51, zhengby
	 */
	public SearchResult<OutSourceArriveDetail> findForTransmitReconcil(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找发外到货来源
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param supplierId
	 * @param billNo
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:35:18, zhengby
	 */
	public SearchResult<OutSourceArriveDetail> findArriveSource(Date dateMin, Date dateMax, Long supplierId, String billNo, Integer pageSize, Integer pageNumber);

	/**
	 * <pre>
	 * 查询返写对账数量 说明:发外加工单明细ID即到货源明细ID
	 * </pre>
	 * @param bean
	 * @since 1.0, 2018年2月23日 下午4:35:38, zhengby
	 */
	public void coutReturnReconcil(OutSourceProcessDetail bean);
}
