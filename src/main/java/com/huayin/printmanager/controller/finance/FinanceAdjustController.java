/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午9:17:05
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.finance;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.Logical;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理  - 财务调整
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午9:17:05, zhengby
 */
@Controller
@RequestMapping(value = "${basePath}/finance/adjust")
public class FinanceAdjustController extends BaseController
{
	/**
	 * <pre>
	 * 页面  - 跳转到创建页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:12, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:adjust:create")
	public String create(ModelMap map)
	{
		return "finance/adjust/create";
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到编辑页面
	 * </pre>
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:15, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("finance:adjust:edit")
	public String edit(ModelMap map, @PathVariable Long id)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map,I18nResource.VALIDATE_FAIL);
		}
		FinanceAdjust order = serviceFactory.getFinanceAdjustService().get(id);
		map.put("order", order);
		return "finance/adjust/edit";
	}
	
	/**
	 * <pre>
	 * 页面  - 查看页面
	 * </pre>
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:28, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions(value = { "finance:adjust:create", "finance:adjust:view" }, logical = Logical.OR)
	public String view(ModelMap map, @PathVariable Long id)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map,I18nResource.VALIDATE_FAIL);
		}
		FinanceAdjust order = serviceFactory.getFinanceAdjustService().get(id);
		map.put("order", order);
		return "finance/adjust/view";
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到查看列表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:22:01, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("finance:adjust:list")
	public String list()
	{
		return "finance/adjust/list";
	}
	
	/**
	 * <pre>
	 * 页面  - 跳转到明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:31, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:adjust:detail:list")
	public String detailList()
	{
		return "finance/adjust/detailList";
	}
	
	/**
	 * <pre>
	 * 数据  - 查询财务调整单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:05, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinanceAdjust> ajaxList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getFinanceAdjustService().findByCondition(queryParam);
	}
	
	/**
	 * <pre>
	 * 数据  - 查询财务调整单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:35, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinanceAdjustDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceAdjustDetail> result = serviceFactory.getFinanceAdjustService().findDetailBycondition(queryParam);
		FinanceAdjustDetail detail = new FinanceAdjustDetail();
		detail.setMaster(new FinanceAdjust());
		BigDecimal adjustMoney = new BigDecimal(0);
		for (FinanceAdjustDetail detail_ : result.getResult())
		{
			adjustMoney = adjustMoney.add(detail_.getAdjustMoney());
		}
		detail.setAdjustMoney(adjustMoney);
		result.getResult().add(detail);
		return result;
	}
	
	/**
	 * <pre>
	 * 功能 - 保存
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:43, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:adjust:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "财务调整", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody FinanceAdjust order, HttpServletRequest request)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getFinanceAdjustService().save(order);
		return returnSuccessBody(order);
	}
	
	/**
	 * <pre>
	 * 功能  - 修改
	 * </pre>
	 * @param order
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:23:58, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("finance:adjust:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "财务调整", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody FinanceAdjust order)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getFinanceAdjustService().update(order);
		return returnSuccessBody(order);
	}
	
	/**
	 * <pre>
	 * 功能 - 删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:24:11, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("finance:adjust:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getFinanceAdjustService().delete(id);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能  - 审核
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年6月26日 上午10:24:23, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:adjust:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		// 数据校验
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		FinanceAdjust order = serviceFactory.getFinanceAdjustService().get(id);
		if (serviceFactory.getFinanceAdjustService().audit(order))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
