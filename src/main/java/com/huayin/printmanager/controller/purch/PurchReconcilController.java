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

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchRefundDetail;
import com.huayin.printmanager.persist.entity.purch.PurchStockDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 采购管理 - 采购对账
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月6日, raintear
 * @version 	   2.0, 2018年2月23日上午11:43:00, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/purch/reconcil")
public class PurchReconcilController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 未对账转对账单
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:45:05, zhengby
	 */
	@RequestMapping(value = "toReconcil")
	@RequiresPermissions("purch:reconcil:create")
	public String toReconcil(HttpServletRequest request, ModelMap map, String[] ids)
	{
		PurchReconcil purchReconcil = serviceFactory.getPurReconcilService().findByCheckbox(ids);
		if (purchReconcil.getDetailList().size() > 0)
		{
			// 查询所有工单WorkProduct，并设置到对账明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (PurchReconcilDetail purchReconcilDetail : purchReconcil.getDetailList())
			{
				if (null != purchReconcilDetail.getWorkId())
				{
					long workId = purchReconcilDetail.getWorkId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						purchReconcilDetail.setProductList(list);
					}
				}
			}

			// 供应商地址
			List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(purchReconcil.getSupplierId());

			map.put("purchReconcil", purchReconcil);
			map.put("supplierAddressList", supplierAddressList);
		}
		return "purch/reconcil/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存采购对账单
	 * </pre>
	 * @param purchReconcil
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:45:49, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购对账", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody PurchReconcil purchReconcil, HttpServletRequest request)
	{
		try
		{
			List<Long> purchStockIds = Lists.newArrayList();
			List<Long> purchRefundIds = Lists.newArrayList();
			// 保存前校验是否库存量>已对账量
			for (PurchReconcilDetail pd : purchReconcil.getDetailList())
			{
				if (BillType.PURCH_PN == pd.getSourceBillType())
				{
					purchStockIds.add(pd.getSourceDetailId());
				}
				else if (BillType.PURCH_PR == pd.getSourceBillType())
				{
					purchRefundIds.add(pd.getSourceDetailId());
				}
			}
			// 查询库存量尚有未对账的记录
			List<PurchStockDetail> list1 = serviceFactory.getPurReconcilService().findStockQtyReconcil(purchStockIds);
			// 查询退货量尚有未对账的记录
			List<PurchRefundDetail> list2 = serviceFactory.getPurReconcilService().findRefundQtyReconcil(purchRefundIds);

			if ((list1.size() == 0 && purchStockIds.size() > 0) || (list2.size() == 0 && purchRefundIds.size() > 0))
			{
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("单据已生成，无需重复操作");
			}

			purchReconcil = serviceFactory.getPurReconcilService().save(purchReconcil);
			request.setAttribute(SystemLogAspect.BILLNO, purchReconcil.getBillNo());
			return returnSuccessBody(purchReconcil);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "PurchReconcil:" + JsonUtils.toJson(purchReconcil));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 更新采购对账单
	 * </pre>
	 * @param purchReconcil
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:46:18, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购对账", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody PurchReconcil purchReconcil, HttpServletRequest request)
	{
		try
		{
			purchReconcil = serviceFactory.getPurReconcilService().update(purchReconcil);
			request.setAttribute(SystemLogAspect.BILLNO, purchReconcil.getBillNo());
      return returnSuccessBody(purchReconcil);
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
	 * 功能  - 查看采购对账单 
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:46:47, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:list")
	public PurchReconcil view(@PathVariable Long id)
	{
		PurchReconcil purchReconcil = serviceFactory.getPurReconcilService().get(id);

		// 查询所有工单WorkProduct，并设置到对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchReconcilDetail purchReconcilDetail : purchReconcil.getDetailList())
		{
			if (null != purchReconcilDetail.getWorkId())
			{
				long workId = purchReconcilDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchReconcilDetail.setProductList(list);
				}
			}
		}

		return purchReconcil;
	}

	/**
	 * <pre>
	 * 功能 - 返回打印采购对账单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:47:15, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			PurchReconcil reconcil = serviceFactory.getPurReconcilService().get(id);
			map = ObjectUtils.objectToMap(reconcil);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			logger.error("查询采购对账打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 采购对账编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:47:47, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("purch:reconcil:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		PurchReconcil purchReconcil = serviceFactory.getPurReconcilService().get(id);

		// 查询所有工单WorkProduct，并设置到对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchReconcilDetail purchReconcilDetail : purchReconcil.getDetailList())
		{
			if (null != purchReconcilDetail.getWorkId())
			{
				long workId = purchReconcilDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchReconcilDetail.setProductList(list);
				}
			}
		}

		// 供应商地址
		List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(purchReconcil.getSupplierId());

		map.put("purchReconcil", purchReconcil);
		map.put("supplierAddressList", supplierAddressList);
		return "purch/reconcil/edit";
	}

	/**
	 * <pre>
	 * 功能 - 强制完工采购对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids 主表ID数组
	 * @param stockIds 到货明细ID数组
	 * @param refundIds 退货明细ID数组
	 * @param flag 完工标记
	 * @return
	 *  @since 1.0, 2018年2月23日 上午11:48:33, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids, @RequestParam(required = false, name = "stockIds[]") Long[] stockIds, @RequestParam(required = false, name = "refundIds[]") Long[] refundIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getPurReconcilService().forceComplete(tableType, ids, stockIds, refundIds, BoolValue.YES))
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
	 * 功能 - 取消强制完工采购对账单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:49:06, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("purch:reconcil:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam(required = false, name = "ids[]") Long[] ids, @RequestParam(required = false, name = "stockIds[]") Long[] stockIds, @RequestParam(required = false, name = "refundIds[]") Long[] refundIds)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getPurReconcilService().forceComplete(tableType, ids, stockIds, refundIds, BoolValue.NO))
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
	 * 功能 - 审核采购对账单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:49:31, zhengby
	 */
	@RequestMapping(value = "check/{id}")
	@RequiresPermissions("purch:reconcil:audit")
	@ResponseBody
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.PURCH_PK, id, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("审核失败");
		}

	}

	/**
	 * <pre>
	 * 功能 - 反审核采购对账单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:49:57, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "checkBack/{id}")
	@RequiresPermissions("purch:reconcil:audit_cancel")
	@ResponseBody
	public AjaxResponseBody refundCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.PURCH_PK, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> pkMap = serviceFactory.getCommonService().findRefBillNo(BillType.PURCH_PK, id);
			return returnErrorBody(pkMap);
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除采购对账单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:50:21, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@RequiresPermissions("purch:reconcil:del")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "采购对账", Operation = Operation.DELETE)
	public AjaxResponseBody delete(HttpServletRequest request, @PathVariable Long id)
	{
		PurchReconcil order = serviceFactory.getPurReconcilService().get(id);
		if (serviceFactory.getPurReconcilService().delete(id))
		{
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		else
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody("失败");
		}
	}

	/**
	 * <pre>
	 * 页面 - 采购对账单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:50:42, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("purch:reconcil:list")
	public String list()
	{
		return "purch/reconcil/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购对账单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:51:19, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("purch:reconcil:list")
	@ResponseBody
	public SearchResult<PurchReconcil> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchReconcil> result = serviceFactory.getPurReconcilService().findByCondition(queryParam);

		PurchReconcil reconcil = new PurchReconcil();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (PurchReconcil purchReconcil : result.getResult())
		{
			totalMoney = totalMoney.add(purchReconcil.getTotalMoney());
			totalTax = totalTax.add(purchReconcil.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(purchReconcil.getNoTaxTotalMoney());
		}
		reconcil.setTotalMoney(totalMoney);
		reconcil.setTotalTax(totalTax);
		reconcil.setNoTaxTotalMoney(noTaxTotalMoney);
		reconcil.setIsCheck(null);
		reconcil.setCreateTime(null);
		reconcil.setReconcilTime(null);
		result.getResult().add(reconcil);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 采购对账单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:51:48, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("purch:reconcil_detail:list")
	public String detail()
	{
		return "purch/reconcil/detail";
	}

	/**
	 * <pre>
	 * 功能 - 返回采购对账单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午11:52:41, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("purch:reconcil_detail:list")
	@ResponseBody
	public SearchResult<PurchReconcilDetail> detail(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchReconcilDetail> result = serviceFactory.getPurReconcilService().findDetailByCondition(queryParam);

		// 查询所有工单WorkProduct，并设置到对账明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (PurchReconcilDetail purchReconcilDetail : result.getResult())
		{
			if (null != purchReconcilDetail.getWorkId())
			{
				long workId = purchReconcilDetail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					purchReconcilDetail.setProductList(list);
				}
			}
		}

		PurchReconcilDetail detail = new PurchReconcilDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal valuationPrice = new BigDecimal(0);
		BigDecimal valuationQty = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		BigDecimal totalPaymentMoney = new BigDecimal(0);
		for (PurchReconcilDetail purchReconcilDetail : result.getResult())
		{
			money = money.add(purchReconcilDetail.getMoney());
			tax = tax.add(purchReconcilDetail.getTax());
			noTaxMoney = noTaxMoney.add(purchReconcilDetail.getNoTaxMoney());
			valuationPrice = valuationPrice.add(purchReconcilDetail.getValuationPrice());
			valuationQty = valuationQty.add(purchReconcilDetail.getValuationQty());
			totalQty = totalQty.add(purchReconcilDetail.getQty());
			totalPaymentMoney = totalPaymentMoney.add(purchReconcilDetail.getPaymentMoney());
		}
		PurchReconcil reconcil = new PurchReconcil();
		detail.setMaster(reconcil);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setValuationPrice(valuationPrice);
		detail.setValuationQty(valuationQty);
		detail.setQty(totalQty);
		detail.setPaymentMoney(totalPaymentMoney);
		result.getResult().add(detail);
		return result;
	}
}
