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
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplement;
import com.huayin.printmanager.persist.entity.stock.StockMaterialSupplementDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产补料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/supplement")
public class StockMaterialSupplementController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 生产补料新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:09:40, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialSupplement:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/supplement/create";
	}

	/**
	 * <pre>
	 * 页面 - 生产补料编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:09:58, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialSupplement:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialSupplement stockMaterialSupplement = serviceFactory.getStockMaterialSupplementService().get(id);
		map.put("order", stockMaterialSupplement);
		return "stock/material/supplement/edit";
	}

	/**
	 * <pre>
	 * 页面 - 生产补料查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:10:09, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialSupplement:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialSupplement stockMaterialSupplement = serviceFactory.getStockMaterialSupplementService().get(id);
		map.put("order", stockMaterialSupplement);
		return "stock/material/supplement/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印生产补料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:10:20, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialSupplement stockMaterialSupplement = serviceFactory.getStockMaterialSupplementService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialSupplement);
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
	 * 功能 - 删除生产补料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:10:32, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialSupplementService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存生产补料
	 * </pre>
	 * @param stockMaterialSupplement
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:10:42, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:create")
	public ServiceResult<StockMaterialSupplement> save(@RequestBody StockMaterialSupplement stockMaterialSupplement)
	{
		return serviceFactory.getStockMaterialSupplementService().save(stockMaterialSupplement);
	}

	/**
	 * <pre>
	 * 功能 - 修改生产补料
	 * </pre>
	 * @param stockMaterialSupplement
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:10:52, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:edit")
	public ServiceResult<StockMaterialSupplement> update(@RequestBody StockMaterialSupplement stockMaterialSupplement)
	{
		return serviceFactory.getStockMaterialSupplementService().update(stockMaterialSupplement);
	}

	/**
	 * <pre>
	 * 功能 - 审核生产补料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:11:04, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSupplementService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核生产补料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:11:17, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		List<StockMaterial> list = serviceFactory.getStockMaterialSupplementService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核生产补料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:11:26, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialSupplement:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialSupplementService().checkBack(id) == 1)
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
	 * 页面 - 生产补料列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:11:38, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialSupplement:list")
	public String list()
	{
		return "stock/material/supplement/list";
	}

	/**
	 * <pre>
	 * 数据 - 生产补料列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:11:49, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialSupplement> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialSupplement> result = serviceFactory.getStockMaterialSupplementService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 生产补料明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:01, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialSupplement:list")
	public String detailList()
	{
		return "stock/material/supplement/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 生产补料明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:12:13, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialSupplementDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialSupplementDetail> result = serviceFactory.getStockMaterialSupplementService().findDetailByCondition(queryParam);

		StockMaterialSupplementDetail detail = new StockMaterialSupplementDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);

		for (StockMaterialSupplementDetail stockMaterialSupplementDetail : result.getResult())
		{
			qty = qty.add(stockMaterialSupplementDetail.getQty());
			money = money.add(stockMaterialSupplementDetail.getMoney());
		}
		StockMaterialSupplement stockMaterialSupplement = new StockMaterialSupplement();
		detail.setMaster(stockMaterialSupplement);
		detail.setQty(qty);
		detail.setMoney(money);
		result.getResult().add(detail);

		return result;
	}
}
