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
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturn;
import com.huayin.printmanager.persist.entity.stock.StockMaterialReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产退料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/return")
public class StockMaterialReturnController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 生产退料新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:03, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialReturn:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/return/create";
	}

	/**
	 * <pre>
	 * 页面 - 生产退料编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:13, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialReturn:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialReturn stockMaterialReturn = serviceFactory.getStockMaterialReturnService().get(id);
		map.put("order", stockMaterialReturn);
		return "stock/material/return/edit";
	}

	/**
	 * <pre>
	 * 页面 - 生产退料查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:23, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialReturn:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialReturn stockMaterialReturn = serviceFactory.getStockMaterialReturnService().get(id);
		map.put("order", stockMaterialReturn);
		return "stock/material/return/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印生产退料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:33, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialReturn stockMaterialReturn = serviceFactory.getStockMaterialReturnService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialReturn);
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
	 * 功能 - 删除生产退料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:43, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialReturnService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存生产退料
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialReturn
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:13:52, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialReturn stockMaterialReturn)
	{
		serviceFactory.getStockMaterialReturnService().save(stockMaterialReturn);
		return returnSuccessBody(stockMaterialReturn);
	}

	/**
	 * <pre>
	 * 功能 - 修改生产退料
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockMaterialReturn
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:01, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockMaterialReturn stockMaterialReturn)
	{
		serviceFactory.getStockMaterialReturnService().update(stockMaterialReturn);
		return returnSuccessBody(stockMaterialReturn);
	}

	/**
	 * <pre>
	 * 功能 - 审核生产退料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:10, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialReturnService().check(id))
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
	 * 功能 - 反审核生产退料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:21, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialReturnService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核生产退料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:32, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialReturn:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialReturnService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 生产退料列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:48, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialReturn:list")
	public String list()
	{
		return "stock/material/return/list";
	}

	/**
	 * <pre>
	 * 数据 - 生产退料列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:14:57, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialReturn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialReturn> result = serviceFactory.getStockMaterialReturnService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 生产退料明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:15:09, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialReturn:list")
	public String detailList()
	{
		return "stock/material/return/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 生产退料明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午2:15:21, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialReturnDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialReturnDetail> result = serviceFactory.getStockMaterialReturnService().findDetailByCondition(queryParam);

		StockMaterialReturnDetail detail = new StockMaterialReturnDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialReturnDetail stockMaterialReturnDetail : result.getResult())
		{
			qty = qty.add(stockMaterialReturnDetail.getQty());
			money = money.add(stockMaterialReturnDetail.getMoney());
		}
		StockMaterialReturn stockMaterialReturn = new StockMaterialReturn();
		detail.setMaster(stockMaterialReturn);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
