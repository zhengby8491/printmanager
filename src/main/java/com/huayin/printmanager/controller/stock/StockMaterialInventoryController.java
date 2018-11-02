/**
 * <pre>
 * Author:		think
 * Create:	 	2018年2月24日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
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
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventory;
import com.huayin.printmanager.persist.entity.stock.StockMaterialInventoryDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料盘点单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/inventory")
public class StockMaterialInventoryController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料盘点单新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:07:28, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialInventory:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/inventory/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料盘点单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:07:43, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialInventory:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialInventory stockMaterialInventory = serviceFactory.getStockMaterialInventoryService().get(id);
		map.put("order", stockMaterialInventory);
		return "stock/material/inventory/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料盘点单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:07:54, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialInventory:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialInventory stockMaterialInventory = serviceFactory.getStockMaterialInventoryService().get(id);
		map.put("order", stockMaterialInventory);
		return "stock/material/inventory/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:08:05, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialInventory stockMaterialInventory = serviceFactory.getStockMaterialInventoryService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialInventory);
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
	 * 功能 - 删除材料盘点单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:08:21, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialInventoryService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存材料盘点单
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialInventory
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:08:33, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialInventory stockMaterialInventory)
	{
		serviceFactory.getStockMaterialInventoryService().save(stockMaterialInventory);
		return returnSuccessBody(stockMaterialInventory);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料盘点单
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialInventory
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:08:41, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialInventory stockMaterialInventory)
	{
		serviceFactory.getStockMaterialInventoryService().update(stockMaterialInventory);
		return returnSuccessBody(stockMaterialInventory);
	}

	/**
	 * <pre>
	 * 功能 - 审核材料盘点单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:08:52, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialInventoryService().check(id) == 1)
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
	 * 功能 - 反审核材料盘点单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:09:03, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialInventory:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialInventoryService().checkBack(id) == 1)
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
	 * 功能  - 查是否存在操作材料库存未审核的单据列表
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:09:17, think
	 */
	@RequestMapping(value = "findNotCheck")
	@ResponseBody
	public AjaxResponseBody findNotCheck(HttpServletRequest request, ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.PURCH_PN);
		billTypeList.add(BillType.PURCH_PR);
		billTypeList.add(BillType.STOCK_SMOI);
		billTypeList.add(BillType.STOCK_SMOO);
		billTypeList.add(BillType.STOCK_SMA);
		billTypeList.add(BillType.STOCK_SMT);
		billTypeList.add(BillType.STOCK_MR);
		billTypeList.add(BillType.STOCK_SM);
		billTypeList.add(BillType.STOCK_RM);
		billTypeList.add(BillType.BEGIN_MATERIAL);
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
	 * @since 1.0, 2018年2月24日 上午10:09:32, think
	 */
	@RequestMapping(value = "notCheckList")
	public String notCheckList()
	{
		return "stock/material/inventory/not_check_list";
	}

	/**
	 * <pre>
	 * 数据 - 未审核出入库单据列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:09:43, think
	 */
	@RequestMapping(value = "ajaxNotCheckList")
	@ResponseBody
	public SearchResult<NotCheckStockVo> ajaxNotCheckList()
	{
		SearchResult<NotCheckStockVo> result = new SearchResult<NotCheckStockVo>();
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.PURCH_PN);
		billTypeList.add(BillType.PURCH_PR);
		billTypeList.add(BillType.STOCK_SMOI);
		billTypeList.add(BillType.STOCK_SMOO);
		billTypeList.add(BillType.STOCK_SMA);
		billTypeList.add(BillType.STOCK_SMT);
		billTypeList.add(BillType.STOCK_MR);
		billTypeList.add(BillType.STOCK_SM);
		billTypeList.add(BillType.STOCK_RM);
		billTypeList.add(BillType.BEGIN_MATERIAL);
		List<NotCheckStockVo> resultList = serviceFactory.getStockService().findNotCheckStock(billTypeList);
		result.setResult(resultList);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料盘点单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:09:52, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialInventory:list")
	public String list()
	{
		return "stock/material/inventory/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料盘点单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:10:18, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialInventory> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialInventory> result = serviceFactory.getStockMaterialInventoryService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料盘点单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:10:31, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialInventory:list")
	public String detailList()
	{
		return "stock/material/inventory/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料盘点单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:10:46, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialInventoryDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialInventoryDetail> result = serviceFactory.getStockMaterialInventoryService().findDetailByCondition(queryParam);

		StockMaterialInventoryDetail detail = new StockMaterialInventoryDetail();

		BigDecimal profitAndLossQty = new BigDecimal(0);
		BigDecimal profitAndLossMoney = new BigDecimal(0);
		BigDecimal qty = new BigDecimal(0);

		for (StockMaterialInventoryDetail stockMaterialInventoryDetail : result.getResult())
		{
			profitAndLossQty = profitAndLossQty.add(stockMaterialInventoryDetail.getProfitAndLossQty());
			qty = qty.add(stockMaterialInventoryDetail.getQty());
			profitAndLossMoney = profitAndLossMoney.add(stockMaterialInventoryDetail.getProfitAndLossMoney());
		}
		StockMaterialInventory stockMaterialInventory = new StockMaterialInventory();
		detail.setMaster(stockMaterialInventory);
		detail.setProfitAndLossQty(profitAndLossQty);
		detail.setProfitAndLossMoney(profitAndLossMoney);
		detail.setQty(qty);
		result.getResult().add(detail);

		return result;
	}
}
