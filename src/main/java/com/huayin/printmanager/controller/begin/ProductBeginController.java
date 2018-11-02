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
import com.huayin.printmanager.persist.entity.begin.ProductBegin;
import com.huayin.printmanager.persist.entity.begin.ProductBeginDetail;
import com.huayin.printmanager.persist.enumerate.BeginBillType;
import com.huayin.printmanager.persist.enumerate.WarehouseType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 产品期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/begin/product")
public class ProductBeginController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 产品期初列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:15:54, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("begin:product:list")
	public String list()
	{
		return "begin/product/list";
	}

	/**
	 * <pre>
	 * 功能 - 产品期初列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:16:05, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<ProductBegin> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProductBeginService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 产品期初新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:16:25, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("begin:product:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().getByBegin(WarehouseType.PRODUCT);
		map.put("warehouseList", warehouseList);
		return "begin/product/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增产品期初
	 * </pre>
	 * @param map
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:17:32, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("begin:product:create")
	public AjaxResponseBody save(ModelMap map, @RequestBody ProductBegin productBegin)
	{
		serviceFactory.getProductBeginService().save(productBegin);
		return returnSuccessBody(productBegin);
	}

	/**
	 * <pre>
	 * 页面 - 产品期初修改
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:16:45, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("begin:product:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		ProductBegin order = serviceFactory.getProductBeginService().get(id);
		List<Warehouse> warehouseList = serviceFactory.getWarehouseService().getByBegin(WarehouseType.PRODUCT);
		map.put("warehouseList", warehouseList);
		map.put("order", order);
		return "begin/product/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改产品期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:17:47, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("begin:product:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody ProductBegin productBegin)
	{
		serviceFactory.getProductBeginService().update(productBegin);
		return returnSuccessBody(productBegin);
	}

	/**
	 * <pre>
	 * 功能 - 查看产品期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:17:01, think
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	@RequiresPermissions("begin:product:list")
	public ProductBegin viewAjax(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		ProductBegin order = serviceFactory.getProductBeginService().get(id);
		ProductBeginDetail totalDetail = new ProductBeginDetail();
		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (ProductBeginDetail detail : order.getDetailList())
		{
			qty = qty + detail.getQty();
			money = money.add(detail.getMoney());
		}
		totalDetail.setQty(qty);
		totalDetail.setMoney(money);
		order.getDetailList().add(totalDetail);
		return order;
	}

	/**
	 * <pre>
	 * 功能 - 删除产品期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:17:20, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("begin:product:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(BeginI18nResource.PRODUCT_VALIDATE_NAME_NOT_EXIST);
		}
		serviceFactory.getProductBeginService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 审核产品期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:17:58, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("begin:product:audit")
	public AjaxResponseBody audit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getProductBeginService().audit(id) == 1)
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
	 * 功能 - 反审核产品期初
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午2:03:11, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("begin:product:auditCancel")
	public AjaxResponseBody auditCancel(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<Long> ids = Lists.newArrayList();
		ProductBegin product = serviceFactory.getProductBeginService().get(id);
		for (ProductBeginDetail detail : product.getDetailList())
		{
			ids.add(detail.getProductId());
		}
		if (serviceFactory.getCommonService().isUsed(BeginBillType.PRODUCTBEGIN, ids))
		{
			return returnErrorBody("反审核失败，已做业务单据");
		}
		if (serviceFactory.getProductBeginService().auditCancel(id) == 1)
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
