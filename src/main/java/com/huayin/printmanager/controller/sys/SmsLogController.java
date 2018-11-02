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

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.SmsLog;
import com.huayin.printmanager.persist.enumerate.SmsLogState;
import com.huayin.printmanager.persist.enumerate.SmsSendType;
import com.huayin.printmanager.persist.enumerate.SmsType;
import com.huayin.printmanager.plugin.PageSupport;

/**
 * <pre>
 * 系统模块 - 短信日志
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/smslog")
public class SmsLogController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 短信日志列表
	 * </pre>
	 * @param mobile
	 * @param content
	 * @param smsSendType
	 * @param type
	 * @param state
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午3:56:09, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:smslog:list")
	@AdminAuth
	public String list(String mobile,String content, SmsSendType smsSendType, SmsType type,SmsLogState state,
			HttpServletRequest request, ModelMap map)
	{
		PageSupport<SmsLog> page = new PageSupport<SmsLog>(request);
		SearchResult<SmsLog> result = serviceFactory.getSmsLogService().findByCondition(mobile, content, smsSendType, type, state,
				page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("mobile", mobile);
		map.put("content", content);
		map.put("smsSendType", smsSendType);
		map.put("type", type);
		map.put("state", state);
		map.put("page", page);
		return "sys/smslog/list";
	}
}
