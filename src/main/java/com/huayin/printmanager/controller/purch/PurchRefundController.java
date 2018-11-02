/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.purch;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchRefund;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.persist.enumerate.WarehouseType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月14日, liudong
 * @version 	   2.0, 2018年2月23日下午2:22:08, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/refund")
public class PurchRefundController extends BaseController
{
	/**
	 * <pre>
	 * 功能 - 返回采购退货单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:23:35, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:refund:list")
	public PurchRefund view(@PathVariable Long id)
	{
		PurchRefund purchRefund = serviceFactory.getPurReturnService().get(id);

		// 查询所有工单WorkProduct，并设置到退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
		{
			if (null != purchRefundDetail.getWorkId())
			{
				long workId = purchRefundDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchRefundDetail.setProductList(list);
				}
			}
		}

		return purchRefund;
	}

	/**
	 * <pre>
	 * 功能 - 根据采购退货单号查找采购退货单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:24:23, zhengby
	 */
	@RequestMapping(value = "toViewAjax/{billNo}")
	@ResponseBody
	@RequiresPermissions("purch:refund:list")
	public PurchRefund toView(@PathVariable String billNo)
	{
		PurchRefund pr = serviceFactory.getPurReturnService().get(billNo);
		return serviceFactory.getPurReturnService().get(pr.getId());
	}

	/**
	 * <pre>
	 * 功能 - 返回打印采购退货单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:24:56, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:refund:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			PurchRefund order = serviceFactory.getPurReturnService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
			map.put("printDate", DateUtils.formatDateTime(new Date()));
		}
		catch (Exception e)
		{
			logger.error("查询采购对账打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到采购退货单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:25:39, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("purch:refund:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		PurchRefund purchRefund = serviceFactory.getPurReturnService().get(id);

		// 查询所有工单WorkProduct，并设置到退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchRefundDetail purchRefundDetail : purchRefund.getDetailList())
		{
			if (null != purchRefundDetail.getWorkId())
			{
				long workId = purchRefundDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchRefundDetail.setProductList(list);
				}
			}
		}

		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().findByType(WarehouseType.MATERIAL);
		map.put("warehouseList", warehouseList);
		map.put("purchRefund", purchRefund);
		return "purch/refund/edit";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到新增采购订单信息
	 * </pre>
	 * @param supplierId
	 * @param stockIds
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:26:11, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("purch:refund:create")
	public String create(Long supplierId, Long[] stockIds, ModelMap map)
	{
		// 供应商
		Supplier supplier = serviceFactory.getSupplierService().get(supplierId);

		// 采购库存
		List<PurchStockDetail> detailList = Lists.newArrayList();
		if (null != stockIds)
		{
			for (Long id : stockIds)
			{
				PurchStockDetail detail = serviceFactory.getPurStockService().getDetailMaster(id);

				// 采购退回 >【入库数量 大于 已退货数量】 并且 【入库数量 大于 已对账数量】
				if (detail.getQty().compareTo(detail.getRefundQty()) != 1 || detail.getQty().compareTo(detail.getReconcilQty()) != 1)
					continue;

				detailList.add(detail);
			}
		}

		if (detailList.size() > 0)
		{
			// 查询所有工单WorkProduct，并设置到库存明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (PurchStockDetail purchStockDetail : detailList)
			{
				if (null != purchStockDetail.getWorkId())
				{
					long workId = purchStockDetail.getWorkId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						purchStockDetail.setProductList(list);
						// 这里用于JsonUtils.toJson(detailList)必须加一个字段
						purchStockDetail.setProductNames(purchStockDetail.getProductNames());
					}
				}
			}

			map.put("detailList", detailList);
			map.put("supplier", supplier);
			map.put("detailListJson", JsonUtils.toJson(detailList));
			map.put("supplierJson", JsonUtils.toJson(supplier));
		}

		return "purch/refund/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存采购退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:26:49, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("purch:refund:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购退货", Operation = Operation.ADD)
	public ServiceResult<PurchRefund> save(@RequestBody PurchRefund order, HttpServletRequest request)
	{
		ServiceResult<PurchRefund> result = serviceFactory.getPurReturnService().save(order);
		request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 更新采购退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:27:10, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("purch:refund:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购退货", Operation = Operation.UPDATE)
	public ServiceResult<PurchRefund> update(@RequestBody PurchRefund order, HttpServletRequest request)
	{
		ServiceResult<PurchRefund> result = serviceFactory.getPurReturnService().update(order);
		request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核采购退货单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:27:38, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@RequiresPermissions("purch:refund:audit")
	@ResponseBody
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getPurReturnService().check(id, BoolValue.NO);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制审核采购退货单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午11:19:30, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@RequiresPermissions("purch:refund:audit")
	@ResponseBody
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getPurReturnService().check(id, BoolValue.YES);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 反审采购退货单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@RequiresPermissions("purch:refund:audit_cancel")
	@ResponseBody
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		Boolean checkBack = serviceFactory.getPurReturnService().checkBack(id);
		if (checkBack)
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> pkMap = serviceFactory.getCommonService().findRefBillNo(BillType.PURCH_PR, id);
			return returnErrorBody(pkMap);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:28:46, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("purch:refund:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchRefund.class : PurchRefundDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 取消强制完工:
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("purch:refund:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchRefund.class : PurchRefundDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delete/{id}")
	@RequiresPermissions("purch:refund:del")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购退货", Operation = Operation.DELETE)
	public AjaxResponseBody delete(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		PurchRefund order = serviceFactory.getPurReturnService().get(id);
		if (serviceFactory.getPurReturnService().delete(id))
		{
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		else
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 跳转到查看列表页面
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("purch:refund:list")
	public String list()
	{
		return "purch/refund/list";
	}

	/**
	 * <pre>
	 * 单据列表AJAX请求
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("purch:refund:list")
	@ResponseBody
	public SearchResult<PurchRefund> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchRefund> result = serviceFactory.getPurReturnService().findByCondition(queryParam);
		PurchRefund refund = new PurchRefund();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (PurchRefund purchRefund : result.getResult())
		{
			totalMoney = totalMoney.add(purchRefund.getTotalMoney());
			totalTax = totalTax.add(purchRefund.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(purchRefund.getNoTaxTotalMoney());
		}
		refund.setTotalMoney(totalMoney);
		refund.setTotalTax(totalTax);
		refund.setNoTaxTotalMoney(noTaxTotalMoney);
		refund.setIsCheck(null);
		refund.setCreateTime(null);
		result.getResult().add(refund);
		return result;
	}

	/**
	 * <pre>
	 * 跳转到明细页面
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("purch:refund_detail:list")
	public String detail()
	{
		return "purch/refund/detail";
	}

	/**
	 * <pre>
	 * 详情表AJAX请求
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	@RequiresPermissions("purch:refund_detail:list")
	public SearchResult<PurchRefundDetail> detailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchRefundDetail> result = serviceFactory.getPurReturnService().findDetailByCondition(queryParam);

		// 查询所有工单WorkProduct，并设置到退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchRefundDetail purchRefundDetail : result.getResult())
		{
			if (null != purchRefundDetail.getWorkId())
			{
				long workId = purchRefundDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchRefundDetail.setProductList(list);
				}
			}
		}

		PurchRefundDetail detail = new PurchRefundDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		BigDecimal valuationPrice = new BigDecimal(0);
		BigDecimal valuationQty = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (PurchRefundDetail purchRefundDetail : result.getResult())
		{
			money = money.add(purchRefundDetail.getMoney());
			tax = tax.add(purchRefundDetail.getTax());
			noTaxMoney = noTaxMoney.add(purchRefundDetail.getNoTaxMoney());
			reconcilQty = reconcilQty.add(purchRefundDetail.getReconcilQty());
			valuationPrice = valuationPrice.add(purchRefundDetail.getValuationPrice());
			valuationQty = valuationQty.add(purchRefundDetail.getValuationQty());
			totalQty = totalQty.add(purchRefundDetail.getQty());
		}
		PurchRefund refund = new PurchRefund();
		detail.setMaster(refund);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setReconcilQty(reconcilQty);
		detail.setValuationPrice(valuationPrice);
		detail.setValuationQty(valuationQty);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 来源采购入库
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "quick_select")
	public String quick_select(Long supplierId, ModelMap map)
	{
		map.put("supplierId", supplierId);
		return "purch/refund/quick_select";
	}

	/**
	 * <pre>
	 * 来源采购入库 数据
	 * </pre>
	 * @return
	 */
	@RequestMapping(value = "ajaxSource")
	@ResponseBody
	public SearchResult<PurchStockDetail> source(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchStockDetail> result = serviceFactory.getPurStockService().findRefundSource(queryParam.getDateMin(), queryParam.getDateMax(), queryParam.getSupplierId(), queryParam.getBillNo(), queryParam.getPageSize(), queryParam.getPageNumber(), queryParam.getAuditFlag());

		// 查询所有工单WorkProduct，并设置到库存明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchStockDetail purchStockDetail : result.getResult())
		{
			if (null != purchStockDetail.getWorkId())
			{
				long workId = purchStockDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchStockDetail.setProductList(list);
				}
			}
		}

		return result;
	}

}
