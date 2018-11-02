/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月5日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.begin.CustomerBegin;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 客户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/begin/customer")
public class CustomerBeginController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 客户期初列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:49:49, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("begin:customer:list")
	public String list()
	{
		return "begin/customer/list";
	}

	/**
	 * <pre>
	 * 功能 - 客户期初列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:50:00, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<CustomerBegin> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getCustomerBeginService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 客户期初新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:50:44, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("begin:customer:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "begin/customer/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增客户期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param customerBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:51:29, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("begin:customer:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody CustomerBegin customerBegin)
	{
		serviceFactory.getCustomerBeginService().save(customerBegin);
		return returnSuccessBody(customerBegin);
	}

	/**
	 * <pre>
	 * 页面 - 客户期初修改
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:50:59, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("begin:customer:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		CustomerBegin order = serviceFactory.getCustomerBeginService().get(id);
		map.put("order", order);
		return "begin/customer/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改客户期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param customerBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:51:56, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("begin:customer:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody CustomerBegin customerBegin)
	{
		serviceFactory.getCustomerBeginService().update(customerBegin);
		return returnSuccessBody(customerBegin);
	}

	/**
	 * <pre>
	 * 功能 - 查看客户期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午11:48:38, think
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("begin:customer:list")
	public CustomerBegin viewAjax(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		CustomerBegin order = serviceFactory.getCustomerBeginService().get(id);
		CustomerBeginDetail totalDetail = new CustomerBeginDetail();
		BigDecimal receiveMoney = new BigDecimal(0);
		BigDecimal advanceMoney = new BigDecimal(0);
		for (CustomerBeginDetail detail : order.getDetailList())
		{
			receiveMoney = receiveMoney.add(detail.getReceiveMoney());
			advanceMoney = advanceMoney.add(detail.getAdvanceMoney());
		}
		totalDetail.setReceiveMoney(receiveMoney);
		totalDetail.setAdvanceMoney(advanceMoney);
		order.getDetailList().add(totalDetail);
		return order;
	}

	/**
	 * <pre>
	 * 功能 - 删除客户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:52:36, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("begin:customer:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(BeginI18nResource.CUSTOMER_VALIDATE_NAME_NOT_EXIST);
		}
		serviceFactory.getCustomerBeginService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 客户期初审核
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:52:50, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("begin:customer:audit")
	public AjaxResponseBody audit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getCustomerBeginService().audit(id))
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
	 * 功能 - 客户期初反审核
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:52:26, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("begin:customer:auditCancel")
	public AjaxResponseBody auditCancel(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<Long> ids = Lists.newArrayList();
		CustomerBegin customer = serviceFactory.getCustomerBeginService().get(id);
		for (CustomerBeginDetail detail : customer.getDetailList())
		{
			ids.add(detail.getCustomerId());
		}
		if (serviceFactory.getCommonService().isUsed(BeginBillType.CUSTOMERBEGIN, ids))
		{
			return returnErrorBody("反审核失败，已做业务单据");
		}
		if (serviceFactory.getCustomerBeginService().auditCancel(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
