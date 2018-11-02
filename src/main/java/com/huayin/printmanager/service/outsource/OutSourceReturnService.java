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

import java.math.BigDecimal;
import java.util.List;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturn;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 发外管理 - 发外退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:47:16, zhengby, 代码规范
 */
public interface OutSourceReturnService
{
	/**
	 * <pre>
	 * 获取发外退货单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:47:55, zhengby
	 */
	public OutSourceReturn get(Long id);

	/**
	 * <pre>
	 * 获取发外退货单明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:48:07, zhengby
	 */
	public OutSourceReturnDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:48:22, zhengby
	 */
	public OutSourceReturn lock(Long id);

	/**
	 * <pre>
	 * 获取发外退货单明细列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:48:34, zhengby
	 */
	public List<OutSourceReturnDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存发外退货单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:48:48, zhengby
	 */
	public ServiceResult<OutSourceReturn> save(OutSourceReturn order);

	/**
	 * <pre>
	 * 更新发外退货单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:49:41, zhengby
	 */
	public ServiceResult<OutSourceReturn> update(OutSourceReturn order);

	/**
	 * <pre>
	 * 单据审核
	 * </pre>
	 * @param id
	 * @param forceCheck
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:49:55, zhengby
	 */
	public List<StockProduct> check(Long id, BoolValue forceCheck);

	/**
	 * <pre>
	 * 反审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午10:27:41, think
	 */
	List<StockProduct> checkBack(Long id);

	/**
	 * <pre>
	 * 删除发外退货单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 下午4:50:09, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 主表多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:50:24, zhengby
	 */
	public SearchResult<OutSourceReturn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:50:33, zhengby
	 */
	public SearchResult<OutSourceReturnDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 明细多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:50:41, zhengby
	 */
	public SearchResult<OutSourceReturnDetail> findForTransmitReconcil(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询退货返写的对账数量
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:50:49, zhengby
	 */
	public BigDecimal coutReturnReconcil(Long id);

}
