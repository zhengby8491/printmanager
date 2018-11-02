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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 预付核销单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/writeoffPayment")
public class FinanceWriteoffPaymentController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 预付核销单新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:22:36, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:writeoffPayment:create")
	public String create(ModelMap map)
	{
		return "finance/writeoffPayment/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存预付核销单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:22:46, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:create")
	public AjaxResponseBody save(@RequestBody FinanceWriteoffPayment order, ModelMap map)
	{
		serviceFactory.getWriteoffPaymentService().save(order);
		return returnSuccessBody(order);
	}

	/**
	 * <pre>
	 * 页面 - 预付核销单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:22:54, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("finance:writeoffPayment:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceWriteoffPayment order = serviceFactory.getWriteoffPaymentService().get(id);
		map.put("order", order);
		return "finance/writeoffPayment/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:23:03, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			FinanceWriteoffPayment order = serviceFactory.getWriteoffPaymentService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			BigDecimal totalMoney = new BigDecimal(0);
			for (FinanceWriteoffPaymentDetail detail : order.getDetailList())
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
	 * 功能 - 审核预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:23:13, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getWriteoffPaymentService().audit(id, BoolValue.YES))
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
	 * 功能 - 反审核预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:23:22, think
	 */
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		FinanceWriteoffPayment order = serviceFactory.getWriteoffPaymentService().get(id);
		if (order.getIsCancel() == BoolValue.YES)
		{// 作废状态下不能反审核
			return returnErrorBody("反审核失败：单据已作废，不能反审核");
		}
		if (serviceFactory.getWriteoffPaymentService().audit(id, BoolValue.NO))
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
	 * 功能 - 强制完工预付核销单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:23:32, think
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinanceWriteoffPayment.class : FinanceWriteoffPaymentDetail.class;
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
	 * 功能 - 取消强制完工预付核销单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:23:47, think
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinanceWriteoffPayment.class : FinanceWriteoffPaymentDetail.class;
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
	 * 功能 - 作废预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:02, think
	 */
	@RequestMapping(value = "cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:cancel")
	public AjaxResponseBody cancel(@PathVariable Long id)
	{
		if (serviceFactory.getWriteoffPaymentService().cancel(id))
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
	 * 功能 - 反作废预付核销单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:11, think
	 */
	@RequestMapping(value = "cancel_back/{id}")
	@ResponseBody
	@RequiresPermissions("finance:writeoffPayment:cancel_back")
	public AjaxResponseBody cancelBack(@PathVariable Long id)
	{
		FinanceWriteoffPayment order = serviceFactory.getWriteoffPaymentService().get(id);
		if (order.getIsCheck() == BoolValue.YES)
		{// 已审核状态下不能反作废
			return returnErrorBody("反作废失败：单据已审核，不能反作废");
		}
		if (serviceFactory.getWriteoffPaymentService().cancelBack(id))
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
	 * 页面 - 预付核销单列表
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:20, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("finance:writeoffPayment:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "finance/writeoffPayment/list";
	}

	/**
	 * <pre>
	 * 数据 - 预付核销单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:31, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinanceWriteoffPayment> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceWriteoffPayment> result = serviceFactory.getWriteoffPaymentService().findByCondition(queryParam);
		FinanceWriteoffPayment payment = new FinanceWriteoffPayment();

		BigDecimal discount = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinanceWriteoffPayment writeoffPayment : result.getResult())
		{
			discount = discount.add(writeoffPayment.getDiscount());
			money = money.add(writeoffPayment.getMoney());
		}
		payment.setDiscount(discount);
		payment.setMoney(money);
		result.getResult().add(payment);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 预付核销单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:41, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:writeoffPayment:list")
	public String detailList()
	{
		return "finance/writeoffPayment/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 预付核销单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:24:50, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinanceWriteoffPaymentDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceWriteoffPaymentDetail> result = serviceFactory.getWriteoffPaymentService().findDetailByCondition(queryParam);
		FinanceWriteoffPaymentDetail detail = new FinanceWriteoffPaymentDetail();

		BigDecimal sourceMoney = new BigDecimal(0);
		BigDecimal sourceBalanceMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinanceWriteoffPaymentDetail writeoffPaymentDetail : result.getResult())
		{
			sourceMoney = sourceMoney.add(writeoffPaymentDetail.getSourceMoney());
			money = money.add(writeoffPaymentDetail.getMoney());
			sourceBalanceMoney = sourceBalanceMoney.add(writeoffPaymentDetail.getSourceBalanceMoney());
		}
		FinanceWriteoffPayment writeoffPayment = new FinanceWriteoffPayment();
		detail.setMaster(writeoffPayment);
		detail.setMoney(money);
		detail.setSourceMoney(sourceMoney);
		detail.setSourceBalanceMoney(sourceBalanceMoney);
		result.getResult().add(detail);
		return result;
	}
}
