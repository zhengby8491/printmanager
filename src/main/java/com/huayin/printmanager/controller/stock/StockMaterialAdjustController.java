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
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjust;
import com.huayin.printmanager.persist.entity.stock.StockMaterialAdjustDetail;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调整单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/adjust")
public class StockMaterialAdjustController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料库存调整单新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:13:50, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialAdjust:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/adjust/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调整单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:14:14, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialAdjust:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialAdjust stockMaterialAdjust = serviceFactory.getStockMaterialAdjustService().get(id);
		map.put("order", stockMaterialAdjust);
		return "stock/material/adjust/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调整单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:14:32, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialAdjust:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialAdjust stockMaterialAdjust = serviceFactory.getStockMaterialAdjustService().get(id);
		map.put("order", stockMaterialAdjust);
		return "stock/material/adjust/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料库存调整单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:14:46, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialAdjust:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialAdjust stockMaterialAdjust = serviceFactory.getStockMaterialAdjustService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialAdjust);
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
	 * 功能 - 删除材料库存调整单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:15:06, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialAdjust:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialAdjustService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存材料库存调整单
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:15:25, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialAdjust:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialAdjust stockMaterialAdjust)
	{
		serviceFactory.getStockMaterialAdjustService().save(stockMaterialAdjust);
		return returnSuccessBody(stockMaterialAdjust);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料库存调整单
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialAdjust
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:15:39, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialAdjust:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialAdjust stockMaterialAdjust)
	{
		serviceFactory.getStockMaterialAdjustService().update(stockMaterialAdjust);
		return returnSuccessBody(stockMaterialAdjust);
	}

	/**
	 * <pre>
	 * 功能 - 审核材料库存调整单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:15:52, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialAdjust:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (serviceFactory.getStockMaterialAdjustService().check(id) == 1)
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
	 * 页面 - 材料库存调整单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:16:03, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialAdjust:list")
	public String list()
	{
		return "stock/material/adjust/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存调整单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:16:20, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialAdjust> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialAdjust> result = serviceFactory.getStockMaterialAdjustService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调整单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:16:41, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialAdjust:list")
	public String detailList()
	{
		return "stock/material/adjust/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存调整单明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:16:59, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialAdjustDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialAdjustDetail> result = serviceFactory.getStockMaterialAdjustService().findDetailByCondition(queryParam);

		StockMaterialAdjustDetail detail = new StockMaterialAdjustDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialAdjustDetail stockMaterialAdjustDetail : result.getResult())
		{
			qty = qty.add(stockMaterialAdjustDetail.getQty());
			money = money.add(stockMaterialAdjustDetail.getMoney());
		}
		StockMaterialAdjust stockMaterialAdjust = new StockMaterialAdjust();
		detail.setMaster(stockMaterialAdjust);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
