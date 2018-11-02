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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherIn;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品其它入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/otherin")
public class StockProductOtherInController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品其它入库新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:57:56, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productOtherin:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/otherIn/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品其它入库编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:05, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productOtherin:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductOtherIn stockProductOtherIn = serviceFactory.getStockProductOtherInService().get(id);
		map.put("order", stockProductOtherIn);
		return "stock/product/otherIn/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品其它入库查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:16, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productOtherin:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductOtherIn stockProductOtherIn = serviceFactory.getStockProductOtherInService().get(id);
		map.put("order", stockProductOtherIn);
		return "stock/product/otherIn/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品其它入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:26, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductOtherIn stockProductOtherIn = serviceFactory.getStockProductOtherInService().get(id);
			map = ObjectUtils.objectToMap(stockProductOtherIn);
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
	 * 功能 - 删除成品其它入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:38, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductOtherInService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品其它入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:48, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockProductOtherIn stockProductOtherIn)
	{
		serviceFactory.getStockProductOtherInService().save(stockProductOtherIn);
		return returnSuccessBody(stockProductOtherIn);
	}

	/**
	 * <pre>
	 * 功能 - 修改成品其它入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductOtherIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:58:57, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockProductOtherIn stockProductOtherIn)
	{
		serviceFactory.getStockProductOtherInService().update(stockProductOtherIn);
		return returnSuccessBody(stockProductOtherIn);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品其它入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:59:11, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductOtherInService().check(id) == 1)
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
	 * 功能 - 反审核成品其它入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:59:22, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductOtherInService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核成品其它入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:59:33, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductOtherInService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 成品其它入库列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:59:43, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productOtherin:list")
	public String list()
	{
		return "stock/product/otherIn/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品其它入库列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:59:53, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductOtherIn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductOtherIn> result = serviceFactory.getStockProductOtherInService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品其它入库明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:02, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productOtherin:list")
	public String detailList()
	{
		return "stock/product/otherIn/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品其它入库明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:00:15, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductOtherInDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductOtherInDetail> result = serviceFactory.getStockProductOtherInService().findDetailByCondition(queryParam);

		StockProductOtherInDetail detail = new StockProductOtherInDetail();

		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (StockProductOtherInDetail stockProductOtherInDetail : result.getResult())
		{
			qty += stockProductOtherInDetail.getQty();
			money = money.add(stockProductOtherInDetail.getMoney());
		}
		StockProductOtherIn stockProductOtherIn = new StockProductOtherIn();
		detail.setMaster(stockProductOtherIn);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
