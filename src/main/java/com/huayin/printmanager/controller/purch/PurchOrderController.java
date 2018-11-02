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
import java.util.ArrayList;
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
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchOrder;
import com.huayin.printmanager.persist.entity.purch.PurchOrderDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购订单
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年6月14日, liudong
 * @version 	   2.0, 2018年2月23日上午11:07:41, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/order")
public class PurchOrderController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 采购订单复制
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年1月23日 下午1:39:30, think
	 */
	@RequestMapping(value = "copy/{id}")
	@RequiresPermissions("purch:order:create")
	public String copy(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		PurchOrder order = serviceFactory.getPurOrderService().get(id);
		map.put("detailList", order.getDetailList());
		map.put("order", order);
		map.put("isCopy", true);
		return "purch/order/create";
	}

	/**
	 * <pre>
	 * 页面 - 工单转采购
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:09:21, zhengby
	 */
	@RequestMapping(value = "toPurch")
	@RequiresPermissions("purch:order:create")
	public String toPurch(HttpServletRequest request, ModelMap map, Long[] ids)
	{
		List<PurchOrderDetail> detailList = serviceFactory.getPurOrderService().toPurch(ids);

		// 查询所有工单WorkProduct，并设置到订单明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchOrderDetail purchOrderDetail : detailList)
		{
			if (null != purchOrderDetail.getSourceId())
			{
				long workId = purchOrderDetail.getSourceId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchOrderDetail.setProductList(list);
				}
			}
		}

		map.put("detailList", detailList);
		return "purch/order/create";
	}

	/**
	 * <pre>
	 * 页面 - 库存预警转采购订单
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:09:56, zhengby
	 */
	@RequestMapping(value = "warnToPurch")
	@RequiresPermissions("purch:order:create")
	public String warnToPurch(HttpServletRequest request, ModelMap map, Long[] ids)
	{
		List<PurchOrderDetail> detailList = serviceFactory.getPurOrderService().warnToPurch(ids);
		map.put("detailList", detailList);
		return "purch/order/create";

	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:10:25, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:order:list")
	public PurchOrder viewAjax(@PathVariable Long id)
	{
		PurchOrder purchOrder = serviceFactory.getPurOrderService().get(id);

		// 查询所有工单WorkProduct，并设置到订单明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
		{
			if (null != purchOrderDetail.getSourceId())
			{
				long workId = purchOrderDetail.getSourceId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchOrderDetail.setProductList(list);
				}
			}
		}

		return purchOrder;
	}

	/**
	 * <pre>
	 * 功能 - 根据采购订单号跳转到查看采购订单信息
	 * </pre>
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月28日 下午7:30:25, zhengby
	 */
	@RequestMapping(value = "toViewAjax/{billNo}")
	@ResponseBody
	@RequiresPermissions("purch:order:list")
	public PurchOrder toViewAjax(@PathVariable String billNo)
	{
		PurchOrder purchOrder = serviceFactory.getPurOrderService().getOrderDetail(billNo);

		// 查询所有工单WorkProduct，并设置到订单明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
		{
			if (null != purchOrderDetail.getSourceId())
			{
				long workId = purchOrderDetail.getSourceId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchOrderDetail.setProductList(list);
				}
			}
		}

		return purchOrder;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到打印采购订单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:11:41, zhengby
	 */
	@RequestMapping(value = "print/{id}")
	@RequiresPermissions("purch:order:print")
	public String print(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		PurchOrder purchOrder = serviceFactory.getPurOrderService().get(id);
		Supplier supplier = serviceFactory.getSupplierService().get(purchOrder.getSupplierId());
		map.put("order", purchOrder);
		map.put("supplier", supplier);
		return "purch/order/print";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:11:58, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:order:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			PurchOrder purchOrder = serviceFactory.getPurOrderService().get(id);
			map = ObjectUtils.objectToMap(purchOrder);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到编辑采购订单信息
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:12:25, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("purch:order:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		PurchOrder purchOrder = serviceFactory.getPurOrderService().get(id);

		// 查询所有工单WorkProduct，并设置到订单明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchOrderDetail purchOrderDetail : purchOrder.getDetailList())
		{
			if (null != purchOrderDetail.getSourceId())
			{
				long workId = purchOrderDetail.getSourceId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchOrderDetail.setProductList(list);
				}
			}
		}

		// 供应商地址
		List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(purchOrder.getSupplierId());
		map.put("order", purchOrder);
		map.put("supplierAddressList", supplierAddressList);
		return "purch/order/edit";
	}

	/**
	 * <pre>
	 * 页面 -采购订单创建
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:12:44, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("purch:order:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "purch/order/create";
	}

	/**
	 * <pre>
	 * 页面  - 从采购未下单转单创建采购订单
	 * </pre>
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年7月16日 下午5:23:35, zhengby
	 */
	@RequestMapping(value = "createFromExterior")
	@RequiresPermissions("purch:order:create")
	public String createFromExterior(Long[] ids, ModelMap map)
	{
		List<Long> idList = new ArrayList<>();
		for(Long id : ids)
		{
			idList.add(id);
		}
		PurchOrder order = serviceFactory.getExteriorPurchService().createPurchOrder(idList);
		map.put("detailList", order.getDetailList());
		map.put("order", order);
		map.put("isCopy", false);
		return "purch/order/create";
	}
	
	/**
	 * <pre>
	 * 功能 - 保存采购订单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:13:16, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购订单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody PurchOrder order, HttpServletRequest request)
	{
		// 保存之前验证是否已采购完，前端的已校验过，后台再校验一次，防止保存重复采购单
		for (PurchOrderDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			WorkMaterial workMaterial = serviceFactory.getWorkService().getMaterial(detail.getSourceDetailId());
			if (workMaterial.getPurchQty().compareTo(workMaterial.getQty()) != -1 )
			{
				// 已采购数量 >= 需采购数量
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		try
		{
			order = serviceFactory.getPurOrderService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception e)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			e.printStackTrace();
			logger.error(e.getMessage(), "PurchOrder:" + JsonUtils.toJson(order));
			return returnSuccessBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 更新采购订单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:13:35, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购订单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody PurchOrder order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getPurOrderService().update(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			PurchOrder newOrder = serviceFactory.getPurOrderService().get(order.getId());
			return returnSuccessBody(newOrder);
		}
		catch (BusinessException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			e.printStackTrace();
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除采购订单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:13:59, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@RequiresPermissions("purch:order:del")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购订单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		PurchOrder order = serviceFactory.getPurOrderService().get(id);
		if (serviceFactory.getPurOrderService().delete(id))
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
	 * 功能 - 审核采购订单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:14:15, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("purch:order:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getCommonService().audit(BillType.PURCH_PO, id, BoolValue.YES))
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
	 * 功能 - 反审采购订单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:14:27, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("purch:order:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getCommonService().audit(BillType.PURCH_PO, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> poMap = serviceFactory.getCommonService().findRefBillNo(BillType.PURCH_PO, id);
			return returnErrorBody(poMap);
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核所有采购订单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:15:00, zhengby
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("purch:order:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getPurOrderService().checkAll())
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
	 * 功能 - 强制完工采购订单
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:15:26, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("produce:work:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getCommonService().forceComplete(PurchOrder.class, ids, BoolValue.YES))
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
	 * 功能 - 取消强制完工采购订单
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:15:54, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("produce:work:complete_cancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getCommonService().forceComplete(PurchOrder.class, ids, BoolValue.NO))
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
	 * 功能 - 强制完工采购订单明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:16:20, zhengby
	 */
	@RequestMapping(value = "forceCompleteYes")
	@RequiresPermissions("produce:work:complete")
	@ResponseBody
	public AjaxResponseBody forceCompleteYes(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getPurOrderService().forceComplete(ids, BoolValue.YES))
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
	 * 功能 - 取消强制完工采购订单明细
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:17:38, zhengby
	 */
	@RequestMapping(value = "forceCompleteNo")
	@RequiresPermissions("produce:work:complete_cancel")
	@ResponseBody
	public AjaxResponseBody forceCompleteNo(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getPurOrderService().forceComplete(ids, BoolValue.NO))
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
	 * 页面 - 跳转到采购订单列表页面
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:18:13, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("purch:order:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "purch/order/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:18:39, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("purch:order:list")
	@ResponseBody
	public SearchResult<PurchOrder> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchOrder> result = serviceFactory.getPurOrderService().findByCondition(queryParam);
		PurchOrder order = new PurchOrder();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (PurchOrder purchOrder : result.getResult())
		{
			totalMoney = totalMoney.add(purchOrder.getTotalMoney());
			totalTax = totalTax.add(purchOrder.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(purchOrder.getNoTaxTotalMoney());
		}
		order.setTotalMoney(totalMoney);
		order.setTotalTax(totalTax);
		order.setNoTaxTotalMoney(noTaxTotalMoney);
		order.setIsCheck(null);
		order.setCreateTime(null);
		order.setPurchTime(null);
		result.getResult().add(order);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到采购订单明细列表查看页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:19:10, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("purch:order_detail:list")
	public String detailList()
	{
		return "purch/order/detail";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购订单明细表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:19:46, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("purch:order_detail:list")
	@ResponseBody
	public SearchResult<PurchOrderDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		try
		{
			SearchResult<PurchOrderDetail> result = serviceFactory.getPurOrderService().findDetailByCondition(queryParam);

			// 查询所有工单WorkProduct，并设置到订单明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (PurchOrderDetail purchOrderDetail : result.getResult())
			{
				if (null != purchOrderDetail.getSourceId())
				{
					long workId = purchOrderDetail.getSourceId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						purchOrderDetail.setProductList(list);
					}
				}
			}

			PurchOrderDetail detail = new PurchOrderDetail();
			BigDecimal money = new BigDecimal(0);
			BigDecimal tax = new BigDecimal(0);
			BigDecimal noTaxMoney = new BigDecimal(0);
			BigDecimal storageQty = new BigDecimal(0);
			BigDecimal price = new BigDecimal(0);
			BigDecimal totalQty = new BigDecimal(0);
			BigDecimal valuationQty = new BigDecimal(0);
			BigDecimal valuationPrice = new BigDecimal(0);
			for (PurchOrderDetail purchOrderDetail : result.getResult())
			{
				money = money.add(purchOrderDetail.getMoney());
				tax = tax.add(purchOrderDetail.getTax());
				noTaxMoney = noTaxMoney.add(purchOrderDetail.getNoTaxMoney());
				storageQty = storageQty.add(purchOrderDetail.getStorageQty());
				price = price.add(purchOrderDetail.getPrice());
				totalQty = totalQty.add(purchOrderDetail.getQty());
				valuationQty = valuationQty.add(purchOrderDetail.getValuationQty());
				valuationPrice = valuationPrice.add(purchOrderDetail.getValuationPrice());
			}
			PurchOrder order = new PurchOrder();
			detail.setMaster(order);
			detail.setMoney(money);
			detail.setTax(tax);
			detail.setNoTaxMoney(noTaxMoney);
			detail.setStorageQty(storageQty);
			detail.setValuationQty(valuationQty);
			detail.setValuationPrice(valuationPrice);
			detail.setPrice(price);
			detail.setQty(totalQty);
			result.getResult().add(detail);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <pre>
	 * 页面 - 单价变更
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年4月12日 上午9:25:08, think
	 */
	@RequestMapping(value = "editPrice/{id}")
	public String editPrice(@PathVariable Long id, ModelMap map)
	{
		PurchOrderDetail purchOrderDetail = serviceFactory.getPurOrderService().getDetail(id);
		map.put("purchOrderDetail", purchOrderDetail);
		return "purch/order/editPrice";
	}

	/**
	 * <pre>
	 * 功能 - 单价变更
	 * </pre>
	 * @param purchOrderDetail
	 * @return
	 * @since 1.0, 2018年4月12日 上午9:27:10, think
	 */
	@RequestMapping(value = "changePrice")
	@RequiresPermissions("purch:order:changePrice")
	@ResponseBody
	public AjaxResponseBody changePrice(@RequestBody PurchOrderDetail purchOrderDetail)
	{
		try
		{
			serviceFactory.getPurOrderService().changePrice(purchOrderDetail);
			return returnSuccessBody();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
