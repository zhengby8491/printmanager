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
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.begin.MaterialBegin;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.persist.enumerate.WarehouseType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 材料期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/begin/material")
public class MaterialBeginController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料期初列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:49:46, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("begin:material:list")
	public String list()
	{
		return "begin/material/list";
	}

	/**
	 * <pre>
	 * 功能 - 材料期初列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:50:02, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<MaterialBegin> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getMaterialBeginService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 材料期初新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:50:28, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("begin:material:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().getByBegin(WarehouseType.MATERIAL);
		map.put("warehouseList", warehouseList);
		return "begin/material/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增材料期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param materialBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:51:35, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("begin:material:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody MaterialBegin materialBegin)
	{
		List<MaterialBeginDetail> resultList = serviceFactory.getMaterialBeginService().isBeginList(materialBegin);
		if (resultList.size() > 0)
		{
			return returnSuccessBody(resultList);
		}
		serviceFactory.getMaterialBeginService().save(materialBegin);
		return returnSuccessBody(materialBegin);
	}

	/**
	 * <pre>
	 * 页面 - 材料期初修改
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:50:43, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("begin:material:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		MaterialBegin order = serviceFactory.getMaterialBeginService().get(id);
		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().getByBegin(WarehouseType.MATERIAL);
		map.put("warehouseList", warehouseList);
		map.put("order", order);
		return "begin/material/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改材料期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param materialBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:51:48, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("begin:material:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody MaterialBegin materialBegin)
	{
		List<MaterialBeginDetail> resultList = serviceFactory.getMaterialBeginService().isBeginList(materialBegin);
		if (resultList.size() > 0)
		{
			return returnSuccessBody(resultList);
		}
		serviceFactory.getMaterialBeginService().update(materialBegin);
		return returnSuccessBody(materialBegin);
	}

	/**
	 * <pre>
	 * 功能 - 查看材料期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:50:51, think
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("begin:material:list")
	public MaterialBegin viewAjax(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		MaterialBegin order = serviceFactory.getMaterialBeginService().get(id);
		MaterialBeginDetail totalDetail = new MaterialBeginDetail();
		BigDecimal qty = new BigDecimal(0);
		BigDecimal valuationQty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (MaterialBeginDetail detail : order.getDetailList())
		{
			qty = qty.add(detail.getQty());
			money = money.add(detail.getMoney());
			valuationQty = valuationQty.add(detail.getValuationQty());
		}
		totalDetail.setQty(qty);
		totalDetail.setMoney(money);
		totalDetail.setValuationQty(valuationQty);
		order.getDetailList().add(totalDetail);
		return order;
	}

	/**
	 * <pre>
	 * 功能 - 删除材料期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:51:26, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("begin:material:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(BeginI18nResource.MATERIAL_VALIDATE_NAME_NOT_EXIST);
		}
		serviceFactory.getMaterialBeginService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能  - 审核材料期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:51:57, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("begin:material:audit")
	public AjaxResponseBody audit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getMaterialBeginService().check(id) == 1)
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
	 * 功能  - 反审核审核材料期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:51:43, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("begin:material:auditCancel")
	public AjaxResponseBody auditCancel(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		MaterialBegin material = serviceFactory.getMaterialBeginService().get(id);
		List<Long> ids = Lists.newArrayList();
		for (MaterialBeginDetail detail : material.getDetailList())
		{
			ids.add(detail.getMaterialId());
		}
		if (serviceFactory.getCommonService().isUsed(BeginBillType.MATERIALBEGIN, ids))
		{
			return returnErrorBody("反审核失败，已做业务单据");
		}
		if (serviceFactory.getMaterialBeginService().checkBack(id) == 1)
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
