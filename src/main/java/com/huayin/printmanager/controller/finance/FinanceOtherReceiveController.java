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
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceOtherReceiveDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 其他收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/otherReceive")
public class FinanceOtherReceiveController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 其他收款单新增
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:42:18, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:otherReceive:create")
	public String create()
	{
		return "finance/otherReceive/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存其他收款单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:42:33, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:create")
	public AjaxResponseBody save(@RequestBody FinanceOtherReceive order, ModelMap map)
	{
		serviceFactory.getOtherReceiveService().save(order);
		return returnSuccessBody(order);

	}

	/**
	 * <pre>
	 * 页面 - 其他收款单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:42:45, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("finance:otherReceive:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceOtherReceive order = null;
		try
		{
			order = serviceFactory.getOtherReceiveService().get(id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		map.put("order", order);
		return "finance/otherReceive/view";
	}

	/**
	 * <pre>
	 * 数据 - 其他收款单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:42:56, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinanceOtherReceive> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceOtherReceive> result = serviceFactory.getOtherReceiveService().findByCondition(queryParam);
		FinanceOtherReceive otherPay = new FinanceOtherReceive();
		BigDecimal money = new BigDecimal(0);
		for (FinanceOtherReceive o : result.getResult())
		{
			money = money.add(o.getMoney());
		}
		otherPay.setMoney(money);
		result.getResult().add(otherPay);
		return result;

	}

	/**
	 * <pre>
	 * 功能 - 删除其他收款单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:43:08, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:delete")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "其他收款单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{
			FinanceOtherReceive order = serviceFactory.getOtherReceiveService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOtherReceiveService().delete(id);
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
	 * 功能 - 审核其他收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:43:19, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getOtherReceiveService().audit(id, BoolValue.YES))
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
	 * 功能 - 反审核其他收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:43:31, think
	 */
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (serviceFactory.getOtherReceiveService().audit(id, BoolValue.NO))
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
	 * 页面 - 其他收款单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:43:44, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("finance:otherReceive:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceOtherReceive order = serviceFactory.getOtherReceiveService().get(id);
		map.put("order", order);
		return "finance/otherReceive/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改其他收款单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:43:56, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "其他收款单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody FinanceOtherReceive order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOtherReceiveService().update(order);
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
	 * 功能 - 打印其他收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:07, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("finance:otherReceive:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			FinanceOtherReceive order = serviceFactory.getOtherReceiveService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			BigDecimal totalMoney = new BigDecimal(0);
			for (FinanceOtherReceiveDetail detail : order.getDetailList())
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
	 * 页面 - 其他收款单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:16, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:otherReceive:detail_list")
	public String detailList()
	{
		return "finance/otherReceive/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 其他收款单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午1:44:27, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinanceOtherReceiveDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceOtherReceiveDetail> result = serviceFactory.getOtherReceiveService().findDetailByCondition(queryParam);
		FinanceOtherReceiveDetail detail = new FinanceOtherReceiveDetail();

		BigDecimal money = new BigDecimal(0);
		for (FinanceOtherReceiveDetail _detail : result.getResult())
		{
			money = money.add(_detail.getMoney());
		}
		FinanceOtherReceive rec = new FinanceOtherReceive();
		detail.setMaster(rec);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}
}
