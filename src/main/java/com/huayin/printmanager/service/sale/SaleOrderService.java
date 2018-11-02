/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年02月22日 下午17:19:23
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.sale;

import java.util.List;
import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sale.SaleOrder;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPack;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart2Product;
import com.huayin.printmanager.persist.entity.sale.SaleOrderProcedure;
import com.huayin.printmanager.persist.enumerate.ResultType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;

/**
 * <pre>
 * 销售管理 - 销售订单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月22日下午5:20:45
 */
public interface SaleOrderService
{
	/**
	 * <pre>
	 * 根据id获取销售订单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:21:10, zhengby
	 */
	public SaleOrder get(Long id);

	/**
	 * <pre>
	 * 根据销售单号获取订单信息
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月27日 下午7:26:58, zhengby
	 */
	public SaleOrder get(String billNo);

	/**
	 * <pre>
	 * 根据id获取销售订单信息
	 * 包含子表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:21:29, zhengby
	 */
	public SaleOrder getHasChildren(Long id);

	/**
	 * <pre>
	 * 根据id获取销售订单明细信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:21:39, zhengby
	 */
	public SaleOrderDetail getDetail(Long id);

	/**
	 * <pre>
	 * 根据id获取销售订单明细信息
	 * 包含主表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:21:50, zhengby
	 */
	public SaleOrderDetail getDetailHasMaster(Long id);

	/**
	 * <pre>
	 * 根据id锁定销售订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:22:02, zhengby
	 */
	public SaleOrder lockHasChildren(Long id);

	/**
	 * <pre>
	 * 根据主表id获得销售订单明细列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:22:12, zhengby
	 */
	public List<SaleOrderDetail> getDetailList(Long id);
	
	/**
   * <pre>
   * 获取最新创建的订单
   * </pre>
   * @return
   * @since 1.0, 2018年4月24日 下午6:48:28, zhengby
   */
  public SaleOrder getLatestOrderHasDetail();

	/**
	 * <pre>
	 * 保存销售订单
	 * </pre>
	 * @param order
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:22:22, zhengby
	 */
	public SaleOrder save(SaleOrder order) throws Exception;

	/**
	 * <pre>
	 * 修改销售订单
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:22:33, zhengby
	 */
	public SaleOrder update(SaleOrder order);

	/**
	 * <pre>
	 * 删除销售订单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月22日 下午5:23:36, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 审核所有
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:23:45, zhengby
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 为创建工单选销售单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:23:53, zhengby
	 */
	public SearchResult<SaleOrderDetail> quickFindForWorkByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 获取所以销售订单年份列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:24:27, zhengby
	 */
	public List<SaleOrder> getYearsFromSaleOrder();

	/**
	 * <pre>
	 * 汇总(按客户)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:24:36, zhengby
	 */
	public SearchResult<SumVo> sumSaleOrderByCustomer(QueryParam queryParam, String type);

	/**
	 * <pre>
	 * 汇总(按产品)
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:24:44, zhengby
	 */
	public SearchResult<SumVo> sumSaleOrderByProduct(QueryParam queryParam, String type);

	/**
	 * <pre>
	 * 汇总(按销售员)
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:24:52, zhengby
	 */
	public SearchResult<SumVo> sumSaleOrderBySeller(QueryParam queryParam);

	/**
	 * <pre>
	 * 分页查询详情列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:01, zhengby
	 */
	public SearchResult<SaleOrderDetail> queryPagDetailList(QueryParam queryParam);

	/**
	 * <pre>
	 * 单价或交期修改
	 * </pre>
	 * @param saleOrderDetail
	 * @throws Exception
	 * @since 1.0, 2018年2月22日 下午5:25:10, zhengby
	 */
	public void changePrice(SaleOrderDetail saleOrderDetail) throws Exception;

	/**
	 * <pre>
	 * 获取部件的材料列表
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:20, zhengby
	 */
	public List<SaleOrderMaterial> getSaleMaterialListByPartId(Long partId);

	/**
	 * <pre>
	 * 获取部件的工序信息
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:28, zhengby
	 */
	public List<SaleOrderProcedure> getProcedureListByPartId(Long partId);

	/**
	 * <pre>
	 * 获取部件的产品信息
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:39, zhengby
	 */
	public List<SaleOrderPart2Product> getPart2ProductByPartId(Long partId);

	/**
	 * <pre>
	 * 获取销售明细部件列表信息
	 * </pre>
	 * @param detailId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:47, zhengby
	 */
	public List<SaleOrderPart> getPartListByDetailId(Long detailId);

	/**
	 * <pre>
	 * 获取销售明细成品信息
	 * </pre>
	 * @param detailId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:25:54, zhengby
	 */
	public SaleOrderPack getPackByDetailId(Long detailId);

	/**
	 * <pre>
	 * 获取成品(装订打包)的材料列表
	 * </pre>
	 * @param packId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:26:04, zhengby
	 */
	public List<SaleOrderMaterial> getSaleMaterialListByPackId(Long packId);

	/**
	 * <pre>
	 * 获取成品(装订打包)的工序信息
	 * </pre>
	 * @param packId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:26:14, zhengby
	 */
	public List<SaleOrderProcedure> getProcedureListByPackId(Long packId);

	/**
	 * <pre>
	 * 获取销售明细所有工序列表
	 * </pre>
	 * @param saleDetailId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:26:22, zhengby
	 */
	public List<SaleOrderProcedure> getProcedureListByDetailId(Long saleDetailId);

	/**
	 * <pre>
	 * 获取销售明细所有材料
	 * </pre>
	 * @param detailId
	 * @return
	 * @since 1.0, 2018年2月22日 下午5:26:29, zhengby
	 */
	public List<SaleOrderMaterial> getSaleMaterialListByDetaiId(Long detailId);
	
	// ==================== 新规范 - 代码重构 ====================

	/**
	 * <pre>
	 * 销售订单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午11:20:05, think
	 */
	public SearchResult<SaleOrder> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询销售订单明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:31:04, think
	 */
	public SearchResult<SaleOrderDetail> findDetailByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询销售订单进度表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:32:04, think
	 */
	public SearchResult<SaleOrderDetail> findFlowByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 报价转订单之前,检查是否需要同步基础数据
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月8日 上午10:19:00, think
	 */
	public Map<ResultType, List<String>> genFromOfferCheck(List<String> ids);

	/**
	 * <pre>
	 * 报价转订单
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月8日 下午2:26:02, think
	 */
	public SaleOrder genFromOffer(List<String> ids);
}
