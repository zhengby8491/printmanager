/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午9:47:12
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.produce;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkPack;
import com.huayin.printmanager.persist.entity.produce.WorkPart;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.produce.WorkReportDetail;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.service.produce.vo.WorkMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 生产管理 - 生产工单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日上午9:47:39, zhengby
 */
public interface WorkService
{
	/**
	 * <pre>
	 * 获取工单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:48:17, zhengby
	 */
	public Work get(Long id);

	/**
	 * <pre>
	 * 根据工单号获取工单信息（只获取工单，没有工单部件等其他信息）
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:48:30, zhengby
	 */
	public Work get(String billNo);

	/**
	 * <pre>
	 * 获取单个产品信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:48:52, zhengby
	 */
	public WorkProduct getProduct(Long id);

	/**
	 * <pre>
	 * 获取单个部件信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:01, zhengby
	 */
	public WorkPart getPart(Long id);

	/**
	 * <pre>
	 * 获取成品信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:11, zhengby
	 */
	public WorkPack getPack(Long id);

	/**
	 * <pre>
	 * 获取单个工序信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:21, zhengby
	 */
	public WorkProcedure getProcedure(Long id);

	/**
	 * <pre>
	 * 获取单个材料信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:34, zhengby
	 */
	public WorkMaterial getMaterial(Long id);

	/**
	 * <pre>
	 * 获取工单产品列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:48, zhengby
	 */
	public List<WorkProduct> getProductListByWorkId(Long workId);

	/**
	 * <pre>
	 * 获取工单部件列表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:49:59, zhengby
	 */
	public List<WorkPart> getPartListByWorkId(Long workId);

	/**
	 * <pre>
	 * 获取工单成品信息
	 * </pre>
	 * @param workId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:50:11, zhengby
	 */
	public WorkPack getPackByWorkId(Long workId);

	/**
	 * <pre>
	 * 获取部件的产品信息
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:50:26, zhengby
	 */
	public List<WorkPart2Product> getPart2ProductByPartId(Long partId);

	/**
	 * <pre>
	 * 获取部件的工序信息
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:50:36, zhengby
	 */
	public List<WorkProcedure> getProcedureListByPartId(Long partId);

	/**
	 * <pre>
	 * 获取部件的材料列表
	 * </pre>
	 * @param partId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:50:46, zhengby
	 */
	public List<WorkMaterial> getWorkMaterialListByPartId(Long partId);

	/**
	 * <pre>
	 * 获取成品(装订打包)的工序信息
	 * </pre>
	 * @param packId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:50:54, zhengby
	 */
	public List<WorkProcedure> getProcedureListByPackId(Long packId);

	/**
	 * <pre>
	 * 获取成品(装订打包)的材料列表
	 * </pre>
	 * @param packId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:51:03, zhengby
	 */
	public List<WorkMaterial> getWorkMaterialListByPackId(Long packId);

	/**
	 * <pre>
	 * 获取工单所有工序列表
	 * </pre>
	 * @param workId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:51:12, zhengby
	 */
	public List<WorkProcedure> getProcedureListByWorkId(Long workId);

	/**
	 * <pre>
	 * 获取工单所有材料列表
	 * </pre>
	 * @param workId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:51:20, zhengby
	 */
	public List<WorkMaterial> getWorkMaterialListByWorkId(Long workId);

	/**
	 * <pre>
	 * 保存
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:53:10, zhengby
	 */
	public Work save(Work order);

	/**
	 * <pre>
	 * 更新
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:53:29, zhengby
	 */
	public Work update(Work order);

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 上午9:53:40, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 审核所有
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:53:49, zhengby
	 */
	public boolean checkAll();

	/**
	 * <pre>
	 * 工单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:53:58, zhengby
	 */
	public SearchResult<Work> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 工单产品列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:54:08, zhengby
	 */
	public SearchResult<WorkProduct> findProductByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 取消发外加工
	 * </pre>
	 * @param type 发外类型
	 * @param ids id数组
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:54:18, zhengby
	 */
	public int cancelOutSourceProcess(OutSourceType type, Long[] ids);

	/**
	 * <pre>
	 * 多条件查询生产工单工序
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:54:30, zhengby
	 */
	public SearchResult<WorkProcedure> findProcedureByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 多条件查询生产工单用料
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:54:37, zhengby
	 */
	public SearchResult<WorkMaterialVo> findMaterialByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找需要发外的工序列表(工序发外)
	 * </pre>
	 * @param workBillNo 工单单据编号
	 * @param procedureName 工序名称
	 * @param productName 成品名称
	 * @param isForceComplete 是否强制完工
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:54:52, zhengby
	 */

	public SearchResult<WorkProcedure> findForTransmitProcedureOut(QueryParam queryParam);

	/**
	 * <pre>
	 * 查找需要发外的产品列表(整单发外)
	 * </pre>
	 * @param workBillNo 工单单据编号
	 * @param customerId 客户ID
	 * @param productId 产品ID
	 * @param isForceComplete 是否强制完工
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:55:12, zhengby
	 */
	public SearchResult<WorkProduct> findForTransmitProductOut(QueryParam queryParam);

	/**
	 * 
	 * <pre>
	 * 打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:55:31, zhengby
	 */
	public Map<String, Object> printDataMap(Long id);

	/**
	 * <pre>
	 * 获取工单产品
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:55:55, zhengby
	 */
	public List<WorkProduct> getWorkProduct(Long id);

	/**
	 * <pre>
	 * 销售明细追踪检查工单是否全部审核
	 * </pre>
	 * @param saleOrderBillNo
	 * @param productId
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:57:00, zhengby
	 */
	public Boolean hasCheckAll(String saleOrderBillNo, Long productId);

	/**
	 * <pre>
	 * 获取工单材料的剩余库存量
	 * </pre>
	 * @param materialId
	 * @param style
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:57:14, zhengby
	 */
	public BigDecimal getWorkEmployQty(Long materialId, String style);

	/**
	 * <pre>
	 * 根据多条件查询生产日报表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:03:26, zhengby
	 */
	public SearchResult<WorkReportDetail> findWorkReportDeailsByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询生产任务
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:03:37, zhengby
	 */
	public SearchResult<WorkReportTask> findProductTaskList(QueryParam queryParam);
	
	/**
	 * <pre>
	 * 查询单条生产任务
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月17日 下午5:27:13, zhengby
	 */
	public WorkReportTask getWorkReportTaskById(Long id);

	/**
	 * <pre>
	 * 保存产量上报
	 * </pre>
	 * @param workReport
	 * @since 1.0, 2018年2月23日 上午10:03:54, zhengby
	 */
	public void saveReport(WorkReport workReport);

	/**
	 * <pre>
	 * 查询生产报告明细
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:04:21, zhengby
	 */
	public List<WorkReportDetail> findReportInfo(Long[] ids);

	/**
	 * <pre>
	 * 根据id查询生产任务信息
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:04:45, zhengby
	 */
	public List<WorkReportTask> findReportTaskInfo(Long[] ids);

	/**
	 * <pre>
	 * 查询上报任务
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:05:19, zhengby
	 */
	public WorkReport findWorkReportById(Long id);

	/**
	 * <pre>
	 * 修改产量上报
	 * </pre>
	 * @param workReport
	 * @since 1.0, 2018年2月23日 上午10:05:27, zhengby
	 */
	public void updateReport(WorkReport workReport);

	/**
	 * <pre>
	 * 审核/反审核   生产任务
	 * </pre>
	 * @param billType
	 * @param id
	 * @param boolValue
	 * @since 1.0, 2018年2月23日 上午10:05:36, zhengby
	 */
	public void auditReport(BillType billType, Long id, BoolValue boolValue);

	/**
	 * <pre>
	 * 删除产量上报单
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 上午10:05:46, zhengby
	 */
	public void deleteReport(Long id);

	/**
	 * <pre>
	 * 查询上报任务信息 
	 * </pre>
	 * @param ids
	 * @param billNo 为MO开头的生产工单或者SO开头的单号
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:05:55, zhengby
	 */
	public List<WorkReportTask> findReportTaskInfo(Long[] ids, String billNo, String companyId);

	/**
	 * <pre>
	 * 根据taskId反查生产日报表明细
	 * </pre>
	 * @param taskId
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:06:14, zhengby
	 */
	public List<WorkReportDetail> findReportDetailByTaskId(Long taskId);

	/**
	 * <pre>
	 * 创建生产任务
	 * </pre>
	 * @param id
	 * @param boolValue
	 * @since 1.0, 2018年2月23日 上午10:06:31, zhengby
	 */
	public void savaReportTask(Long id, BoolValue boolValue);

	/**
	 * <pre>
	 * 判断生产任务是否上报过
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:06:44, zhengby
	 */
	public boolean findReportDetailByTask(Long id);

	/**
	 * <pre>
	 * 根据工单id删除生产任务
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 上午10:06:57, zhengby
	 */
	public void deleteReportTask(Long id);

	/**
	 * <pre>
	 * 根据工单编号查询生产任务明细
	 * </pre>
	 * @param billNo
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:07:06, zhengby
	 */
	public SearchResult<Map<String, Object>> findReportTaskDetail(String billNo, String companyId);

	/**<pre>
	 * 筛选掉所有未上报数小于0的生产任务 
	 * 筛选掉不排除
	 * 筛选掉发外
	 * </pre>
	 * @param ids
	 * @param billNo
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:07:48, zhengby
	 */
	List<WorkReportTask> findReportTaskUnreportQtyInfo(Long[] ids, String billNo, String companyId);

	/**
	 * <pre>
	 * 根据工单id强制完工生产任务
	 * </pre>
	 * @param id
	 * @param boolValue
	 * @since 1.0, 2018年2月23日 上午10:08:03, zhengby
	 */
	public void completeTaskByWorkId(Long[] id, BoolValue boolValue);

	/**
	 * <pre>
	 * 修改生产任务
	 * </pre>
	 * @param id
	 * @param boolValue
	 * @param update_part_list
	 * @param del_part_list
	 * @since 1.0, 2018年2月23日 上午10:08:16, zhengby
	 */
	public void updateReportTask(Long id, BoolValue boolValue, List<WorkPart> update_part_list, List<WorkPart> del_part_list);

	/**
	 * <pre>
	 * 修改生产任务状态
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年2月23日 上午10:08:27, zhengby
	 */
	public void updateReportTaskState(Long id);

	/**
	 * <pre>
	 * 查询材料是否被下级订单引用
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:08:37, zhengby
	 */
	public Map<String, Object> findMateriaState(Long id);

	/**
	 * <pre>
	 * 查询工序是否被下级订单引用
	 * </pre>
	 * @param procedureId
	 * @param billNo
	 * @param procedureRefId
	 * @param partName
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:08:53, zhengby
	 */
	public Map<String, Object> findProcedureState(Long procedureId, String billNo, Long procedureRefId, String partName);

	/**
	 * <pre>
	 * 更新材料信息
	 * </pre>
	 * @param order
	 * @throws Exception
	 * @since 1.0, 2018年2月23日 上午10:09:04, zhengby
	 */
	public void updateMateria(Work order) throws Exception;

	/**
	 * <pre>
	 * 更新工序信息
	 * </pre>
	 * @param order
	 * @throws Exception
	 * @since 1.0, 2018年2月23日 上午10:09:15, zhengby
	 */
	public void updateProcedure(Work order) throws Exception;

	/**
	 * <pre>
	 * 查询工单是否被产量上报引用
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:09:27, zhengby
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List> findProductTask(Long id);

	/**
	 * <pre>
	 * 查询订单采购入库记录
	 * </pre>
	 * @param workBillNo
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:09:37, zhengby
	 */
	public Map<String, Object> findPurchStockLog(String workBillNo);

	/**
	 * <pre>
	 * 查询工单所有材料信息
	 * </pre>
	 * @param workId
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:10:07, zhengby
	 */
	SearchResult<Map<String, Object>> findWorkMaterials(Long workId, String companyId);

	/**
	 * <pre>
	 * 查询销售订单的工单明细
	 * </pre>
	 * @param saleBillId
	 * @param productCode
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:10:16, zhengby
	 */
	SearchResult<WorkProduct> findSaleProductByCondition(Long saleBillId, String productCode);

	// ==================== 新规范 - 代码重构 ====================

	/**
	 * <pre>
	 * 根据sourceDetailId计算工单产品已入库数量
	 * </pre>
	 * @param sourceDetailId
	 * @return
	 * @since 1.0, 2018年1月22日 上午11:03:22, think
	 */
	public Integer countInStockQty(long sourceDetailId);

	/**
	 * <pre>
	 * 查询所有工单产品信息
	 * </pre>
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年1月22日 上午10:00:42, think
	 */
	public SearchResult<WorkProduct> findAllProduct(BoolValue isCheck);

	/**
	 * <pre>
	 * 查询所有已审核的工单产品信息
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月22日 上午9:39:19, think
	 */
	public SearchResult<WorkProduct> findAllProduct();

	/**
	 * <pre>
	 * 查询所有已审核的工单产品信息，并以Map<workId, List<WorkProduct>>返回
	 * </pre>
	 * @return
	 * @since 1.0, 2018年4月16日 下午3:10:00, think
	 */
	public Map<Long, List<WorkProduct>> findAllProductForMap();
	
	/**
	 * <pre>
	 * 设置工艺信息和材料信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年10月8日 下午3:45:22, zhengxchn@163.com
	 */
	public void setProceduresMaterials(OutSourceProcessDetail detail);

}
