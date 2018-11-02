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

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterial;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTake;
import com.huayin.printmanager.persist.entity.stock.StockMaterialTakeDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 库存管理 - 生产领料
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月24日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/stockmaterial/take")
public class StockMaterialTakeController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 转生产领料
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:24:19, think
	 */
	@RequestMapping(value = "toTake")
	public String toTake(HttpServletRequest request, ModelMap map, Long[] ids)
	{
		List<StockMaterialTakeDetail> detailList = serviceFactory.getStockMaterialTakeService().toTake(ids);
		map.put("detailList", detailList);
		return "stock/material/take/create";
	}

	/**
	 * <pre>
	 * 页面 - 生产领料新增
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:25:02, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("stock:materialTake:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "stock/material/take/create";
	}

	/**
	 * <pre>
	 * 页面 - 生产领料编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:25:17, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("stock:materialTake:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialTake stockMaterialTake = serviceFactory.getStockMaterialTakeService().get(id);
		map.put("order", stockMaterialTake);
		return "stock/material/take/edit";
	}

	/**
	 * <pre>
	 * 页面 - 生产领料查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:25:36, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("stock:materialTake:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		StockMaterialTake stockMaterialTake = serviceFactory.getStockMaterialTakeService().get(id);
		map.put("order", stockMaterialTake);
		return "stock/material/take/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印生产领料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:25:46, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("stock:material_take:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			StockMaterialTake stockMaterialTake = serviceFactory.getStockMaterialTakeService().get(id);
			map = ObjectUtils.objectToMap(stockMaterialTake);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			logger.error("查询生产领料打印数据发生业务逻辑异常!", e);
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 删除生产领料
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:25:55, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:del")
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("该库存已不存在");
		}
		serviceFactory.getStockMaterialTakeService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 保存生产领料
	 * </pre>
	 * @param stockMaterialTake
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:26:05, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:create")
	public AjaxResponseBody save(@RequestBody StockMaterialTake stockMaterialTake)
	{
		// 保存之前验证是否已领完料，前端的已校验过，后台再校验一次，防止保存重复领料单
		for (StockMaterialTakeDetail detail : stockMaterialTake.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			WorkMaterial source = serviceFactory.getWorkService().getMaterial(detail.getSourceDetailId());
			if (source.getTakeQty().compareTo(source.getQty()) != -1)
			{
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		ServiceResult<StockMaterialTake> result = serviceFactory.getStockMaterialTakeService().save(stockMaterialTake);
		return returnSuccessBody(result); 
	}

	/**
	 * <pre>
	 * 功能 - 修改生产领料
	 * </pre>
	 * @param stockMaterialTake
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:26:18, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:edit")
	public ServiceResult<StockMaterialTake> update(@RequestBody StockMaterialTake stockMaterialTake)
	{
		return serviceFactory.getStockMaterialTakeService().update(stockMaterialTake);
	}

	/**
	 * <pre>
	 * 功能 - 审核生产领料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:26:36, think
	 */
	@RequestMapping(value = "check/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:audit")
	public AjaxResponseBody check(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getStockMaterialTakeService().check(id, BoolValue.NO);
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
	 * 功能 - 强制审核生产领料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月17日 上午9:53:13, think
	 */
	@RequestMapping(value = "forceCheck/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:audit")
	public AjaxResponseBody forceCheck(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		List<StockMaterial> list = serviceFactory.getStockMaterialTakeService().check(id, BoolValue.YES);
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
	 * 功能 - 反审核生产领料
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:27:01, think
	 */
	@RequestMapping(value = "checkBack/{id}")
	@ResponseBody
	@RequiresPermissions("stock:materialTake:audit_cancel")
	public AjaxResponseBody checkBack(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{

		if (serviceFactory.getStockMaterialTakeService().checkBack(id))
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
	 * 功能 - 标记强制完工
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:27:11, think
	 */
	@RequestMapping(value = "forceCompleteYes")
	@RequiresPermissions("produce:work:complete")
	@ResponseBody
	public AjaxResponseBody forceCompleteYes(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getStockMaterialTakeService().forceComplete(ids, BoolValue.YES))
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
	 * 功能 - 取消强制完工
	 * </pre>
	 * @param request
	 * @param map
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:27:28, think
	 */
	@RequestMapping(value = "forceCompleteNo")
	@RequiresPermissions("produce:work:complete_cancel")
	@ResponseBody
	public AjaxResponseBody forceCompleteNo(HttpServletRequest request, ModelMap map, @RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getStockMaterialTakeService().forceComplete(ids, BoolValue.NO))
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
	 * 页面 - 生产领料列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:27:42, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("stock:materialTake:list")
	public String list()
	{
		return "stock/material/take/list";
	}

	/**
	 * <pre>
	 * 数据 - 生产领料列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:27:54, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<StockMaterialTake> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialTake> result = serviceFactory.getStockMaterialTakeService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 生产领料明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:28:05, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("stock:materialTake:list")
	public String detailList()
	{
		return "stock/material/take/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 生产领料明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月24日 下午4:28:17, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<StockMaterialTakeDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<StockMaterialTakeDetail> result = serviceFactory.getStockMaterialTakeService().findDetailByCondition(queryParam);

		StockMaterialTakeDetail detail = new StockMaterialTakeDetail();
		try
		{
			BigDecimal qty = new BigDecimal(0);
			BigDecimal money = new BigDecimal(0);

			for (StockMaterialTakeDetail stockMaterialTakeDetail : result.getResult())
			{
				qty = qty.add(stockMaterialTakeDetail.getQty());
				money = money.add(stockMaterialTakeDetail.getMoney());
			}
			StockMaterialTake stockMaterialTake = new StockMaterialTake();
			detail.setMaster(stockMaterialTake);
			detail.setQty(qty);
			detail.setMoney(money);
			result.getResult().add(detail);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
