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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SystemLog;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.plugin.PageSupport;

/**
 * <pre>
 * 系统模块 - 系统日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/systemlog")
public class SystemLogController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 系统日志列表
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param type
	 * @param companyName
	 * @param userName
	 * @param ip
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:05:07, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:systemlog:list")
	public String list(@DateTimeFormat(iso = ISO.DATE) Date dateMin, @DateTimeFormat(iso = ISO.DATE) Date dateMax, SystemLogType type, String companyName, String userName, String ip, HttpServletRequest request, ModelMap map)
	{
		PageSupport<SystemLog> page = new PageSupport<SystemLog>(request);
		SearchResult<SystemLog> result = serviceFactory.getSystemLogService().findByCondition(dateMin, dateMax, type, companyName, userName, ip, page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		map.put("type", type);
		map.put("companyName", companyName);
		map.put("userName", userName);
		map.put("ip", ip);
		map.put("page", page);
		return "sys/systemlog/list";
	}
}
