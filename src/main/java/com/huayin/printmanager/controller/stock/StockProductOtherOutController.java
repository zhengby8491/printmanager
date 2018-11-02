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
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockProductOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品其它出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/otherout")
public class StockProductOtherOutController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品其它出库新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:17:02, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productOtherout:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/otherOut/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品其它出库编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:17:14, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productOtherout:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductOtherOut stockProductOtherOut = serviceFactory.getStockProductOtherOutService().get(id);
		map.put("order", stockProductOtherOut);
		return "stock/product/otherOut/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品其它出库查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:17:24, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productOtherout:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductOtherOut stockProductOtherOut = serviceFactory.getStockProductOtherOutService().get(id);
		map.put("order", stockProductOtherOut);
		return "stock/product/otherOut/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品其它出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:17:37, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductOtherOut stockProductOtherOut = serviceFactory.getStockProductOtherOutService().get(id);
			map = ObjectUtils.objectToMap(stockProductOtherOut);
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
	 * 功能 - 删除成品其它出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:17:51, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductOtherOutService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品其它出库
	 * </pre>
	 * @param stockProductOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:18:00, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:create")
	public ServiceResult<StockProductOtherOut> save(@RequestBody StockProductOtherOut stockProductOtherOut)
	{
		return serviceFactory.getStockProductOtherOutService().save(stockProductOtherOut);
	}

	/**
	 * <pre>
	 * 功能 - 修改成品其它出库
	 * </pre>
	 * @param stockProductOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:18:13, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:edit")
	public ServiceResult<StockProductOtherOut> update(@RequestBody StockProductOtherOut stockProductOtherOut)
	{
		return serviceFactory.getStockProductOtherOutService().update(stockProductOtherOut);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品其它出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:18:37, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductOtherOutService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核成品其它出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:18:47, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductOtherOutService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核成品其它出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:19:23, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherout:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductOtherOutService().checkBack(id) == 1)
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
	 * 页面 - 成品其它出库列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:19:35, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productOtherout:list")
	public String list()
	{
		return "stock/product/otherOut/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品其它出库列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:19:45, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductOtherOut> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductOtherOut> result = serviceFactory.getStockProductOtherOutService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品其它出库明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:19:57, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productOtherout:list")
	public String detailList()
	{
		return "stock/product/otherOut/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品其它出库明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午6:20:10, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductOtherOutDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductOtherOutDetail> result = serviceFactory.getStockProductOtherOutService().findDetailByCondition(queryParam);

		StockProductOtherOutDetail detail = new StockProductOtherOutDetail();

		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (StockProductOtherOutDetail stockProductOtherOutDetail : result.getResult())
		{
			qty += stockProductOtherOutDetail.getQty();
			money = money.add(stockProductOtherOutDetail.getMoney());
		}
		StockProductOtherOut stockProductOtherOut = new StockProductOtherOut();
		detail.setMaster(stockProductOtherOut);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}

}
