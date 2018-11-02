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
import com.huayin.printmanager.persist.entity.stock.StockProductTransfer;
import com.huayin.printmanager.persist.entity.stock.StockProductTransferDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/transfer")
public class StockProductTransferController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品调拨单新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:30:05, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productTransfer:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/transfer/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品调拨单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:30:14, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productTransfer:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductTransfer stockProductTransfer = serviceFactory.getStockProductTransferService().get(id);
		map.put("order", stockProductTransfer);
		return "stock/product/transfer/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品调拨单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:30:25, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productTransfer:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductTransfer stockProductTransfer = serviceFactory.getStockProductTransferService().get(id);
		map.put("order", stockProductTransfer);
		return "stock/product/transfer/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:30:35, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductTransfer stockProductTransfer = serviceFactory.getStockProductTransferService().get(id);
			map = ObjectUtils.objectToMap(stockProductTransfer);
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
	 * 功能 - 删除成品调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:30:57, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductTransferService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品调拨单
	 * </pre>
	 * @param stockProductTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:31:06, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:create")
	public ServiceResult<StockProductTransfer> save(@RequestBody StockProductTransfer stockProductTransfer)
	{
		return serviceFactory.getStockProductTransferService().save(stockProductTransfer);
	}

	/**
	 * <pre>
	 * 功能  - 修改成品调拨单
	 * </pre>
	 * @param stockProductTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:31:16, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:edit")
	public ServiceResult<StockProductTransfer> update(@RequestBody StockProductTransfer stockProductTransfer)
	{
		return serviceFactory.getStockProductTransferService().update(stockProductTransfer);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:31:26, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductTransferService().check(id, BoolValue.NO);
		if (list.size() > 0)
		{
			return returnSuccessBody(list);
		}
		else if (list.size() == 0)
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
	 * 功能 - 强制审核成品调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午9:36:50, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductTransferService().check(id, BoolValue.YES);
		if (list.size() > 0)
		{
			return returnSuccessBody(list);
		}
		else if (list.size() == 0)
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
	 * 功能 - 反审核成品调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:31:44, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductTransferService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核成品调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月18日 上午9:48:22, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productTransfer:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductTransferService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 成品调拨单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:03, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productTransfer:list")
	public String list()
	{
		return "stock/product/transfer/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品调拨单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:12, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductTransfer> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductTransfer> result = serviceFactory.getStockProductTransferService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品调拨单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:24, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productTransfer:list")
	public String detailList()
	{
		return "stock/product/transfer/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品调拨单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 上午9:32:34, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductTransferDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductTransferDetail> result = serviceFactory.getStockProductTransferService().findDetailByCondition(queryParam);

		StockProductTransferDetail detail = new StockProductTransferDetail();

		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		for (StockProductTransferDetail stockProductTransferDetail : result.getResult())
		{
			qty += stockProductTransferDetail.getQty();
			money = money.add(stockProductTransferDetail.getMoney());
		}
		StockProductTransfer stockProductTransfer = new StockProductTransfer();
		detail.setMaster(stockProductTransfer);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}
}
