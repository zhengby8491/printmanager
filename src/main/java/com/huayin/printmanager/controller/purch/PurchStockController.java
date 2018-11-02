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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStock;
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
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购入库
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年6月14日, liudong
 * @version 	   2.0, 2018年2月23日下午3:04:07, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/stock")
public class PurchStockController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 采购未入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:04:57, zhengby
	 */
	@RequestMapping(value = "toStock")
	@RequiresPermissions("purch:stock:create")
	public String toStock(HttpServletRequest request, ModelMap map, Long[] ids)
	{
		PurchStock purchStock = serviceFactory.getPurStockService().findListByDetailIds(ids);

		if (purchStock.getDetailList().size() > 0)
		{
			// 查询所有工单WorkProduct，并设置库存明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
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

			// 供应商地址
			List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(purchStock.getSupplierId());

			List<Warehouse> warehouseList = serviceFactory.getWarehouseService().findByType(WarehouseType.MATERIAL);
			map.put("warehouseList", warehouseList);
			map.put("purchStock", purchStock);
			map.put("supplierAddressList", supplierAddressList);
		}
		return "purch/stock/create";
	}

	/**
	 * <pre>
	 * 功能 - 查看采购入库单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:05:23, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:stock:list")
	public PurchStock view(@PathVariable Long id)
	{
		PurchStock purchStock = serviceFactory.getPurStockService().get(id);

		// 查询所有工单WorkProduct，并设置库存明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
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

		return purchStock;
	}

	/**
	 * <pre>
	 * 功能 - 根据入库单查询入库单
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月29日 上午11:47:02, zhengby
	 */
	@RequestMapping(value = "toViewAjax/{billNo}")
	@ResponseBody
	@RequiresPermissions("purch:stock:list")
	public PurchStock toView(@PathVariable String billNo)
	{
		PurchStock ps = serviceFactory.getPurStockService().get(billNo);
		return serviceFactory.getPurStockService().get(ps.getId());
	}

	/**
	 * <pre>
	 * 功能 - 返回采购入库单打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:06:18, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:stock:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			PurchStock purchStock = serviceFactory.getPurStockService().get(id);
			map = ObjectUtils.objectToMap(purchStock);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			logger.error("查询采购入库打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到采购入库单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:07:35, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("purch:stock:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		PurchStock purchStock = serviceFactory.getPurStockService().get(id);

		// 查询所有工单WorkProduct，并设置库存明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchStockDetail purchStockDetail : purchStock.getDetailList())
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

		// 供应商地址
		List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(purchStock.getSupplierId());
		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().findByType(WarehouseType.MATERIAL);
		map.put("warehouseList", warehouseList);
		map.put("purchStock", purchStock);

		map.put("supplierAddressList", supplierAddressList);

		return "purch/stock/edit";
	}

	/**
	 * <pre>
	 * 功能 - 保存采购入库单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:08:15, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("purch:stock:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购入库", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody PurchStock order, HttpServletRequest request)
	{
		// 保存之前验证是否已采购完，前端的已校验过，后台再校验一次，防止保存重复采购单
		for (PurchStockDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			PurchOrderDetail source = serviceFactory.getPurOrderService().getDetail(detail.getSourceDetailId());
			if (source.getStorageQty().compareTo(source.getQty()) != -1)
			{
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		try
		{
			order = serviceFactory.getPurStockService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "PurchStock:" + JsonUtils.toJson(order));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 更新采购入库单
	 * </pre>
	 * @param purchStock
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:08:55, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("purch:stock:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购入库", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody PurchStock purchStock, HttpServletRequest request)
	{
		try
		{
			purchStock = serviceFactory.getPurStockService().update(purchStock);
			request.setAttribute(SystemLogAspect.BILLNO, purchStock.getBillNo());
			PurchStock new_purchStock = serviceFactory.getPurStockService().get(purchStock.getId());
			return returnSuccessBody(new_purchStock);
		}
		catch (BusinessException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除采购入库单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:09:19, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("purch:stock:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购入库", Operation = Operation.DELETE)
	public AjaxResponseBody delete(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		PurchStock order = serviceFactory.getPurStockService().get(id);
		if (serviceFactory.getPurStockService().delete(id))
		{
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		else
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody("删除失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 查退货单此材料的退货数量
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:10:03, zhengby
	 */
	@RequestMapping(value = "findRefundQty/{id}")
	@ResponseBody
	public AjaxResponseBody findRefundQty(HttpServletRequest request, @PathVariable Long id)
	{
		Double refundQty = serviceFactory.getPurStockService().findRefundQty(id);
		return returnSuccessBody(refundQty);

	}

	/**
	 * <pre>
	 * 功能 - 审核采购入库单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:10:23, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@RequiresPermissions("purch:stock:audit")
	@ResponseBody
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getPurStockService().check(id))
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
	 * 功能 - 反审核采购入库单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:10:51, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@RequiresPermissions("purch:stock:audit_cancel")
	@ResponseBody
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getPurStockService().checkBack(id, BoolValue.NO);
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
			Map<String, List> pnMap = serviceFactory.getCommonService().findRefBillNo(BillType.PURCH_PN, id);
			return returnErrorBody(pnMap);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制反审核
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月19日 下午4:20:08, think
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "forceCheckBack/{id}")
	@RequiresPermissions("purch:stock:audit_cancel")
	@ResponseBody
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getPurStockService().checkBack(id, BoolValue.YES);
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
			Map<String, List> pnMap = serviceFactory.getCommonService().findRefBillNo(BillType.PURCH_PN, id);
			return returnErrorBody(pnMap);
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制完工采购入库单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:11:28, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("purch:stock:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchStock.class : PurchOrderDetail.class;
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
	 * 功能 - 取消强制完工采购入库单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:11:50, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("purch:stock:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchStock.class : PurchOrderDetail.class;
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
	 * 页面 - 跳转到采购入库单查看列表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:12:17, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("purch:stock:list")
	public String list()
	{
		return "purch/stock/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购入库单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:12:48, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("purch:stock:list")
	@ResponseBody
	public SearchResult<PurchStock> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchStock> result = serviceFactory.getPurStockService().findByCondition(queryParam);
		PurchStock stock = new PurchStock();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (PurchStock purchStock : result.getResult())
		{
			totalMoney = totalMoney.add(purchStock.getTotalMoney());
			totalTax = totalTax.add(purchStock.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(purchStock.getNoTaxTotalMoney());
		}
		stock.setTotalMoney(totalMoney);
		stock.setTotalTax(totalTax);
		stock.setNoTaxTotalMoney(noTaxTotalMoney);
		stock.setIsCheck(null);
		stock.setCreateTime(null);
		stock.setStorageTime(null);
		result.getResult().add(stock);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到采购入库单明细查看列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:13:23, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("purch:stock_detail:list")
	public String toDetail()
	{
		return "purch/stock/detail";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购入库单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午3:13:53, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("purch:stock_detail:list")
	@ResponseBody
	public SearchResult<PurchStockDetail> detail(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchStockDetail> result = serviceFactory.getPurStockService().findDetailByCondition(queryParam);

		// 查询所有工单WorkProduct，并设置库存明细中
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

		PurchStockDetail detail = new PurchStockDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal reconcilQty = new BigDecimal(0);
		BigDecimal valuationPrice = new BigDecimal(0);
		BigDecimal valuationQty = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (PurchStockDetail purchStockDetail : result.getResult())
		{
			money = money.add(purchStockDetail.getMoney());
			tax = tax.add(purchStockDetail.getTax());
			noTaxMoney = noTaxMoney.add(purchStockDetail.getNoTaxMoney());
			reconcilQty = reconcilQty.add(purchStockDetail.getReconcilQty());
			valuationPrice = valuationPrice.add(purchStockDetail.getValuationPrice());
			valuationQty = valuationQty.add(purchStockDetail.getValuationQty());
			totalQty = totalQty.add(purchStockDetail.getQty());
		}
		PurchStock stock = new PurchStock();
		detail.setMaster(stock);
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

}
