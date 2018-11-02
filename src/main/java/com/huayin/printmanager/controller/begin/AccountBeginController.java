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
import com.huayin.printmanager.persist.entity.begin.AccountBegin;
import com.huayin.printmanager.persist.entity.begin.AccountBeginDetail;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 账户期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月5日
 * @since        2.0, 2018年1月5日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/begin/account")
public class AccountBeginController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 账户期初列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:24:05, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("begin:account:list")
	public String list()
	{
		return "begin/account/list";
	}

	/**
	 * <pre>
	 * 功能 - 账户期初Ajax请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:24:21, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<AccountBegin> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getAccountBeginService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 账户期初新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:23:48, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("begin:account:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "begin/account/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增账户期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param accountBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:25:11, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("begin:account:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody AccountBegin accountBegin)
	{
		serviceFactory.getAccountBeginService().save(accountBegin);
		return returnSuccessBody(accountBegin);
	}

	/**
	 * <pre>
	 * 页面 - 账户期初修改
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:25:29, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("begin:account:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		AccountBegin order = serviceFactory.getAccountBeginService().get(id);
		map.put("order", order);
		return "begin/account/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改账户期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param accountBegin
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:26:00, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("begin:account:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody AccountBegin accountBegin)
	{
		serviceFactory.getAccountBeginService().update(accountBegin);
		return returnSuccessBody(accountBegin);
	}

	/**
	 * <pre>
	 * 功能 - 账户期初查看
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:26:24, think
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("begin:account:list")
	public AccountBegin view(@PathVariable Long id, ModelMap map)
	{
		AccountBegin accountBegin = serviceFactory.getAccountBeginService().get(id);
		AccountBeginDetail accountBeginDetail = new AccountBeginDetail();
		BigDecimal money = new BigDecimal(0);
		for (AccountBeginDetail detail : accountBegin.getDetailList())
		{
			money = money.add(detail.getBeginMoney());
		}
		accountBeginDetail.setBeginMoney(money);
		accountBegin.getDetailList().add(accountBeginDetail);
		return accountBegin;
	}

	/**
	 * <pre>
	 * 功能 - 删除账户期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:26:55, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("begin:account:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(BeginI18nResource.ACCOUNT_VALIDATE_NAME_NOT_EXIST);
		}
		serviceFactory.getAccountBeginService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 账户期初审核
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月5日 上午10:27:08, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("begin:account:audit")
	public AjaxResponseBody audit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getAccountBeginService().audit(id))
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
	 * 功能 - 账户期初反审核
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 上午11:52:35, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("begin:account:auditCancel")
	public AjaxResponseBody auditCancel(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<Long> ids = Lists.newArrayList();
		AccountBegin account = serviceFactory.getAccountBeginService().get(id);
		for (AccountBeginDetail detail : account.getDetailList())
		{
			ids.add(detail.getAccountId());
		}
		if (serviceFactory.getCommonService().isUsed(BeginBillType.ACCOUNTBEGIN, ids))
		{
			return returnErrorBody("反审核失败，已做业务单据");
		}
		if (serviceFactory.getAccountBeginService().auditCancel(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
