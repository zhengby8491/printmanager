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
import com.huayin.printmanager.persist.entity.sys.SystemVersionNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 版本公告
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/versionNotice")
public class SystemVersionNoticeController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 版本公告新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:09:16, think
	 */
	@RequestMapping(value = "create")
//	@RequiresPermissions("sys:systemversionnotice:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/versionnotice/create";
	}
	
	/**
	 * <pre>
	 * 功能 - 版本公告新增
	 * </pre>
	 * @param systemVersionNotice
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:09:31, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
//	@RequiresPermissions("sys:systemversionnotice:create")
	@AdminAuth
	public AjaxResponseBody save(@RequestBody SystemVersionNotice systemVersionNotice, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(systemVersionNotice.getTitle()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		serviceFactory.getSystemVersionNoticeService().save(systemVersionNotice);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 页面 - 版本公告修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:09:42, think
	 */
	@RequestMapping(value = "edit/{id}")
//	@RequiresPermissions("sys:systemversionnotice:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		SystemVersionNotice SystemVersionNotice = serviceFactory.getSystemVersionNoticeService().get(id);
		map.put("systemNotice", SystemVersionNotice);
		return "sys/versionnotice/edit";
	}
	
	/**
	 * <pre>
	 * 功能 - 版本公告修改
	 * </pre>
	 * @param SystemVersionNotice
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:09:56, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
//	@RequiresPermissions("sys:systemversionnotice:edit")
	@AdminAuth
	public AjaxResponseBody update(@RequestBody SystemVersionNotice SystemVersionNotice, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(SystemVersionNotice.getTitle()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		SystemVersionNotice _SystemVersionNotice = serviceFactory.getSystemVersionNoticeService().get(SystemVersionNotice.getId());
		if(Validate.validateObjectsNullOrEmpty(_SystemVersionNotice)){
			
			return returnErrorBody("公告已不存在");
		}
		
		_SystemVersionNotice.setTitle(SystemVersionNotice.getTitle());
		_SystemVersionNotice.setPublish(SystemVersionNotice.getPublish());
		_SystemVersionNotice.setNoticeTime(SystemVersionNotice.getNoticeTime());
		_SystemVersionNotice.setContent(SystemVersionNotice.getContent());
		serviceFactory.getSystemVersionNoticeService().update(_SystemVersionNotice);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能 - 版本公告删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:10:07, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
//	@RequiresPermissions("sys:systemversionnotice:del")
	@AdminAuth
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		serviceFactory.getSystemVersionNoticeService().delete(id);
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 页面 - 版本公告查看
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:10:17, think
	 */
	@RequestMapping(value = "view/{id}")
//	@RequiresPermissions("sys:systemversionnotice:list")
	public String view(@PathVariable Long id,ModelMap map)
	{
		SystemVersionNotice SystemVersionNotice = serviceFactory.getSystemVersionNoticeService().get(id);
		map.put("SystemVersionNotice", SystemVersionNotice);
		return "sys/service/system_notice_view";
	}
	
	/**
	 * <pre>
	 * 页面 - 版本公告列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午4:10:27, think
	 */
	@RequestMapping(value = "list")
//	@RequiresPermissions("sys:systemversionnotice:list")
	@AdminAuth
	public String list(QueryParam queryParam) throws Exception
	{
		return "sys/versionnotice/list";

	}
	
	/**
	 * <pre>
	 * Ajax列表 - 版本公告列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:10:44, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<SystemVersionNotice> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SystemVersionNotice> result =serviceFactory.getSystemVersionNoticeService().findByCondition(queryParam);
		return result;
	}
	
	/**
	 * <pre>
	 * Ajax列表 - 查询所有已发布公告
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:11:28, think
	 */
	@RequestMapping(value = "ajaxPublisList")
	@ResponseBody
	public SearchResult<SystemVersionNotice> ajaxPublisList(@RequestBody QueryParam queryParam)
	{
		SearchResult<SystemVersionNotice> result = serviceFactory.getSystemVersionNoticeService().findAllPublish(queryParam);
		return result;
	}
}
