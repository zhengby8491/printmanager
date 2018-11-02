/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.begin;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.BeginI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.begin.SupplierBegin;
import com.huayin.printmanager.persist.entity.begin.SupplierBeginDetail;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 供应商期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/begin/supplier")
public class SupplierBeginController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 供应商期初列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:34:03, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("begin:supplier:list")
	public String list()
	{
		return "begin/supplier/list";
	}

	/**
	 * <pre>
	 * 功能 - 供应商期初列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:34:22, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<SupplierBegin> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getSupplierBeginService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 供应商期初新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:34:33, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("begin:supplier:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "begin/supplier/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增供应商期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param supplierBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:35:32, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("begin:supplier:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody SupplierBegin supplierBegin)
	{
		serviceFactory.getSupplierBeginService().save(supplierBegin);
		return returnSuccessBody(supplierBegin);
	}

	/**
	 * <pre>
	 * 页面 - 供应商期初修改
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:34:49, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("begin:supplier:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		SupplierBegin order = serviceFactory.getSupplierBeginService().get(id);
		map.put("order", order);
		return "begin/supplier/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改供应商期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param supplierBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:35:41, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("begin:supplier:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody SupplierBegin supplierBegin)
	{
		serviceFactory.getSupplierBeginService().update(supplierBegin);
		return returnSuccessBody(supplierBegin);
	}

	/**
	 * <pre>
	 * 功能 - 查看供应商期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:35:00, think
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("begin:supplier:list")
	public SupplierBegin viewAjax(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		SupplierBegin order = serviceFactory.getSupplierBeginService().get(id);
		SupplierBeginDetail totalDetail = new SupplierBeginDetail();
		BigDecimal paymentMoney = new BigDecimal(0);
		BigDecimal advanceMoney = new BigDecimal(0);
		for (SupplierBeginDetail detail : order.getDetailList())
		{
			paymentMoney = paymentMoney.add(detail.getPaymentMoney());
			advanceMoney = advanceMoney.add(detail.getAdvanceMoney());
		}
		totalDetail.setPaymentMoney(paymentMoney);
		totalDetail.setAdvanceMoney(advanceMoney);
		order.getDetailList().add(totalDetail);
		return order;
	}

	/**
	 * <pre>
	 * 功能 - 删除供应商期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:35:19, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("begin:supplier:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(BeginI18nResource.SUPPLIER_VALIDAE_NAME_NOT_EXIST);
		}
		serviceFactory.getSupplierBeginService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能  - 审核供应商期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午9:35:51, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("begin:supplier:audit")
	public AjaxResponseBody audit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getSupplierBeginService().audit(id))
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
	 * 功能  - 反审核供应商期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午2:04:04, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("begin:supplier:auditCancel")
	public AjaxResponseBody auditCancel(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<Long> ids = Lists.newArrayList();
		SupplierBegin supplier = serviceFactory.getSupplierBeginService().get(id);
		for (SupplierBeginDetail detail : supplier.getDetailList())
		{
			ids.add(detail.getSupplierId());
		}
		if (serviceFactory.getCommonService().isUsed(BeginBillType.SUPPLIERBEGIN, ids))
		{
			return returnErrorBody("反审核失败，已做业务单据");
		}
		if (serviceFactory.getSupplierBeginService().auditCancel(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
