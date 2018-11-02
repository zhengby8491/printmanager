/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 单位信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/unit")
public class UnitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 单位信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:13:57, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:unit:list")
	public String list()
	{
		return "basic/unit/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:14:11, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Unit> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getUnitService().findByCondition(queryParam);
	}
	
	/**
	 * <pre>
	 * 页面 - 单位信息新增
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:14:20, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:unit:create")
	public String create()
	{
		return "basic/unit/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增单位信息
	 * </pre>
	 * @param unit
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:14:34, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:unit:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Unit unit, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(unit.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Unit obj = serviceFactory.getUnitService().getByName(unit.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Unit _unit = new Unit();
		_unit.setCompanyId(UserUtils.getCompanyId());
		_unit.setAccuracy(unit.getAccuracy());
		_unit.setName(unit.getName());
		_unit.setSort(unit.getSort());
		_unit.setMemo(unit.getMemo());

		serviceFactory.getUnitService().save(_unit);
		UserUtils.clearCacheBasic(BasicType.UNIT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 单位信息修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:15:10, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:unit:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Unit unit = serviceFactory.getUnitService().get(id);
		map.put("unit", unit);
		return "basic/unit/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改单位信息
	 * </pre>
	 * @param unit
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:15:32, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:unit:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Unit unit, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(unit.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Unit obj = serviceFactory.getUnitService().getByName(unit.getName());
		Unit _unit = serviceFactory.getUnitService().get(unit.getId());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_unit.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_unit.setName(unit.getName());
		_unit.setAccuracy(unit.getAccuracy());
		_unit.setSort(unit.getSort());
		_unit.setMemo(unit.getMemo());

		serviceFactory.getUnitService().update(_unit);
		UserUtils.clearCacheBasic(BasicType.UNIT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除单位信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:15:57, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:unit:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.UNIT, id);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}
}
