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
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料其他入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/otherin")
public class StockMaterialOtherInController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料其他入库新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:25:30, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialOtherin:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/otherIn/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料其他入库编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:25:45, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialOtherin:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialOtherIn stockMaterialOtherIn = serviceFactory.getStockMaterialOtherInService().get(id);
		map.put("order", stockMaterialOtherIn);
		return "stock/material/otherIn/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料其他入库查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:25:54, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialOtherin:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialOtherIn stockMaterialOtherIn = serviceFactory.getStockMaterialOtherInService().get(id);
		map.put("order", stockMaterialOtherIn);
		return "stock/material/otherIn/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料其他入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:04, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialOtherIn stockMaterialOtherIn = serviceFactory.getStockMaterialOtherInService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialOtherIn);
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
	 * 功能 - 删除材料其他入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:16, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialOtherInService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存材料其他入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:28, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialOtherIn stockMaterialOtherIn)
	{
		serviceFactory.getStockMaterialOtherInService().save(stockMaterialOtherIn);
		return returnSuccessBody(stockMaterialOtherIn);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料其他入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:36, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialOtherIn stockMaterialOtherIn)
	{
		serviceFactory.getStockMaterialOtherInService().update(stockMaterialOtherIn);
		return returnSuccessBody(stockMaterialOtherIn);
	}

	/**
	 * <pre>
	 * 功能 - 审核材料其他入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:46, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialOtherInService().check(id) == 1)
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
	 * 功能 - 反审核材料其他入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:26:59, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialOtherInService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核材料其他入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:27:12, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherin:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialOtherInService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 材料其他入库列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:27:25, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialOtherin:list")
	public String list()
	{
		return "stock/material/otherIn/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料其他入库列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:27:35, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialOtherIn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialOtherIn> result = serviceFactory.getStockMaterialOtherInService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料其他入库明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:27:46, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialOtherin:list")
	public String detailList()
	{
		return "stock/material/otherIn/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料其他入库明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午10:28:00, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialOtherInDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialOtherInDetail> result = serviceFactory.getStockMaterialOtherInService().findDetailByCondition(queryParam);

		StockMaterialOtherInDetail detail = new StockMaterialOtherInDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialOtherInDetail stockMaterialOtherInDetail : result.getResult())
		{
			qty = qty.add(stockMaterialOtherInDetail.getQty());
			money = money.add(stockMaterialOtherInDetail.getMoney());
		}
		StockMaterialOtherIn stockMaterialOtherIn = new StockMaterialOtherIn();
		detail.setMaster(stockMaterialOtherIn);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
