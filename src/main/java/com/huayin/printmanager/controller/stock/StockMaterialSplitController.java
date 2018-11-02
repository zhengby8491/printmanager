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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplit;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSplitDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料分切
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/split")
public class StockMaterialSplitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料分切新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:38:13, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialSplit:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/split/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料分切编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:44:06, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialSplit:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialSplit stockMaterialSplit = serviceFactory.getStockMaterialSplitService().get(id);
		map.put("order", stockMaterialSplit);
		return "stock/material/split/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料分切查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:44:16, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialSplit:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialSplit stockMaterialSplit = serviceFactory.getStockMaterialSplitService().get(id);
		map.put("order", stockMaterialSplit);
		return "stock/material/split/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料分切
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:44:28, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialSplit stockMaterialSplit = serviceFactory.getStockMaterialSplitService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialSplit);
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
	 * 功能 - 删除材料分切
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:44:38, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialSplitService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialSplit
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:44:48, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:create")
	public ServiceResult<StockMaterialSplit> save(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialSplit stockMaterialSplit)
	{
		return serviceFactory.getStockMaterialSplitService().save(stockMaterialSplit);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialSplit
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:45:03, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:edit")
	public ServiceResult<StockMaterialSplit> update(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialSplit stockMaterialSplit)
	{
		return serviceFactory.getStockMaterialSplitService().update(stockMaterialSplit);
	}

	/**
	 * <pre>
	 * 功能  - 审核材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:45:13, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSplitService().check(id, BoolValue.NO);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制审核材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:45:23, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSplitService().check(id, BoolValue.YES);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 反审核材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:45:35, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSplitService().checkBack(id, BoolValue.NO);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 强制反审核材料分切
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:45:51, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSplit:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSplitService().checkBack(id, BoolValue.YES);
		if (list != null)
		{
			if (list.size() > 0)
			{
				return returnSuccessBody(list);
			}
			else
			{
				return returnSuccessBody();
			}
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 页面 - 材料分切列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:46:04, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialSplit:list")
	public String list()
	{
		return "stock/material/split/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料分切列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:46:14, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialSplit> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialSplit> result = serviceFactory.getStockMaterialSplitService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料分切明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:46:30, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialSplit:list")
	public String detailList()
	{
		return "stock/material/split/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料分切明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:46:42, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialSplitDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialSplitDetail> result = serviceFactory.getStockMaterialSplitService().findDetailByCondition(queryParam);

		StockMaterialSplitDetail detail = new StockMaterialSplitDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal valuationQty = new BigDecimal(0);
		BigDecimal qtyM = new BigDecimal(0);
		BigDecimal valuationQtyM = new BigDecimal(0);
		for (StockMaterialSplitDetail stockMaterialSplitDetail : result.getResult())
		{
			qty = qty.add(stockMaterialSplitDetail.getQty());
			valuationQty = valuationQty.add(stockMaterialSplitDetail.getValuationQty());

			qtyM = qtyM.add(stockMaterialSplitDetail.getMaster().getQty());
			valuationQtyM = valuationQtyM.add(stockMaterialSplitDetail.getMaster().getValuationQty());

		}
		StockMaterialSplit stockMaterialSplit = new StockMaterialSplit();
		stockMaterialSplit.setQty(qtyM);
		stockMaterialSplit.setValuationQty(valuationQtyM);

		detail.setMaster(stockMaterialSplit);
		detail.setQty(qty);
		detail.setValuationQty(valuationQty);
		result.getResult().add(detail);

		return result;
	}
}
