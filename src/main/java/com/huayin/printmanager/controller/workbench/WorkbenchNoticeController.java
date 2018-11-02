/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.workbench;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统 - 公告
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月16日, raintear
 * @version 	   2.0, 2018年2月27日下午2:08:01, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/workbench/notice")
public class WorkbenchNoticeController extends BaseController
{
	/**
	 * <pre>
	 * 功能  - 查公告
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:09:09, zhengby
	 */
	@RequestMapping(value = "get/{id}")
	@ResponseBody
	public WorkbenchNotice get(@PathVariable Long id)
	{
		return serviceFactory.getWorkbenchNoticeService().get(id);
	}

	/**
	 * <pre>
	 * 功能  - 查最后一条公告
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:09:23, zhengby
	 */
	@RequestMapping(value = "getNotice")
	@ResponseBody
	public String getNotice()
	{
		return serviceFactory.getWorkbenchNoticeService().getNotice();
	}

	/**
	 * <pre>
	 * 页面 - 跳转到公告新增页面
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:09:32, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("workbench:notice:create")
	public String create(HttpServletRequest request, ModelMap map)
	{
		return "workbench/notice/create";
	}

	/**
	 * <pre>
	 * 功能  - 保存公告
	 * </pre>
	 * @param notice
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:09:57, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public AjaxResponseBody save(@RequestBody WorkbenchNotice notice)
	{
		if (serviceFactory.getWorkbenchNoticeService().save(notice.getContent()) != null)
		{
			return returnSuccessBody(notice);
		}
		else
		{
			return returnErrorBody("保存失败");
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到公告修改页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:10:16, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("workbench:notice:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		WorkbenchNotice notice = serviceFactory.getWorkbenchNoticeService().get(id);
		map.put("notice", notice);
		return "workbench/notice/edit";
	}

	/**
	 * <pre>
	 * 功能  - 更新公告
	 * </pre>
	 * @param notice
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:10:38, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	public AjaxResponseBody update(@RequestBody WorkbenchNotice notice)
	{

		if (serviceFactory.getWorkbenchNoticeService().update(notice) != null)
		{
			return returnSuccessBody(notice);
		}
		else
		{
			return returnErrorBody("修改失败");
		}
	}

	/**
	 * <pre>
	 * 功能  - 删除公告
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:11:15, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("workbench:notice:del")
	public AjaxResponseBody save(@PathVariable Long id)
	{
		if (serviceFactory.getWorkbenchNoticeService().del(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("删除失败");
		}
	}

	/**
	 * <pre>
	 * 页面  - 跳转到公告列表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:11:42, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("workbench:notice:list")
	public String list()
	{
		return "workbench/notice/list";
	}

	/**
	 * <pre>
	 * 功能  - 返回公告列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:11:40, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("workbench:notice:list")
	@ResponseBody
	public SearchResult<WorkbenchNotice> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkbenchNotice> result = serviceFactory.getWorkbenchNoticeService().findNotice(queryParam);
		return result;
	}

}
