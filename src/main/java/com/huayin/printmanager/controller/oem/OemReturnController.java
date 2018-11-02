/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月14日 下午6:35:48
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.oem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
import com.huayin.printmanager.domain.annotation.Logical;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工管理  - 代工退货
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月14日下午6:35:48, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/oem/return")
public class OemReturnController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到退货单列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:37:42, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("oem:return:list")
	public String list()
	{
		return "oem/return/list";
	}

	/**
	 * <pre>
	 * 数据  - 返回退货单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:38:03, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("oem:return:list")
	@ResponseBody
	public SearchResult<OemReturn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemReturn> result = serviceFactory.getOemReturnService().findByCondition(queryParam);
		OemReturn order = new OemReturn();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal totalNoTaxMony = new BigDecimal(0);
		for (OemReturn o : result.getResult())
		{
			totalMoney = totalMoney.add(o.getTotalMoney());
			totalTax = totalTax.add(o.getTotalTax());
			totalNoTaxMony = totalNoTaxMony.add(o.getNoTaxTotalMoney());
		}
		order.setTotalMoney(totalMoney);
		order.setTotalTax(totalTax);
		order.setNoTaxTotalMoney(totalNoTaxMony);
		order.setIsCheck(null);
		result.getResult().add(order);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到退货单明细列表查看
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:38:16, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("oem:return:detail:list")
	public String detailList()
	{
		return "oem/return/detailList";
	}

	/**
	 * <pre>
	 * 数据  - 返回退货单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:38:29, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@RequiresPermissions("oem:return:detail:list")
	@ResponseBody
	public SearchResult<OemReturnDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OemReturnDetail> result = serviceFactory.getOemReturnService().findDetailBycondition(queryParam);
		OemReturnDetail detail = new OemReturnDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OemReturnDetail o : result.getResult())
		{
			money = money.add(o.getMoney());
			tax = tax.add(o.getTax());
			noTaxMoney = noTaxMoney.add(o.getNoTaxMoney());
			totalQty = totalQty.add(o.getQty());
		}
		OemReturn oemReturn = new OemReturn();
		detail.setMaster(oemReturn);
		detail.setMoney(money);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setQty(totalQty);
		result.getResult().add(detail);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 跳转到创建退货单页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:38:53, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("oem:return:create")
	public String create(Long customerId, String billNo, ModelMap map)
	{
		// if (Validate.validateObjectsNullOrEmpty(customerId))
		// {
		// return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		// }
		if (customerId != null)
		{
			QueryParam queryParam = new QueryParam();
			queryParam.setId(customerId);
			queryParam.setBillNo(billNo);
			SearchResult<OemDeliverDetail> result = serviceFactory.getOemDeliverService().findDeliverRecords(queryParam);
			SearchResult<Customer> res = serviceFactory.getCustomerService().quickFindByCondition(queryParam);
			if (CollectionUtils.isEmpty(result.getResult()))
			{
				return "oem/return/create";
			}
			String customerJSON = JsonUtils.toJson(res.getResult().get(0));
			String deliverDetail = JsonUtils.toJson(result.getResult());

			map.put("customerJSON", customerJSON);
			map.put("deliverDetailJSON", deliverDetail);
		}
		return "oem/return/create";
	}

	/**
	 * <pre>
	 * 功能  - 保存退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:39:10, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("oem:return:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工退货单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OemReturn order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			serviceFactory.getOemReturnService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OemReturn:" + JsonUtils.toJson(order));
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到退货单编辑页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:39:23, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("oem:return:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OemReturn order = serviceFactory.getOemReturnService().get(id);
		map.put("order", order);
		return "oem/return/edit";
	}

	/**
	 * <pre>
	 * 页面  - 跳转到退货单查看页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:39:37, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions(value = {"oem:return:create", "oem:return:view"}, logical = Logical.OR)
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		OemReturn order = serviceFactory.getOemReturnService().get(id);
		map.put("order", order);
		return "oem/return/view";
	}

	/**
	 * <pre>
	 * 功能  - 打印退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:39:54, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("oem:return:list")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			OemReturn order = serviceFactory.getOemReturnService().get(id);
			mapData = ObjectUtils.objectToMap(order);
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
			mapData.put("printDate", DateUtils.formatDateTime(new Date()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;
	}

	/**
	 * <pre>
	 * 功能  - 修改退货单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:40:15, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("oem:return:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工退货单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OemReturn order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			serviceFactory.getOemReturnService().update(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (BusinessException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 删除退货单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:40:37, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("oem:return:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "代工退货单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		try
		{
			OemReturn order = serviceFactory.getOemReturnService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOemReturnService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 审核退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:40:49, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("oem:return:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_ER, id, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 反审核退货单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:41:00, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("oem:return:auditcancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().audit(BillType.OEM_ER, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OEM_ER, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能  - 强制完工
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:41:26, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("oem:return:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReturn.class, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}

	/**
	 * <pre>
	 * 功能  - 取消强制完工
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月14日 下午6:41:37, zhengby
	 */
	@RequestMapping(value = "completeCancel")
	@ResponseBody
	@RequiresPermissions("oem:return:completecancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateArrayNullOrEmpty(ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReturn.class, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}
}
