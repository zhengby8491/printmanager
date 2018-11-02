/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.stock;

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
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjust;
import com.huayin.printmanager.persist.entity.stock.StockProductAdjustDetail;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品调整
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/adjust")
public class StockProductAdjustController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品调整新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:48:24, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productAdjust:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/adjust/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品调整编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:48:35, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productAdjust:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductAdjust stockProductAdjust = serviceFactory.getStockProductAdjustService().get(id);
		map.put("order", stockProductAdjust);
		return "stock/product/adjust/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品调整查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:48:53, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productAdjust:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductAdjust stockProductAdjust = serviceFactory.getStockProductAdjustService().get(id);
		map.put("order", stockProductAdjust);
		return "stock/product/adjust/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品调整
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:49:04, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productAdjust:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductAdjust stockProductAdjust = serviceFactory.getStockProductAdjustService().get(id);
			map = ObjectUtils.objectToMap(stockProductAdjust);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 删除成品调整
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:49:34, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productAdjust:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductAdjustService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品调整
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:49:44, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productAdjust:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockProductAdjust stockProductAdjust)
	{
		serviceFactory.getStockProductAdjustService().save(stockProductAdjust);
		return returnSuccessBody(stockProductAdjust);
	}

	/**
	 * <pre>
	 * 功能 - 修改成品调整
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:49:52, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productAdjust:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockProductAdjust stockProductAdjust)
	{
		serviceFactory.getStockProductAdjustService().update(stockProductAdjust);
		return returnSuccessBody(stockProductAdjust);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品调整
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:50:05, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productAdjust:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductAdjustService().check(id) == 1)
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
	 * 页面 - 成品调整列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:50:16, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productAdjust:list")
	public String list()
	{
		return "stock/product/adjust/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品调整列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:50:29, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductAdjust> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductAdjust> result = serviceFactory.getStockProductAdjustService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品调整明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:50:51, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productAdjust:list")
	public String detailList()
	{
		return "stock/product/adjust/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品调整明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:51:02, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductAdjustDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductAdjustDetail> result = serviceFactory.getStockProductAdjustService().findDetailByCondition(queryParam);

		StockProductAdjustDetail detail = new StockProductAdjustDetail();

		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (StockProductAdjustDetail stockProductAdjustDetail : result.getResult())
		{
			qty += stockProductAdjustDetail.getQty();
			money = money.add(stockProductAdjustDetail.getMoney());
		}
		StockProductAdjust stockProductAdjust = new StockProductAdjust();
		detail.setMaster(stockProductAdjust);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}
}
