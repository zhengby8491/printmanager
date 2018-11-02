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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.sys.AgentQuotient;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统模块 - 代理商管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/agentquotient")
public class AgentQuotientController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 代理商新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:24:11, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("sys:agentquotient:create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/agentquotient/create";
	}

	/**
	 * <pre>
	 * 功能 - 代理商新增
	 * </pre>
	 * @param agentQuotient
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:31:48, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:agentquotient:create")
	@AdminAuth
	public AjaxResponseBody save(@ModelAttribute("agentQuotient") AgentQuotient agentQuotient, MultipartHttpServletRequest request)
	{
		// agentQuotient.setPhotoUrl("http://" + request.getServerName() + ":" + request.getServerPort());
		MultipartFile file = request.getFile("pic");
		serviceFactory.getAgentQuotientService().save(agentQuotient, file);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 代理商修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:32:05, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:agentquotient:edit")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		AgentQuotient agentQuotient = serviceFactory.getAgentQuotientService().get(id);
		map.put("agentQuotient", agentQuotient);
		return "sys/agentquotient/edit";
	}

	/**
	 * <pre>
	 * 功能 - 代理商修改
	 * </pre>
	 * @param agentQuotient
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:32:26, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:agentquotient:edit")
	@AdminAuth
	public AjaxResponseBody update(@ModelAttribute("agentQuotient") AgentQuotient agentQuotient, HttpServletRequest request)
	{
		MultipartFile file = null;
		if (request instanceof MultipartHttpServletRequest)
		{
			file = ((MultipartHttpServletRequest) request).getFile("pic");
			/*
			 * if(file!=null){ agentQuotient.setPhotoUrl("http://" + request.getServerName() + ":" +
			 * request.getServerPort()); }
			 */
		}
		serviceFactory.getAgentQuotientService().update(agentQuotient, file);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 代理商删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:32:43, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("sys:agentquotient:del")
	@AdminAuth
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		serviceFactory.getAgentQuotientService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 代理商列表
	 * </pre>
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午2:32:55, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:agentquotient:list")
	public String list() throws Exception
	{
		return "sys/agentquotient/list";

	}

	/**
	 * <pre>
	 * Ajax列表 - 代理商列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:33:09, think
	 */
	@RequestMapping(value = "ajaxList")
	@RequiresPermissions("sys:agentquotient:list")
	@ResponseBody
	public SearchResult<AgentQuotient> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<AgentQuotient> result = serviceFactory.getAgentQuotientService().findByCondition(queryParam);
		return result;
	}
}
