/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.finance;

import java.math.BigDecimal;
import java.util.Arrays;
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
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinancePayment;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/payment")
public class FinancePaymentController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 付款单新增
	 * </pre>
	 * @param map
	 * @param request
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:47:56, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:payment:create")
	public String create(ModelMap map, HttpServletRequest request, Long[] ids, BillType billType)
	{
		if (ids == null)
		{
			return "finance/payment/create";
		}
		QueryParam queryParam = new QueryParam();
		queryParam.setIds(Arrays.asList(ids));
		Long supplierId = null;
		if (BillType.PURCH_PK == billType) // 采购对账
		{
			SearchResult<PurchReconcilDetail> result = serviceFactory.getPaymentService().findPurchShouldPayment(queryParam);
			if (result.getResult().size() > 0)
			{
				for (PurchReconcilDetail detail : result.getResult())
				{
					detail.getMaster().setBillTypeText(detail.getMaster().getBillTypeText());
				}

				List<PurchReconcilDetail> list = Lists.newArrayList();
				// 将本次应付账款为负数的排在最前
				for (PurchReconcilDetail detail : result.getResult())
				{
					if (detail.getMoney().compareTo(detail.getPaymentMoney()) == -1)
					{
						list.add(detail);
					}
				}
				// 将本次应付账款为正数的排在最后
				for (PurchReconcilDetail detail_ : result.getResult())
				{
					if (detail_.getMoney().compareTo(detail_.getPaymentMoney()) != -1)
					{
						list.add(detail_);
					}
				}
				map.put("detail", list);
				supplierId = result.getResult().get(0).getMaster().getSupplierId();
			}
		}
		else if (BillType.OUTSOURCE_OC == billType) // 发外对账单
		{
			SearchResult<OutSourceReconcilDetail> result = serviceFactory.getPaymentService().findOutSourceShouldPayment(queryParam);
			if (result.getResult().size() > 0)
			{
				for (OutSourceReconcilDetail detail : result.getResult())
				{
					detail.getMaster().setBillTypeText(detail.getMaster().getBillTypeText());
				}
				List<OutSourceReconcilDetail> list = Lists.newArrayList();
				// 将本次应付账款为负数的排在最前
				for (OutSourceReconcilDetail detail : result.getResult())
				{
					if (detail.getMoney().compareTo(detail.getPaymentMoney()) == -1)
					{
						list.add(detail);
					}
				}
				// 将本次应付账款为正数的排在最后
				for (OutSourceReconcilDetail detail_ : result.getResult())
				{
					if (detail_.getMoney().compareTo(detail_.getPaymentMoney()) != -1)
					{
						list.add(detail_);
					}
				}
				map.put("detail", list);
				supplierId = result.getResult().get(0).getMaster().getSupplierId();
			}
		}
		Supplier supplier = serviceFactory.getSupplierService().get(supplierId);
		map.put("supplier", supplier);
		return "finance/payment/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存付款单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:48:07, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:payment:create")
	public AjaxResponseBody save(@RequestBody FinancePayment order, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		for (FinancePaymentDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.OUTSOURCE_OC)
			{
				OutSourceReconcilDetail source = serviceFactory.getOutSourceReconcilService().getDetail(detail.getSourceDetailId());
				if (source.getPaymentMoney().abs().compareTo(source.getMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			else if (detail.getSourceBillType() == BillType.PURCH_PK)
			{
				PurchReconcilDetail source = serviceFactory.getPurReconcilService().getDetail(detail.getSourceDetailId());
				if (source.getPaymentMoney().abs().compareTo(source.getMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			else if (detail.getSourceBillType() == BillType.BEGIN_SUPPLIER)
			{
				SupplierBeginDetail source = serviceFactory.getSupplierBeginService().getDetail(detail.getSourceId()).get(0);
				if (source.getPaymentedMoney().abs().compareTo(source.getPaymentMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		serviceFactory.getPaymentService().save(order);
		return returnSuccessBody(order);
	}

	/**
	 * <pre>
	 * 页面 - 付款单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:48:20, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("finance:payment:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinancePayment order = serviceFactory.getPaymentService().get(id);
		map.put("order", order);
		return "finance/payment/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:48:31, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("finance:payment:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			FinancePayment order = serviceFactory.getPaymentService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			BigDecimal totalMoney = new BigDecimal(0);
			for (FinancePaymentDetail detail : order.getDetailList())
			{
				totalMoney = detail.getMoney().add(totalMoney);
			}
			map.put("totalMoney", totalMoney);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 审核付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:48:42, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:payment:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getPaymentService().audit(id, BoolValue.YES))
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
	 * 功能 - 反审核付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:48:58, think
	 */
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:payment:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		FinancePayment order = serviceFactory.getPaymentService().get(id);
		if (order.getIsCancel() == BoolValue.YES)
		{// 作废状态下不能反审核
			return returnErrorBody("反审核失败：单据已作废，不能反审核");
		}
		if (serviceFactory.getPaymentService().audit(id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("反审核失败：已被下游单据引用");
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核所有付款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:49:12, think
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("finance:payment:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getPaymentService().checkAll())
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
	 * 功能 - 强制完工付款单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:49:23, think
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("finance:payment:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinancePayment.class : FinancePaymentDetail.class;
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
	 * 功能 - 取消强制完工付款单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:49:39, think
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:payment:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinancePayment.class : FinancePaymentDetail.class;
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
	 * 功能 - 作废付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:49:59, think
	 */
	@RequestMapping(value = "cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:payment:cancel")
	public AjaxResponseBody cancel(@PathVariable Long id)
	{
		if (serviceFactory.getPaymentService().cancel(id))
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
	 * 功能 - 反作废付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:50:10, think
	 */
	@RequestMapping(value = "cancel_back/{id}")
	@ResponseBody
	@RequiresPermissions("finance:payment:cancel_back")
	public AjaxResponseBody cancelBack(@PathVariable Long id)
	{
		FinancePayment order = serviceFactory.getPaymentService().get(id);
		if (order.getIsCheck() == BoolValue.YES)
		{// 已审核状态下不能反作废
			return returnErrorBody("反作废失败：单据已审核，不能反作废");
		}
		if (serviceFactory.getPaymentService().cancelBack(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("反作废失败");
		}
	}

	/**
	 * <pre>
	 * 页面 - 付款单列表
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:50:19, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("finance:payment:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "finance/payment/list";
	}

	/**
	 * <pre>
	 * 数据 - 付款单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:50:30, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinancePayment> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinancePayment> result = serviceFactory.getPaymentService().findByCondition(queryParam);
		FinancePayment payment = new FinancePayment();
		BigDecimal discount = new BigDecimal(0);
		BigDecimal advance = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinancePayment _payment : result.getResult())
		{
			discount = discount.add(_payment.getDiscount());
			money = money.add(_payment.getMoney());
			advance = advance.add(_payment.getAdvance());
		}
		payment.setDiscount(discount);
		payment.setMoney(money);
		payment.setAdvance(advance);
		result.getResult().add(payment);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 付款单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:50:42, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:payment:list")
	public String detailList()
	{
		return "finance/payment/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 付款单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:50:58, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinancePaymentDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinancePaymentDetail> result = serviceFactory.getPaymentService().findDetailByCondition(queryParam);
		FinancePaymentDetail detail = new FinancePaymentDetail();

		BigDecimal sourceMoney = new BigDecimal(0);
		BigDecimal sourceBalanceMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinancePaymentDetail paymentDetail : result.getResult())
		{
			sourceMoney = sourceMoney.add(paymentDetail.getSourceMoney());
			money = money.add(paymentDetail.getMoney());
			sourceBalanceMoney = sourceBalanceMoney.add(paymentDetail.getSourceBalanceMoney());
		}
		FinancePayment payment = new FinancePayment();
		detail.setMaster(payment);
		detail.setMoney(money);
		detail.setSourceMoney(sourceMoney);
		detail.setSourceBalanceMoney(sourceBalanceMoney);
		result.getResult().add(detail);
		return result;
	}
}
