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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherPaymentDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 其他付款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/otherPayment")
public class FinanceOtherPaymentController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 其他付款单新增
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:33:42, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:otherPayment:create")
	public String create()
	{

		return "finance/otherPayment/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存其他付款单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:33:54, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:create")
	public AjaxResponseBody save(@RequestBody FinanceOtherPayment order, ModelMap map)
	{
		serviceFactory.getOtherPaymentService().save(order);
		return returnSuccessBody(order);

	}

	/**
	 * <pre>
	 * 页面 - 其他付款单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:34:06, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("finance:otherPayment:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceOtherPayment order = null;
		try
		{
			order = serviceFactory.getOtherPaymentService().get(id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		map.put("order", order);
		return "finance/otherPayment/view";
	}

	/**
	 * <pre>
	 * 数据 - 其他付款单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:34:36, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinanceOtherPayment> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceOtherPayment> result = serviceFactory.getOtherPaymentService().findByCondition(queryParam);
		FinanceOtherPayment otherPay = new FinanceOtherPayment();
		BigDecimal money = new BigDecimal(0);
		for (FinanceOtherPayment o : result.getResult())
		{
			money = money.add(o.getMoney());
		}
		otherPay.setMoney(money);
		result.getResult().add(otherPay);
		return result;

	}

	/**
	 * <pre>
	 * 功能 - 删除其他付款单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:34:26, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:delete")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "其他付款单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{
			FinanceOtherPayment order = serviceFactory.getOtherPaymentService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOtherPaymentService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核其他付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:34:49, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getOtherPaymentService().audit(id, BoolValue.YES))
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
	 * 功能 - 反审核其他付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:00, think
	 */
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (serviceFactory.getOtherPaymentService().audit(id, BoolValue.NO))
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
	 * 页面 - 其他付款单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:11, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("finance:otherPayment:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceOtherPayment order = serviceFactory.getOtherPaymentService().get(id);
		map.put("order", order);
		return "finance/otherPayment/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改其他付款单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:23, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "其他付款单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody FinanceOtherPayment order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOtherPaymentService().update(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order.getId());
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
	 * 功能 - 打印其他付款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:32, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherPayment:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			FinanceOtherPayment order = serviceFactory.getOtherPaymentService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			BigDecimal totalMoney = new BigDecimal(0);
			for (FinanceOtherPaymentDetail detail : order.getDetailList())
			{
				totalMoney = detail.getMoney().add(totalMoney);
			}
			map.put("totalMoney", totalMoney);
			map.put("order", order);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 其他付款单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:42, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:otherPayment:detail_list")
	public String detailList()
	{
		return "finance/otherPayment/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 其他付款单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午11:35:52, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinanceOtherPaymentDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceOtherPaymentDetail> result = serviceFactory.getOtherPaymentService().findDetailByCondition(queryParam);
		FinanceOtherPaymentDetail detail = new FinanceOtherPaymentDetail();

		BigDecimal money = new BigDecimal(0);
		for (FinanceOtherPaymentDetail paymentDetail : result.getResult())
		{
			money = money.add(paymentDetail.getMoney());
		}
		FinanceOtherPayment payment = new FinanceOtherPayment();
		detail.setMaster(payment);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}
}
