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
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransfer;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTransferDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料库存调拨单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/transfer")
public class StockMaterialTransferController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料库存调拨单新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:35:36, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialTransfer:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/transfer/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调拨单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:35:47, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialTransfer:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialTransfer stockMaterialTransfer = serviceFactory.getStockMaterialTransferService().get(id);
		map.put("order", stockMaterialTransfer);
		return "stock/material/transfer/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调拨单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:36:09, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialTransfer:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialTransfer stockMaterialTransfer = serviceFactory.getStockMaterialTransferService().get(id);
		map.put("order", stockMaterialTransfer);
		return "stock/material/transfer/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料库存调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:36:24, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialTransfer stockMaterialTransfer = serviceFactory.getStockMaterialTransferService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialTransfer);
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
	 * 功能 - 删除材料库存调拨单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:36:37, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialTransferService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存材料库存调拨单
	 * </pre>
	 * @param stockMaterialTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:36:48, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:create")
	public ServiceResult<StockMaterialTransfer> save(@RequestBody StockMaterialTransfer stockMaterialTransfer)
	{
		return serviceFactory.getStockMaterialTransferService().save(stockMaterialTransfer);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料库存调拨单
	 * </pre>
	 * @param stockMaterialTransfer
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:36:58, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:edit")
	public ServiceResult<StockMaterialTransfer> update(@RequestBody StockMaterialTransfer stockMaterialTransfer)
	{
		return serviceFactory.getStockMaterialTransferService().update(stockMaterialTransfer);
	}

	/**
	 * <pre>
	 * 功能 - 审核材料库存调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:37:09, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialTransferService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核材料库存调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月17日 下午2:27:11, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialTransferService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核材料库存调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:37:33, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getStockMaterialTransferService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核材料库存调拨单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月17日 下午3:43:54, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTransfer:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getStockMaterialTransferService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 材料库存调拨单列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:37:56, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialTransfer:list")
	public String list()
	{
		return "stock/material/transfer/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存调拨单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:38:06, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialTransfer> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialTransfer> result = serviceFactory.getStockMaterialTransferService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料库存调拨单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:38:17, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialTransfer:list")
	public String detailList()
	{
		return "stock/material/transfer/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料库存调拨单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:38:30, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialTransferDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialTransferDetail> result = serviceFactory.getStockMaterialTransferService().findDetailByCondition(queryParam);

		StockMaterialTransferDetail detail = new StockMaterialTransferDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialTransferDetail stockMaterialTransferDetail : result.getResult())
		{
			qty = qty.add(stockMaterialTransferDetail.getQty());
			money = money.add(stockMaterialTransferDetail.getMoney());
		}
		StockMaterialTransfer stockMaterialTransfer = new StockMaterialTransfer();
		detail.setMaster(stockMaterialTransfer);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
