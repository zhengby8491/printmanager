/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.TemplateDataModel;
import com.huayin.printmanager.persist.entity.sys.TemplateModel;
import com.huayin.printmanager.persist.enumerate.PrintModleName;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 自定义模板
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
public class TemplateController extends BaseController
{
	/**
	 * <pre>
	 * Ajax列表 - 模板列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:23:56, think
	 */
	@RequestMapping(value = "${basePath}/template/data/ajaxList")
	@ResponseBody
	// @RequiresPermissions("sys:template:list") 用户没有模板管理权限，但是有打印的权限，需要下拉看到模板列表。暂时注释
	public SearchResult<TemplateDataModel> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<TemplateDataModel> result = serviceFactory.getTemplateService().findDataByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 模板新增
	 * </pre>
	 * @param templateModel
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:24:04, think
	 */
	@RequestMapping(value = "${basePath}/template/addByAdmin")
	@ResponseBody
	@RequiresPermissions("sys:template:add")
	public AjaxResponseBody addByAdmin(TemplateModel templateModel)
	{
		try
		{
			serviceFactory.getTemplateService().addTemplateByAdmin(templateModel);
			return returnSuccessBody(templateModel.getId());
		}
		catch (ServiceException e)
		{
			return returnErrorBody(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}
	}

	/**
	 * <pre>
	 * 功能 - 模板修改
	 * </pre>
	 * @param templateModel
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:24:38, think
	 */
	@RequestMapping(value = "${basePath}/template/editByAdmin")
	@ResponseBody
	@RequiresPermissions("sys:template:edit")
	public AjaxResponseBody editByAdmin(TemplateModel templateModel)
	{
		try
		{
			serviceFactory.getTemplateService().editTemplateByAdmin(templateModel);
			return returnSuccessBody(templateModel.getId());
		}
		catch (ServiceException e)
		{
			return returnErrorBody(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}
	}

	/**
	 * <pre>
	 * Ajax列表 - 管理员模板查询列
	 * </pre>
	 * @param billType
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:24:59, think
	 */
	@RequestMapping(value = "${basePath}/template/list")
	@ResponseBody
	@RequiresPermissions("sys:template:list")
	public AjaxResponseBody list(PrintModleName billType)
	{
		try
		{
			if (!UserUtils.isSystemCompany())
			{
				returnSuccessBody();
			}
			List<TemplateModel> list = serviceFactory.getTemplateService().listTemplate(billType);
			return returnSuccessBody(list);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}

	}

	/**
	 * <pre>
	 * 功能 - 管理员选择模板
	 * </pre>
	 * @param billType
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:25:17, think
	 */
	@RequestMapping(value = "${basePath}/template/listByAdmin")
	@ResponseBody
	@RequiresPermissions("sys:template:list")
	public AjaxResponseBody listByAdmin(PrintModleName billType)
	{
		try
		{
			if (!UserUtils.isSystemCompany())
			{
				returnSuccessBody();
			}
			List<TemplateModel> list = serviceFactory.getTemplateService().listTemplateByAdmin(billType);
			return returnSuccessBody(list);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}

	}

	/**
	 * <pre>
	 * Ajax列表 - 模板查询列表 只查标题和ID
	 * </pre>
	 * @param billType
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:26:20, think
	 */
	@RequestMapping(value = "${basePath}/template/listWhitTitle")
	@ResponseBody
	// @RequiresPermissions("sys:template:list") 用户没有模板管理权限，但是有打印的权限，需要下拉看到模板列表。暂时注释
	public AjaxResponseBody listWhitTitle(PrintModleName billType)
	{
		Map<Long, String> data = new HashMap<Long, String>();
		List<TemplateModel> list = serviceFactory.getTemplateService().listTemplate(billType);
		if (list == null)
		{
			return returnErrorBody("");
		}
		for (TemplateModel template : list)
		{
			data.put(template.getId(), template.getTitle());
		}
		return returnSuccessBody(data);
	}

	/**
	 * <pre>
	 * 功能 - 模板条件查询
	 * </pre>
	 * @param query
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:25:28, think
	 */
	@RequestMapping(value = "${basePath}/template/query")
	@ResponseBody
	@RequiresPermissions("sys:template:list")
	public SearchResult<TemplateModel> queryTemplate(@RequestBody QueryParam query)
	{
		SearchResult<TemplateModel> result = serviceFactory.getTemplateService().queryTemplate(query);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 模板查询单个
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:25:44, think
	 */
	@RequestMapping(value = "${basePath}/template/get")
	@ResponseBody
	// @RequiresPermissions("sys:template:view") 用户没有模板管理权限，但是单据里面有打印权限，需要看到模板的数据
	public AjaxResponseBody get(Long id)
	{
		try
		{
			TemplateModel templateModel = serviceFactory.getTemplateService().getTemplate(id);
			if (templateModel == null)
			{
				return returnErrorBody("暂时没有找到相应数据!");
			}
			return returnSuccessBody(templateModel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}

	}

	/**
	 * <pre>
	 * 功能 - 系统管理员查询单个模板
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:25:57, think
	 */
	@RequestMapping(value = "${basePath}/template/getByAdmin")
	@ResponseBody
	@RequiresPermissions("sys:template:view")
	public AjaxResponseBody getByAdmin(Long id)
	{
		try
		{
			if (id == null)
			{
				return returnErrorBody("请求参数错误!");
			}
			TemplateModel templateModel = serviceFactory.getTemplateService().getTemplateByAdmin(id);
			if (templateModel == null)
			{
				return returnErrorBody("暂时没有找到相应数据!");
			}
			return returnSuccessBody(templateModel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}

	}

	/**
	 * <pre>
	 * 功能 - 模板删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:26:10, think
	 */
	@RequestMapping(value = "${basePath}/template/del")
	@ResponseBody
	@RequiresPermissions("sys:template:del")
	public AjaxResponseBody delete(Long id)
	{
		try
		{
			serviceFactory.getTemplateService().delTemplate(id);
			return returnSuccessBody("删除成功!");
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
			return returnErrorBody(e.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("系统发生异常，请稍后再试!");
		}

	}
}
