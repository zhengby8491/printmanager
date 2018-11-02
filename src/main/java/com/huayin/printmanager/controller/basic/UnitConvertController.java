/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.util.List;

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
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 单位换算
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/unitConvert")
public class UnitConvertController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 单位换算列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:33:51, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:unitConvert:list")
	public String list()
	{
		return "basic/unitConvert/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:34:02, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<UnitConvert> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getUnitConvertService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 单位换算新增
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:50, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:unitConvert:create")
	public String create()
	{
		return "basic/unitConvert/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增单位换算
	 * </pre>
	 * @param unitConvert
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:34:14, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:unitConvert:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位换算", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody UnitConvert unitConvert, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(unitConvert.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		UnitConvert obj = serviceFactory.getUnitConvertService().getByUnit(unitConvert.getSourceUnitId(), unitConvert.getConversionUnitId());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(BasicI18nResource.UNITCONVERT_VALIDATE_NAME_EXIST);
		}
		serviceFactory.getUnitConvertService().save(unitConvert);
		UserUtils.clearCacheBasic(BasicType.UNITCONVERT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 单位换算修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:34:29, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:unitConvert:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		UnitConvert unitConvert = serviceFactory.getUnitConvertService().get(id);
		map.put("unitConvert", unitConvert);
		return "basic/unitConvert/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改单位信息
	 * </pre>
	 * @param unitConvert
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:34:42, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:unitConvert:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位换算", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody UnitConvert unitConvert, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(unitConvert.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		serviceFactory.getUnitConvertService().update(unitConvert);
		UserUtils.clearCacheBasic(BasicType.UNITCONVERT);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除单位换算
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:34:55, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:unitConvert:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "单位换算", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.UNITCONVERT, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 获取换算公式
	 * </pre>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getByUnit")
	@ResponseBody
	public UnitConvert getByUnit(Long sourceUnitId, Long conversionUnitId)
	{
		try
		{
			List<UnitConvert> resultList = (List<UnitConvert>) UserUtils.getBasicListParam("UNITCONVERT", "sourceUnitId", sourceUnitId.toString());
			for (UnitConvert unitConvert : resultList)
			{
				if (unitConvert.getConversionUnitId().equals(conversionUnitId))
				{
					return unitConvert;
				}
			}
			return serviceFactory.getUnitConvertService().getByUnit(sourceUnitId, conversionUnitId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
