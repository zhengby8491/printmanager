/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

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
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 仓库信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/warehouse")
public class WarehouseController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 仓库信息列表
	 * </pre>
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年1月4日 上午9:51:39, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:warehouse:list")
	public String list() throws Exception
	{
		return "basic/warehouse/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:51:49, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Warehouse> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getWarehouseService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 仓库新增
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:51:59, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:warehouse:create")
	public String create()
	{
		return "basic/warehouse/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增仓库
	 * </pre>
	 * @param warehouse
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:52:11, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:warehouse:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "仓库信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Warehouse warehouse, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(warehouse.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Warehouse obj = serviceFactory.getWarehouseService().getByName(warehouse.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Warehouse _warehouse = new Warehouse();
		_warehouse.setCompanyId(UserUtils.getCompanyId());
		_warehouse.setName(warehouse.getName());
		_warehouse.setWarehouseType(warehouse.getWarehouseType());
		_warehouse.setIsBad(warehouse.getIsBad());
		_warehouse.setSort(warehouse.getSort());
		_warehouse.setMemo(warehouse.getMemo());

		Warehouse _warehouseNew = serviceFactory.getWarehouseService().save(_warehouse);
		UserUtils.clearCacheBasic(BasicType.WAREHOUSE);
		return returnSuccessBody(_warehouseNew);
	}

	/**
	 * <pre>
	 * 页面 - 仓库修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:52:36, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:warehouse:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Warehouse warehouse = serviceFactory.getWarehouseService().get(id);
		map.put("warehouse", warehouse);
		return "basic/warehouse/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改仓库
	 * </pre>
	 * @param warehouse
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:53:03, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:warehouse:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "仓库信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Warehouse warehouse, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(warehouse.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Warehouse obj = serviceFactory.getWarehouseService().getByName(warehouse.getName());
		Warehouse _warehouse = serviceFactory.getWarehouseService().get(warehouse.getId());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_warehouse.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_warehouse.setName(warehouse.getName());
		_warehouse.setWarehouseType(warehouse.getWarehouseType());
		_warehouse.setIsBad(warehouse.getIsBad());
		_warehouse.setSort(warehouse.getSort());
		_warehouse.setMemo(warehouse.getMemo());
		
		serviceFactory.getWarehouseService().update(_warehouse);
		UserUtils.clearCacheBasic(BasicType.WAREHOUSE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除仓库
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:53:13, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "仓库信息", Operation = Operation.DELETE)
	@RequiresPermissions("basic:warehouse:del")
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.WAREHOUSE, id);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
