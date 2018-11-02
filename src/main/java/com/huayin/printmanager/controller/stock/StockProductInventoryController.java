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
import java.util.ArrayList;
import java.util.List;
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
import com.huayin.printmanager.persist.entity.stock.StockProductInventory;
import com.huayin.printmanager.persist.entity.stock.StockProductInventoryDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品盘点
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/inventory")
public class StockProductInventoryController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品盘点新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:45:55, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productInventory:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/inventory/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品盘点编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:46:06, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productInventory:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductInventory stockProductInventory = serviceFactory.getStockProductInventoryService().get(id);
		map.put("order", stockProductInventory);
		return "stock/product/inventory/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品盘点查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:46:16, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productInventory:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductInventory stockProductInventory = serviceFactory.getStockProductInventoryService().get(id);
		map.put("order", stockProductInventory);
		return "stock/product/inventory/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:46:28, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductInventory stockProductInventory = serviceFactory.getStockProductInventoryService().get(id);
			map = ObjectUtils.objectToMap(stockProductInventory);
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
	 * 功能 - 删除成品盘点
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:46:39, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductInventoryService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品盘点
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductInventory
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:46:57, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockProductInventory stockProductInventory)
	{
		serviceFactory.getStockProductInventoryService().save(stockProductInventory);
		return returnSuccessBody(stockProductInventory);
	}

	/**
	 * <pre>
	 * 功能 - 修改成品盘点
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductInventory
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:47:09, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockProductInventory stockProductInventory)
	{
		serviceFactory.getStockProductInventoryService().update(stockProductInventory);
		return returnSuccessBody(stockProductInventory);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品盘点
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:47:17, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductInventoryService().check(id) == 1)
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
	 * 功能 - 反审核成品盘点
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:47:28, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productInventory:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductInventoryService().checkBack(id) == 1)
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
	 * 查是否存在操作成品库存未审核的单据列表
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:47:38, think
	 */
	@RequestMapping(value = "findNotCheck")
	@ResponseBody
	public AjaxResponseBody findNotCheck(HttpServletRequest request, ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.SALE_IV);
		billTypeList.add(BillType.SALE_IR);
		billTypeList.add(BillType.STOCK_SPIN);
		billTypeList.add(BillType.STOCK_SPOI);
		billTypeList.add(BillType.STOCK_SPOO);
		billTypeList.add(BillType.STOCK_SPI);
		billTypeList.add(BillType.STOCK_SPA);
		billTypeList.add(BillType.STOCK_SPT);
		billTypeList.add(BillType.OUTSOURCE_OA);
		billTypeList.add(BillType.OUTSOURCE_OR);
		billTypeList.add(BillType.BEGIN_PRODUCT);
		if (!serviceFactory.getStockService().findIsHasNotCheck(billTypeList))
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
	 * 页面 - 未审核出入库单据列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:47:54, think
	 */
	@RequestMapping(value = "notCheckList")
	public String notCheckList()
	{
		return "stock/product/inventory/not_check_list";
	}

	/**
	 * <pre>
	 * 数据 - 未审核出入库单据列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:48:06, think
	 */
	@RequestMapping(value = "ajaxNotCheckList")
	@ResponseBody
	public SearchResult<NotCheckStockVo> ajaxNotCheckList()
	{
		SearchResult<NotCheckStockVo> result = new SearchResult<NotCheckStockVo>();
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.SALE_IV);
		billTypeList.add(BillType.SALE_IR);
		billTypeList.add(BillType.STOCK_SPIN);
		billTypeList.add(BillType.STOCK_SPOI);
		billTypeList.add(BillType.STOCK_SPOO);
		billTypeList.add(BillType.STOCK_SPI);
		billTypeList.add(BillType.STOCK_SPA);
		billTypeList.add(BillType.STOCK_SPT);
		billTypeList.add(BillType.OUTSOURCE_OA);
		billTypeList.add(BillType.OUTSOURCE_OR);
		billTypeList.add(BillType.BEGIN_PRODUCT);
		List<NotCheckStockVo> resultList = serviceFactory.getStockService().findNotCheckStock(billTypeList);
		result.setResult(resultList);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品盘点列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:48:25, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productInventory:list")
	public String list()
	{
		return "stock/product/inventory/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品盘点列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:48:35, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductInventory> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductInventory> result = serviceFactory.getStockProductInventoryService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品盘点明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:48:45, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productInventory:list")
	public String detailList()
	{
		return "stock/product/inventory/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品盘点明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:48:59, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductInventoryDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductInventoryDetail> result = serviceFactory.getStockProductInventoryService().findDetailByCondition(queryParam);
		StockProductInventoryDetail detail = new StockProductInventoryDetail();

		Integer qty = new Integer(0);
		Integer profitAndLossQty = new Integer(0);
		BigDecimal profitAndLossMoney = new BigDecimal(0);
		for (StockProductInventoryDetail stockProductInventoryDetail : result.getResult())
		{
			qty += stockProductInventoryDetail.getQty();
			profitAndLossQty += stockProductInventoryDetail.getProfitAndLossQty();
			profitAndLossMoney = profitAndLossMoney.add(stockProductInventoryDetail.getProfitAndLossMoney());
		}
		StockProductInventory stockProductInventory = new StockProductInventory();
		detail.setMaster(stockProductInventory);
		detail.setQty(qty);
		detail.setProfitAndLossQty(profitAndLossQty);
		detail.setProfitAndLossMoney(profitAndLossMoney);
		result.getResult().add(detail);
		return result;
	}
}
