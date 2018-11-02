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
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SystemNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 系统公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/notice")
public class SystemNoticeController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 系统公告新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:06:10, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:systemnotice:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/systemnotice/create";
	}

	/**
	 * <pre>
	 * 功能 - 系统公告新增
	 * </pre>
	 * @param systemNotice
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:06:34, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:systemnotice:create")
	@AdminAuth
	public AjaxResponseBody save(@RequestBody SystemNotice systemNotice, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(systemNotice.getTitle()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		serviceFactory.getSystemNoticeService().save(systemNotice);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 系统公告修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:06:50, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:systemnotice:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SystemNotice systemNotice = serviceFactory.getSystemNoticeService().get(id);
		map.put("systemNotice", systemNotice);
		return "sys/systemnotice/edit";
	}

	/**
	 * <pre>
	 * 功能 - 系统公告修改
	 * </pre>
	 * @param systemNotice
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:07:02, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:systemnotice:edit")
	@AdminAuth
	public AjaxResponseBody update(@RequestBody SystemNotice systemNotice, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(systemNotice.getTitle()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SystemNotice _systemNotice = serviceFactory.getSystemNoticeService().get(systemNotice.getId());
		if (Validate.validateObjectsNullOrEmpty(_systemNotice))
		{

			return returnErrorBody("公告已不存在");
		}

		_systemNotice.setTitle(systemNotice.getTitle());
		_systemNotice.setPublish(systemNotice.getPublish());
		_systemNotice.setNoticeTime(systemNotice.getNoticeTime());
		_systemNotice.setContent(systemNotice.getContent());
		serviceFactory.getSystemNoticeService().update(_systemNotice);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 系统公告删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:07:17, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:systemnotice:del")
	@AdminAuth
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		serviceFactory.getSystemNoticeService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 系统公告查看
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:07:28, think
	 */
	@RequestMapping(value = "view/{id}")
	public String view(@PathVariable Long id, ModelMap map)
	{
		SystemNotice systemNotice = serviceFactory.getSystemNoticeService().get(id);
		map.put("systemNotice", systemNotice);
		return "sys/service/system_notice_view";
	}

	/**
	 * <pre>
	 * 页面 - 系统公告列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午4:07:42, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:systemnotice:list")
	@AdminAuth
	public String list(QueryParam queryParam) throws Exception
	{
		return "sys/systemnotice/list";

	}

	/**
	 * <pre>
	 * Ajax列表 - 系统公告
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:07:54, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<SystemNotice> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SystemNotice> result = serviceFactory.getSystemNoticeService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * Ajax列表 - 查询所有已发布公告
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:08:06, think
	 */
	@RequestMapping(value = "ajaxPublisList")
	@ResponseBody
	public SearchResult<SystemNotice> ajaxPublisList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SystemNotice> result = serviceFactory.getSystemNoticeService().findAllPublish(queryParam);
		return result;
	}
}
