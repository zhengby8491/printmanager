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

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOut;
import com.huayin.printmanager.persist.entity.stock.StockMaterialOtherOutDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 材料其他出库
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/otherout")
public class StockMaterialOtherOutController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料其他出库新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:06:27, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialOtherout:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/otherOut/create";
	}

	/**
	 * <pre>
	 * 页面 - 材料其他出库编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:06:39, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialOtherout:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialOtherOut stockMaterialOtherOut = serviceFactory.getStockMaterialOtherOutService().get(id);
		map.put("order", stockMaterialOtherOut);
		return "stock/material/otherOut/edit";
	}

	/**
	 * <pre>
	 * 页面 - 材料其他出库查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:06:52, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialOtherout:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialOtherOut stockMaterialOtherOut = serviceFactory.getStockMaterialOtherOutService().get(id);
		map.put("order", stockMaterialOtherOut);
		return "stock/material/otherOut/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印材料其他出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:07:04, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:productOtherin:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialOtherOut stockMaterialOtherOut = serviceFactory.getStockMaterialOtherOutService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialOtherOut);
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
	 * 功能 - 删除材料其他出库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:07:15, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialOtherOutService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存材料其他出库
	 * </pre>
	 * @param stockMaterialOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:07:25, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:create")
	public ServiceResult<StockMaterialOtherOut> save(@RequestBody StockMaterialOtherOut stockMaterialOtherOut)
	{
		return serviceFactory.getStockMaterialOtherOutService().save(stockMaterialOtherOut);
	}

	/**
	 * <pre>
	 * 功能 - 修改材料其他出库
	 * </pre>
	 * @param stockMaterialOtherOut
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:07:38, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:edit")
	public ServiceResult<StockMaterialOtherOut> update(@RequestBody StockMaterialOtherOut stockMaterialOtherOut)
	{
		return serviceFactory.getStockMaterialOtherOutService().update(stockMaterialOtherOut);
	}

	/**
	 * <pre>
	 * 功能 - 审核材料其他出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:07:49, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialOtherOutService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核材料其他出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:08:00, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialOtherOutService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核材料其他出库
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:08:17, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialOtherout:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialOtherOutService().checkBack(id) == 1)
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
	 * 页面 - 材料其他出库列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:08:27, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialOtherout:list")
	public String list()
	{
		return "stock/material/otherOut/list";
	}

	/**
	 * <pre>
	 * 数据 - 材料其他出库列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:08:37, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialOtherOut> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialOtherOut> result = serviceFactory.getStockMaterialOtherOutService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 材料其他出库明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:08:50, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialOtherout:list")
	public String detailList()
	{
		return "stock/material/otherOut/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 材料其他出库明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午2:09:02, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialOtherOutDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialOtherOutDetail> result = serviceFactory.getStockMaterialOtherOutService().findDetailByCondition(queryParam);

		StockMaterialOtherOutDetail detail = new StockMaterialOtherOutDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialOtherOutDetail stockMaterialOtherOutDetail : result.getResult())
		{
			qty = qty.add(stockMaterialOtherOutDetail.getQty());
			money = money.add(stockMaterialOtherOutDetail.getMoney());
		}
		StockMaterialOtherOut stockMaterialOtherOut = new StockMaterialOtherOut();
		detail.setMaster(stockMaterialOtherOut);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);
		return result;
	}

}
