/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SystemConfig;
import com.huayin.printmanager.plugin.PageSupport;

/**
 * <pre>
 * 系统模块 - 系统参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/systemconfig")
public class SystemConfigController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 系统参数新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:03:09, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:systemconfig:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/systemconfig/create";
	}

	/**
	 * <pre>
	 * 功能 - 系统参数新增
	 * </pre>
	 * @param systemConfig
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:03:24, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:systemconfig:edit")
	@AdminAuth
	public AjaxResponseBody save(SystemConfig systemConfig, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(systemConfig.getId(), systemConfig.getValue()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SystemConfig _systemConfig = serviceFactory.getSystemConfigService().get(systemConfig.getId().toString());
		if (!Validate.validateObjectsNullOrEmpty(_systemConfig))
		{
			return returnErrorBody("系统参数已存在");
		}

		serviceFactory.getSystemConfigService().save(systemConfig);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 系统参数修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:03:35, think
	 */
	@RequestMapping(value = "edit")
	@RequiresPermissions("sys:systemconfig:edit")
	@AdminAuth
	public String edit(String id, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SystemConfig systemConfig = serviceFactory.getSystemConfigService().get(id);
		map.put("systemConfig", systemConfig);
		return "sys/systemconfig/edit";
	}

	/**
	 * <pre>
	 * 功能 - 系统参数修改
	 * </pre>
	 * @param systemConfig
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:03:48, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:systemconfig:edit")
	@AdminAuth
	public AjaxResponseBody update(SystemConfig systemConfig, ModelMap map)
	{
		SystemConfig _systemConfig = serviceFactory.getSystemConfigService().get(systemConfig.getId().toString());

		if (Validate.validateObjectsNullOrEmpty(systemConfig.getId(), systemConfig.getValue(), _systemConfig))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		_systemConfig.setValue(systemConfig.getValue());
		_systemConfig.setDescription(systemConfig.getDescription());
		serviceFactory.getSystemConfigService().update(_systemConfig);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 系统参数删除
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:04:02, think
	 */
	@RequestMapping(value = "del")
	@ResponseBody
	@RequiresPermissions("sys:systemconfig:del")
	@AdminAuth
	public boolean delete(String id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			serviceFactory.getSystemConfigService().delete(id);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * <pre>
	 * 页面 - 系统参数列表
	 * </pre>
	 * @param systemConfigId
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:04:15, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:systemconfig:list")
	@AdminAuth
	public String list(String systemConfigId, HttpServletRequest request, ModelMap map)
	{
		PageSupport<SystemConfig> page = new PageSupport<SystemConfig>(request);
		page.setPageSize(100);
		SearchResult<SystemConfig> result = serviceFactory.getSystemConfigService().findByCondition(systemConfigId, page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("systemConfigId", systemConfigId);
		map.put("page", page);
		return "sys/systemconfig/list";
	}
}
