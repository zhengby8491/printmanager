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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.stock.StockProduct;
import com.huayin.printmanager.persist.entity.stock.StockProductIn;
import com.huayin.printmanager.persist.entity.stock.StockProductInDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 成品入库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockproduct/in")
public class StockProductInController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 成品入库新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:03:16, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:productIn:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/product/in/create";
	}

	/**
	 * <pre>
	 * 页面 - 成品入库编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:03:28, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:productIn:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductIn stockProductIn = serviceFactory.getStockProductInService().get(id);
		map.put("order", stockProductIn);
		return "stock/product/in/edit";
	}

	/**
	 * <pre>
	 * 页面 - 成品入库查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:03:38, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:productIn:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockProductIn stockProductIn = serviceFactory.getStockProductInService().get(id);
		map.put("order", stockProductIn);
		return "stock/product/in/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印成品入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:03:52, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productIn:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockProductIn stockProductIn = serviceFactory.getStockProductInService().get(id);
			map = ObjectUtils.objectToMap(stockProductIn);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			logger.error("查询成品入库打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 删除成品入库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:04:03, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productIn:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockProductInService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:04:12, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:productIn:create")
	public AjaxResponseBody save(HttpServletRequest request, ModelMap map, @RequestBody StockProductIn stockProductIn)
	{
		if (Validate.validateObjectsNullOrEmpty(stockProductIn))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		// 保存之前验证是否已入库完，前端的已校验过，后台再校验一次，防止保存重复入库单
		for (StockProductInDetail detail : stockProductIn.getDetailList())
		{
			if (null != detail.getSourceDetailId())
			{
				WorkProduct source = serviceFactory.getWorkService().getProduct(detail.getSourceDetailId());
				if (source.getInStockQty() >= source.getProduceQty())
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		serviceFactory.getStockProductInService().save(stockProductIn);
		return returnSuccessBody(stockProductIn);
	}

	/**
	 * <pre>
	 * 功能 - 修改成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param stockProductIn
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:04:23, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:productIn:edit")
	public AjaxResponseBody update(HttpServletRequest request, ModelMap map, @RequestBody StockProductIn stockProductIn)
	{
		serviceFactory.getStockProductInService().update(stockProductIn);
		return returnSuccessBody(stockProductIn);
	}

	/**
	 * <pre>
	 * 功能 - 审核成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:04:42, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productIn:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockProductInService().check(id))
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
	 * 功能 - 反审核成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:04:52, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productIn:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductInService().checkBack(id, BoolValue.NO);
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
	 * 功能 - 强制反审核成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:05:02, think
	 */
	@RequestMapping(value = "forceCheckBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productIn:audit_cancel")
	public AjaxResponseBody forceCheckBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockProduct> list = serviceFactory.getStockProductInService().checkBack(id, BoolValue.YES);
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
	 * 页面 - 转成品入库
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:05:13, think
	 */
	@RequestMapping(value = "toProductIn")
	@RequiresPermissions("stock:productIn:create")
	public String toProductIn(HttpServletRequest request, ModelMap map, Long[] ids)
	{
		List<StockProductInDetail> detailList = serviceFactory.getStockProductInService().transmit(ids);
		map.put("detailList", detailList);
		return "stock/product/in/create";
	}

	/**
	 * <pre>
	 * 功能 - 手动完工
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:05:27, think
	 */
	@RequestMapping(value = "forceCompleteYes")
	@RequiresPermissions("produce:work:complete")
	@ResponseBody
	public AjaxResponseBody forceCompleteYes(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{

		if (serviceFactory.getCommonService().forceComplete(WorkProduct.class, ids, BoolValue.YES))
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
	 * 功能 - 取消手动完工
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:05:40, think
	 */
	@RequestMapping(value = "forceCompleteNo")
	@RequiresPermissions("produce:work:complete_cancel")
	@ResponseBody
	public AjaxResponseBody forceCompleteNo(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{

		if (serviceFactory.getCommonService().forceComplete(WorkProduct.class, ids, BoolValue.NO))
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
	 * 页面 - 成品入库列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:06:06, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:productIn:list")
	public String list()
	{
		return "stock/product/in/list";
	}

	/**
	 * <pre>
	 * 数据 - 成品入库列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:06:15, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockProductIn> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductIn> result = serviceFactory.getStockProductInService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 成品入库明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:06:25, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:productIn:list")
	public String detailList()
	{
		return "stock/product/in/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 成品入库明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午5:06:39, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockProductInDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockProductInDetail> result = serviceFactory.getStockProductInService().findDetailByCondition(queryParam);

		StockProductInDetail detail = new StockProductInDetail();

		Integer qty = new Integer(0);
		BigDecimal money = new BigDecimal(0);
		Integer sourceQty = new Integer(0);
		for (StockProductInDetail stockProductInDetail : result.getResult())
		{
			qty += stockProductInDetail.getQty();
			money = money.add(stockProductInDetail.getMoney());
			sourceQty += stockProductInDetail.getSourceQty();
		}
		StockProductIn stockProductIn = new StockProductIn();
		detail.setMaster(stockProductIn);
		detail.setSourceQty(sourceQty);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
