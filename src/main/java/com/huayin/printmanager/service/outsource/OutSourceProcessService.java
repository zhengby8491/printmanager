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

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;

/**
 * <pre>
 * 发外管理 -发外加工
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日下午4:36:11, zhengby, 代码规范
 */
public interface OutSourceProcessService
{
	/**
	 * <pre>
	 * 获取发外加工单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:37:39, zhengby
	 */
	public OutSourceProcess get(Long id);

	/**
	 * <pre>
	 * 获取发外加工单明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:37:54, zhengby
	 */
	public OutSourceProcessDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:38:20, zhengby
	 */
	public OutSourceProcess lock(Long id);

	/**
	 * <pre>
	 * 获取发外加工明细表列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:38:33, zhengby
	 */
	public List<OutSourceProcessDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 保存发外加工单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:39:15, zhengby
	 */
	public OutSourceProcess save(OutSourceProcess order);

	/**
	 * <pre>
	 * 更新发外加工单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:39:26, zhengby
	 */
	public OutSourceProcess update(OutSourceProcess order);

	/**
	 * <pre>
	 * 删除发外加工单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 下午4:39:36, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 审核所有发外加工单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:39:44, zhengby
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 多条件查询发外加工单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:39:55, zhengby
	 */
	public SearchResult<OutSourceProcess> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 多条件查询发外加工单明细表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:40:02, zhengby
	 */
	public SearchResult<OutSourceProcessDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 多条件查询发外加工单明细表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:40:43, zhengby
	 */
	public SearchResult<OutSourceProcessDetail> findForTransmitArrive(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取所有发外加工单年份列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:41:24, zhengby
	 */
	public List<OutSourceProcess> getYearsFromOutSourceProcess();

	/**
	 * <pre>
	 * 发外加工汇总(按加工商)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:41:41, zhengby
	 */
	public SearchResult<SumVo> sumOutsourceBySupplier(QueryParam queryParam, String type);

	/**
	 * 发外加工汇总(按工序)
	 * <pre>
	 * 
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 *  @since 1.0, 2018年2月23日 下午4:41:41, zhengby
	 */
	public SearchResult<SumVo> sumOutsourceByProcedure(QueryParam queryParam, String type);

	/**
	 * <pre>
	 * 发外加工汇总(按发外类型)
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:42:15, zhengby
	 */
	public SearchResult<SumVo> sumOutsourceByType(QueryParam queryParam);

	/**
	 * <pre>
	 * 查加工单强制完工数量
	 * </pre>
	 * @param billNo
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:42:29, zhengby
	 */
	public Integer getCompleteQty(String billNo, Long productId);

	/**
	 * <pre>
	 * 根据发外加工单号查询加工单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月29日 下午2:58:43, zhengby
	 */
	OutSourceProcess get(String billNo);

	/**
	 * <pre>
	 * 单价变更
	 * </pre>
	 * @param outSourceProcessDetail
	 * @throws Exception
	 * @since 1.0, 2018年4月17日 下午6:04:06, think
	 */
	public void changePrice(OutSourceProcessDetail outSourceProcessDetail) throws Exception;
}
